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

import com.liferay.portal.model.SystemEventEntry;

/**
 * The persistence interface for the system event entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntryPersistenceImpl
 * @see SystemEventEntryUtil
 * @generated
 */
public interface SystemEventEntryPersistence extends BasePersistence<SystemEventEntry> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SystemEventEntryUtil} to access the system event entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the system event entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry findByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry fetchByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry findByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last system event entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching system event entry, or <code>null</code> if a matching system event entry could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry fetchByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry[] findByGroupId_PrevAndNext(
		long systemEventId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the system event entries where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	* @throws SystemException if a system exception occurred
	*/
	public void removeByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of system event entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public int countByGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_C_C(
		long groupId, long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry findByG_C_C_First(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry fetchByG_C_C_First(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry findByG_C_C_Last(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry fetchByG_C_C_Last(
		long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry[] findByG_C_C_PrevAndNext(
		long systemEventId, long groupId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_C_C(long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	*
	* @param groupId the group ID
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the number of matching system event entries
	* @throws SystemException if a system exception occurred
	*/
	public int countByG_C_C(long groupId, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findByG_E_C_C(
		long groupId, int eventType, long classNameId, long classPK, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry findByG_E_C_C_First(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry fetchByG_E_C_C_First(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry findByG_E_C_C_Last(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry fetchByG_E_C_C_Last(
		long groupId, int eventType, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portal.model.SystemEventEntry[] findByG_E_C_C_PrevAndNext(
		long systemEventId, long groupId, int eventType, long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the system event entries where groupId = &#63; and eventType = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	*
	* @param groupId the group ID
	* @param eventType the event type
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_E_C_C(long groupId, int eventType, long classNameId,
		long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public int countByG_E_C_C(long groupId, int eventType, long classNameId,
		long classPK)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Caches the system event entry in the entity cache if it is enabled.
	*
	* @param systemEventEntry the system event entry
	*/
	public void cacheResult(
		com.liferay.portal.model.SystemEventEntry systemEventEntry);

	/**
	* Caches the system event entries in the entity cache if it is enabled.
	*
	* @param systemEventEntries the system event entries
	*/
	public void cacheResult(
		java.util.List<com.liferay.portal.model.SystemEventEntry> systemEventEntries);

	/**
	* Creates a new system event entry with the primary key. Does not add the system event entry to the database.
	*
	* @param systemEventId the primary key for the new system event entry
	* @return the new system event entry
	*/
	public com.liferay.portal.model.SystemEventEntry create(long systemEventId);

	/**
	* Removes the system event entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry that was removed
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry remove(long systemEventId)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	public com.liferay.portal.model.SystemEventEntry updateImpl(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the system event entry with the primary key or throws a {@link com.liferay.portal.NoSuchSystemEventEntryException} if it could not be found.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry
	* @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry findByPrimaryKey(
		long systemEventId)
		throws com.liferay.portal.NoSuchSystemEventEntryException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the system event entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param systemEventId the primary key of the system event entry
	* @return the system event entry, or <code>null</code> if a system event entry with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.SystemEventEntry fetchByPrimaryKey(
		long systemEventId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the system event entries.
	*
	* @return the system event entries
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portal.model.SystemEventEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the system event entries from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of system event entries.
	*
	* @return the number of system event entries
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}