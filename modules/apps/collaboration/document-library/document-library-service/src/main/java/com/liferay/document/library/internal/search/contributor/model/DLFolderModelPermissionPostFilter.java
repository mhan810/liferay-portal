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

package com.liferay.document.library.internal.search.contributor.model;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.search.contributor.model.ModelPermissionPostFilter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFolder"
	},
	service = ModelPermissionPostFilter.class
)
public class DLFolderModelPermissionPostFilter
	implements ModelPermissionPostFilter {

	@Override
	public boolean hasPermission(
		PermissionChecker permissionChecker, String entryClassName,
		long entryClassPK, String actionId) {

		DLFolder dlFolder = dlFolderLocalService.fetchDLFolder(entryClassPK);

		return contains(permissionChecker, dlFolder, ActionKeys.VIEW);
	}

	protected boolean contains(
		PermissionChecker permissionChecker, DLFolder dlFolder,
		String actionId) {

		try {
			return dlFolderModelResourcePermission.contains(
				permissionChecker, dlFolder, actionId);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			return false;
		}
	}

	@Reference
	protected DLFolderLocalService dlFolderLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.document.library.kernel.model.DLFolder)",
		unbind = "-"
	)
	protected ModelResourcePermission<DLFolder> dlFolderModelResourcePermission;

	private static final Log _log = LogFactoryUtil.getLog(
		DLFolderModelPermissionPostFilter.class);

}