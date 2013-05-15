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

import com.liferay.portal.NoSuchBackgroundTaskException;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.impl.BackgroundTaskImpl;
import com.liferay.portal.model.impl.BackgroundTaskModelImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The persistence implementation for the background task service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see BackgroundTaskPersistence
 * @see BackgroundTaskUtil
 * @generated
 */
public class BackgroundTaskPersistenceImpl extends BasePersistenceImpl<BackgroundTask>
	implements BackgroundTaskPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link BackgroundTaskUtil} to access the background task persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = BackgroundTaskImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_C",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_C",
			new String[] { Long.class.getName(), String.class.getName() },
			BackgroundTaskModelImpl.GROUPID_COLUMN_BITMASK |
			BackgroundTaskModelImpl.CLASSNAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_C = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns all the background tasks where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @return the matching background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public List<BackgroundTask> findByG_C(long groupId, String className)
		throws SystemException {
		return findByG_C(groupId, className, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

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
	public List<BackgroundTask> findByG_C(long groupId, String className,
		int start, int end) throws SystemException {
		return findByG_C(groupId, className, start, end, null);
	}

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
	public List<BackgroundTask> findByG_C(long groupId, String className,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C;
			finderArgs = new Object[] { groupId, className };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C;
			finderArgs = new Object[] {
					groupId, className,
					
					start, end, orderByComparator
				};
		}

		List<BackgroundTask> list = (List<BackgroundTask>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (BackgroundTask backgroundTask : list) {
				if ((groupId != backgroundTask.getGroupId()) ||
						!Validator.equals(className,
							backgroundTask.getClassName())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_BACKGROUNDTASK_WHERE);

			query.append(_FINDER_COLUMN_G_C_GROUPID_2);

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_G_C_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_C_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_G_C_CLASSNAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BackgroundTaskModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindClassName) {
					qPos.add(className);
				}

				if (!pagination) {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<BackgroundTask>(list);
				}
				else {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

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
	public BackgroundTask findByG_C_First(long groupId, String className,
		OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = fetchByG_C_First(groupId, className,
				orderByComparator);

		if (backgroundTask != null) {
			return backgroundTask;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBackgroundTaskException(msg.toString());
	}

	/**
	 * Returns the first background task in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching background task, or <code>null</code> if a matching background task could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BackgroundTask fetchByG_C_First(long groupId, String className,
		OrderByComparator orderByComparator) throws SystemException {
		List<BackgroundTask> list = findByG_C(groupId, className, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

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
	public BackgroundTask findByG_C_Last(long groupId, String className,
		OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = fetchByG_C_Last(groupId, className,
				orderByComparator);

		if (backgroundTask != null) {
			return backgroundTask;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBackgroundTaskException(msg.toString());
	}

	/**
	 * Returns the last background task in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching background task, or <code>null</code> if a matching background task could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BackgroundTask fetchByG_C_Last(long groupId, String className,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_C(groupId, className);

		List<BackgroundTask> list = findByG_C(groupId, className, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

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
	public BackgroundTask[] findByG_C_PrevAndNext(long backgroundTaskId,
		long groupId, String className, OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = findByPrimaryKey(backgroundTaskId);

		Session session = null;

		try {
			session = openSession();

			BackgroundTask[] array = new BackgroundTaskImpl[3];

			array[0] = getByG_C_PrevAndNext(session, backgroundTask, groupId,
					className, orderByComparator, true);

			array[1] = backgroundTask;

			array[2] = getByG_C_PrevAndNext(session, backgroundTask, groupId,
					className, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected BackgroundTask getByG_C_PrevAndNext(Session session,
		BackgroundTask backgroundTask, long groupId, String className,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BACKGROUNDTASK_WHERE);

		query.append(_FINDER_COLUMN_G_C_GROUPID_2);

		boolean bindClassName = false;

		if (className == null) {
			query.append(_FINDER_COLUMN_G_C_CLASSNAME_1);
		}
		else if (className.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_C_CLASSNAME_3);
		}
		else {
			bindClassName = true;

			query.append(_FINDER_COLUMN_G_C_CLASSNAME_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(BackgroundTaskModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindClassName) {
			qPos.add(className);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(backgroundTask);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<BackgroundTask> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the background tasks where groupId = &#63; and className = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByG_C(long groupId, String className)
		throws SystemException {
		for (BackgroundTask backgroundTask : findByG_C(groupId, className,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(backgroundTask);
		}
	}

	/**
	 * Returns the number of background tasks where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @return the number of matching background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public int countByG_C(long groupId, String className)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_C;

		Object[] finderArgs = new Object[] { groupId, className };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_BACKGROUNDTASK_WHERE);

			query.append(_FINDER_COLUMN_G_C_GROUPID_2);

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_G_C_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_C_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_G_C_CLASSNAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindClassName) {
					qPos.add(className);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_C_GROUPID_2 = "backgroundTask.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_C_CLASSNAME_1 = "backgroundTask.className IS NULL";
	private static final String _FINDER_COLUMN_G_C_CLASSNAME_2 = "backgroundTask.className = ?";
	private static final String _FINDER_COLUMN_G_C_CLASSNAME_3 = "(backgroundTask.className IS NULL OR backgroundTask.className = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C_S = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_C_S",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_S = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED,
			BackgroundTaskImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_C_S",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			},
			BackgroundTaskModelImpl.GROUPID_COLUMN_BITMASK |
			BackgroundTaskModelImpl.CLASSNAME_COLUMN_BITMASK |
			BackgroundTaskModelImpl.STATUS_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_C_S = new FinderPath(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C_S",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns all the background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param status the status
	 * @return the matching background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public List<BackgroundTask> findByG_C_S(long groupId, String className,
		int status) throws SystemException {
		return findByG_C_S(groupId, className, status, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

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
	public List<BackgroundTask> findByG_C_S(long groupId, String className,
		int status, int start, int end) throws SystemException {
		return findByG_C_S(groupId, className, status, start, end, null);
	}

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
	public List<BackgroundTask> findByG_C_S(long groupId, String className,
		int status, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_S;
			finderArgs = new Object[] { groupId, className, status };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C_S;
			finderArgs = new Object[] {
					groupId, className, status,
					
					start, end, orderByComparator
				};
		}

		List<BackgroundTask> list = (List<BackgroundTask>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (BackgroundTask backgroundTask : list) {
				if ((groupId != backgroundTask.getGroupId()) ||
						!Validator.equals(className,
							backgroundTask.getClassName()) ||
						(status != backgroundTask.getStatus())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_BACKGROUNDTASK_WHERE);

			query.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(BackgroundTaskModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(status);

				if (!pagination) {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<BackgroundTask>(list);
				}
				else {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

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
	public BackgroundTask findByG_C_S_First(long groupId, String className,
		int status, OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = fetchByG_C_S_First(groupId, className,
				status, orderByComparator);

		if (backgroundTask != null) {
			return backgroundTask;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBackgroundTaskException(msg.toString());
	}

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
	public BackgroundTask fetchByG_C_S_First(long groupId, String className,
		int status, OrderByComparator orderByComparator)
		throws SystemException {
		List<BackgroundTask> list = findByG_C_S(groupId, className, status, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

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
	public BackgroundTask findByG_C_S_Last(long groupId, String className,
		int status, OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = fetchByG_C_S_Last(groupId, className,
				status, orderByComparator);

		if (backgroundTask != null) {
			return backgroundTask;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBackgroundTaskException(msg.toString());
	}

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
	public BackgroundTask fetchByG_C_S_Last(long groupId, String className,
		int status, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_C_S(groupId, className, status);

		List<BackgroundTask> list = findByG_C_S(groupId, className, status,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

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
	public BackgroundTask[] findByG_C_S_PrevAndNext(long backgroundTaskId,
		long groupId, String className, int status,
		OrderByComparator orderByComparator)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = findByPrimaryKey(backgroundTaskId);

		Session session = null;

		try {
			session = openSession();

			BackgroundTask[] array = new BackgroundTaskImpl[3];

			array[0] = getByG_C_S_PrevAndNext(session, backgroundTask, groupId,
					className, status, orderByComparator, true);

			array[1] = backgroundTask;

			array[2] = getByG_C_S_PrevAndNext(session, backgroundTask, groupId,
					className, status, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected BackgroundTask getByG_C_S_PrevAndNext(Session session,
		BackgroundTask backgroundTask, long groupId, String className,
		int status, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BACKGROUNDTASK_WHERE);

		query.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

		boolean bindClassName = false;

		if (className == null) {
			query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_1);
		}
		else if (className.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_3);
		}
		else {
			bindClassName = true;

			query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_2);
		}

		query.append(_FINDER_COLUMN_G_C_S_STATUS_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(BackgroundTaskModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (bindClassName) {
			qPos.add(className);
		}

		qPos.add(status);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(backgroundTask);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<BackgroundTask> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the background tasks where groupId = &#63; and className = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param status the status
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByG_C_S(long groupId, String className, int status)
		throws SystemException {
		for (BackgroundTask backgroundTask : findByG_C_S(groupId, className,
				status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(backgroundTask);
		}
	}

	/**
	 * Returns the number of background tasks where groupId = &#63; and className = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param status the status
	 * @return the number of matching background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public int countByG_C_S(long groupId, String className, int status)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_C_S;

		Object[] finderArgs = new Object[] { groupId, className, status };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_BACKGROUNDTASK_WHERE);

			query.append(_FINDER_COLUMN_G_C_S_GROUPID_2);

			boolean bindClassName = false;

			if (className == null) {
				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_1);
			}
			else if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_3);
			}
			else {
				bindClassName = true;

				query.append(_FINDER_COLUMN_G_C_S_CLASSNAME_2);
			}

			query.append(_FINDER_COLUMN_G_C_S_STATUS_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (bindClassName) {
					qPos.add(className);
				}

				qPos.add(status);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_C_S_GROUPID_2 = "backgroundTask.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_C_S_CLASSNAME_1 = "backgroundTask.className IS NULL AND ";
	private static final String _FINDER_COLUMN_G_C_S_CLASSNAME_2 = "backgroundTask.className = ? AND ";
	private static final String _FINDER_COLUMN_G_C_S_CLASSNAME_3 = "(backgroundTask.className IS NULL OR backgroundTask.className = '') AND ";
	private static final String _FINDER_COLUMN_G_C_S_STATUS_2 = "backgroundTask.status = ?";

	/**
	 * Caches the background task in the entity cache if it is enabled.
	 *
	 * @param backgroundTask the background task
	 */
	public void cacheResult(BackgroundTask backgroundTask) {
		EntityCacheUtil.putResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskImpl.class, backgroundTask.getPrimaryKey(),
			backgroundTask);

		backgroundTask.resetOriginalValues();
	}

	/**
	 * Caches the background tasks in the entity cache if it is enabled.
	 *
	 * @param backgroundTasks the background tasks
	 */
	public void cacheResult(List<BackgroundTask> backgroundTasks) {
		for (BackgroundTask backgroundTask : backgroundTasks) {
			if (EntityCacheUtil.getResult(
						BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
						BackgroundTaskImpl.class, backgroundTask.getPrimaryKey()) == null) {
				cacheResult(backgroundTask);
			}
			else {
				backgroundTask.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all background tasks.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(BackgroundTaskImpl.class.getName());
		}

		EntityCacheUtil.clearCache(BackgroundTaskImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the background task.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(BackgroundTask backgroundTask) {
		EntityCacheUtil.removeResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskImpl.class, backgroundTask.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<BackgroundTask> backgroundTasks) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (BackgroundTask backgroundTask : backgroundTasks) {
			EntityCacheUtil.removeResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
				BackgroundTaskImpl.class, backgroundTask.getPrimaryKey());
		}
	}

	/**
	 * Creates a new background task with the primary key. Does not add the background task to the database.
	 *
	 * @param backgroundTaskId the primary key for the new background task
	 * @return the new background task
	 */
	public BackgroundTask create(long backgroundTaskId) {
		BackgroundTask backgroundTask = new BackgroundTaskImpl();

		backgroundTask.setNew(true);
		backgroundTask.setPrimaryKey(backgroundTaskId);

		return backgroundTask;
	}

	/**
	 * Removes the background task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param backgroundTaskId the primary key of the background task
	 * @return the background task that was removed
	 * @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BackgroundTask remove(long backgroundTaskId)
		throws NoSuchBackgroundTaskException, SystemException {
		return remove((Serializable)backgroundTaskId);
	}

	/**
	 * Removes the background task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the background task
	 * @return the background task that was removed
	 * @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BackgroundTask remove(Serializable primaryKey)
		throws NoSuchBackgroundTaskException, SystemException {
		Session session = null;

		try {
			session = openSession();

			BackgroundTask backgroundTask = (BackgroundTask)session.get(BackgroundTaskImpl.class,
					primaryKey);

			if (backgroundTask == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBackgroundTaskException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(backgroundTask);
		}
		catch (NoSuchBackgroundTaskException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected BackgroundTask removeImpl(BackgroundTask backgroundTask)
		throws SystemException {
		backgroundTask = toUnwrappedModel(backgroundTask);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(backgroundTask)) {
				backgroundTask = (BackgroundTask)session.get(BackgroundTaskImpl.class,
						backgroundTask.getPrimaryKeyObj());
			}

			if (backgroundTask != null) {
				session.delete(backgroundTask);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (backgroundTask != null) {
			clearCache(backgroundTask);
		}

		return backgroundTask;
	}

	@Override
	public BackgroundTask updateImpl(
		com.liferay.portal.model.BackgroundTask backgroundTask)
		throws SystemException {
		backgroundTask = toUnwrappedModel(backgroundTask);

		boolean isNew = backgroundTask.isNew();

		BackgroundTaskModelImpl backgroundTaskModelImpl = (BackgroundTaskModelImpl)backgroundTask;

		Session session = null;

		try {
			session = openSession();

			if (backgroundTask.isNew()) {
				session.save(backgroundTask);

				backgroundTask.setNew(false);
			}
			else {
				session.merge(backgroundTask);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !BackgroundTaskModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((backgroundTaskModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						backgroundTaskModelImpl.getOriginalGroupId(),
						backgroundTaskModelImpl.getOriginalClassName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C,
					args);

				args = new Object[] {
						backgroundTaskModelImpl.getGroupId(),
						backgroundTaskModelImpl.getClassName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C,
					args);
			}

			if ((backgroundTaskModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_S.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						backgroundTaskModelImpl.getOriginalGroupId(),
						backgroundTaskModelImpl.getOriginalClassName(),
						backgroundTaskModelImpl.getOriginalStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C_S, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_S,
					args);

				args = new Object[] {
						backgroundTaskModelImpl.getGroupId(),
						backgroundTaskModelImpl.getClassName(),
						backgroundTaskModelImpl.getStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C_S, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_S,
					args);
			}
		}

		EntityCacheUtil.putResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
			BackgroundTaskImpl.class, backgroundTask.getPrimaryKey(),
			backgroundTask);

		return backgroundTask;
	}

	protected BackgroundTask toUnwrappedModel(BackgroundTask backgroundTask) {
		if (backgroundTask instanceof BackgroundTaskImpl) {
			return backgroundTask;
		}

		BackgroundTaskImpl backgroundTaskImpl = new BackgroundTaskImpl();

		backgroundTaskImpl.setNew(backgroundTask.isNew());
		backgroundTaskImpl.setPrimaryKey(backgroundTask.getPrimaryKey());

		backgroundTaskImpl.setBackgroundTaskId(backgroundTask.getBackgroundTaskId());
		backgroundTaskImpl.setGroupId(backgroundTask.getGroupId());
		backgroundTaskImpl.setCompanyId(backgroundTask.getCompanyId());
		backgroundTaskImpl.setUserId(backgroundTask.getUserId());
		backgroundTaskImpl.setUserName(backgroundTask.getUserName());
		backgroundTaskImpl.setCreateDate(backgroundTask.getCreateDate());
		backgroundTaskImpl.setModifiedDate(backgroundTask.getModifiedDate());
		backgroundTaskImpl.setClassName(backgroundTask.getClassName());
		backgroundTaskImpl.setCompletedDate(backgroundTask.getCompletedDate());
		backgroundTaskImpl.setData(backgroundTask.getData());
		backgroundTaskImpl.setName(backgroundTask.getName());
		backgroundTaskImpl.setServletContextName(backgroundTask.getServletContextName());
		backgroundTaskImpl.setStatus(backgroundTask.getStatus());

		return backgroundTaskImpl;
	}

	/**
	 * Returns the background task with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the background task
	 * @return the background task
	 * @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BackgroundTask findByPrimaryKey(Serializable primaryKey)
		throws NoSuchBackgroundTaskException, SystemException {
		BackgroundTask backgroundTask = fetchByPrimaryKey(primaryKey);

		if (backgroundTask == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchBackgroundTaskException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return backgroundTask;
	}

	/**
	 * Returns the background task with the primary key or throws a {@link com.liferay.portal.NoSuchBackgroundTaskException} if it could not be found.
	 *
	 * @param backgroundTaskId the primary key of the background task
	 * @return the background task
	 * @throws com.liferay.portal.NoSuchBackgroundTaskException if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BackgroundTask findByPrimaryKey(long backgroundTaskId)
		throws NoSuchBackgroundTaskException, SystemException {
		return findByPrimaryKey((Serializable)backgroundTaskId);
	}

	/**
	 * Returns the background task with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the background task
	 * @return the background task, or <code>null</code> if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BackgroundTask fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		BackgroundTask backgroundTask = (BackgroundTask)EntityCacheUtil.getResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
				BackgroundTaskImpl.class, primaryKey);

		if (backgroundTask == _nullBackgroundTask) {
			return null;
		}

		if (backgroundTask == null) {
			Session session = null;

			try {
				session = openSession();

				backgroundTask = (BackgroundTask)session.get(BackgroundTaskImpl.class,
						primaryKey);

				if (backgroundTask != null) {
					cacheResult(backgroundTask);
				}
				else {
					EntityCacheUtil.putResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
						BackgroundTaskImpl.class, primaryKey,
						_nullBackgroundTask);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(BackgroundTaskModelImpl.ENTITY_CACHE_ENABLED,
					BackgroundTaskImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return backgroundTask;
	}

	/**
	 * Returns the background task with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param backgroundTaskId the primary key of the background task
	 * @return the background task, or <code>null</code> if a background task with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BackgroundTask fetchByPrimaryKey(long backgroundTaskId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)backgroundTaskId);
	}

	/**
	 * Returns all the background tasks.
	 *
	 * @return the background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public List<BackgroundTask> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

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
	public List<BackgroundTask> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

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
	public List<BackgroundTask> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<BackgroundTask> list = (List<BackgroundTask>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_BACKGROUNDTASK);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_BACKGROUNDTASK;

				if (pagination) {
					sql = sql.concat(BackgroundTaskModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<BackgroundTask>(list);
				}
				else {
					list = (List<BackgroundTask>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the background tasks from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (BackgroundTask backgroundTask : findAll()) {
			remove(backgroundTask);
		}
	}

	/**
	 * Returns the number of background tasks.
	 *
	 * @return the number of background tasks
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_BACKGROUNDTASK);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	/**
	 * Initializes the background task persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.portal.util.PropsUtil.get(
						"value.object.listener.com.liferay.portal.model.BackgroundTask")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<BackgroundTask>> listenersList = new ArrayList<ModelListener<BackgroundTask>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<BackgroundTask>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(BackgroundTaskImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_BACKGROUNDTASK = "SELECT backgroundTask FROM BackgroundTask backgroundTask";
	private static final String _SQL_SELECT_BACKGROUNDTASK_WHERE = "SELECT backgroundTask FROM BackgroundTask backgroundTask WHERE ";
	private static final String _SQL_COUNT_BACKGROUNDTASK = "SELECT COUNT(backgroundTask) FROM BackgroundTask backgroundTask";
	private static final String _SQL_COUNT_BACKGROUNDTASK_WHERE = "SELECT COUNT(backgroundTask) FROM BackgroundTask backgroundTask WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "backgroundTask.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No BackgroundTask exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No BackgroundTask exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static Log _log = LogFactoryUtil.getLog(BackgroundTaskPersistenceImpl.class);
	private static Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"data"
			});
	private static BackgroundTask _nullBackgroundTask = new BackgroundTaskImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<BackgroundTask> toCacheModel() {
				return _nullBackgroundTaskCacheModel;
			}
		};

	private static CacheModel<BackgroundTask> _nullBackgroundTaskCacheModel = new CacheModel<BackgroundTask>() {
			public BackgroundTask toEntityModel() {
				return _nullBackgroundTask;
			}
		};
}