/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.*;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.staging.DataHandlerLocatorUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestImpl;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.*;
import com.liferay.portal.service.*;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.lar.LayoutDataHandler;
import com.liferay.portal.service.persistence.lar.PortletDataHandler;
import com.liferay.portal.theme.ThemeLoader;
import com.liferay.portal.theme.ThemeLoaderFactory;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetCategoryUtil;
import com.liferay.util.ContentUtil;

import java.io.File;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class LARExporter {

	public File digest(
		long groupId, boolean privateLayout, long[] layoutIds,
		Map<String, String[]> parameterMap, Date startDate, Date endDate) {

		try {
			ImportExportThreadLocal.setLayoutExportInProcess(true);

			DataHandlerContext context = _initDataHandlerContext(
				groupId, privateLayout, parameterMap, startDate, endDate);

			doCreateDigest(layoutIds, context);

			return context.getLarDigest().getDigestFile();
		}
		catch (Exception e) {
			_log.error(e);

			return null;
		}
	}

	public File export(
		long groupId, boolean privateLayout, long[] layoutIds,
		Map<String, String[]> parameterMap, Date startDate, Date endDate) {

		try {
			DataHandlerContext context =
				DataHandlerContextThreadLocal.getDataHandlerContext();

			doExport(context);

			context.getZipWriter().addEntry(
				"/digest.xml", context.getLarDigest().getDigestString());

			return context.getZipWriter().getFile();
		}
		catch (Exception e) {
			_log.error(e);

			return null;
		}
		finally {
			ImportExportThreadLocal.setLayoutExportInProcess(false);
		}
	}

	protected void doCreateDigest(
			long[] layoutIds, DataHandlerContext context)
		throws Exception, PortalException {

		long lastPublishDate = System.currentTimeMillis();

		Map parameterMap = context.getParameters();

		boolean exportCategories = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.CATEGORIES);
		boolean exportIgnoreLastPublishDate = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.IGNORE_LAST_PUBLISH_DATE);
		boolean exportPortletArchivedSetups = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_ARCHIVED_SETUPS);
		boolean exportPortletUserPreferences = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_USER_PREFERENCES);
		boolean exportTheme = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME);
		boolean exportThemeSettings = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME_REFERENCE);
		boolean exportLogo = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.LOGO);
		boolean exportLayoutSetSettings = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.LAYOUT_SET_SETTINGS);
		boolean updateLastPublishDate = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.UPDATE_LAST_PUBLISH_DATE);

		boolean privateLayout = context.isPrivateLayout();

		Group group = GroupLocalServiceUtil.getGroup(context.getGroupId());

		long defaultUserId = UserLocalServiceUtil.getDefaultUserId(
			group.getCompanyId());

		if (context.getEndDate() != null) {
			lastPublishDate = context.getEndDate().getTime();
		}

		if (exportIgnoreLastPublishDate) {
			context.setEndDate(null);
			context.setStartDate(null);
		}

		StopWatch stopWatch = null;

		if (_log.isInfoEnabled()) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		HashMap<String, String> metadata = new HashMap<String, String>();

		metadata.put(
			"build-number", String.valueOf(ReleaseInfo.getBuildNumber()));
		metadata.put("export-date", Time.getRFC822());

		if (context.hasDateRange()) {
			metadata.put(
				"start-date",
				String.valueOf(context.getStartDate()));
			metadata.put(
				"end-date", String.valueOf(context.getEndDate()));
		}

		metadata.put("group-id", String.valueOf(group.getGroupId()));
		metadata.put("private-layout", String.valueOf(privateLayout));

		String type = "layout-set";

		if (group.isLayoutPrototype()) {
			type = "layout-prototype";

			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.getLayoutPrototype(
					group.getClassPK());

			metadata.put("type-uuid", layoutPrototype.getUuid());
		}
		else if (group.isLayoutSetPrototype()) {
			type ="layout-set-prototype";

			LayoutSetPrototype layoutSetPrototype =
				LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(
					group.getClassPK());

			metadata.put("type-uuid", layoutSetPrototype.getUuid());
		}

		metadata.put("type", type);

		LarDigest digest = context.getLarDigest();

		digest.addMetaData(metadata);

		Portlet layoutConfigurationPortlet =
			PortletLocalServiceUtil.getPortletById(
				group.getCompanyId(), PortletKeys.LAYOUT_CONFIGURATION);

		Map<String, Object[]> portletIds =
			new LinkedHashMap<String, Object[]>();

		List<Layout> layouts = null;

		if ((layoutIds == null) || (layoutIds.length == 0)) {
			layouts = LayoutLocalServiceUtil.getLayouts(
				group.getGroupId(), privateLayout);
		}
		else {
			layouts = LayoutLocalServiceUtil.getLayouts(
				group.getGroupId(), privateLayout, layoutIds);
		}

		// Always exportable portlets

		List<Portlet> portlets = getAlwaysExportablePortlets(
			group.getCompanyId());

		for (Portlet portlet : portlets) {
			if (!group.isStagedPortlet(portlet.getPortletId())) {
				continue;
			}

			BaseDataHandler portletDataHandler = DataHandlerLocatorUtil.locate(
				portlet.getPortletDataHandlerClass());

			if (portletDataHandler != null) {
				portletDataHandler.digest(portlet);
			}
		}

		LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
			group.getGroupId(), privateLayout);

		// Layouts

		LayoutDataHandler layoutLarPersistence =
			(LayoutDataHandler) DataHandlerLocatorUtil.locate(
					Layout.class.getName());

		for (Layout layout : layouts) {
			layoutLarPersistence.digest(layout);
		}

		if (_log.isInfoEnabled()) {
			if (stopWatch != null) {
				_log.info(
					"Exporting layouts takes " + stopWatch.getTime() + " ms");
			}
			else {
				_log.info("Exporting layouts is finished");
			}
		}

		if (updateLastPublishDate) {
			LayoutExporter.updateLastPublishDate(layoutSet, lastPublishDate);
		}

		context.getLarDigest().close();
	}

	protected void doExport(DataHandlerContext context)
		throws Exception, PortalException {

		for (LarDigestItem item : context.getLarDigest()) {
			String type = item.getType();
			String classPK = item.getClassPK();

			BaseDataHandler dataHandler =
				DataHandlerLocatorUtil.locate(type);

			if (dataHandler != null) {
				dataHandler.serialize(classPK);
			}
		}
	}

	protected void exportAssetCategories(PortletDataContext portletDataContext)
			throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("categories-hierarchy");

		Element assetVocabulariesElement = rootElement.addElement(
			"vocabularies");

		List<AssetVocabulary> assetVocabularies =
			AssetVocabularyLocalServiceUtil.getGroupVocabularies(
				portletDataContext.getGroupId());

		for (AssetVocabulary assetVocabulary : assetVocabularies) {
			_portletExporter.exportAssetVocabulary(
				portletDataContext, assetVocabulariesElement, assetVocabulary);
		}

		Element categoriesElement = rootElement.addElement("categories");

		List<AssetCategory> assetCategories = AssetCategoryUtil.findByGroupId(
				portletDataContext.getGroupId());

		for (AssetCategory assetCategory : assetCategories) {
			_portletExporter.exportAssetCategory(
				portletDataContext, assetVocabulariesElement, categoriesElement,
				assetCategory);
		}

		_portletExporter.exportAssetCategories(portletDataContext, rootElement);

		portletDataContext.addZipEntry(
			portletDataContext.getRootPath() + "/categories-hierarchy.xml",
			document.formattedString());
	}

	protected void exportTheme(LayoutSet layoutSet, ZipWriter zipWriter)
			throws Exception {

		Theme theme = layoutSet.getTheme();

		String lookAndFeelXML = ContentUtil.get(
			"com/liferay/portal/dependencies/liferay-look-and-feel.xml.tmpl");

		lookAndFeelXML = StringUtil.replace(
			lookAndFeelXML,
			new String[] {
				"[$TEMPLATE_EXTENSION$]", "[$VIRTUAL_PATH$]"
			},
			new String[] {
				theme.getTemplateExtension(), theme.getVirtualPath()
			}
		);

		String servletContextName = theme.getServletContextName();

		ServletContext servletContext = ServletContextPool.get(
			servletContextName);

		if (servletContext == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Servlet context not found for theme " +
						theme.getThemeId());
			}

			return;
		}

		File themeZip = new File(zipWriter.getPath() + "/theme.zip");

		ZipWriter themeZipWriter = ZipWriterFactoryUtil.getZipWriter(themeZip);

		themeZipWriter.addEntry("liferay-look-and-feel.xml", lookAndFeelXML);

		File cssPath = null;
		File imagesPath = null;
		File javaScriptPath = null;
		File templatesPath = null;

		if (!theme.isLoadFromServletContext()) {
			ThemeLoader themeLoader = ThemeLoaderFactory.getThemeLoader(
				servletContextName);

			if (themeLoader == null) {
				_log.error(
					servletContextName + " does not map to a theme loader");
			}
			else {
				String realPath =
					themeLoader.getFileStorage().getPath() + StringPool.SLASH +
						theme.getName();

				cssPath = new File(realPath + "/css");
				imagesPath = new File(realPath + "/images");
				javaScriptPath = new File(realPath + "/javascript");
				templatesPath = new File(realPath + "/templates");
			}
		}
		else {
			cssPath = new File(servletContext.getRealPath(theme.getCssPath()));
			imagesPath = new File(
				servletContext.getRealPath(theme.getImagesPath()));
			javaScriptPath = new File(
				servletContext.getRealPath(theme.getJavaScriptPath()));
			templatesPath = new File(
				servletContext.getRealPath(theme.getTemplatesPath()));
		}

		exportThemeFiles("css", cssPath, themeZipWriter);
		exportThemeFiles("images", imagesPath, themeZipWriter);
		exportThemeFiles("javascript", javaScriptPath, themeZipWriter);
		exportThemeFiles("templates", templatesPath, themeZipWriter);
	}

	protected void exportThemeFiles(String path, File dir, ZipWriter zipWriter)
			throws Exception {

		if ((dir == null) || !dir.exists()) {
			return;
		}

		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				exportThemeFiles(
					path + StringPool.SLASH + file.getName(), file, zipWriter);
			}
			else {
				zipWriter.addEntry(
					path + StringPool.SLASH + file.getName(),
					FileUtil.getBytes(file));
			}
		}
	}

	public static List<Portlet> getAlwaysExportablePortlets(long companyId)
			throws Exception {

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets(companyId);

		Iterator<Portlet> itr = portlets.iterator();

		com.liferay.portal.kernel.lar.PortletDataHandler legacyDataHandler =
			null;

		while (itr.hasNext()) {
			Portlet portlet = itr.next();

			if (!portlet.isActive()) {
				itr.remove();

				continue;
			}

			BaseDataHandler dataHandler = DataHandlerLocatorUtil.locate(
				portlet.getPortletDataHandlerClass());

			if (dataHandler == null) {
				itr.remove();

				continue;
			}
			if (dataHandler != null) {
				PortletDataHandler portletDataHandler =
					(PortletDataHandler)dataHandler;

				if (!portletDataHandler.isAlwaysExportable()) {
					itr.remove();
				}
			}
		}

		return portlets;
	}

	private DataHandlerContext _initDataHandlerContext(
			long groupId, boolean privateLayout,
			Map<String, String[]> parameters, Date startDate, Date endDate)
		throws Exception {

		DataHandlerContext context = new DataHandlerContextImpl();

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		context.setCompanyId(group.getCompanyId());
		context.setEndDate(endDate);
		context.setGroupId(groupId);
		context.setPrivateLayout(privateLayout);
		context.setScopeGroupId(groupId);
		context.setStartDate(startDate);

		context.setParameters(parameters);

		LarDigest larDigest = new LarDigestImpl();
		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		context.setLarDigest(larDigest);
		context.setZipWriter(zipWriter);

		DataHandlerContextThreadLocal.setDataHandlerContext(context);

		return context;
	}

	private static Log _log = LogFactoryUtil.getLog(LARExporter.class);

	private LarDigest _larDigest;
	private File _larDigestFile;
	private LayoutExporter _layoutExporter = new LayoutExporter();
	private PermissionExporter _permissionExporter = new PermissionExporter();
	private PortletExporter _portletExporter = new PortletExporter();

}