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

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.contributor.model.ModelPermissionPostFilter;
import com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry"
	},
	service = ModelPermissionPostFilter.class
)
public class DLFileEntryModelPermissionPostFilter
	implements ModelPermissionPostFilter {

	@Override
	public boolean hasPermission(
		PermissionChecker permissionChecker, String entryClassName,
		long entryClassPK, String actionId) {

		try {
			return DLFileEntryPermission.contains(
				permissionChecker, entryClassPK, ActionKeys.VIEW);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			return false;
		}
	}

	@Override
	public boolean isVisible(long classPK, int status) {
		try {
			FileEntry fileEntry = dlAppLocalService.getFileEntry(classPK);

			FileVersion fileVersion = fileEntry.getFileVersion();

			return isVisible(fileVersion.getStatus(), status);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			return false;
		}
	}

	protected boolean isVisible(int entryStatus, int queryStatus) {
		if (((queryStatus != WorkflowConstants.STATUS_ANY) &&
			 (entryStatus == queryStatus)) ||
			(entryStatus != WorkflowConstants.STATUS_IN_TRASH)) {

			return true;
		}

		return false;
	}

	@Reference
	protected DLAppLocalService dlAppLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		DLFileEntryModelPermissionPostFilter.class);

}