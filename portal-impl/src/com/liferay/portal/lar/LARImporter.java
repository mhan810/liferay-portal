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

import com.liferay.portal.LARFileException;
import com.liferay.portal.LARTypeException;
import com.liferay.portal.LayoutImportException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchLayoutPrototypeException;
import com.liferay.portal.NoSuchLayoutSetPrototypeException;
import com.liferay.portal.kernel.lar.*;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.staging.LarPersistenceLocatorUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactoryUtil;
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
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionCacheUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import org.apache.commons.lang.time.StopWatch;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Kocsis
 */
public class LARImporter {

	public void importa(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, File file)
			throws Exception {

		try {
			ImportExportThreadLocal.setLayoutImportInProcess(true);

			DataHandlerContext context = _initLarPersistenceContext(
				groupId, parameterMap);

			doImport(context, file);
		}
		finally {
			ImportExportThreadLocal.setLayoutImportInProcess(false);

			CacheUtil.clearCache();
			JournalContentUtil.clearCache();
			PermissionCacheUtil.clearCache();
		}
	}

	protected void doImport(DataHandlerContext context, File file)
		throws Exception {

		Map parameterMap = context.getParameters();

		boolean deleteMissingLayouts = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			Boolean.TRUE.booleanValue());
		boolean deletePortletData = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.DELETE_PORTLET_DATA);
		boolean importCategories = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.CATEGORIES);
		boolean importPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PERMISSIONS);
		boolean importPublicLayoutPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PUBLIC_LAYOUT_PERMISSIONS);
		boolean importPortletData = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_DATA);
		boolean importPortletSetup = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_SETUP);
		boolean importPortletArchivedSetups = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_ARCHIVED_SETUPS);
		boolean importPortletUserPreferences = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_USER_PREFERENCES);
		boolean importTheme = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME);

		boolean layoutSetPrototypeLinkEnabled = MapUtil.getBoolean(
			parameterMap,
			PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_LINK_ENABLED, true);

		long groupId = context.getGroupId();
		Group group = GroupLocalServiceUtil.getGroup(groupId);

		if (group.isLayoutSetPrototype()) {
			layoutSetPrototypeLinkEnabled = false;
		}

		String userIdStrategy = MapUtil.getString(
			parameterMap, PortletDataHandlerKeys.USER_ID_STRATEGY);

		if (_log.isDebugEnabled()) {
			_log.debug("Delete portlet data " + deletePortletData);
			_log.debug("Import categories " + importCategories);
			_log.debug("Import permissions " + importPermissions);
			_log.debug("Import portlet data " + importPortletData);
			_log.debug("Import portlet setup " + importPortletSetup);
			_log.debug(
				"Import portlet archived setups " +
					importPortletArchivedSetups);
			_log.debug(
				"Import portlet user preferences " +
					importPortletUserPreferences);
			_log.debug("Import theme " + importTheme);
		}

		StopWatch stopWatch = null;

		if (_log.isInfoEnabled()) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		LayoutCache layoutCache = new LayoutCache();

		boolean privateLayout = context.isPrivateLayout();

		long companyId = context.getCompanyId();

		User user = context.getUser();
		long userId = user.getUserId();

		UserIdStrategy strategy = _portletImporter.getUserIdStrategy(
			user, userIdStrategy);

		ZipReader zipReader = ZipReaderFactoryUtil.getZipReader(file);

		PortletDataContext portletDataContext = new PortletDataContextImpl(
			companyId, groupId, parameterMap, new HashSet<String>(), strategy,
			zipReader);

		portletDataContext.setPortetDataContextListener(
			new PortletDataContextListenerImpl(portletDataContext));

		portletDataContext.setPrivateLayout(privateLayout);

		// Manifest

		File digestFile = portletDataContext.getZipEntryAsFile("/digest.xml");

		if (digestFile == null) {
			throw new LARFileException("manifest.xml not found in the LAR");
		}

		LarDigest larDigest = null;

		try {
			larDigest = new LarDigestImpl(digestFile);

			context.setLarDigest(larDigest);
		}
		catch (Exception e) {
			throw new LARFileException(e);
		}

		// Build compatibility

		Map<String, String> metaData = larDigest.getMetaData();

		int buildNumber = ReleaseInfo.getBuildNumber();

		int importBuildNumber = GetterUtil.getInteger(
			metaData.get("build-number"));

		if (buildNumber != importBuildNumber) {
			throw new LayoutImportException(
				"LAR build number " + importBuildNumber + " does not match " +
					"portal build number " + buildNumber);
		}

		// Type compatibility

		String larType = metaData.get("type");

		if (!larType.equals("layout-prototype") &&
			!larType.equals("layout-set") &&
			!larType.equals("layout-set-prototype")) {

			throw new LARTypeException(
				"Invalid type of LAR file (" + larType + ")");
		}

		// Layout prototypes validity

		//validateLayoutPrototypes(companyId, layoutsElement, layoutElements);

		// Group id

		long sourceGroupId = GetterUtil.getLong(metaData.get("group-id"));

		context.setSourceGroupId(sourceGroupId);

		// Layout and layout set prototype

		String layoutSetPrototypeUuid = metaData.get(
			"layout-set-prototype-uuid");

		if (group.isLayoutPrototype() && larType.equals("layout-prototype")) {
			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.getLayoutPrototype(
					group.getClassPK());

			String layoutPrototypeUuid = GetterUtil.getString(
				metaData.get("type-uuid"));

			LayoutPrototype existingLayoutPrototype = null;

			if (Validator.isNotNull(layoutPrototypeUuid)) {
				try {
					existingLayoutPrototype =
						LayoutPrototypeLocalServiceUtil.
							getLayoutPrototypeByUuidAndCompanyId(
								layoutPrototypeUuid, companyId);
				}
				catch (NoSuchLayoutPrototypeException nslpe) {
				}
			}

			if (existingLayoutPrototype == null) {
				layoutPrototype.setUuid(layoutPrototypeUuid);

				LayoutPrototypeLocalServiceUtil.updateLayoutPrototype(
						layoutPrototype);
			}
		}
		else if (group.isLayoutSetPrototype() &&
			larType.equals("layout-set-prototype")) {

			LayoutSetPrototype layoutSetPrototype =
				LayoutSetPrototypeLocalServiceUtil.getLayoutSetPrototype(
					group.getClassPK());

			String importedLayoutSetPrototypeUuid = GetterUtil.getString(
				metaData.get("type-uuid"));

			LayoutSetPrototype existingLayoutSetPrototype = null;

			if (Validator.isNotNull(importedLayoutSetPrototypeUuid)) {
				try {
					existingLayoutSetPrototype =
						LayoutSetPrototypeLocalServiceUtil.
							getLayoutSetPrototypeByUuidAndCompanyId(
								importedLayoutSetPrototypeUuid, companyId);
				}
				catch (NoSuchLayoutSetPrototypeException nslspe) {
				}
			}

			if (existingLayoutSetPrototype == null) {
				layoutSetPrototype.setUuid(importedLayoutSetPrototypeUuid);

				LayoutSetPrototypeLocalServiceUtil.updateLayoutSetPrototype(
						layoutSetPrototype);
			}
		}
		else if (larType.equals("layout-set-prototype")) {
			layoutSetPrototypeUuid = GetterUtil.getString(
				metaData.get("type-uuid"));
		}

		context.setAttribute(
			"layoutSetPrototypeUuid", layoutSetPrototypeUuid);

		context.setAttribute(
			"layoutSetPrototypeLinkEnabled", layoutSetPrototypeLinkEnabled);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();


		// Read asset categories, asset tags, comments, locks, permissions, and
		// ratings entries to make them available to the data handlers through
		// the context

		if (importPermissions) {
			_permissionImporter.readPortletDataPermissions(portletDataContext);
		}

		if (importCategories || group.isCompany()) {
			_portletImporter.readAssetCategories(portletDataContext);
		}

		_portletImporter.readAssetTags(portletDataContext);
		_portletImporter.readComments(portletDataContext);
		_portletImporter.readExpandoTables(portletDataContext);
		_portletImporter.readLocks(portletDataContext);
		_portletImporter.readRatingsEntries(portletDataContext);

		// Layouts

		List<Layout> previousLayouts = LayoutUtil.findByG_P(
			groupId, privateLayout);

		// Remove layouts that were deleted from the layout set prototype

		if (Validator.isNotNull(layoutSetPrototypeUuid) &&
				layoutSetPrototypeLinkEnabled) {

			LayoutSetPrototype layoutSetPrototype =
				LayoutSetPrototypeLocalServiceUtil.
					getLayoutSetPrototypeByUuidAndCompanyId(
						layoutSetPrototypeUuid, companyId);

			Group layoutSetPrototypeGroup = layoutSetPrototype.getGroup();

			for (Layout layout : previousLayouts) {
				String sourcePrototypeLayoutUuid =
					layout.getSourcePrototypeLayoutUuid();

				if (Validator.isNull(layout.getSourcePrototypeLayoutUuid())) {
					continue;
				}

				Layout sourcePrototypeLayout = LayoutUtil.fetchByUUID_G(
					sourcePrototypeLayoutUuid,
					layoutSetPrototypeGroup.getGroupId());

				if (sourcePrototypeLayout == null) {
					LayoutLocalServiceUtil.deleteLayout(
						layout, false, serviceContext);
				}
			}
		}

		List<Layout> newLayouts = new ArrayList<Layout>();

		Set<Long> newLayoutIds = new HashSet<Long>();

		LayoutSetLarPesistence layoutSetLarPesistence =
			(LayoutSetLarPesistence)LarPersistenceLocatorUtil.locate(
				LayoutSet.class.getName());

		layoutSetLarPesistence.importData(null);

		LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
			context.getGroupId(), context.isPrivateLayout());

		context.setAttribute("newLayouts", newLayouts);
		context.setAttribute("newLayoutIds", newLayoutIds);
		context.setAttribute("previousLayouts", previousLayouts);

		for (LarDigestItem item : larDigest) {
			BaseDataHandler larPesistence =
				LarPersistenceLocatorUtil.locate(item.getType());

			larPesistence.importData(item);
		}

		Map<Long, Layout> newLayoutsMap =
			(Map<Long, Layout>)context.getNewPrimaryKeysMap(Layout.class);

		Element portletsElement = null; //rootElement.element("portlets");

		List<Element> portletElements = portletsElement.elements("portlet");

		// Delete portlet data

		if (deletePortletData) {
			if (_log.isDebugEnabled()) {
				if (portletElements.size() > 0) {
					_log.debug("Deleting portlet data");
				}
			}

			for (Element portletElement : portletElements) {
				String portletId = portletElement.attributeValue("portlet-id");
				long layoutId = GetterUtil.getLong(
					portletElement.attributeValue("layout-id"));
				long plid = newLayoutsMap.get(layoutId).getPlid();

				portletDataContext.setPlid(plid);

				_portletImporter.deletePortletData(
					portletDataContext, portletId, plid);
			}
		}

		// Import portlets

		if (_log.isDebugEnabled()) {
			if (portletElements.size() > 0) {
				_log.debug("Importing portlets");
			}
		}

		for (Element portletElement : portletElements) {
			String portletPath = portletElement.attributeValue("path");
			String portletId = portletElement.attributeValue("portlet-id");
			long layoutId = GetterUtil.getLong(
					portletElement.attributeValue("layout-id"));
			long oldPlid = GetterUtil.getLong(
					portletElement.attributeValue("old-plid"));

			Portlet portlet = PortletLocalServiceUtil.getPortletById(
				portletDataContext.getCompanyId(), portletId);

			if (!portlet.isActive() || portlet.isUndeployedPortlet()) {
				continue;
			}

			Layout layout = newLayoutsMap.get(layoutId);

			long plid = LayoutConstants.DEFAULT_PLID;

			if (layout != null) {
				plid = layout.getPlid();
			}

			layout = LayoutUtil.fetchByPrimaryKey(plid);

			if ((layout == null) && !group.isCompany()) {
				continue;
			}

			portletDataContext.setPlid(plid);
			portletDataContext.setOldPlid(oldPlid);

			Document portletDocument = SAXReaderUtil.read(
				portletDataContext.getZipEntryAsString(portletPath));

			portletElement = portletDocument.getRootElement();

			// The order of the import is important. You must always import
			// the portlet preferences first, then the portlet data, then
			// the portlet permissions. The import of the portlet data
			// assumes that portlet preferences already exist.

			_portletImporter.setPortletScope(
				portletDataContext, portletElement);

			long portletPreferencesGroupId = groupId;

			try {

				if ((layout != null) && !group.isCompany()) {
					portletPreferencesGroupId = layout.getGroupId();
				}

				// Portlet preferences

				_portletImporter.importPortletPreferences(
					portletDataContext, layoutSet.getCompanyId(),
					portletPreferencesGroupId, layout, null, portletElement,
					importPortletSetup, importPortletArchivedSetups,
					importPortletUserPreferences, false);

				// Portlet data

				Element portletDataElement = portletElement.element(
					"portlet-data");

				if (importPortletData && (portletDataElement != null)) {
					_portletImporter.importPortletData(
						portletDataContext, portletId, plid,
						portletDataElement);
				}
			}
			finally {
				_portletImporter.resetPortletScope(
						portletDataContext, portletPreferencesGroupId);
			}

			// Portlet permissions

			if (importPermissions) {
				_permissionImporter.importPortletPermissions(
					layoutCache, companyId, groupId, userId, layout,
					portletElement, portletId);
			}

			// Archived setups

			_portletImporter.importPortletPreferences(
				portletDataContext, layoutSet.getCompanyId(), groupId, null,
				null, portletElement, importPortletSetup,
				importPortletArchivedSetups, importPortletUserPreferences,
				false);
		}

		if (importPermissions) {
			if (userId > 0) {
				Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
					User.class);

				indexer.reindex(userId);
			}
		}

		// Asset links

		_portletImporter.readAssetLinks(portletDataContext);

		// Delete missing layouts

		if (deleteMissingLayouts) {
			deleteMissingLayouts(
				groupId, privateLayout, newLayoutIds, previousLayouts,
				serviceContext);
		}

		// Page count

		LayoutSetLocalServiceUtil.updatePageCount(groupId, privateLayout);

		if (_log.isInfoEnabled()) {
			_log.info("Importing layouts takes " + stopWatch.getTime() + " ms");
		}

		// Site

		GroupLocalServiceUtil.updateSite(groupId, true);

		// Web content layout type

		for (Layout layout : newLayouts) {
			UnicodeProperties typeSettingsProperties =
					layout.getTypeSettingsProperties();

			String articleId = typeSettingsProperties.getProperty("article-id");

			if (Validator.isNotNull(articleId)) {
				Map<String, String> articleIds =
						(Map<String, String>)portletDataContext.
								getNewPrimaryKeysMap(
										JournalArticle.class + ".articleId");

				typeSettingsProperties.setProperty(
						"article-id",
						MapUtil.getString(articleIds, articleId, articleId));

				LayoutUtil.update(layout, false);
			}
		}

		zipReader.close();

	}

	protected void deleteMissingLayouts(
			long groupId, boolean privateLayout, Set<Long> newLayoutIds,
			List<Layout> previousLayouts, ServiceContext serviceContext)
		throws Exception {

		// Layouts

		if (_log.isDebugEnabled()) {
			if (newLayoutIds.size() > 0) {
				_log.debug("Delete missing layouts");
			}
		}

		for (Layout layout : previousLayouts) {
			if (!newLayoutIds.contains(layout.getLayoutId())) {
				try {
					LayoutLocalServiceUtil.deleteLayout(
						layout, false, serviceContext);
				}
				catch (NoSuchLayoutException nsle) {
				}
			}
		}

		// Layout set

		LayoutSetLocalServiceUtil.updatePageCount(groupId, privateLayout);
	}

	private DataHandlerContext _initLarPersistenceContext(
			long groupId, Map<String, String[]> parameters)
		throws Exception {

		DataHandlerContext context =
			new DataHandlerContextImpl();

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		context.setGroupId(groupId);
		context.setScopeGroupId(groupId);
		context.setCompanyId(group.getCompanyId());

		context.setParameters(parameters);

		DataHandlerContextThreadLocal.setDataHandlerContext(
				context);

		return context;
	}


	private static Log _log = LogFactoryUtil.getLog(LARImporter.class);

	private PermissionImporter _permissionImporter = new PermissionImporter();
	private PortletImporter _portletImporter = new PortletImporter();

}