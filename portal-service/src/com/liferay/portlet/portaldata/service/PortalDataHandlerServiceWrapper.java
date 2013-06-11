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

package com.liferay.portlet.portaldata.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link PortalDataHandlerService}.
 *
 * @author    Brian Wing Shun Chan
 * @see       PortalDataHandlerService
 * @generated
 */
public class PortalDataHandlerServiceWrapper implements PortalDataHandlerService,
	ServiceWrapper<PortalDataHandlerService> {
	public PortalDataHandlerServiceWrapper(
		PortalDataHandlerService portalDataHandlerService) {
		_portalDataHandlerService = portalDataHandlerService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _portalDataHandlerService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_portalDataHandlerService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.io.File exportPortalDataAsFile(java.lang.Class type,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _portalDataHandlerService.exportPortalDataAsFile(type,
			parameters, startDate, endDate);
	}

	@Override
	public java.io.File exportPortalDataAsFile(java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _portalDataHandlerService.exportPortalDataAsFile(portletId,
			parameters, startDate, endDate);
	}

	@Override
	public void importPortalData(java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_portalDataHandlerService.importPortalData(portletId, parameters, file);
	}

	@Override
	public void importPortalData(java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.InputStream data)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_portalDataHandlerService.importPortalData(portletId, parameters, data);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public PortalDataHandlerService getWrappedPortalDataHandlerService() {
		return _portalDataHandlerService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedPortalDataHandlerService(
		PortalDataHandlerService portalDataHandlerService) {
		_portalDataHandlerService = portalDataHandlerService;
	}

	@Override
	public PortalDataHandlerService getWrappedService() {
		return _portalDataHandlerService;
	}

	@Override
	public void setWrappedService(
		PortalDataHandlerService portalDataHandlerService) {
		_portalDataHandlerService = portalDataHandlerService;
	}

	private PortalDataHandlerService _portalDataHandlerService;
}