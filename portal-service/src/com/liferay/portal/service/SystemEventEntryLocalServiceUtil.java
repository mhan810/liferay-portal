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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * Provides the local service utility for SystemEventEntry. This utility wraps
 * {@link com.liferay.portal.service.impl.SystemEventEntryLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntryLocalService
 * @see com.liferay.portal.service.base.SystemEventEntryLocalServiceBaseImpl
 * @see com.liferay.portal.service.impl.SystemEventEntryLocalServiceImpl
 * @generated
 */
public class SystemEventEntryLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.service.impl.SystemEventEntryLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the system event entry to the database. Also notifies the appropriate model listeners.
	*
	* @param systemEventEntry the system event entry
	* @return the system event entry that was added
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry addSystemEventEntry(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addSystemEventEntry(systemEventEntry);
	}

	/**
	* Creates a new system event entry with the primary key. Does not add the system event entry to the database.
	*
	* @param systemEventId the primary key for the new system event entry
	* @return the new system event entry
	*/
	public static com.liferay.portal.model.SystemEventEntry createSystemEventEntry(
		long systemEventId) {
		return getService().createSystemEventEntry(systemEventId);
	}

	/**
	* Deletes the system event entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry that was removed
	* @throws PortalException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry deleteSystemEventEntry(
		long systemEventId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteSystemEventEntry(systemEventId);
	}

	/**
	* Deletes the system event entry from the database. Also notifies the appropriate model listeners.
	*
	* @param systemEventEntry the system event entry
	* @return the system event entry that was removed
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry deleteSystemEventEntry(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteSystemEventEntry(systemEventEntry);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

	public static com.liferay.portal.model.SystemEventEntry fetchSystemEventEntry(
		long systemEventId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchSystemEventEntry(systemEventId);
	}

	/**
	* Returns the system event entry with the primary key.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry
	* @throws PortalException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry getSystemEventEntry(
		long systemEventId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getSystemEventEntry(systemEventId);
	}

	public static com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns a range of all the system event entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @return the range of system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> getSystemEventEntries(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getSystemEventEntries(start, end);
	}

	/**
	* Returns the number of system event entries.
	*
	* @return the number of system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static int getSystemEventEntriesCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getSystemEventEntriesCount();
	}

	/**
	* Updates the system event entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param systemEventEntry the system event entry
	* @return the system event entry that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry updateSystemEventEntry(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateSystemEventEntry(systemEventEntry);
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

	public static void addEvent(long groupId, long userId, int eventType,
		long classNameId, long classPK, java.lang.String classUuid)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService()
			.addEvent(groupId, userId, eventType, classNameId, classPK,
			classUuid);
	}

	public static void deleteEvents(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().deleteEvents(groupId);
	}

	public static com.liferay.portal.model.SystemEventEntry fetchEvent(
		long groupId, int eventType, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchEvent(groupId, eventType, classNameId, classPK);
	}

	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findEvents(
		long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findEvents(groupId, classNameId, classPK);
	}

	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findEvents(
		long groupId, int eventType, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findEvents(groupId, eventType, classNameId, classPK);
	}

	public static SystemEventEntryLocalService getService() {
		if (_service == null) {
			_service = (SystemEventEntryLocalService)PortalBeanLocatorUtil.locate(SystemEventEntryLocalService.class.getName());

			ReferenceRegistry.registerReference(SystemEventEntryLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(SystemEventEntryLocalService service) {
	}

	private static SystemEventEntryLocalService _service;
}