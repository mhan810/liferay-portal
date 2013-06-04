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

import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.social.model.SocialActivitySet;

/**
 * @author Zsolt Berentey
 */
public class SocialActivitySetPermissionImpl
	implements SocialActivitySetPermission {

	public boolean contains(
		PermissionChecker permissionChecker, SocialActivitySet activitySet,
		String actionId) {

		if (permissionChecker.hasOwnerPermission(
				activitySet.getCompanyId(), SocialActivitySet.class.getName(),
				activitySet.getActivitySetId(), activitySet.getUserId(),
				actionId)) {

			return true;
		}

		if (permissionChecker.hasPermission(
				activitySet.getGroupId(), SocialActivitySet.class.getName(),
				activitySet.getActivitySetId(), actionId)) {

			return true;
		}

		if (permissionChecker.hasOwnerPermission(
				activitySet.getCompanyId(), activitySet.getClassName(),
				activitySet.getClassPK(), activitySet.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			activitySet.getGroupId(), activitySet.getClassName(),
			activitySet.getClassPK(), actionId);
	}

}