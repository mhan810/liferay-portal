/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.mobile.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.MethodCache;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * The utility for the device profile action local service. This utility wraps {@link com.liferay.portal.mobile.service.impl.DeviceProfileActionLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Edward C. Han
 * @see DeviceProfileActionLocalService
 * @see com.liferay.portal.mobile.service.base.DeviceProfileActionLocalServiceBaseImpl
 * @see com.liferay.portal.mobile.service.impl.DeviceProfileActionLocalServiceImpl
 * @generated
 */
public class DeviceProfileActionLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.mobile.service.impl.DeviceProfileActionLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the device profile action to the database. Also notifies the appropriate model listeners.
	*
	* @param deviceProfileAction the device profile action
	* @return the device profile action that was added
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction addDeviceProfileAction(
		com.liferay.portal.mobile.model.DeviceProfileAction deviceProfileAction)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addDeviceProfileAction(deviceProfileAction);
	}

	/**
	* Creates a new device profile action with the primary key. Does not add the device profile action to the database.
	*
	* @param deviceProfileActionId the primary key for the new device profile action
	* @return the new device profile action
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction createDeviceProfileAction(
		long deviceProfileActionId) {
		return getService().createDeviceProfileAction(deviceProfileActionId);
	}

	/**
	* Deletes the device profile action with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param deviceProfileActionId the primary key of the device profile action
	* @throws PortalException if a device profile action with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static void deleteDeviceProfileAction(long deviceProfileActionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService().deleteDeviceProfileAction(deviceProfileActionId);
	}

	/**
	* Deletes the device profile action from the database. Also notifies the appropriate model listeners.
	*
	* @param deviceProfileAction the device profile action
	* @throws SystemException if a system exception occurred
	*/
	public static void deleteDeviceProfileAction(
		com.liferay.portal.mobile.model.DeviceProfileAction deviceProfileAction)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().deleteDeviceProfileAction(deviceProfileAction);
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the device profile action with the primary key.
	*
	* @param deviceProfileActionId the primary key of the device profile action
	* @return the device profile action
	* @throws PortalException if a device profile action with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction getDeviceProfileAction(
		long deviceProfileActionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileAction(deviceProfileActionId);
	}

	/**
	* Returns the device profile action with the UUID in the group.
	*
	* @param uuid the UUID of device profile action
	* @param groupId the group id of the device profile action
	* @return the device profile action
	* @throws PortalException if a device profile action with the UUID in the group could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction getDeviceProfileActionByUuidAndGroupId(
		java.lang.String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileActionByUuidAndGroupId(uuid, groupId);
	}

	/**
	* Returns a range of all the device profile actions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of device profile actions
	* @param end the upper bound of the range of device profile actions (not inclusive)
	* @return the range of device profile actions
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.mobile.model.DeviceProfileAction> getDeviceProfileActions(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileActions(start, end);
	}

	/**
	* Returns the number of device profile actions.
	*
	* @return the number of device profile actions
	* @throws SystemException if a system exception occurred
	*/
	public static int getDeviceProfileActionsCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileActionsCount();
	}

	/**
	* Updates the device profile action in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param deviceProfileAction the device profile action
	* @return the device profile action that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction updateDeviceProfileAction(
		com.liferay.portal.mobile.model.DeviceProfileAction deviceProfileAction)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateDeviceProfileAction(deviceProfileAction);
	}

	/**
	* Updates the device profile action in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param deviceProfileAction the device profile action
	* @param merge whether to merge the device profile action with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the device profile action that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.mobile.model.DeviceProfileAction updateDeviceProfileAction(
		com.liferay.portal.mobile.model.DeviceProfileAction deviceProfileAction,
		boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateDeviceProfileAction(deviceProfileAction, merge);
	}

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

	public static com.liferay.portal.mobile.model.DeviceProfileAction addDeviceProfileAction(
		long groupId, long deviceProfileId, long deviceProfileRuleId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		java.lang.String type,
		com.liferay.portal.kernel.util.UnicodeProperties typeSettingsProperties)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .addDeviceProfileAction(groupId, deviceProfileId,
			deviceProfileRuleId, nameMap, descriptionMap, type,
			typeSettingsProperties);
	}

	public static com.liferay.portal.mobile.model.DeviceProfileAction addDeviceProfileAction(
		long groupId, long deviceProfileId, long deviceProfileRuleId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		java.lang.String type, java.lang.String typeSettings)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .addDeviceProfileAction(groupId, deviceProfileId,
			deviceProfileRuleId, nameMap, descriptionMap, type, typeSettings);
	}

	public static void deleteDeviceProfileActions(long deviceProfileRuleId)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().deleteDeviceProfileActions(deviceProfileRuleId);
	}

	public static com.liferay.portal.mobile.model.DeviceProfileAction fetchDeviceProfileAction(
		long deviceProfileActionId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchDeviceProfileAction(deviceProfileActionId);
	}

	public static java.util.List<com.liferay.portal.mobile.model.DeviceProfileAction> getDeviceProfileActions(
		long deviceProfileRuleId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileActions(deviceProfileRuleId);
	}

	public static java.util.List<com.liferay.portal.mobile.model.DeviceProfileAction> getDeviceProfileActions(
		long deviceProfileRuleId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .getDeviceProfileActions(deviceProfileRuleId, start, end);
	}

	public static int getDeviceProfileActionsCount(long deviceProfileRuleId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getDeviceProfileActionsCount(deviceProfileRuleId);
	}

	public static DeviceProfileActionLocalService getService() {
		if (_service == null) {
			_service = (DeviceProfileActionLocalService)PortalBeanLocatorUtil.locate(DeviceProfileActionLocalService.class.getName());

			ReferenceRegistry.registerReference(DeviceProfileActionLocalServiceUtil.class,
				"_service");
			MethodCache.remove(DeviceProfileActionLocalService.class);
		}

		return _service;
	}

	public void setService(DeviceProfileActionLocalService service) {
		MethodCache.remove(DeviceProfileActionLocalService.class);

		_service = service;

		ReferenceRegistry.registerReference(DeviceProfileActionLocalServiceUtil.class,
			"_service");
		MethodCache.remove(DeviceProfileActionLocalService.class);
	}

	private static DeviceProfileActionLocalService _service;
}