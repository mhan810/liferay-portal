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

import com.liferay.portal.model.BackgroundTask;

/**
 * The persistence interface for the background task service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see BackgroundTaskPersistenceImpl
 * @see BackgroundTaskUtil
 * @generated
 */
public interface BackgroundTaskPersistence extends BasePersistence<BackgroundTask> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link BackgroundTaskUtil} to access the background task persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the background tasks where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @return the matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C(
		long groupId, java.lang.String className)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the background tasks where groupId = &#63; and className = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param className the class name
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @return the range of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C(
		long groupId, java.lang.String className, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the background tasks where groupId = &#63; and className = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param className the class name
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C(
		long groupId, java.lang.String className, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first background task in the ordered set where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask findByG_C_First(
		long groupId, java.lang.String className,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first background task in the ordered set where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching background task, or <code>null</code> if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask fetchByG_C_First(
		long groupId, java.lang.String className,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last background task in the ordered set where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask findByG_C_Last(
		long groupId, java.lang.String className,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last background task in the ordered set where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching background task, or <code>null</code> if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask fetchByG_C_Last(
		long groupId, java.lang.String className,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the background tasks before and after the current background task in the ordered set where groupId = &#63; and className = &#63;.
	*
	* @param backgroundTaskId the primary key of the current background task
	* @param groupId the group ID
	* @param className the class name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask[] findByG_C_PrevAndNext(
		long backgroundTaskId, long groupId, java.lang.String className,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the background tasks where groupId = &#63; and className = &#63; from the database.
	*
	* @param groupId the group ID
	* @param className the class name
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_C(long groupId, java.lang.String className)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of background tasks where groupId = &#63; and className = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @return the number of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public int countByG_C(long groupId, java.lang.String className)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @return the matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C_S(
		long groupId, java.lang.String className, int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @return the range of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C_S(
		long groupId, java.lang.String className, int status, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findByG_C_S(
		long groupId, java.lang.String className, int status, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first background task in the ordered set where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask findByG_C_S_First(
		long groupId, java.lang.String className, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first background task in the ordered set where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching background task, or <code>null</code> if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask fetchByG_C_S_First(
		long groupId, java.lang.String className, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last background task in the ordered set where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask findByG_C_S_Last(
		long groupId, java.lang.String className, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last background task in the ordered set where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching background task, or <code>null</code> if a matching background task could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask fetchByG_C_S_Last(
		long groupId, java.lang.String className, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the background tasks before and after the current background task in the ordered set where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param backgroundTaskId the primary key of the current background task
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask[] findByG_C_S_PrevAndNext(
		long backgroundTaskId, long groupId, java.lang.String className,
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the background tasks where groupId = &#63; and className = &#63; and status = &#63; from the database.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_C_S(long groupId, java.lang.String className,
		int status) throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	*
	* @param groupId the group ID
	* @param className the class name
	* @param status the status
	* @return the number of matching background tasks
	* @throws SystemException if a system exception occurred
	*/
	public int countByG_C_S(long groupId, java.lang.String className, int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Caches the background task in the entity cache if it is enabled.
	*
	* @param backgroundTask the background task
	*/
	public void cacheResult(
		com.liferay.portal.model.BackgroundTask backgroundTask);

	/**
	* Caches the background tasks in the entity cache if it is enabled.
	*
	* @param backgroundTasks the background tasks
	*/
	public void cacheResult(
		java.util.List<com.liferay.portal.model.BackgroundTask> backgroundTasks);

	/**
	* Creates a new background task with the primary key. Does not add the background task to the database.
	*
	* @param backgroundTaskId the primary key for the new background task
	* @return the new background task
	*/
	public com.liferay.portal.model.BackgroundTask create(long backgroundTaskId);

	/**
	* Removes the background task with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param backgroundTaskId the primary key of the background task
	* @return the background task that was removed
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask remove(long backgroundTaskId)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	public com.liferay.portal.model.BackgroundTask updateImpl(
		com.liferay.portal.model.BackgroundTask backgroundTask)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the background task with the primary key or throws a {@link com.liferay.portal.NoSuchBackgroundTaskException} if it could not be found.
	*
	* @param backgroundTaskId the primary key of the background task
	* @return the background task
	* @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask findByPrimaryKey(
		long backgroundTaskId)
		throws com.liferay.portal.NoSuchBackgroundTaskException,
			com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the background task with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param backgroundTaskId the primary key of the background task
	* @return the background task, or <code>null</code> if a background task with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.model.BackgroundTask fetchByPrimaryKey(
		long backgroundTaskId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the background tasks.
	*
	* @return the background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the background tasks.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @return the range of background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the background tasks.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.BackgroundTaskModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of background tasks
	* @param end the upper bound of the range of background tasks (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of background tasks
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.model.BackgroundTask> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the background tasks from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of background tasks.
	*
	* @return the number of background tasks
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}