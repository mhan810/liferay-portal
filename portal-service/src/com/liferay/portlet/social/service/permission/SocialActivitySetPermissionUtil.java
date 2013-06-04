/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.social.service.permission;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.social.model.SocialActivitySet;

/**
 * @author Zsolt Berentey
 */
public class SocialActivitySetPermissionUtil {

	public static boolean contains(
		PermissionChecker permissionChecker, SocialActivitySet activitySet,
		String actionId) {

		return getSocialActivitySetPermission().contains(
			permissionChecker, activitySet, actionId);
	}

	public static SocialActivitySetPermission getSocialActivitySetPermission() {
		return _socialActivitySetPermission;
	}

	public void setSocialActivitySetPermission(
		SocialActivitySetPermission socialActivitySetPermission) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_socialActivitySetPermission = socialActivitySetPermission;
	}

	private static SocialActivitySetPermission _socialActivitySetPermission;

}