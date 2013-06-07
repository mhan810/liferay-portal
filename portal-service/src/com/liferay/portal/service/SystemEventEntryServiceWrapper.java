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

package com.liferay.portal.service;

/**
 * Provides a wrapper for {@link SystemEventEntryService}.
 *
 * @author    Brian Wing Shun Chan
 * @see       SystemEventEntryService
 * @generated
 */
public class SystemEventEntryServiceWrapper implements SystemEventEntryService,
	ServiceWrapper<SystemEventEntryService> {
	public SystemEventEntryServiceWrapper(
		SystemEventEntryService systemEventEntryService) {
		_systemEventEntryService = systemEventEntryService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _systemEventEntryService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_systemEventEntryService.setBeanIdentifier(beanIdentifier);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public SystemEventEntryService getWrappedSystemEventEntryService() {
		return _systemEventEntryService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedSystemEventEntryService(
		SystemEventEntryService systemEventEntryService) {
		_systemEventEntryService = systemEventEntryService;
	}

	@Override
	public SystemEventEntryService getWrappedService() {
		return _systemEventEntryService;
	}

	@Override
	public void setWrappedService(
		SystemEventEntryService systemEventEntryService) {
		_systemEventEntryService = systemEventEntryService;
	}

	private SystemEventEntryService _systemEventEntryService;
}