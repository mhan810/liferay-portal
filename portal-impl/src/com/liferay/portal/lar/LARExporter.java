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
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.ImportExportThreadLocal;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestImpl;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.lar.AssetCategoryDataHandler;
import com.liferay.portal.service.persistence.lar.AssetVocabularyDataHandler;
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

	public static List<Portlet> getAlwaysExportablePortlets(long companyId)
		throws Exception {

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets(companyId);

		Iterator<Portlet> itr = portlets.iterator();

		PortletDataHandler portletDataHandler = null;

		while (itr.hasNext()) {
			Portlet portlet = itr.next();

			portletDataHandler =
				(PortletDataHandler)DataHandlersUtil.getDataHandlerInstance(
					portlet.getPortletId());

			if (!portlet.isActive()) {
				itr.remove();

				continue;
			}

			if (portletDataHandler == null) {
				itr.remove();

				continue;
			}

			if (!portletDataHandler.isAlwaysExportable()) {
				itr.remove();
			}
		}

		return portlets;
	}

	public File digest(
		long groupId, boolean privateLayout, long[] layoutIds,
		Map<String, String[]> parameterMap, Date startDate, Date endDate) {

		try {
			ImportExportThreadLocal.setLayoutExportInProcess(true);

			//Broadcasting message about starting of process

			Message message = new Message();
			message.put("command", "larExporterDigest");

			MessageBusUtil.sendMessage(
				DestinationNames.LAR_EXPORT_IMPORT, message);

			_initDataHandlerContext(
				groupId, privateLayout, parameterMap, startDate, endDate);

			doCreateDigest(layoutIds, _context);

			return _context.getLarDigest().getDigestFile();
		}
		catch (Exception e) {
			_log.error(e);

			return null;
		}
	}

	public File serialize(
		long groupId, boolean privateLayout, long[] layoutIds,
		Map<String, String[]> parameterMap, Date startDate, Date endDate) {

		try {
			//Broadcasting message about starting of process

			Message message = new Message();
			message.put("command", "larExporterSerialize");

			MessageBusUtil.sendMessage(
				DestinationNames.LAR_EXPORT_IMPORT, message);

			// Serializing

			doSerialize(_context);

			_context.getZipWriter().addEntry(
				"/digest.xml", _context.getLarDigest().getDigestString());

			return _context.getZipWriter().getFile();
		}
		catch (Exception e) {
			_log.error(e);

			return null;
		}
		finally {
			ImportExportThreadLocal.setLayoutExportInProcess(false);
		}
	}

	protected void digestAssetCategories(DataHandlerContext context)
			throws Exception {

		List<AssetVocabulary> assetVocabularies =
			AssetVocabularyLocalServiceUtil.getGroupVocabularies(
				context.getGroupId());

		AssetVocabularyDataHandler vocabularyDataHandler =
			(AssetVocabularyDataHandler)DataHandlersUtil.getDataHandlerInstance(
				AssetVocabulary.class.getName());

		for (AssetVocabulary assetVocabulary : assetVocabularies) {
			vocabularyDataHandler.digest(assetVocabulary, _context);
		}

		List<AssetCategory> assetCategories = AssetCategoryUtil.findByGroupId(
			context.getGroupId());

		AssetCategoryDataHandler categoryDataHandler =
			(AssetCategoryDataHandler)DataHandlersUtil.getDataHandlerInstance(
				AssetCategory.class.getName());

		for (AssetCategory assetCategory : assetCategories) {
			categoryDataHandler.digest(assetCategory, _context);
		}
	}

	protected void doCreateDigest(long[] layoutIds, DataHandlerContext context)
		throws Exception, PortalException {

		long lastPublishDate = System.currentTimeMillis();

		// Parameters

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

		context.setAttribute("layoutCache", new LayoutCache());

		LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
				group.getGroupId(), privateLayout);

		UnicodeProperties typeSettings = layoutSet.getSettingsProperties();

		if (typeSettings.containsKey("last-publish-date")) {
			Date date = DateUtil.parseDate(
				typeSettings.get("last-publish-date"),
				LocaleThreadLocal.getDefaultLocale());

			context.setLastPublishDate(date);
		}
		else {
			context.setLastPublishDate(new Date(lastPublishDate));
		}

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

		// Assembly metadata for LAR
		HashMap<String, String> metadata = new HashMap<String, String>();

		metadata.put(
			"available-locales",
			StringUtil.merge(LanguageUtil.getAvailableLocales()));
		metadata.put(
			"build-number", String.valueOf(ReleaseInfo.getBuildNumber()));
		metadata.put("export-date", Time.getRFC822());

		if (context.hasDateRange()) {
			metadata.put("start-date", String.valueOf(context.getStartDate()));
			metadata.put("end-date", String.valueOf(context.getEndDate()));
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

		long plid = LayoutConstants.DEFAULT_PLID;

		if (!layouts.isEmpty()) {
			Layout firstLayout = layouts.get(0);

			plid = firstLayout.getPlid();
		}

		if (group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		for (Portlet portlet : portlets) {
			if (!group.isStagedPortlet(portlet.getPortletId())) {
				continue;
			}

			Layout layout = LayoutLocalServiceUtil.fetchLayout(plid);

			if (layout == null) {
				if (!group.isCompany() &&
					(plid <= LayoutConstants.DEFAULT_PLID)) {

					continue;
				}

				if (_log.isWarnEnabled()) {
					_log.warn(
						"Assuming global scope because no layout was found");
				}

				layout = new LayoutImpl();

				layout.setGroupId(group.getGroupId());
				layout.setCompanyId(context.getCompanyId());
			}

			context.setPlid(plid);
			context.setOldPlid(plid);
			context.setScopeGroupId(group.getGroupId());
			context.setAttribute("scopeType", StringPool.BLANK);
			context.setAttribute("scopeLayoutUuid", StringPool.BLANK);
			context.setAttribute("layout", layout);

			PortletDataHandler portletDataHandler =
				(PortletDataHandler)DataHandlersUtil.getDataHandlerInstance(
					portlet.getPortletId());

			if (portletDataHandler != null) {
				portletDataHandler.digest(portlet, context);
			}
		}

		// Layouts

		LayoutDataHandler layoutDataHandler =
			(LayoutDataHandler)DataHandlersUtil.getDataHandlerInstance(
				Layout.class.getName());

		for (Layout layout : layouts) {
			layoutDataHandler.digest(layout, _context);
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
			updateLastPublishDate(layoutSet, lastPublishDate);
		}

		context.getLarDigest().write();
		context.getLarDigest().close();
	}

	protected void doSerialize(DataHandlerContext context)
		throws Exception, PortalException {

		for (LarDigestItem item : context.getLarDigest()) {
			BaseDataHandler dataHandler =
				DataHandlersUtil.getDataHandlerInstance(item.getType());

			if (dataHandler != null) {
				dataHandler.serialize(item, context);
			}
		}
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

	protected void updateLastPublishDate(
			LayoutSet layoutSet, long lastPublishDate)
		throws Exception {

		UnicodeProperties settingsProperties =
			layoutSet.getSettingsProperties();

		if (lastPublishDate <= 0) {
			settingsProperties.remove("last-publish-date");
		}
		else {
			settingsProperties.setProperty(
				"last-publish-date", String.valueOf(lastPublishDate));
		}

		LayoutSetLocalServiceUtil.updateSettings(
			layoutSet.getGroupId(), layoutSet.isPrivateLayout(),
			settingsProperties.toString());
	}

	private void _initDataHandlerContext(
			long groupId, boolean privateLayout,
			Map<String, String[]> parameters, Date startDate, Date endDate)
		throws Exception {

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		_context = new DataHandlerContextImpl(
			group.getCompanyId(), groupId, parameters, startDate, endDate,
			zipWriter);

		_context.setPrivateLayout(privateLayout);
		_context.setScopeGroupId(groupId);

		LarDigest larDigest = new LarDigestImpl();

		_context.setLarDigest(larDigest);
	}

	private static Log _log = LogFactoryUtil.getLog(LARExporter.class);

	private DataHandlerContext _context;

}