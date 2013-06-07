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

package com.liferay.portal.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.model.SystemEventEntry;
import com.liferay.portal.service.ServiceContext;

import java.util.List;

/**
 * The persistence utility for the system event entry service. This utility wraps {@link SystemEventEntryPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntryPersistence
 * @see SystemEventEntryPersistenceImpl
 * @generated
 */
public class SystemEventEntryUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache(com.liferay.portal.model.BaseModel)
	 */
	public static void clearCache(SystemEventEntry systemEventEntry) {
		getPersistence().clearCache(systemEventEntry);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public long countWithDynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<SystemEventEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) throws SystemException {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<SystemEventEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<SystemEventEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel)
	 */
	public static SystemEventEntry update(SystemEventEntry systemEventEntry)
		throws SystemException {
		return getPersistence().update(systemEventEntry);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, ServiceContext)
	 */
	public static SystemEventEntry update(SystemEventEntry systemEventEntry,
		ServiceContext serviceContext) throws SystemException {
		return getPersistence().update(systemEventEntry, serviceContext);
	}

	/**
	* Returns all the system event entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	* Returns a range of all the system event entries where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @return the range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	* Returns an ordered range of all the system event entries where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByGroupId(groupId, start, end, orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByGroupId_First(groupId, orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	* Returns the system event entries before and after the current system event entry in the ordered set where groupId = &#63;.
	*
	* @param systemEventId the primary key of the current system event entry
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry[] findByGroupId_PrevAndNext(
		long systemEventId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByGroupId_PrevAndNext(systemEventId, groupId,
			orderByComparator);
	}

	/**
	* Removes all the system event entries where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	* Returns the number of system event entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static int countByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	* Returns all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByG_C_C(groupId, classNameId, classPK);
	}

	/**
	* Returns a range of all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @return the range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_C_C(groupId, classNameId, classPK, start, end);
	}

	/**
	* Returns an ordered range of all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_C_C(groupId, classNameId, classPK, start, end,
			orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByG_C_C_First(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_C_C_First(groupId, classNameId, classPK,
			orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByG_C_C_First(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByG_C_C_First(groupId, classNameId, classPK,
			orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByG_C_C_Last(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_C_C_Last(groupId, classNameId, classPK,
			orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByG_C_C_Last(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByG_C_C_Last(groupId, classNameId, classPK,
			orderByComparator);
	}

	/**
	* Returns the system event entries before and after the current system event entry in the ordered set where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param systemEventId the primary key of the current system event entry
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry[] findByG_C_C_PrevAndNext(
		long systemEventId, long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_C_C_PrevAndNext(systemEventId, groupId,
			classNameId, classPK, orderByComparator);
	}

	/**
	* Removes all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByG_C_C(long groupId, long classNameId,
		long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeByG_C_C(groupId, classNameId, classPK);
	}

	/**
	* Returns the number of system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the number of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static int countByG_C_C(long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByG_C_C(groupId, classNameId, classPK);
	}

	/**
	* Returns all the system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C(groupId, eventType, classNameId, classPK);
	}

	/**
	* Returns a range of all the system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @return the range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C(groupId, eventType, classNameId, classPK,
			start, end);
	}

	/**
	* Returns an ordered range of all the system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C(groupId, eventType, classNameId, classPK,
			start, end, orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByG_E_C_C_First(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C_First(groupId, eventType, classNameId,
			classPK, orderByComparator);
	}

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByG_E_C_C_First(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByG_E_C_C_First(groupId, eventType, classNameId,
			classPK, orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByG_E_C_C_Last(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C_Last(groupId, eventType, classNameId,
			classPK, orderByComparator);
	}

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByG_E_C_C_Last(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByG_E_C_C_Last(groupId, eventType, classNameId,
			classPK, orderByComparator);
	}

	/**
	* Returns the system event entries before and after the current system event entry in the ordered set where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param systemEventId the primary key of the current system event entry
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry[] findByG_E_C_C_PrevAndNext(
		long systemEventId, long groupId, int eventType, long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByG_E_C_C_PrevAndNext(systemEventId, groupId,
			eventType, classNameId, classPK, orderByComparator);
	}

	/**
	* Removes all the system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByG_E_C_C(long groupId, int eventType,
		long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence()
			.removeByG_E_C_C(groupId, eventType, classNameId, classPK);
	}

	/**
	* Returns the number of system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the number of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static int countByG_E_C_C(long groupId, int eventType,
		long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .countByG_E_C_C(groupId, eventType, classNameId, classPK);
	}

	/**
	* Caches the system event entry in the entity cache if it is enabled.
	*
	* @param systemEventEntry the system event entry
	*/
	public static void cacheResult(
		com.liferay.portal.model.SystemEventEntry systemEventEntry) {
		getPersistence().cacheResult(systemEventEntry);
	}

	/**
	* Caches the system event entries in the entity cache if it is enabled.
	*
	* @param systemEventEntries the system event entries
	*/
	public static void cacheResult(
		java.util.List<com.liferay.portal.model.SystemEventEntry> systemEventEntries) {
		getPersistence().cacheResult(systemEventEntries);
	}

	/**
	* Creates a new system event entry with the primary key. Does not add the system event entry to the database.
	*
	* @param systemEventId the primary key for the new system event entry
	* @return the new system event entry
	*/
	public static com.liferay.portal.model.SystemEventEntry create(
		long systemEventId) {
		return getPersistence().create(systemEventId);
	}

	/**
	* Removes the system event entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry that was removed
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry remove(
		long systemEventId)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().remove(systemEventId);
	}

	public static com.liferay.portal.model.SystemEventEntry updateImpl(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().updateImpl(systemEventEntry);
	}

	/**
	* Returns the system event entry with the primary key or throws a {@link com.liferay.portal.NoSuchSystemEventEntryException} if it could not be found.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry findByPrimaryKey(
		long systemEventId)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByPrimaryKey(systemEventId);
	}

	/**
	* Returns the system event entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry, or <code>null</code> if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portal.model.SystemEventEntry fetchByPrimaryKey(
		long systemEventId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByPrimaryKey(systemEventId);
	}

	/**
	* Returns all the system event entries.
	*
	* @return the system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll();
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
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the system event entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.SystemEventEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of system event entries
	* @param end the upper bound of the range of system event entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portal.model.SystemEventEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Removes all the system event entries from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public static void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of system event entries.
	*
	* @return the number of system event entries
	* @throws SystemException if a system exception occurred
	*/
	public static int countAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countAll();
	}

	public static SystemEventEntryPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (SystemEventEntryPersistence)PortalBeanLocatorUtil.locate(SystemEventEntryPersistence.class.getName());

			ReferenceRegistry.registerReference(SystemEventEntryUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setPersistence(SystemEventEntryPersistence persistence) {
	}

	private static SystemEventEntryPersistence _persistence;
}