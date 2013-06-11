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
import com.liferay.portal.lar.PortalDataExporter;
import com.liferay.portal.lar.PortalDataImporter;
import com.liferay.portlet.portaldata.service.base.PortalDataHandlerLocalServiceBaseImpl;

import java.io.File;
import java.io.InputStream;

import java.util.Date;
import java.util.Map;

/**
 * The implementation of the portal data handler local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portlet.portaldata.service.PortalDataHandlerLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.portaldata.service.base.PortalDataHandlerLocalServiceBaseImpl
 * @see com.liferay.portlet.portaldata.service.PortalDataHandlerLocalServiceUtil
 */
public class PortalDataHandlerLocalServiceImpl
	extends PortalDataHandlerLocalServiceBaseImpl {

	public File exportPortalDataAsFile(
			long companyId, Class type, Map<String, String[]> parameters,
			Date startDate, Date endDate)
		throws PortalException, SystemException {

		PortalDataExporter portalDataExporter = new PortalDataExporter();

		try {
			return portalDataExporter.exportPortalDataAsFile(
				companyId, type, parameters, startDate, endDate);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
	}

	public File exportPortalDataAsFile(
			long companyId, String portletId, Map<String, String[]> parameters,
			Date startDate, Date endDate)
		throws PortalException, SystemException {

		PortalDataExporter portalDataExporter = new PortalDataExporter();

		try {
			return portalDataExporter.exportPortalDataAsFile(
				companyId, portletId, parameters, startDate, endDate);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
	}

	public void importPortalData(
			long userId, long companyId, String portletId,
			Map<String, String[]> parameters, File file)
		throws PortalException, SystemException {

		PortalDataImporter portalDataImporter = new PortalDataImporter();

		try {
			portalDataImporter.importPortalData(
				userId, companyId, portletId, parameters, file);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
	}

	public void importPortalData(
			long userId, long companyId, String portletId,
			Map<String, String[]> parameters, InputStream data)
		throws PortalException, SystemException {

		PortalDataImporter portalDataImporter = new PortalDataImporter();

		try {
			portalDataImporter.importPortalData(
				userId, companyId, portletId, parameters, data);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
	}
}