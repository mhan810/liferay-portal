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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.base.StagingServiceBaseImpl;
import com.liferay.portal.service.permission.GroupPermissionUtil;

import java.util.Map;

/**
 * The implementation of the staging remote service.
 *
 * @author Brian Wing Shun Chan
 * @author Mate Thurzo
 * @see    com.liferay.portal.service.base.StagingServiceBaseImpl
 * @see    com.liferay.portal.service.StagingServiceUtil
 */
public class StagingServiceImpl extends StagingServiceBaseImpl {

	@Override
	public void importTransferredLayouts(
			long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, String fileName)
		throws PortalException, SystemException {

		GroupPermissionUtil.check(
			getPermissionChecker(), groupId, ActionKeys.EXPORT_IMPORT_LAYOUTS);

		stagingLocalService.importTransferredLayouts(
			getUserId(), groupId, privateLayout, parameterMap, fileName);
	}

	@Override
	public void receiveLayouts(long groupId, String fileName, byte[] bytes)
		throws PortalException, SystemException {

		GroupPermissionUtil.check(
			getPermissionChecker(), groupId, ActionKeys.EXPORT_IMPORT_LAYOUTS);

		stagingLocalService.receiveLayouts(fileName, bytes);
	}

	@Override
	public void validateTransferredLayouts(
			long groupId, String fileName, String checksum)
		throws PortalException, SystemException {

		GroupPermissionUtil.check(
			getPermissionChecker(), groupId, ActionKeys.EXPORT_IMPORT_LAYOUTS);

		stagingLocalService.validateTransferredLayouts(fileName, checksum);
	}

}