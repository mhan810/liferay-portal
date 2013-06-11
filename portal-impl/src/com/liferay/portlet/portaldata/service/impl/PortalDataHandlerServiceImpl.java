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

package com.liferay.portlet.portaldata.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.permission.PortalPermissionUtil;
import com.liferay.portlet.portaldata.service.base.PortalDataHandlerServiceBaseImpl;

import java.io.File;
import java.io.InputStream;

import java.util.Date;
import java.util.Map;

/**
 * The implementation of the portal data handler remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portlet.portaldata.service.PortalDataHandlerService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.portaldata.service.base.PortalDataHandlerServiceBaseImpl
 * @see com.liferay.portlet.portaldata.service.PortalDataHandlerServiceUtil
 */
public class PortalDataHandlerServiceImpl
	extends PortalDataHandlerServiceBaseImpl {

	public File exportPortalDataAsFile(
			Class type, Map<String, String[]> parameters, Date startDate,
			Date endDate)
		throws PortalException, SystemException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, ActionKeys.EXPORT_PORTAL_DATA);

		long companyId = permissionChecker.getCompanyId();

		return portalDataHandlerLocalService.exportPortalDataAsFile(
			companyId, type, parameters, startDate, endDate);
	}

	public File exportPortalDataAsFile(
			String portletId, Map<String, String[]> parameters, Date startDate,
			Date endDate)
		throws PortalException, SystemException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, ActionKeys.EXPORT_PORTAL_DATA);

		long companyId = permissionChecker.getCompanyId();

		return portalDataHandlerLocalService.exportPortalDataAsFile(
			companyId, portletId, parameters, startDate, endDate);
	}

	public void importPortalData(
			String portletId, Map<String, String[]> parameters, File file)
		throws PortalException, SystemException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, ActionKeys.EXPORT_PORTAL_DATA);

		long companyId = permissionChecker.getCompanyId();
		long userId = permissionChecker.getUserId();

		portalDataHandlerLocalService.importPortalData(
			userId, companyId, portletId, parameters, file);
	}

	public void importPortalData(
			String portletId, Map<String, String[]> parameters,
			InputStream data)
		throws PortalException, SystemException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, ActionKeys.EXPORT_PORTAL_DATA);

		long companyId = permissionChecker.getCompanyId();
		long userId = permissionChecker.getUserId();

		portalDataHandlerLocalService.importPortalData(
			userId, companyId, portletId, parameters, data);
	}

}