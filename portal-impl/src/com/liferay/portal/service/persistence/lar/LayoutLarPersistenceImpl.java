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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.lar.LarPersistenceContext;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.staging.LayoutStagingUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutRevision;
import com.liferay.portal.model.LayoutStagingHandler;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.service.persistence.LayoutRevisionUtil;
import com.liferay.portal.service.persistence.impl.BaseLarPersistenceImpl;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

/**
 * @author Mate Thurzo
 */
public class LayoutLarPersistenceImpl extends BaseLarPersistenceImpl<Layout>
	implements LayoutLarPersistence {

	public static final String SAME_GROUP_FRIENDLY_URL =
		"/[$SAME_GROUP_FRIENDLY_URL$]";

	@Override
	public void doDigest(Layout layout) throws Exception {
		LarPersistenceContext larPersistenceContext =
			getLarPersistenceContext();

		String path = getEntityPath(layout);

		if (isPathProcessed(path)) {
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

		if (layoutRevision != null) {
			//TODO create layoutrevision digest
		}

		long parentLayoutId = layout.getParentLayoutId();

		if (parentLayoutId != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
			Layout parentLayout = LayoutLocalServiceUtil.getLayout(
				layout.getGroupId(), layout.isPrivateLayout(), parentLayoutId);

			if (parentLayout != null) {
				//TODO digest parent layout
			}
		}

		String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

		if (Validator.isNotNull(layoutPrototypeUuid)) {
			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					getLayoutPrototypeByUuidAndCompanyId(
						layoutPrototypeUuid,
						larPersistenceContext.getCompanyId());

			//TODO digest layout prototype
		}

		boolean deleteLayout = MapUtil.getBoolean(
			larPersistenceContext.getParameters(),
			"delete_" + layout.getPlid());

		LarDigest digest = larPersistenceContext.getLarDigest();

		LarDigestItem digestItem = new LarDigestItemImpl();

		if (deleteLayout) {
			digestItem.setAction(LarDigesterConstants.ACTION_DELETE);
			digestItem.setPath(path);
			digestItem.setType(Layout.class.getName());
			digestItem.setClassPK(StringUtil.valueOf(layout.getLayoutId()));

			digest.write(digestItem);

			return;
		}

		if (layout.isIconImage()) {
			Image image = ImageLocalServiceUtil.getImage(
				layout.getIconImageId());

			if (image != null) {
				String iconPath = getLayoutIconPath(layout, image);

				imageLarPersistence.digest(image);
			}
		}

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath(path);
		digestItem.setType(Layout.class.getName());
		digestItem.setClassPK(String.valueOf(layout.getPlid()));

		digest.write(digestItem);

		/*_portletExporter.exportPortletData(
			portletDataContext, layoutConfigurationPortlet, layout, null,
			layoutElement);*/

		// Layout permissions

		/*if (exportPermissions) {
			_permissionExporter.exportLayoutPermissions(
					portletDataContext, layoutCache,
					portletDataContext.getCompanyId(),
					portletDataContext.getScopeGroupId(), layout, layoutElement);
		}*/

		if (layout.isTypeArticle()) {
			exportJournalArticle(layout);
		}
		else if (layout.isTypeLinkToLayout()) {
			UnicodeProperties typeSettingsProperties =
				layout.getTypeSettingsProperties();

			long linkToLayoutId = GetterUtil.getLong(
				typeSettingsProperties.getProperty(
					"linkToLayoutId", StringPool.BLANK));

			if (linkToLayoutId > 0) {
				Layout linkedToLayout = LayoutLocalServiceUtil.getLayout(
					larPersistenceContext.getScopeGroupId(),
					layout.isPrivateLayout(), linkToLayoutId);

				digest(linkedToLayout);
			}
		}
		else if (layout.isTypePortlet()) {
			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			for (String portletId : layoutTypePortlet.getPortletIds()) {
				javax.portlet.PortletPreferences jxPreferences =
					PortletPreferencesFactoryUtil.getLayoutPortletSetup(
						layout, portletId);

				String scopeType = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeType", null));
				String scopeLayoutUuid = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeLayoutUuid", null));

				long scopeGroupId = larPersistenceContext.getScopeGroupId();

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
								scopeLayoutUuid,
								larPersistenceContext.getGroupId());

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

				String key = PortletPermissionUtil.getPrimaryKey(
					layout.getPlid(), portletId);

				// TODO remove hardcoded bookmarks value

				if (portletId.equals("28")) {
					Portlet portlet = PortletLocalServiceUtil.getPortletById(
						portletId);

					bookmarksPortletLarPersistence.digest(portlet);
				}
			}
		}
	}

	@Override
	public void doSerialize(Layout layout) throws Exception {
		String path = getEntityPath(layout);

		fixTypeSettings(layout);

		addZipEntry(path, layout);
	}

	public void deserialize(Document document) {
		return;
	}

	public Layout getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				Long plid = Long.valueOf(classPK);

				Layout layout = LayoutLocalServiceUtil.getLayout(plid);

				return layout;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	private void exportJournalArticle(Layout layout) throws Exception {

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

		journalArticleLarPersistence.digest(article);
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

	private String getLayoutIconPath(Layout layout, Image image) {

		StringBundler sb = new StringBundler(5);

		sb.append(getLayoutPath(layout.getLayoutId()));
		sb.append("/icons/");
		sb.append(image.getImageId());
		sb.append(StringPool.PERIOD);
		sb.append(image.getType());

		return sb.toString();
	}

	private static final Log _log =
		LogFactoryUtil.getLog(LayoutLarPersistenceImpl.class);

}
