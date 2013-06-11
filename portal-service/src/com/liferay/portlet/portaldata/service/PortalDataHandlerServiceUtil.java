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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * Provides the remote service utility for PortalDataHandler. This utility wraps
 * {@link com.liferay.portlet.portaldata.service.impl.PortalDataHandlerServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see PortalDataHandlerService
 * @see com.liferay.portlet.portaldata.service.base.PortalDataHandlerServiceBaseImpl
 * @see com.liferay.portlet.portaldata.service.impl.PortalDataHandlerServiceImpl
 * @generated
 */
public class PortalDataHandlerServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portlet.portaldata.service.impl.PortalDataHandlerServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.io.File exportPortalDataAsFile(java.lang.Class type,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .exportPortalDataAsFile(type, parameters, startDate, endDate);
	}

	public static java.io.File exportPortalDataAsFile(
		java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .exportPortalDataAsFile(portletId, parameters, startDate,
			endDate);
	}

	public static void importPortalData(java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService().importPortalData(portletId, parameters, file);
	}

	public static void importPortalData(java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.InputStream data)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService().importPortalData(portletId, parameters, data);
	}

	public static PortalDataHandlerService getService() {
		if (_service == null) {
			_service = (PortalDataHandlerService)PortalBeanLocatorUtil.locate(PortalDataHandlerService.class.getName());

			ReferenceRegistry.registerReference(PortalDataHandlerServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(PortalDataHandlerService service) {
	}

	private static PortalDataHandlerService _service;
}