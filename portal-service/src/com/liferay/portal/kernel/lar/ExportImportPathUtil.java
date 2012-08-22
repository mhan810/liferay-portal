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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.util.PortletKeys;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class ExportImportPathUtil {

	public static String getEntityPath(Object object) {
		if (object instanceof BaseModel) {
			BaseModel baseModel = (BaseModel)object;

			Map<String, Object> modelAttributes =
				baseModel.getModelAttributes();

			StringBundler sb = new StringBundler();

			sb.append(StringPool.FORWARD_SLASH);
			sb.append("group");
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(modelAttributes.get("groupId"));
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getModelClassName());
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getPrimaryKeyObj() + ".xml");

			return sb.toString();
		}

		return StringPool.BLANK;
	}

	public static String getPermissionPath(Object entity) {
		String path = StringPool.BLANK;

		if (entity instanceof Portlet) {
			path = getPortletPath((Portlet)entity);
		}
		else {
			path = getEntityPath(entity);
		}

		String permissionPath = StringUtil.replace(
			path, ".xml", "-permissions.xml");

		return permissionPath;
	}

	public static String getPortletPath(Portlet portlet) {
		StringBundler sb = new StringBundler();

		sb.append("/portlets/");
		sb.append(portlet.getInstanceId());
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(portlet.getPortletId() + ".xml");

		return sb.toString();
	}

	public static String getPortletPreferencesPath(
		String portletId, long ownerId, int ownerType, long plid) {

		StringBundler sb = new StringBundler(8);

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

}