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
import com.liferay.portal.LayoutPrototypeException;
import com.liferay.portal.LocaleException;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchLayoutPrototypeException;
import com.liferay.portal.NoSuchLayoutSetPrototypeException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.*;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactoryUtil;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestImpl;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionCacheUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Daniel Kocsis
 */
public class LARImporter {

	public void importLar(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, File file)
		throws Exception {

		try {
			ImportExportThreadLocal.setLayoutImportInProcess(true);

			_initLarPersistenceContext(
				userId, groupId, privateLayout, parameterMap);

			doImport(_context, file);
		}
		finally {
			ImportExportThreadLocal.setLayoutImportInProcess(false);

			CacheUtil.clearCache();
			JournalContentUtil.clearCache();
			PermissionCacheUtil.clearCache();
		}
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

	protected void doImport(DataHandlerContext context, File file)
		throws Exception {

		Map parameterMap = context.getParameters();

		boolean deleteMissingLayouts = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			Boolean.TRUE.booleanValue());
		boolean importPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PERMISSIONS);
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

		StopWatch stopWatch = null;

		if (_log.isInfoEnabled()) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		boolean privateLayout = context.isPrivateLayout();

		long companyId = context.getCompanyId();

		User user = context.getUser();
		long userId = user.getUserId();

		context.setUserIdStrategy(user, userIdStrategy);

		ZipReader zipReader = ZipReaderFactoryUtil.getZipReader(file);

		context.setZipReader(zipReader);

		String dataStrategy = MapUtil.getString(
			parameterMap, PortletDataHandlerKeys.DATA_STRATEGY,
			PortletDataHandlerKeys.DATA_STRATEGY_MIRROR);

		context.setDataStrategy(dataStrategy);

		// Digest

		LarDigest larDigest = null;

		try {
			larDigest = new LarDigestImpl(zipReader);

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
			!larType.equals("layout-set-prototype") &&
			!larType.equals("portlet")) {

			throw new LARTypeException(
				"Invalid type of LAR file (" + larType + ")");
		}

		// Available locales

		Locale[] sourceAvailableLocales = LocaleUtil.fromLanguageIds(
			StringUtil.split(metaData.get("available-locales")));

		Locale[] targetAvailableLocales = LanguageUtil.getAvailableLocales();

		for (Locale sourceAvailableLocale : sourceAvailableLocales) {
			if (!ArrayUtil.contains(
					targetAvailableLocales, sourceAvailableLocale)) {

				LocaleException le = new LocaleException();

				le.setSourceAvailableLocales(sourceAvailableLocales);
				le.setTargetAvailableLocales(targetAvailableLocales);

				throw le;
			}
		}

		// Layout prototypes validation

		List<LarDigestItem> layoutItems = larDigest.findDigestItems(
			0, null, Layout.class.getName(), null);

		validateLayoutPrototypes(companyId, metaData, layoutItems);

		// Group id

		LayoutCache layoutCache = new LayoutCache();

		context.setAttribute("layoutCache", layoutCache);

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

		context.setAttribute("layoutSetPrototypeUuid", layoutSetPrototypeUuid);

		context.setAttribute(
			"layoutSetPrototypeLinkEnabled", layoutSetPrototypeLinkEnabled);

		// Read asset categories, asset tags, comments, locks, permissions, and
		// ratings entries to make them available to the data handlers through
		// the context

		// toDo: asset, comment, expando, lock, rating
		// import needs to be moved here!

		/*
		if (importCategories || group.isCompany()) {
			_portletImporter.readAssetCategories(portletDataContext);
		}

		readAssetTags(portletDataContext);
		readComments(portletDataContext);
		readExpandoTables(portletDataContext);
		readLocks(portletDataContext);
		readRatingsEntries(portletDataContext);
		*/

		// Layouts

		List<Layout> previousLayouts = LayoutUtil.findByG_P(
			groupId, privateLayout);

		// Remove layouts that were deleted from the layout set prototype

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

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

		Map<Long, Layout> newLayoutsMap =
			(Map<Long, Layout>)context.getNewPrimaryKeysMap(Layout.class);

		context.setAttribute("newLayouts", newLayouts);
		context.setAttribute("newLayoutIds", newLayoutIds);
		context.setAttribute("newLayoutsMap", newLayoutsMap);
		context.setAttribute("previousLayouts", previousLayouts);

		for (LarDigestItem item : larDigest) {
			BaseDataHandler dataHandler =
				DataHandlersUtil.getDataHandlerInstance(item.getType());

			dataHandler.importData(item);
		}

		if (importPermissions) {
			if (userId > 0) {
				Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
					User.class);

				indexer.reindex(userId);
			}
		}

		// Asset links

		//readAssetLinks(portletDataContext);
		//readAssetLinks(portletDataContext);

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
					(Map<String, String>)context.getNewPrimaryKeysMap(
						JournalArticle.class + ".articleId");

				typeSettingsProperties.setProperty(
					"article-id",
					MapUtil.getString(articleIds, articleId, articleId));

				LayoutUtil.update(layout, false);
			}
		}

		zipReader.close();
	}

	protected void validateLayoutPrototypes(
			long companyId, Map<String, String> metadata,
			List<LarDigestItem> layoutItems)
		throws Exception {

		List<Tuple> missingLayoutPrototypes = new ArrayList<Tuple>();

		String layoutSetPrototypeUuid = metadata.get(
			"layout-set-prototype-uuid");

		if (Validator.isNotNull(layoutSetPrototypeUuid)) {
			try {
				LayoutSetPrototypeLocalServiceUtil.
					getLayoutSetPrototypeByUuidAndCompanyId(
						layoutSetPrototypeUuid, companyId);
			}
			catch (NoSuchLayoutSetPrototypeException nlspe) {
				String layoutSetPrototypeName = metadata.get(
					"layout-set-prototype-name");

				missingLayoutPrototypes.add(
					new Tuple(
						LayoutSetPrototype.class.getName(),
						layoutSetPrototypeUuid, layoutSetPrototypeName));
			}
		}

		for (LarDigestItem layoutItem : layoutItems) {
			Map<String, String> layoutMetadata = layoutItem.getMetadata();

			String layoutPrototypeUuid = layoutMetadata.get(
				"layout-prototype-uuid");

			if (Validator.isNotNull(layoutPrototypeUuid)) {
				try {
					LayoutPrototypeLocalServiceUtil.
						getLayoutPrototypeByUuidAndCompanyId(
							layoutPrototypeUuid, companyId);
				}
				catch (NoSuchLayoutPrototypeException nslpe) {
					String layoutPrototypeName = layoutMetadata.get(
						"layout-prototype-name");

					missingLayoutPrototypes.add(
						new Tuple(
							LayoutPrototype.class.getName(),
							layoutPrototypeUuid, layoutPrototypeName));
				}
			}
		}

		if (!missingLayoutPrototypes.isEmpty()) {
			throw new LayoutPrototypeException(missingLayoutPrototypes);
		}
	}

	private void _initLarPersistenceContext(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameters)
		throws Exception {

		_context = new DataHandlerContextImpl();

		Group group = GroupLocalServiceUtil.getGroup(groupId);
		User user = UserLocalServiceUtil.getUser(userId);

		_context.setCompanyId(group.getCompanyId());
		_context.setGroupId(groupId);
		_context.setParameters(parameters);
		_context.setPrivateLayout(privateLayout);
		_context.setScopeGroupId(groupId);
		_context.setUser(user);

		DataHandlerContextThreadLocal.setDataHandlerContext(_context);
	}

	private static Log _log = LogFactoryUtil.getLog(LARImporter.class);

	private DataHandlerContext _context;

}