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

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.PortletItem;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PortletItemLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class PortletPreferencesDataHandlerImpl
	extends BaseDataHandlerImpl<PortletPreferences>
	implements PortletPreferencesDataHandler {

	@Override
	public LarDigestItem doDigest(PortletPreferences preferences)
		throws Exception {

		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(preferences);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		Map metadataMap = new HashMap<String, String>();

		if (preferences.getOwnerType() ==
				PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) {

			PortletItem portletItem =
				PortletItemLocalServiceUtil.getPortletItem(
					preferences.getOwnerId());

			metadataMap.put(
				METADATA_ARCHIVE_USER_UUID, portletItem.getUserUuid());
			metadataMap.put(METADATA_ARCHIVE_NAME, portletItem.getName());
		}
		else if (preferences.getOwnerType() ==
				PortletKeys.PREFS_OWNER_TYPE_USER) {

			User user = UserLocalServiceUtil.fetchUserById(
				preferences.getOwnerId());

			if (user == null) {
				return null;
			}

			metadataMap.put(METADATA_USER_UUID, user.getUserUuid());
		}

		digestItem.setMetadata(metadataMap);

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath(path);
		digestItem.setType(PortletPreferences.class.getName());
		digestItem.setClassPK(
			StringUtil.valueOf(preferences.getPortletPreferencesId()));

		return digestItem;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		boolean importPortletData = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PORTLET_DATA);
		boolean importPortletSetup = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PORTLET_SETUP);
		boolean importPortletArchivedSetups = MapUtil.getBoolean(
			context.getParameters(),
			PortletDataHandlerKeys.PORTLET_ARCHIVED_SETUPS);
		boolean importPortletUserPreferences = MapUtil.getBoolean(
			context.getParameters(),
			PortletDataHandlerKeys.PORTLET_USER_PREFERENCES);

		PortletPreferences portletPreferences =
			(PortletPreferences)getZipEntryAsObject(item.getPath());

		Map<String, String> metadata = item.getMetadata();

		long ownerId = GetterUtil.getLong(portletPreferences.getOwnerId());
		int ownerType = GetterUtil.getInteger(
			portletPreferences.getOwnerType());

		long plid = portletPreferences.getPlid();
		String portletId = portletPreferences.getPortletId();

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_COMPANY) {
			return;
		}

		if (((ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) ||
			(ownerType == PortletKeys.PREFS_OWNER_TYPE_LAYOUT)) &&
			!importPortletSetup) {

			return;
		}

		if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) &&
			!importPortletArchivedSetups) {

			return;
		}

		if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) &&
			(ownerId != PortletKeys.PREFS_OWNER_ID_DEFAULT) &&
			!importPortletUserPreferences) {

			return;
		}

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) {
			plid = PortletKeys.PREFS_PLID_SHARED;
			ownerId = context.getScopeGroupId();
		}

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED) {
			portletId = PortletConstants.getRootPortletId(portletId);

			String userUuid = MapUtil.getString(
				metadata, METADATA_ARCHIVE_USER_UUID);
			String name = MapUtil.getString(metadata, METADATA_ARCHIVE_NAME);

			long userId = context.getUserId(userUuid);

			PortletItem portletItem =
				PortletItemLocalServiceUtil.updatePortletItem(
					userId, context.getGroupId(), name, portletId,
					PortletPreferences.class.getName());

			plid = 0;
			ownerId = portletItem.getPortletItemId();
		}
		else if (ownerType == PortletKeys.PREFS_OWNER_TYPE_USER) {
			String userUuid = MapUtil.getString(metadata, METADATA_USER_UUID);

			ownerId = context.getUserId(userUuid);
		}

		String rootPotletId = PortletConstants.getRootPortletId(
			portletId);

		if (rootPotletId.equals(PortletKeys.ASSET_PUBLISHER)) {
			/*xml = updateAssetPublisherPortletPreferences(
				portletDataContext, companyId, ownerId, ownerType, plid,
				portletId, xml);*/
		}

		// update portlet preferences

		if (importPortletData) {
			javax.portlet.PortletPreferences jxPreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
					context.getCompanyId(), portletPreferences.getOwnerId(),
					portletPreferences.getOwnerType(),
					portletPreferences.getPlid(),
					portletPreferences.getPortletId());

			PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, jxPreferences);
		}
		else {
			Portlet portlet = PortletLocalServiceUtil.getPortletById(
				context.getCompanyId(), portletId);

			PortletDataHandler portletDataHandler = null;

			// Portlet preferences to be updated only when importing data

			//TODO fix this
			String[] dataPortletPreferences = new String[0];
				//portletDataHandler.getDataPortletPreferences();

			// Current portlet preferences

			javax.portlet.PortletPreferences oldJxPreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
					context.getCompanyId(), ownerId, ownerType, plid,
					portletId);

			// New portlet preferences

			javax.portlet.PortletPreferences jxPreferences =
				PortletPreferencesFactoryUtil.fromXML(
					context.getCompanyId(), ownerId, ownerType, plid,
					portletId, portletPreferences.toXmlString());

			Enumeration<String> enu = jxPreferences.getNames();

			while (enu.hasMoreElements()) {
				String name = enu.nextElement();

				if (!ArrayUtil.contains(dataPortletPreferences, name)) {
					String value = GetterUtil.getString(
							jxPreferences.getValue(name, null));

					oldJxPreferences.setValue(name, value);
				}
			}

			PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, oldJxPreferences);
		}
	}

	@Override
	public PortletPreferences getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				long preferencesId = Long.valueOf(classPK);

				PortletPreferences preferences =
					PortletPreferencesLocalServiceUtil.getPortletPreferences(
						preferencesId);

				return preferences;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	@Override
	public String getEntityPath(PortletPreferences preferences) {
		StringBundler sb = new StringBundler();

		sb.append(StringPool.FORWARD_SLASH);
		sb.append("preferences");
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(preferences.getPortletId());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(preferences.getPortletId() + ".xml");

		return sb.toString();
	}

	private static final String METADATA_ARCHIVE_USER_UUID =
		"archive-user-uuid";
	private static final String METADATA_ARCHIVE_NAME = "archive-name";
	private static final String METADATA_USER_UUID = "user-uuid";

}
