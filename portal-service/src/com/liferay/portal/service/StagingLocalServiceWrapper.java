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
 * Provides a wrapper for {@link StagingLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see StagingLocalService
 * @generated
 */
public class StagingLocalServiceWrapper implements StagingLocalService,
	ServiceWrapper<StagingLocalService> {
	public StagingLocalServiceWrapper(StagingLocalService stagingLocalService) {
		_stagingLocalService = stagingLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _stagingLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_stagingLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.String getLarChecksum(java.io.File file) {
		return _stagingLocalService.getLarChecksum(file);
	}

	@Override
	public void importTransferredLayouts(long userId, long groupId,
		boolean privateLayout,
		java.util.Map<java.lang.String, java.lang.String[]> parameterMap,
		java.lang.String fileName)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_stagingLocalService.importTransferredLayouts(userId, groupId,
			privateLayout, parameterMap, fileName);
	}

	@Override
	public void receiveLayouts(java.lang.String fileName, byte[] bytes)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_stagingLocalService.receiveLayouts(fileName, bytes);
	}

	@Override
	public void transferLayouts(java.io.File file,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_stagingLocalService.transferLayouts(file, serviceContext);
	}

	@Override
	public void validateTransferredLayouts(java.lang.String fileName,
		java.lang.String checksum)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_stagingLocalService.validateTransferredLayouts(fileName, checksum);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public StagingLocalService getWrappedStagingLocalService() {
		return _stagingLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedStagingLocalService(
		StagingLocalService stagingLocalService) {
		_stagingLocalService = stagingLocalService;
	}

	@Override
	public StagingLocalService getWrappedService() {
		return _stagingLocalService;
	}

	@Override
	public void setWrappedService(StagingLocalService stagingLocalService) {
		_stagingLocalService = stagingLocalService;
	}

	private StagingLocalService _stagingLocalService;
}