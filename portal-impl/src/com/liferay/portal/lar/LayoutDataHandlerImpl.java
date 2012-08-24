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

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.StagedDataHandlerImpl;
import com.liferay.portal.kernel.lar.StagedPortletDataHandler;
import com.liferay.portal.kernel.lar.digest.LarDigest;
import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryDependency;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryDependencyImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutRevision;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutStagingHandler;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.persistence.LayoutRevisionUtil;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.sites.util.SitesUtil;

import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class LayoutDataHandlerImpl extends StagedDataHandlerImpl<Layout>
	implements LayoutDataHandler {

	public static final String SAME_GROUP_FRIENDLY_URL =
		"/[$SAME_GROUP_FRIENDLY_URL$]";

	public void export(
			Layout layout, DataHandlerContext context,
			LarDigestModule parentPortletModule)
		throws Exception {

		context.setPlid(layout.getPlid());

		String path = ExportImportPathUtil.getEntityPath(layout);

		if (context.isPathProcessed(path)) {
			return;
		}

		LayoutRevision layoutRevision = null;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		boolean exportLAR = ParamUtil.getBoolean(serviceContext, "exportLAR");

		if (!exportLAR && LayoutStagingUtil.isBranchingLayout(layout) &&
			!layout.isTypeURL()) {

			long layoutSetBranchId = ParamUtil.getLong(
				serviceContext, "layoutSetBranchId");

			if (layoutSetBranchId <= 0) {
				return;
			}

			layoutRevision = LayoutRevisionUtil.fetchByL_H_P(
				layoutSetBranchId, true, layout.getPlid());

			if (layoutRevision == null) {
				return;
			}

			LayoutStagingHandler layoutStagingHandler =
				LayoutStagingUtil.getLayoutStagingHandler(layout);

			layoutStagingHandler.setLayoutRevision(layoutRevision);
		}

		LarDigest digest = context.getLarDigest();

		LarDigestEntry digestEntry = new LarDigestEntryImpl();

		if (layoutRevision != null) {
			// layoutRevisionDataHandler.export();
		}

		// Children Layouts

		List<Layout> childrenLayouts = LayoutUtil.findByG_P_P(
			layout.getGroupId(), layout.isPrivateLayout(),
			layout.getLayoutId());

		for (Layout childLayout : childrenLayouts) {
			LarDigestEntryDependency childrenDependency =
				new LarDigestEntryDependencyImpl(
					Layout.class.getName(), childLayout.getUuid());

			digestEntry.addDependency(childrenDependency);

			export(childLayout, context, parentPortletModule);
		}

		// Layout prototype

		String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

		if (Validator.isNotNull(layoutPrototypeUuid)) {
			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					getLayoutPrototypeByUuidAndCompanyId(
						layoutPrototypeUuid, context.getCompanyId());

			LarDigestEntryDependency prototypeDependecy =
				new LarDigestEntryDependencyImpl(
					LayoutPrototype.class.getName(), layoutPrototypeUuid);

			digestEntry.addDependency(prototypeDependecy);

			layoutPrototypeDataHandler.export(
				layoutPrototype, context, parentPortletModule);
		}

		boolean deleteLayout = MapUtil.getBoolean(
			context.getParameters(), "delete_" + layout.getPlid());

		if (deleteLayout) {
			digestEntry.setAction(AssetAction.DELETE);
			digestEntry.setPath(path);
			digestEntry.setClassName(Layout.class.getName());
			digestEntry.setClassPK(StringUtil.valueOf(layout.getLayoutId()));
			digestEntry.setUuid(layout.getUuid());

			parentPortletModule.addEntry(digestEntry);

			return;
		}

		if (layout.isIconImage()) {
			Image image = ImageLocalServiceUtil.getImage(
				layout.getIconImageId());

			if (image != null) {
				imageDataHandler.export(image, context, parentPortletModule);

				LarDigestEntryDependency dependency =
					new LarDigestEntryDependencyImpl(
						Image.class.getName(),
						String.valueOf(image.getImageId()));

				digestEntry.addDependency(dependency);
			}
		}

		digestEntry.setAction(AssetAction.ADD);
		digestEntry.setPath(path);
		digestEntry.setClassName(Layout.class.getName());
		digestEntry.setClassPK(String.valueOf(layout.getPlid()));
		digestEntry.setUuid(layout.getUuid());

		parentPortletModule.addEntry(digestEntry);

		if (layout.isTypeArticle()) {
			exportJournalArticle(layout, context, parentPortletModule);
		}
		else if (layout.isTypeLinkToLayout()) {
			UnicodeProperties typeSettingsProperties =
				layout.getTypeSettingsProperties();

			long linkToLayoutId = GetterUtil.getLong(
				typeSettingsProperties.getProperty(
					"linkToLayoutId", StringPool.BLANK));

			if (linkToLayoutId > 0) {
				Layout linkedToLayout = LayoutLocalServiceUtil.getLayout(
					context.getScopeGroupId(), layout.isPrivateLayout(),
					linkToLayoutId);

				LarDigestEntryDependency dependency =
					new LarDigestEntryDependencyImpl(
						Layout.class.getName(), linkedToLayout.getUuid());

				digestEntry.addDependency(dependency);

				export(linkedToLayout, context, parentPortletModule);
			}
		}
		else if (layout.isTypePortlet()) {
			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			for (String portletId : layoutTypePortlet.getPortletIds()) {
				Portlet portlet = PortletLocalServiceUtil.getPortletById(
					portletId);

				javax.portlet.PortletPreferences jxPreferences =
					PortletPreferencesFactoryUtil.getLayoutPortletSetup(
						layout, portletId);

				String scopeType = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeType", null));
				String scopeLayoutUuid = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeLayoutUuid", null));

				long scopeGroupId = context.getScopeGroupId();

				if (Validator.isNotNull(scopeType)) {
					Group scopeGroup = null;

					if (scopeType.equals("company")) {
						scopeGroup = GroupLocalServiceUtil.getCompanyGroup(
							layout.getCompanyId());
					}
					else if (scopeType.equals("layout")) {
						Layout scopeLayout = null;

						scopeLayout = LayoutLocalServiceUtil.
							fetchLayoutByUuidAndGroupId(
								scopeLayoutUuid, context.getGroupId());

						if (scopeLayout == null) {
							continue;
						}

						scopeGroup = scopeLayout.getScopeGroup();
					}
					else {
						throw new IllegalArgumentException(
							"Scope type " + scopeType + " is invalid");
					}

					if (scopeGroup != null) {
						scopeGroupId = scopeGroup.getGroupId();
					}
				}

				StagedPortletDataHandler portletDataHandler =
					(StagedPortletDataHandler)DataHandlersUtil.
						getDataHandlerInstance(portlet.getPortletId());

				context.setPlid(layout.getPlid());
				context.setOldPlid(layout.getPlid());
				context.setScopeGroupId(scopeGroupId);
				context.setAttribute("scopeType", scopeType);
				context.setAttribute("scopeLayoutUuid", scopeLayoutUuid);
				context.setAttribute("layout", layout);

				if (portlet == null) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Do not export portlet " + portletId +
								" because the portlet does not exist");
					}

					continue;
				}

				if (portletDataHandler != null) {
					portletDataHandler.export(portlet, context, null);
				}
			}
		}

		context.resetAttribute(DataHandlerContext.ATTRIBUTE_NAME_PLID);

		// Serializing

		fixTypeSettings(layout);

		addZipEntry(context.getZipWriter(), path, layout);
	}

	@Override
	public void importData(LarDigestEntry entry, DataHandlerContext context)
		throws Exception {

		Map parameterMap = context.getParameters();

		LarDigest digest = context.getLarDigest();

		boolean importPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PERMISSIONS);
		boolean importPublicLayoutPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PUBLIC_LAYOUT_PERMISSIONS);
		boolean importThemeSettings = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME_REFERENCE);

		String layoutsImportMode = MapUtil.getString(
			parameterMap, PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE,
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID);
		String portletsMergeMode = MapUtil.getString(
			parameterMap, PortletDataHandlerKeys.PORTLETS_MERGE_MODE,
			PortletDataHandlerKeys.PORTLETS_MERGE_MODE_REPLACE);

		long groupId = context.getGroupId();

		Map<Long, Layout> newLayoutsMap =
			(Map<Long, Layout>)context.getNewPrimaryKeysMap(Layout.class);

		Layout layout = (Layout)getZipEntryAsObject(
				context.getZipReader(), entry.getPath());

		String layoutUuid = layout.getUuid();

		long layoutId = layout.getLayoutId();

		long oldLayoutId = layoutId;

		User user = context.getUser();

		boolean deleteLayout = (entry.getAction() == AssetAction.DELETE);

		if (deleteLayout) {
			Layout existingLayout =
				LayoutLocalServiceUtil.fetchLayoutByUuidAndGroupId(
					layoutUuid, groupId);

			if (existingLayout != null) {
				newLayoutsMap.put(oldLayoutId, existingLayout);

				ServiceContext serviceContext =
					ServiceContextThreadLocal.getServiceContext();

				LayoutLocalServiceUtil.deleteLayout(
					existingLayout, false, serviceContext);
			}

			return;
		}

		String path = entry.getPath();

		if (context.isPathProcessed(path)) {
			return;
		}

		Layout existingLayout = null;
		Layout importedLayout = null;

		String friendlyURL = layout.getFriendlyURL();

		boolean privateLayout = context.isPrivateLayout();

		if (layoutsImportMode.equals(
				PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_ADD_AS_NEW)) {

			layoutId = LayoutLocalServiceUtil.getNextLayoutId(
				groupId, privateLayout);
			friendlyURL = StringPool.SLASH + layoutId;
		}
		else if (layoutsImportMode.equals(
				PortletDataHandlerKeys.
					LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_NAME)) {

			Locale locale = LocaleUtil.getDefault();

			String localizedName = layout.getName(locale);

			List<Layout> previousLayouts = (List<Layout>)context.getAttribute(
				"previousLayouts");

			for (Layout curLayout : previousLayouts) {
				if (localizedName.equals(curLayout.getName(locale)) ||
					friendlyURL.equals(curLayout.getFriendlyURL())) {

					existingLayout = curLayout;

					break;
				}
			}

			if (existingLayout == null) {
				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
		}
		else if (layoutsImportMode.equals(
					PortletDataHandlerKeys.
						LAYOUTS_IMPORT_MODE_CREATED_FROM_PROTOTYPE)) {

			existingLayout = LayoutUtil.fetchByG_P_SPLU(
				groupId, privateLayout, layout.getUuid());

			if (SitesUtil.isLayoutModifiedSinceLastMerge(existingLayout)) {
				newLayoutsMap.put(oldLayoutId, existingLayout);

				return;
			}
		}
		else {

			// The default behaviour of import mode is
			// PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID

			existingLayout = LayoutUtil.fetchByUUID_G(
				layout.getUuid(), groupId);

			if (existingLayout == null) {
				existingLayout = LayoutUtil.fetchByG_P_F(
					groupId, privateLayout, friendlyURL);
			}

			if (existingLayout == null) {
				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
		}

		if (_log.isDebugEnabled()) {
			if (existingLayout == null) {
				_log.debug(
					"Layout with {groupId=" + groupId + ",privateLayout=" +
						privateLayout + ",layoutId=" + layoutId +
							"} does not exist");
			}
			else {
				_log.debug(
					"Layout with {groupId=" + groupId + ",privateLayout=" +
						privateLayout + ",layoutId=" + layoutId + "} exists");
			}
		}

		if (existingLayout == null) {
			long plid = CounterLocalServiceUtil.increment();

			importedLayout = LayoutUtil.create(plid);

			if (layoutsImportMode.equals(
					PortletDataHandlerKeys.
						LAYOUTS_IMPORT_MODE_CREATED_FROM_PROTOTYPE)) {

				importedLayout.setSourcePrototypeLayoutUuid(layout.getUuid());

				layoutId = LayoutLocalServiceUtil.getNextLayoutId(
					groupId, privateLayout);
			}
			else {
				importedLayout.setUuid(layout.getUuid());
				importedLayout.setCreateDate(layout.getCreateDate());
				importedLayout.setModifiedDate(layout.getModifiedDate());
				importedLayout.setLayoutPrototypeUuid(
					layout.getLayoutPrototypeUuid());
				importedLayout.setLayoutPrototypeLinkEnabled(
					layout.isLayoutPrototypeLinkEnabled());
				importedLayout.setSourcePrototypeLayoutUuid(
					layout.getSourcePrototypeLayoutUuid());
			}

			importedLayout.setGroupId(groupId);
			importedLayout.setPrivateLayout(privateLayout);
			importedLayout.setLayoutId(layoutId);

			// Resources

			boolean addGroupPermissions = true;

			Group group = importedLayout.getGroup();

			if (privateLayout && group.isUser()) {
				addGroupPermissions = false;
			}

			boolean addGuestPermissions = false;

			if (!privateLayout || layout.isTypeControlPanel()) {
				addGuestPermissions = true;
			}

			ResourceLocalServiceUtil.addResources(
				user.getCompanyId(), groupId, user.getUserId(),
				Layout.class.getName(), importedLayout.getPlid(), false,
				addGroupPermissions, addGuestPermissions);

			LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
				groupId, privateLayout);

			importedLayout.setLayoutSet(layoutSet);
		}
		else {
			importedLayout = existingLayout;
		}

		newLayoutsMap.put(oldLayoutId, importedLayout);

		long parentLayoutId = layout.getParentLayoutId();

		List<LarDigestEntry> resultEntries = digest.findDigestItems(
			null, null, Layout.class.getName(),
			StringUtil.valueOf(parentLayoutId), StringPool.BLANK);

		LarDigestEntry parentLayoutDigestEntry = null;
		String parentLayoutUuid = null;

		if ((resultEntries != null) && !resultEntries.isEmpty()) {
			parentLayoutDigestEntry = resultEntries.get(0);

			parentLayoutUuid = "";
				//parentLayoutItem.getMetadataValue("parent-layout-uuid");
		}

		if ((parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) &&
			(parentLayoutDigestEntry != null)) {

			importData(parentLayoutDigestEntry, context);

			Layout parentLayout = newLayoutsMap.get(parentLayoutId);

			parentLayoutId = parentLayout.getLayoutId();
		}
		else if (Validator.isNotNull(parentLayoutUuid)) {
			Layout parentLayout =
				LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
					parentLayoutUuid, groupId);

			parentLayoutId = parentLayout.getLayoutId();
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Importing layout with layout id " + layoutId +
					" and parent layout id " + parentLayoutId);
		}

		importedLayout.setCompanyId(user.getCompanyId());
		importedLayout.setParentLayoutId(parentLayoutId);
		importedLayout.setName(layout.getName());
		importedLayout.setTitle(layout.getTitle());
		importedLayout.setDescription(layout.getDescription());
		importedLayout.setKeywords(layout.getKeywords());
		importedLayout.setRobots(layout.getRobots());
		importedLayout.setType(layout.getType());

		if (layout.isTypeArticle()) {
			importJournalArticle(null, layout);

			importedLayout.setTypeSettings(layout.getTypeSettings());
		}
		else if (layout.isTypePortlet() &&
				Validator.isNotNull(layout.getTypeSettings()) &&
				!portletsMergeMode.equals(
					PortletDataHandlerKeys.PORTLETS_MERGE_MODE_REPLACE)) {

			mergePortlets(
				importedLayout, layout.getTypeSettings(), portletsMergeMode);
		}
		else if (layout.isTypeLinkToLayout()) {
			UnicodeProperties typeSettingsProperties =
				layout.getTypeSettingsProperties();

			long linkToLayoutId = GetterUtil.getLong(
				typeSettingsProperties.getProperty(
					"linkToLayoutId", StringPool.BLANK));

			if (linkToLayoutId > 0) {
				resultEntries = digest.findDigestItems(
					null, null, Layout.class.getName(),
					StringUtil.valueOf(linkToLayoutId), StringPool.BLANK);

				LarDigestEntry linkedLayoutDigestEntry = resultEntries.get(0);

				if (linkedLayoutDigestEntry != null) {
					importData(linkedLayoutDigestEntry, context);

					Layout linkedLayout = newLayoutsMap.get(linkToLayoutId);

					typeSettingsProperties.setProperty(
						"privateLayout",
						String.valueOf(linkedLayout.getPrivateLayout()));
					typeSettingsProperties.setProperty(
						"linkToLayoutId",
						String.valueOf(linkedLayout.getLayoutId()));
				}
				else {
					if (_log.isWarnEnabled()) {
						StringBundler sb = new StringBundler();

						sb.append("Unable to link layout with friendly URL ");
						sb.append(layout.getFriendlyURL());
						sb.append(" and layout id ");
						sb.append(layout.getLayoutId());
						sb.append(" to layout with layout id ");
						sb.append(linkToLayoutId);

						_log.warn(sb.toString());
					}
				}
			}

			importedLayout.setTypeSettings(layout.getTypeSettings());
		}
		else {
			importedLayout.setTypeSettings(layout.getTypeSettings());
		}

		importedLayout.setHidden(layout.isHidden());
		importedLayout.setFriendlyURL(friendlyURL);

		if (importThemeSettings) {
			importedLayout.setThemeId(layout.getThemeId());
			importedLayout.setColorSchemeId(layout.getColorSchemeId());
		}
		else {
			importedLayout.setThemeId(StringPool.BLANK);
			importedLayout.setColorSchemeId(StringPool.BLANK);
		}

		importedLayout.setWapThemeId(layout.getWapThemeId());
		importedLayout.setWapColorSchemeId(layout.getWapColorSchemeId());
		importedLayout.setCss(layout.getCss());
		importedLayout.setPriority(layout.getPriority());
		importedLayout.setLayoutPrototypeUuid(layout.getLayoutPrototypeUuid());
		importedLayout.setLayoutPrototypeLinkEnabled(
			layout.isLayoutPrototypeLinkEnabled());

		// toDo: review StagingUtil.updateLastImportSettings
		/*StagingUtil.updateLastImportSettings(
			layoutElement, importedLayout, portletDataContext);*/

		fixTypeSettings(importedLayout);

		importedLayout.setIconImage(false);

		if (layout.isIconImage()) {
			String iconImagePath = "";
			//item.getMetadataValue("icon-image-path");

			byte[] iconBytes = getZipEntryAsByteArray(
				context.getZipReader(), iconImagePath);

			if ((iconBytes != null) && (iconBytes.length > 0)) {
				importedLayout.setIconImage(true);

				if (importedLayout.getIconImageId() == 0) {
					long iconImageId = CounterLocalServiceUtil.increment();

					importedLayout.setIconImageId(iconImageId);
				}

				ImageLocalServiceUtil.updateImage(
					importedLayout.getIconImageId(), iconBytes);
			}
		}
		else {
			ImageLocalServiceUtil.deleteImage(importedLayout.getIconImageId());
		}

		// toDo: review expando export-import
		/*ServiceContext serviceContext = createServiceContext(
			getExpandoPath(path), importedLayout, null);

		importedLayout.setExpandoBridgeAttributes(serviceContext); */

		LayoutUtil.update(importedLayout, false);

		context.setPlid(importedLayout.getPlid());
		context.setOldPlid(layout.getPlid());

		List<Layout> newLayouts = (List<Layout>)context.getAttribute(
			"newLayouts");

		Set<Long> newLayoutIds = (Set<Long>)context.getAttribute(
			"newLayoutIds");

		newLayoutIds.add(importedLayout.getLayoutId());

		newLayouts.add(importedLayout);

		if (importPublicLayoutPermissions) {
			String resourceName = Layout.class.getName();
			String resourcePrimKey = String.valueOf(importedLayout.getPlid());

			Role guestRole = RoleLocalServiceUtil.getRole(
				importedLayout.getCompanyId(), RoleConstants.GUEST);

			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				importedLayout.getCompanyId(), resourceName,
				ResourceConstants.SCOPE_INDIVIDUAL, resourcePrimKey,
				guestRole.getRoleId(), new String[]{ActionKeys.VIEW});
		}

		context.setAttribute("newLayoutsMap", newLayoutsMap);
	}

	private String[] appendPortletIds(
		String[] portletIds, String[] newPortletIds, String portletsMergeMode) {

		for (String portletId : newPortletIds) {
			if (ArrayUtil.contains(portletIds, portletId)) {
				continue;
			}

			if (portletsMergeMode.equals(
					PortletDataHandlerKeys.PORTLETS_MERGE_MODE_ADD_TO_BOTTOM)) {

				portletIds = ArrayUtil.append(portletIds, portletId);
			}
			else {
				portletIds = ArrayUtil.append(
					new String[] {portletId}, portletIds);
			}
		}

		return portletIds;
	}

	private void exportJournalArticle(
			Layout layout, DataHandlerContext context,
			LarDigestModule digestModule)
		throws Exception {

		UnicodeProperties typeSettingsProperties =
			layout.getTypeSettingsProperties();

		String articleId = typeSettingsProperties.getProperty(
			"article-id", StringPool.BLANK);

		long articleGroupId = layout.getGroupId();

		if (Validator.isNull(articleId)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No article id found in typeSettings of layout " +
						layout.getPlid());
			}
		}

		JournalArticle article = null;

		article = JournalArticleLocalServiceUtil.getLatestArticle(
			articleGroupId, articleId, WorkflowConstants.STATUS_APPROVED);

		if (article == null) {
			return;
		}

		journalArticleDataHandler.export(article, context, digestModule);

		// toDo: link the article to the layout
	}

	private void fixTypeSettings(Layout layout) throws Exception {
		if (!layout.isTypeURL()) {
			return;
		}

		UnicodeProperties typeSettings = layout.getTypeSettingsProperties();

		String url = GetterUtil.getString(typeSettings.getProperty("url"));

		String friendlyURLPrivateGroupPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING;
		String friendlyURLPrivateUserPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING;
		String friendlyURLPublicPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING;

		if (!url.startsWith(friendlyURLPrivateGroupPath) &&
			!url.startsWith(friendlyURLPrivateUserPath) &&
			!url.startsWith(friendlyURLPublicPath)) {

			return;
		}

		int x = url.indexOf(CharPool.SLASH, 1);

		if (x == -1) {
			return;
		}

		int y = url.indexOf(CharPool.SLASH, x + 1);

		if (y == -1) {
			return;
		}

		String friendlyURL = url.substring(x, y);
		String groupFriendlyURL = layout.getGroup().getFriendlyURL();

		if (!friendlyURL.equals(groupFriendlyURL)) {
			return;
		}

		typeSettings.setProperty(
			"url",
			url.substring(0, x) + SAME_GROUP_FRIENDLY_URL + url.substring(y));
	}

	private void importJournalArticle(
			PortletDataContext portletDataContext, Layout layout)
		throws Exception {

		return;
	}

	private void mergePortlets(
			Layout layout, String newTypeSettings, String portletsMergeMode) {

		try {
			UnicodeProperties previousTypeSettingsProperties =
				layout.getTypeSettingsProperties();

			LayoutTypePortlet previousLayoutType =
				(LayoutTypePortlet)layout.getLayoutType();

			LayoutTemplate previousLayoutTemplate =
				previousLayoutType.getLayoutTemplate();

			List<String> previousColumns = previousLayoutTemplate.getColumns();

			UnicodeProperties newTypeSettingsProperties = new UnicodeProperties(
				true);

			newTypeSettingsProperties.load(newTypeSettings);

			String layoutTemplateId = newTypeSettingsProperties.getProperty(
				LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID);

			previousTypeSettingsProperties.setProperty(
				LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID,
				layoutTemplateId);

			String nestedColumnIds = newTypeSettingsProperties.getProperty(
				LayoutTypePortletConstants.NESTED_COLUMN_IDS);

			if (Validator.isNotNull(nestedColumnIds)) {
				previousTypeSettingsProperties.setProperty(
					LayoutTypePortletConstants.NESTED_COLUMN_IDS,
					nestedColumnIds);

				String[] nestedColumnIdsArray = StringUtil.split(
					nestedColumnIds);

				for (String nestedColumnId : nestedColumnIdsArray) {
					String nestedColumnValue =
						newTypeSettingsProperties.getProperty(nestedColumnId);

					previousTypeSettingsProperties.setProperty(
						nestedColumnId, nestedColumnValue);
				}
			}

			LayoutTemplate newLayoutTemplate =
				LayoutTemplateLocalServiceUtil.getLayoutTemplate(
					layoutTemplateId, false, null);

			String[] newPortletIds = new String[0];

			for (String columnId : newLayoutTemplate.getColumns()) {
				String columnValue = newTypeSettingsProperties.getProperty(
					columnId);

				String[] portletIds = StringUtil.split(columnValue);

				if (!previousColumns.contains(columnId)) {
					newPortletIds = ArrayUtil.append(newPortletIds, portletIds);
				}
				else {
					String[] previousPortletIds = StringUtil.split(
						previousTypeSettingsProperties.getProperty(columnId));

					portletIds = appendPortletIds(
						previousPortletIds, portletIds, portletsMergeMode);

					previousTypeSettingsProperties.setProperty(
						columnId, StringUtil.merge(portletIds));
				}
			}

			// Add portlets in non-existent column to the first column

			String columnId = previousColumns.get(0);

			String[] portletIds = StringUtil.split(
				previousTypeSettingsProperties.getProperty(columnId));

			appendPortletIds(portletIds, newPortletIds, portletsMergeMode);

			previousTypeSettingsProperties.setProperty(
				columnId, StringUtil.merge(portletIds));

			layout.setTypeSettings(previousTypeSettingsProperties.toString());
		}
		catch (IOException ioe) {
			layout.setTypeSettings(newTypeSettings);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutDataHandlerImpl.class);

}