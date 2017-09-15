/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.calendar.internal.search.contributor.model;

import com.liferay.calendar.service.permission.CalendarPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.search.contributor.model.ModelPermissionPostFilter;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"indexer.class.name=com.liferay.calendar.model.Calendar"},
	service = ModelPermissionPostFilter.class
)
public class CalendarModelPermissionPostFilter
	implements ModelPermissionPostFilter {

	@Override
	public boolean hasPermission(
		PermissionChecker permissionChecker, String entryClassName,
		long entryClassPK, String actionId) {

		try {
			return CalendarPermission.contains(
				permissionChecker, entryClassPK, ActionKeys.VIEW);
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

}