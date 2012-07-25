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

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.NoSuchPortletPreferencesException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.PortletItem;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletItemLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.service.persistence.PortletPreferencesUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.asset.NoSuchEntryException;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetLinkLocalServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class PortletDataHandlerImpl extends BaseDataHandlerImpl<Portlet>
	implements PortletDataHandler {

	public void deserialize(Document document) {
		return;
	}

	public void digest(Portlet portlet) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		long plid = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		long layoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

		Layout layout = (Layout)context.getAttribute("layout");

		if (layout != null) {
			plid = layout.getPlid();
			layoutId = layout.getLayoutId();
		}

		String portletId = portlet.getPortletId();

		if (portlet == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Do not export portlet " + portletId +
						" because the portlet does not exist");
			}

			return;
		}

		if (!portlet.isInstanceable() &&
			!portlet.isPreferencesUniquePerLayout()) { //&&
			//context.hasNotUniquePerLayout(portletId)) {

			return;
		}

		boolean exportPortletData = false;

		if (context.getParameters().containsKey(
				PortletDataHandlerKeys.PORTLET_DATA + "_" +
					PortletConstants.getRootPortletId(portlet.getPortletId()))) {

			exportPortletData = MapUtil.getBoolean(
				context.getParameters(),
				PortletDataHandlerKeys.PORTLET_DATA + "_" +
					PortletConstants.getRootPortletId(portlet.getPortletId()));
		}
		else {
			exportPortletData = MapUtil.getBoolean(
				context.getParameters(), PortletDataHandlerKeys.PORTLET_DATA);
		}

		boolean exportPortletDataAll = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PORTLET_DATA_ALL);
		boolean exportPortletSetup = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PORTLET_SETUP);
		boolean exportPortletUserPreferences = MapUtil.getBoolean(
			context.getParameters(),
			PortletDataHandlerKeys.PORTLET_USER_PREFERENCES);

		if (exportPortletDataAll) {
			exportPortletData = true;
		}

		LarDigestItem item = doDigest(portlet);

		Map<String, String> metaDataMap = item.getMetadata();

		if (metaDataMap == null) {
			metaDataMap = new HashMap<String, String>();
		}

		metaDataMap.put("portlet-id", portletId);
		metaDataMap.put("root-portlet-id",
			PortletConstants.getRootPortletId(portletId));
		metaDataMap.put("old-plid", String.valueOf(plid));
		metaDataMap.put("scope-layout-type",
			StringUtil.valueOf(context.getAttribute("scopeType")));
		metaDataMap.put("scope-layout-uuid",
			StringUtil.valueOf(context.getAttribute("scopeLayoutUuid")));

		metaDataMap.put("portlet-id", portletId);
		metaDataMap.put("layout-id", String.valueOf(layoutId));

	/*	if (exportPortletSetup) {
			digestPortletPreferences(
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, false,
				portlet.getPortletId(), metaDataMap);

			digestPortletPreferences(
				context.getScopeGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				false, portlet.getPortletId(), metaDataMap);

			digestPortletPreferences(
				context.getCompanyId(), PortletKeys.PREFS_OWNER_TYPE_COMPANY,
				false, portlet.getPortletId(), metaDataMap);
		}

		if (exportPortletUserPreferences) {
			List<PortletPreferences> portletPreferencesList =
				PortletPreferencesLocalServiceUtil.getPortletPreferences(
					PortletKeys.PREFS_OWNER_TYPE_USER, context.getPlid(),
					portlet.getPortletId());

			for (PortletPreferences portletPreferences :
					portletPreferencesList) {

				boolean defaultUser = false;

				if (portletPreferences.getOwnerId() ==
						PortletKeys.PREFS_OWNER_ID_DEFAULT) {

					defaultUser = true;
				}

				digestPortletPreferences(
					portletPreferences.getOwnerId(),
					PortletKeys.PREFS_OWNER_TYPE_USER, defaultUser,
					portlet.getPortletId(), metaDataMap);
			}

			try {
				PortletPreferences groupPortletPreferences =
					PortletPreferencesLocalServiceUtil.getPortletPreferences(
						context.getScopeGroupId(),
						PortletKeys.PREFS_OWNER_TYPE_GROUP,
						PortletKeys.PREFS_PLID_SHARED, portlet.getPortletId());

				digestPortletPreference(
					groupPortletPreferences, portlet.getPortletId(),
					context.getScopeGroupId(),
					PortletKeys.PREFS_OWNER_TYPE_GROUP, false,
					PortletKeys.PREFS_PLID_SHARED, metaDataMap);
			}
			catch (NoSuchPortletPreferencesException nsppe) {
			}
		} */

		String path = item.getPath();

		if (!context.isPathProcessed(path)) {
			//context.addPrimaryKey(String.class, path);

			context.getLarDigest().write(item);
		}
	}

	@Override
	public LarDigestItem doDigest(Portlet portlet) throws Exception {
		return new LarDigestItemImpl();
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		importEntityPermissions(item.getPermissions());
	}

	@Override
	public void importData(LarDigestItem item) throws Exception{
		DataHandlerContext context = getDataHandlerContext();

		Map<String, String> metadata = item.getMetadata();

		Map parameterMap = context.getParameters();

		boolean deletePortletData = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.DELETE_PORTLET_DATA);
		boolean importPermissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PERMISSIONS);
		boolean importPortletData = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_DATA);
		boolean importPortletArchivedSetups = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_ARCHIVED_SETUPS);
		boolean importPortletSetup = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_SETUP);
		boolean importPortletUserPreferences = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PORTLET_USER_PREFERENCES);

		String portletPath = metadata.get("path");
		String portletId = metadata.get("portlet-id");
		long layoutId = GetterUtil.getLong(metadata.get("layout-id"));
		long oldPlid = GetterUtil.getLong(metadata.get("old-plid"));

		long groupId = context.getGroupId();
		Group group = GroupLocalServiceUtil.getGroup(groupId);

		Map<Long, Layout> newLayoutsMap =
			(Map<Long, Layout>)context.getAttribute("newLayoutsMap");

		if (deletePortletData) {
			long plid = newLayoutsMap.get(layoutId).getPlid();

			context.setPlid(plid);

			deletePortletData(context, portletId, plid);
		}

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			context.getCompanyId(), portletId);

		if (!portlet.isActive() || portlet.isUndeployedPortlet()) {
			return;
		}

		Layout layout = newLayoutsMap.get(layoutId);

		long plid = LayoutConstants.DEFAULT_PLID;

		if (layout != null) {
			plid = layout.getPlid();
		}

		layout = LayoutUtil.fetchByPrimaryKey(plid);

		if ((layout == null) && !group.isCompany()) {
			return;
		}

		context.setPlid(plid);
		context.setOldPlid(oldPlid);

		// The order of the import is important. You must always import
		// the portlet preferences first, then the portlet data, then
		// the portlet permissions. The import of the portlet data
		// assumes that portlet preferences already exist.

		setPortletScope(context, item);

		long portletPreferencesGroupId = groupId;

		//boolean importData = importPortletData && (portletDataElement != null);

		try {
			if ((layout != null) && !group.isCompany()) {
				portletPreferencesGroupId = layout.getGroupId();
			}

			// Portlet preferences

			/*importPortletPreferences(
				context, layoutSet.getCompanyId(),
				portletPreferencesGroupId, layout, null, portletElement,
				importPortletSetup, importPortletArchivedSetups,
				importPortletUserPreferences, false, importData);

			// Portlet data

			if (importData) {
				importPortletData(
					portletDataContext, portletId, plid,
					portletDataElement);
			}  */
		}
		finally {
			resetPortletScope(context, portletPreferencesGroupId);
		}

		// Portlet permissions

		if (importPermissions) {
			importPortletPermissions(layout, portletId, item);
		}

		// Archived setups

		/*
		importPortletPreferences(
			context, layoutSet.getCompanyId(), groupId, null,
			null, portletElement, importPortletSetup,
			importPortletArchivedSetups, importPortletUserPreferences,
			false, importData);
		*/
	}

	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	public boolean isAlwaysExportable() {
		return _ALWAYS_EXPORTABLE;
	}

	public boolean isAlwaysStaged() {
		return _ALWAYS_STAGED;
	}

	public boolean isDataLocalized() {
		return _DATA_LOCALIZED;
	}

	public boolean isPublishToLiveByDefault() {
		return _PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	@Override
	public Portlet getEntity(String classPK) {
		return null;
	}

	protected void deletePortletData(
			DataHandlerContext context, String portletId, long plid)
		throws Exception {

		long ownerId = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		int ownerType = PortletKeys.PREFS_OWNER_TYPE_LAYOUT;

		PortletPreferences portletPreferences = null;
		/*	context.fetchByO_O_P_P(
				ownerId, ownerType, plid, portletId); */

		if (portletPreferences == null) {
			portletPreferences =
					new com.liferay.portal.model.impl.PortletPreferencesImpl();
		}

		String xml = deletePortletData(
			context, portletId, portletPreferences);

		if (xml != null) {
			PortletPreferencesLocalServiceUtil.updatePreferences(
					ownerId, ownerType, plid, portletId, xml);
		}
	}

	protected String deletePortletData(
			DataHandlerContext context, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			context.getCompanyId(), portletId);

		if (portlet == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Do not delete portlet data for " + portletId +
						" because the portlet does not exist");
			}

			return null;
		}

		com.liferay.portal.kernel.lar.PortletDataHandler portletDataHandler =
				portlet.getLegacyPortletDataHandlerInstance();

		if (portletDataHandler == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Do not delete portlet data for " + portletId +
						" because the portlet does not have a " +
							"PortletDataHandler");
			}

			return null;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Deleting data for " + portletId);
		}

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)
				PortletPreferencesFactoryUtil.fromDefaultXML(
					portletPreferences.getPreferences());

		try {
			/*portletPreferencesImpl =
				(PortletPreferencesImpl)portletDataHandler.deleteData(
					context, portletId, portletPreferencesImpl);*/
		}
		finally {
			context.setGroupId(context.getScopeGroupId());
		}

		if (portletPreferencesImpl == null) {
			return null;
		}

		return PortletPreferencesFactoryUtil.toXML(portletPreferencesImpl);
	}


	protected void digestPortletPreference(
			PortletPreferences portletPreferences, String portletId,
			long ownerId, int ownerType, boolean defaultUser, long plid,
			Map<String, String> metadataMap)
		throws Exception {

		String preferencesXML = portletPreferences.getPreferences();

		if (Validator.isNull(preferencesXML)) {
			preferencesXML = PortletConstants.DEFAULT_PREFERENCES;
		}

		String rootPotletId = PortletConstants.getRootPortletId(portletId);

		if (rootPotletId.equals(PortletKeys.ASSET_PUBLISHER)) {
			/*preferencesXML = updateAssetPublisherPortletPreferences(
				preferencesXML, plid);*/
		}

		Document document = SAXReaderUtil.read(preferencesXML);

		metadataMap.put("owner-id", String.valueOf(ownerId));
		metadataMap.put("owner-type", String.valueOf(ownerType));
		metadataMap.put("default-user", String.valueOf(defaultUser));
		metadataMap.put("plid", String.valueOf(plid));
		metadataMap.put("portlet-id", portletId);

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) {
			PortletItem portletItem =
				PortletItemLocalServiceUtil.getPortletItem(ownerId);

			metadataMap.put(
				"archive-user-uuid", portletItem.getUserUuid());
			metadataMap.put("archive-name", portletItem.getName());
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) {
			User user = UserLocalServiceUtil.fetchUserById(ownerId);

			if (user == null) {
				return;
			}

			metadataMap.put("user-uuid", user.getUserUuid());
		}

		List<Node> nodes = document.selectNodes(
			"/portlet-preferences/preference[name/text() = " +
				"'last-publish-date']");

		for (Node node : nodes) {
			document.remove(node);
		}

		String path = getPortletPreferencesPath(
			portletId, ownerId, ownerType, plid);

		metadataMap.put("path", path);
	}
	
	protected void digestPortletPreferences(
			long ownerId, int ownerType, boolean defaultUser, String portletId,
			Map<String, String> metadataMap)
		throws Exception {

		DataHandlerContext context = getDataHandlerContext();

		long plid = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		Layout layout = null;

		try {
			plid = context.getPlid();
			layout = LayoutLocalServiceUtil.getLayout(plid);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("No plid is available from context: " +
					e.getMessage());
			}
		}

		PortletPreferences portletPreferences = null;

		if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_COMPANY) ||
			(ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) ||
			(ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED)) {

			plid = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		}

		try {
			portletPreferences =
				PortletPreferencesLocalServiceUtil.getPortletPreferences(
					ownerId, ownerType, plid, portletId);
		}
		catch (NoSuchPortletPreferencesException nsppe) {
			return;
		}

		LayoutTypePortlet layoutTypePortlet = null;

		if (layout != null) {
			layoutTypePortlet = (LayoutTypePortlet)layout.getLayoutType();
		}

		if ((layoutTypePortlet == null) ||
			layoutTypePortlet.hasPortletId(portletId)) {

			digestPortletPreference(
				portletPreferences, portletId, ownerId,
				ownerType, defaultUser, plid, metadataMap);
		}

		return;
	}

	protected String getPortletPreferencesPath(
		String portletId, long ownerId, int ownerType, long plid) {

		StringBundler sb = new StringBundler(8);

		sb.append(getPortletPath(portletId));
		sb.append("/preferences/");

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_COMPANY) {
			sb.append("company/");
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) {
			sb.append("group/");
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_LAYOUT) {
			sb.append("layout/");
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) {
			sb.append("user/");
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) {
			sb.append("archived/");
		}

		sb.append(ownerId);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append(plid);
		sb.append(CharPool.FORWARD_SLASH);
		sb.append("portlet-preferences.xml");

		return sb.toString();
	}

	protected void importPortletData(
			DataHandlerContext context, String portletId, long plid,
			Element portletDataElement)
		throws Exception {

		long ownerId = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		int ownerType = PortletKeys.PREFS_OWNER_TYPE_LAYOUT;

		PortletPreferences portletPreferences =
			PortletPreferencesUtil.fetchByO_O_P_P(
				ownerId, ownerType, plid, portletId);

		/*if (portletPreferences == null) {
			portletPreferences =
				new PortletPreferencesImpl();
		}

		String xml = importPortletData(
			context, portletId, portletPreferences,
			portletDataElement);


		if (xml != null) {
			PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, xml);
		}
		*/
	}

	protected String importPortletData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, Element portletDataElement)
		throws Exception {

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			portletDataContext.getCompanyId(), portletId);

		if (portlet == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Do not import portlet data for " + portletId +
						" because the portlet does not exist");
			}

			return null;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Importing data for " + portletId);
		}

		PortletPreferencesImpl portletPreferencesImpl = null;

		if (portletPreferences != null) {
			portletPreferencesImpl =
				(PortletPreferencesImpl)
					PortletPreferencesFactoryUtil.fromDefaultXML(
						portletPreferences.getPreferences());
		}

		String portletData = portletDataContext.getZipEntryAsString(
			portletDataElement.attributeValue("path"));

		/*portletPreferencesImpl =
			(PortletPreferencesImpl)importData(
				portletDataContext, portletId, portletPreferencesImpl,
				portletData);
		*/

		if (portletPreferencesImpl == null) {
			return null;
		}

		return PortletPreferencesFactoryUtil.toXML(portletPreferencesImpl);
	}

	protected void importPortletPreferences(
			DataHandlerContext portletDataContext, long companyId, long groupId,
			Layout layout, String portletId, Element parentElement,
			boolean importPortletSetup, boolean importPortletArchivedSetups,
			boolean importPortletUserPreferences, boolean preserveScopeLayoutId,
			boolean importPortletData)
		throws Exception {

		long defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
		long plid = 0;
		String scopeType = StringPool.BLANK;
		String scopeLayoutUuid = StringPool.BLANK;

		if (layout != null) {
			plid = layout.getPlid();

			if (preserveScopeLayoutId && (portletId != null)) {
				javax.portlet.PortletPreferences jxPreferences =
					PortletPreferencesFactoryUtil.getLayoutPortletSetup(
						layout, portletId);

				scopeType = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeType", null));
				scopeLayoutUuid = GetterUtil.getString(
					jxPreferences.getValue("lfrScopeLayoutUuid", null));

				//portletDataContext.setScopeType(scopeType);
				//portletDataContext.setScopeLayoutUuid(scopeLayoutUuid);
			}
		}

		List<Element> portletPreferencesElements = parentElement.elements(
			"portlet-preferences");

		for (Element portletPreferencesElement : portletPreferencesElements) {
			String path = portletPreferencesElement.attributeValue("path");

			if (!portletDataContext.isPathProcessed(path)) {
				String xml = null;

				Element element = null;

				try {
					xml = getZipEntryAsString(path);

					Document preferencesDocument = SAXReaderUtil.read(xml);

					element = preferencesDocument.getRootElement();
				}
				catch (DocumentException de) {
					throw new SystemException(de);
				}

				long ownerId = GetterUtil.getLong(
					element.attributeValue("owner-id"));
				int ownerType = GetterUtil.getInteger(
					element.attributeValue("owner-type"));

				if (ownerType == PortletKeys.PREFS_OWNER_TYPE_COMPANY) {
					continue;
				}

				if (((ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) ||
					(ownerType == PortletKeys.PREFS_OWNER_TYPE_LAYOUT)) &&
					!importPortletSetup) {

					continue;
				}

				if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) &&
					!importPortletArchivedSetups) {

					continue;
				}

				if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) &&
					(ownerId != PortletKeys.PREFS_OWNER_ID_DEFAULT) &&
					!importPortletUserPreferences) {

					continue;
				}

				if (ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) {
					plid = PortletKeys.PREFS_PLID_SHARED;
					ownerId = portletDataContext.getScopeGroupId();
				}

				boolean defaultUser = GetterUtil.getBoolean(
					element.attributeValue("default-user"));

				if (portletId == null) {
					portletId = element.attributeValue("portlet-id");
				}

				if (ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) {
					portletId = PortletConstants.getRootPortletId(portletId);

					String userUuid = element.attributeValue(
						"archive-user-uuid");
					String name = element.attributeValue("archive-name");

					long userId = portletDataContext.getUserId(userUuid);

					PortletItem portletItem =
						PortletItemLocalServiceUtil.updatePortletItem(
							userId, groupId, name, portletId,
							PortletPreferences.class.getName());

					plid = 0;
					ownerId = portletItem.getPortletItemId();
				}
				else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) {
					String userUuid = element.attributeValue("user-uuid");

					ownerId = portletDataContext.getUserId(userUuid);
				}

				if (defaultUser) {
					ownerId = defaultUserId;
				}

				String rootPotletId = PortletConstants.getRootPortletId(
					portletId);

				// toDo: make updatePortletPreferences general!
				/*if (rootPotletId.equals(PortletKeys.ASSET_PUBLISHER)) {
					xml = updateAssetPublisherPortletPreferences(
						portletDataContext, companyId, ownerId, ownerType, plid,
						portletId, xml);
				}

				updatePortletPreferences(
					portletDataContext, ownerId, ownerType, plid, portletId,
					xml, importPortletData);
				*/
			}
		}

		if (preserveScopeLayoutId && (layout != null)) {
			javax.portlet.PortletPreferences jxPreferences =
				PortletPreferencesFactoryUtil.getLayoutPortletSetup(
					layout, portletId);

			try {
				jxPreferences.setValue("lfrScopeType", scopeType);
				jxPreferences.setValue("lfrScopeLayoutUuid", scopeLayoutUuid);

				jxPreferences.store();
			}
			finally {
				//portletDataContext.setScopeType(scopeType);
				//portletDataContext.setScopeLayoutUuid(scopeLayoutUuid);
			}
		}
	}

	protected void importPortletPermissions(
			Layout layout, String portletId, LarDigestItem item)
		throws Exception {

		Map<String, List<String>> permissions = item.getPermissions();

		if (permissions != null) {
			String resourceName = PortletConstants.getRootPortletId(portletId);

			String resourcePrimKey = PortletPermissionUtil.getPrimaryKey(
					layout.getPlid(), portletId);

			importPermissions(
				resourceName, resourcePrimKey, permissions);
		}
	}

	protected void readAssetLinks(DataHandlerContext context)
			throws Exception {

		String xml = getZipEntryAsString(getSourceRootPath() + "/links.xml");

		if (xml == null) {
			return;
		}

		Document document = SAXReaderUtil.read(xml);

		Element rootElement = document.getRootElement();

		List<Element> assetLinkElements = rootElement.elements("asset-link");

		for (Element assetLinkElement : assetLinkElements) {
			String sourceUuid = GetterUtil.getString(
				assetLinkElement.attributeValue("source-uuid"));
			String[] assetEntryUuidArray = StringUtil.split(
				GetterUtil.getString(
					assetLinkElement.attributeValue("target-uuids")));
			int assetLinkType = GetterUtil.getInteger(
				assetLinkElement.attributeValue("type"));

			List<Long> assetEntryIds = new ArrayList<Long>();

			for (String assetEntryUuid : assetEntryUuidArray) {
				try {
					AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
						context.getScopeGroupId(), assetEntryUuid);

					assetEntryIds.add(assetEntry.getEntryId());
				}
				catch (NoSuchEntryException nsee) {
				}
			}

			if (assetEntryIds.isEmpty()) {
				continue;
			}

			long[] assetEntryIdsArray = ArrayUtil.toArray(
				assetEntryIds.toArray(new Long[assetEntryIds.size()]));

			try {
				AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
					context.getScopeGroupId(), sourceUuid);

				AssetLinkLocalServiceUtil.updateLinks(
					assetEntry.getUserId(), assetEntry.getEntryId(),
					assetEntryIdsArray, assetLinkType);
			}
			catch (NoSuchEntryException nsee) {
			}
		}
	}


	protected void resetPortletScope(
		DataHandlerContext context, long groupId) {

		context.setScopeGroupId(groupId);
		//context.setScopeLayoutUuid(StringPool.BLANK);
		//context.setScopeType(StringPool.BLANK);
	}

	protected void setPortletScope(
		DataHandlerContext context, LarDigestItem item) {

		// Portlet data scope

		Map<String, String> metadata = item.getMetadata();

		String scopeLayoutUuid = GetterUtil.getString(
			metadata.get("scope-layout-uuid"));
		String scopeLayoutType = GetterUtil.getString(
				metadata.get("scope-layout-type"));

		//context.setScopeLayoutUuid(scopeLayoutUuid);
		//context.setScopeType(scopeLayoutType);

		// Layout scope

		try {
			Group scopeGroup = null;

			if (scopeLayoutType.equals("company")) {
				scopeGroup = GroupLocalServiceUtil.getCompanyGroup(
					context.getCompanyId());
			}
			else if (Validator.isNotNull(scopeLayoutUuid)) {
				Layout scopeLayout =
					LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
						scopeLayoutUuid, context.getGroupId());

				if (scopeLayout.hasScopeGroup()) {
					scopeGroup = scopeLayout.getScopeGroup();
				}
				else {
					String name = String.valueOf(scopeLayout.getPlid());

					scopeGroup = GroupLocalServiceUtil.addGroup(
						context.getUserId(null),
						GroupConstants.DEFAULT_PARENT_GROUP_ID,
						Layout.class.getName(), scopeLayout.getPlid(), name,
						null, 0, null, false, true, null);
				}

				Group group = scopeLayout.getGroup();

				if (group.isStaged() && !group.isStagedRemotely()) {
					try {
						Layout oldLayout =
							LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
								scopeLayoutUuid,
								context.getSourceGroupId());

						Group oldScopeGroup = oldLayout.getScopeGroup();

						oldScopeGroup.setLiveGroupId(scopeGroup.getGroupId());

						GroupLocalServiceUtil.updateGroup(oldScopeGroup, true);
					}
					catch (NoSuchLayoutException nsle) {
						if (_log.isWarnEnabled()) {
							_log.warn(nsle);
						}
					}
				}

				context.setScopeGroupId(scopeGroup.getGroupId());
			}
		}
		catch (PortalException pe) {
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void updatePortletPreferences(
			PortletDataContext portletDataContext, long ownerId, int ownerType,
			long plid, String portletId, String xml, boolean importData)
		throws Exception {

		if (importData) {
			PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, xml);
		}
		else {
			Portlet portlet = PortletLocalServiceUtil.getPortletById(
				portletDataContext.getCompanyId(), portletId);

			if (portlet == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Do not update portlet preferences for " + portletId +
							" because the portlet does not exist");
				}

				return;
			}

			PortletDataHandler portletDataHandler = null;
			//portlet.getPortletDataHandlerInstance();

			if (portletDataHandler == null) {
				PortletPreferencesLocalServiceUtil.updatePreferences(
						ownerId, ownerType, plid, portletId, xml);

				return;
			}

			// Portlet preferences to be updated only when importing data

			String[] dataPortletPreferences = null;
				//portletDataHandler.getDataPortletPreferences();

			// Current portlet preferences

			javax.portlet.PortletPreferences portletPreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
					portletDataContext.getCompanyId(), ownerId, ownerType, plid,
					portletId);

			// New portlet preferences

			javax.portlet.PortletPreferences jxPreferences =
				PortletPreferencesFactoryUtil.fromXML(
					portletDataContext.getCompanyId(), ownerId, ownerType, plid,
					portletId, xml);

			Enumeration<String> enu = jxPreferences.getNames();

			while (enu.hasMoreElements()) {
				String name = enu.nextElement();

				if (!ArrayUtil.contains(dataPortletPreferences, name)) {
					String value = GetterUtil.getString(
						jxPreferences.getValue(name, null));

					portletPreferences.setValue(name, value);
				}
			}

			PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, portletPreferences);
		}
	}

	private static final Log _log =
		LogFactoryUtil.getLog(PortletDataHandlerImpl.class);

	private static final boolean _ALWAYS_EXPORTABLE = false;

	private static final boolean _ALWAYS_STAGED = false;

	private static final boolean _PUBLISH_TO_LIVE_BY_DEFAULT = false;

	private static final boolean _DATA_LOCALIZED = false;

}
