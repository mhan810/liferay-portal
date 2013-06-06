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

import com.liferay.portal.NoSuchSystemEventEntryException;
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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.SystemEventEntry;
import com.liferay.portal.model.impl.SystemEventEntryImpl;
import com.liferay.portal.model.impl.SystemEventEntryModelImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the system event entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntryPersistence
 * @see SystemEventEntryUtil
 * @generated
 */
public class SystemEventEntryPersistenceImpl extends BasePersistenceImpl<SystemEventEntry>
	implements SystemEventEntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link SystemEventEntryUtil} to access the system event entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = SystemEventEntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByGroupId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			SystemEventEntryModelImpl.GROUPID_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the system event entries where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching system event entries
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<SystemEventEntry> findByGroupId(long groupId)
		throws SystemException {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<SystemEventEntry> findByGroupId(long groupId, int start, int end)
		throws SystemException {
		return findByGroupId(groupId, start, end, null);
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
	@Override
	public List<SystemEventEntry> findByGroupId(long groupId, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId, start, end, orderByComparator };
		}

		List<SystemEventEntry> list = (List<SystemEventEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (SystemEventEntry systemEventEntry : list) {
				if ((groupId != systemEventEntry.getGroupId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (!pagination) {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<SystemEventEntry>(list);
				}
				else {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
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
	 * Returns the first system event entry in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching system event entry
	 * @throws com.liferay.portal.NoSuchSystemEventEntryException if a matching system event entry could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry findByGroupId_First(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByGroupId_First(groupId,
				orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
	}

	/**
	 * Returns the first system event entry in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching system event entry, or <code>null</code> if a matching system event entry could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry fetchByGroupId_First(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		List<SystemEventEntry> list = findByGroupId(groupId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry findByGroupId_Last(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByGroupId_Last(groupId,
				orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
	}

	/**
	 * Returns the last system event entry in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching system event entry, or <code>null</code> if a matching system event entry could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry fetchByGroupId_Last(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByGroupId(groupId);

		List<SystemEventEntry> list = findByGroupId(groupId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry[] findByGroupId_PrevAndNext(long systemEventId,
		long groupId, OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = findByPrimaryKey(systemEventId);

		Session session = null;

		try {
			session = openSession();

			SystemEventEntry[] array = new SystemEventEntryImpl[3];

			array[0] = getByGroupId_PrevAndNext(session, systemEventEntry,
					groupId, orderByComparator, true);

			array[1] = systemEventEntry;

			array[2] = getByGroupId_PrevAndNext(session, systemEventEntry,
					groupId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected SystemEventEntry getByGroupId_PrevAndNext(Session session,
		SystemEventEntry systemEventEntry, long groupId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

		query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

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
			query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(systemEventEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<SystemEventEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the system event entries where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByGroupId(long groupId) throws SystemException {
		for (SystemEventEntry systemEventEntry : findByGroupId(groupId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(systemEventEntry);
		}
	}

	/**
	 * Returns the number of system event entries where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching system event entries
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByGroupId(long groupId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_GROUPID;

		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

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

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "systemEventEntry.groupId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C_C = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_C = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			SystemEventEntryModelImpl.GROUPID_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CLASSPK_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_C_C = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_C_C",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			});

	/**
	 * Returns all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63;.
	 *
	 * @param groupId the group ID
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @return the matching system event entries
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<SystemEventEntry> findByG_C_C(long groupId, long classNameId,
		long classPK) throws SystemException {
		return findByG_C_C(groupId, classNameId, classPK, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<SystemEventEntry> findByG_C_C(long groupId, long classNameId,
		long classPK, int start, int end) throws SystemException {
		return findByG_C_C(groupId, classNameId, classPK, start, end, null);
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
	@Override
	public List<SystemEventEntry> findByG_C_C(long groupId, long classNameId,
		long classPK, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_C;
			finderArgs = new Object[] { groupId, classNameId, classPK };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_C_C;
			finderArgs = new Object[] {
					groupId, classNameId, classPK,
					
					start, end, orderByComparator
				};
		}

		List<SystemEventEntry> list = (List<SystemEventEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (SystemEventEntry systemEventEntry : list) {
				if ((groupId != systemEventEntry.getGroupId()) ||
						(classNameId != systemEventEntry.getClassNameId()) ||
						(classPK != systemEventEntry.getClassPK())) {
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

			query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_G_C_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_G_C_C_CLASSPK_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(classNameId);

				qPos.add(classPK);

				if (!pagination) {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<SystemEventEntry>(list);
				}
				else {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
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
	@Override
	public SystemEventEntry findByG_C_C_First(long groupId, long classNameId,
		long classPK, OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByG_C_C_First(groupId,
				classNameId, classPK, orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
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
	@Override
	public SystemEventEntry fetchByG_C_C_First(long groupId, long classNameId,
		long classPK, OrderByComparator orderByComparator)
		throws SystemException {
		List<SystemEventEntry> list = findByG_C_C(groupId, classNameId,
				classPK, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry findByG_C_C_Last(long groupId, long classNameId,
		long classPK, OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByG_C_C_Last(groupId,
				classNameId, classPK, orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
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
	@Override
	public SystemEventEntry fetchByG_C_C_Last(long groupId, long classNameId,
		long classPK, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_C_C(groupId, classNameId, classPK);

		List<SystemEventEntry> list = findByG_C_C(groupId, classNameId,
				classPK, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry[] findByG_C_C_PrevAndNext(long systemEventId,
		long groupId, long classNameId, long classPK,
		OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = findByPrimaryKey(systemEventId);

		Session session = null;

		try {
			session = openSession();

			SystemEventEntry[] array = new SystemEventEntryImpl[3];

			array[0] = getByG_C_C_PrevAndNext(session, systemEventEntry,
					groupId, classNameId, classPK, orderByComparator, true);

			array[1] = systemEventEntry;

			array[2] = getByG_C_C_PrevAndNext(session, systemEventEntry,
					groupId, classNameId, classPK, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected SystemEventEntry getByG_C_C_PrevAndNext(Session session,
		SystemEventEntry systemEventEntry, long groupId, long classNameId,
		long classPK, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

		query.append(_FINDER_COLUMN_G_C_C_GROUPID_2);

		query.append(_FINDER_COLUMN_G_C_C_CLASSNAMEID_2);

		query.append(_FINDER_COLUMN_G_C_C_CLASSPK_2);

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
			query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(classNameId);

		qPos.add(classPK);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(systemEventEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<SystemEventEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the system event entries where groupId = &#63; and classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_C_C(long groupId, long classNameId, long classPK)
		throws SystemException {
		for (SystemEventEntry systemEventEntry : findByG_C_C(groupId,
				classNameId, classPK, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(systemEventEntry);
		}
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
	@Override
	public int countByG_C_C(long groupId, long classNameId, long classPK)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_C_C;

		Object[] finderArgs = new Object[] { groupId, classNameId, classPK };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_G_C_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_G_C_C_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(classNameId);

				qPos.add(classPK);

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

	private static final String _FINDER_COLUMN_G_C_C_GROUPID_2 = "systemEventEntry.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_C_C_CLASSNAMEID_2 = "systemEventEntry.classNameId = ? AND ";
	private static final String _FINDER_COLUMN_G_C_C_CLASSPK_2 = "systemEventEntry.classPK = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_E_C_C = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_E_C_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_E_C_C =
		new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED,
			SystemEventEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_E_C_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Long.class.getName(), Long.class.getName()
			},
			SystemEventEntryModelImpl.GROUPID_COLUMN_BITMASK |
			SystemEventEntryModelImpl.EVENTTYPE_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CLASSPK_COLUMN_BITMASK |
			SystemEventEntryModelImpl.CREATEDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_E_C_C = new FinderPath(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_E_C_C",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Long.class.getName(), Long.class.getName()
			});

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
	@Override
	public List<SystemEventEntry> findByG_E_C_C(long groupId, int eventType,
		long classNameId, long classPK) throws SystemException {
		return findByG_E_C_C(groupId, eventType, classNameId, classPK,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<SystemEventEntry> findByG_E_C_C(long groupId, int eventType,
		long classNameId, long classPK, int start, int end)
		throws SystemException {
		return findByG_E_C_C(groupId, eventType, classNameId, classPK, start,
			end, null);
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
	@Override
	public List<SystemEventEntry> findByG_E_C_C(long groupId, int eventType,
		long classNameId, long classPK, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_E_C_C;
			finderArgs = new Object[] { groupId, eventType, classNameId, classPK };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_E_C_C;
			finderArgs = new Object[] {
					groupId, eventType, classNameId, classPK,
					
					start, end, orderByComparator
				};
		}

		List<SystemEventEntry> list = (List<SystemEventEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (SystemEventEntry systemEventEntry : list) {
				if ((groupId != systemEventEntry.getGroupId()) ||
						(eventType != systemEventEntry.getEventType()) ||
						(classNameId != systemEventEntry.getClassNameId()) ||
						(classPK != systemEventEntry.getClassPK())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(6 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(6);
			}

			query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_G_E_C_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_E_C_C_EVENTTYPE_2);

			query.append(_FINDER_COLUMN_G_E_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_G_E_C_C_CLASSPK_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(eventType);

				qPos.add(classNameId);

				qPos.add(classPK);

				if (!pagination) {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<SystemEventEntry>(list);
				}
				else {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
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
	@Override
	public SystemEventEntry findByG_E_C_C_First(long groupId, int eventType,
		long classNameId, long classPK, OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByG_E_C_C_First(groupId,
				eventType, classNameId, classPK, orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", eventType=");
		msg.append(eventType);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
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
	@Override
	public SystemEventEntry fetchByG_E_C_C_First(long groupId, int eventType,
		long classNameId, long classPK, OrderByComparator orderByComparator)
		throws SystemException {
		List<SystemEventEntry> list = findByG_E_C_C(groupId, eventType,
				classNameId, classPK, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry findByG_E_C_C_Last(long groupId, int eventType,
		long classNameId, long classPK, OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByG_E_C_C_Last(groupId,
				eventType, classNameId, classPK, orderByComparator);

		if (systemEventEntry != null) {
			return systemEventEntry;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", eventType=");
		msg.append(eventType);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchSystemEventEntryException(msg.toString());
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
	@Override
	public SystemEventEntry fetchByG_E_C_C_Last(long groupId, int eventType,
		long classNameId, long classPK, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_E_C_C(groupId, eventType, classNameId, classPK);

		List<SystemEventEntry> list = findByG_E_C_C(groupId, eventType,
				classNameId, classPK, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public SystemEventEntry[] findByG_E_C_C_PrevAndNext(long systemEventId,
		long groupId, int eventType, long classNameId, long classPK,
		OrderByComparator orderByComparator)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = findByPrimaryKey(systemEventId);

		Session session = null;

		try {
			session = openSession();

			SystemEventEntry[] array = new SystemEventEntryImpl[3];

			array[0] = getByG_E_C_C_PrevAndNext(session, systemEventEntry,
					groupId, eventType, classNameId, classPK,
					orderByComparator, true);

			array[1] = systemEventEntry;

			array[2] = getByG_E_C_C_PrevAndNext(session, systemEventEntry,
					groupId, eventType, classNameId, classPK,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected SystemEventEntry getByG_E_C_C_PrevAndNext(Session session,
		SystemEventEntry systemEventEntry, long groupId, int eventType,
		long classNameId, long classPK, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_SYSTEMEVENTENTRY_WHERE);

		query.append(_FINDER_COLUMN_G_E_C_C_GROUPID_2);

		query.append(_FINDER_COLUMN_G_E_C_C_EVENTTYPE_2);

		query.append(_FINDER_COLUMN_G_E_C_C_CLASSNAMEID_2);

		query.append(_FINDER_COLUMN_G_E_C_C_CLASSPK_2);

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
			query.append(SystemEventEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(eventType);

		qPos.add(classNameId);

		qPos.add(classPK);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(systemEventEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<SystemEventEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
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
	@Override
	public void removeByG_E_C_C(long groupId, int eventType, long classNameId,
		long classPK) throws SystemException {
		for (SystemEventEntry systemEventEntry : findByG_E_C_C(groupId,
				eventType, classNameId, classPK, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(systemEventEntry);
		}
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
	@Override
	public int countByG_E_C_C(long groupId, int eventType, long classNameId,
		long classPK) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_E_C_C;

		Object[] finderArgs = new Object[] {
				groupId, eventType, classNameId, classPK
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_SYSTEMEVENTENTRY_WHERE);

			query.append(_FINDER_COLUMN_G_E_C_C_GROUPID_2);

			query.append(_FINDER_COLUMN_G_E_C_C_EVENTTYPE_2);

			query.append(_FINDER_COLUMN_G_E_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_G_E_C_C_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(eventType);

				qPos.add(classNameId);

				qPos.add(classPK);

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

	private static final String _FINDER_COLUMN_G_E_C_C_GROUPID_2 = "systemEventEntry.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_E_C_C_EVENTTYPE_2 = "systemEventEntry.eventType = ? AND ";
	private static final String _FINDER_COLUMN_G_E_C_C_CLASSNAMEID_2 = "systemEventEntry.classNameId = ? AND ";
	private static final String _FINDER_COLUMN_G_E_C_C_CLASSPK_2 = "systemEventEntry.classPK = ?";

	/**
	 * Caches the system event entry in the entity cache if it is enabled.
	 *
	 * @param systemEventEntry the system event entry
	 */
	@Override
	public void cacheResult(SystemEventEntry systemEventEntry) {
		EntityCacheUtil.putResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryImpl.class, systemEventEntry.getPrimaryKey(),
			systemEventEntry);

		systemEventEntry.resetOriginalValues();
	}

	/**
	 * Caches the system event entries in the entity cache if it is enabled.
	 *
	 * @param systemEventEntries the system event entries
	 */
	@Override
	public void cacheResult(List<SystemEventEntry> systemEventEntries) {
		for (SystemEventEntry systemEventEntry : systemEventEntries) {
			if (EntityCacheUtil.getResult(
						SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
						SystemEventEntryImpl.class,
						systemEventEntry.getPrimaryKey()) == null) {
				cacheResult(systemEventEntry);
			}
			else {
				systemEventEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all system event entries.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(SystemEventEntryImpl.class.getName());
		}

		EntityCacheUtil.clearCache(SystemEventEntryImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the system event entry.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(SystemEventEntry systemEventEntry) {
		EntityCacheUtil.removeResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryImpl.class, systemEventEntry.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<SystemEventEntry> systemEventEntries) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (SystemEventEntry systemEventEntry : systemEventEntries) {
			EntityCacheUtil.removeResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
				SystemEventEntryImpl.class, systemEventEntry.getPrimaryKey());
		}
	}

	/**
	 * Creates a new system event entry with the primary key. Does not add the system event entry to the database.
	 *
	 * @param systemEventId the primary key for the new system event entry
	 * @return the new system event entry
	 */
	@Override
	public SystemEventEntry create(long systemEventId) {
		SystemEventEntry systemEventEntry = new SystemEventEntryImpl();

		systemEventEntry.setNew(true);
		systemEventEntry.setPrimaryKey(systemEventId);

		return systemEventEntry;
	}

	/**
	 * Removes the system event entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param systemEventId the primary key of the system event entry
	 * @return the system event entry that was removed
	 * @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry remove(long systemEventId)
		throws NoSuchSystemEventEntryException, SystemException {
		return remove((Serializable)systemEventId);
	}

	/**
	 * Removes the system event entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the system event entry
	 * @return the system event entry that was removed
	 * @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry remove(Serializable primaryKey)
		throws NoSuchSystemEventEntryException, SystemException {
		Session session = null;

		try {
			session = openSession();

			SystemEventEntry systemEventEntry = (SystemEventEntry)session.get(SystemEventEntryImpl.class,
					primaryKey);

			if (systemEventEntry == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchSystemEventEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(systemEventEntry);
		}
		catch (NoSuchSystemEventEntryException nsee) {
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
	protected SystemEventEntry removeImpl(SystemEventEntry systemEventEntry)
		throws SystemException {
		systemEventEntry = toUnwrappedModel(systemEventEntry);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(systemEventEntry)) {
				systemEventEntry = (SystemEventEntry)session.get(SystemEventEntryImpl.class,
						systemEventEntry.getPrimaryKeyObj());
			}

			if (systemEventEntry != null) {
				session.delete(systemEventEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (systemEventEntry != null) {
			clearCache(systemEventEntry);
		}

		return systemEventEntry;
	}

	@Override
	public SystemEventEntry updateImpl(
		com.liferay.portal.model.SystemEventEntry systemEventEntry)
		throws SystemException {
		systemEventEntry = toUnwrappedModel(systemEventEntry);

		boolean isNew = systemEventEntry.isNew();

		SystemEventEntryModelImpl systemEventEntryModelImpl = (SystemEventEntryModelImpl)systemEventEntry;

		Session session = null;

		try {
			session = openSession();

			if (systemEventEntry.isNew()) {
				session.save(systemEventEntry);

				systemEventEntry.setNew(false);
			}
			else {
				session.merge(systemEventEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !SystemEventEntryModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((systemEventEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						systemEventEntryModelImpl.getOriginalGroupId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] { systemEventEntryModelImpl.getGroupId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}

			if ((systemEventEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						systemEventEntryModelImpl.getOriginalGroupId(),
						systemEventEntryModelImpl.getOriginalClassNameId(),
						systemEventEntryModelImpl.getOriginalClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_C,
					args);

				args = new Object[] {
						systemEventEntryModelImpl.getGroupId(),
						systemEventEntryModelImpl.getClassNameId(),
						systemEventEntryModelImpl.getClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_C_C,
					args);
			}

			if ((systemEventEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_E_C_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						systemEventEntryModelImpl.getOriginalGroupId(),
						systemEventEntryModelImpl.getOriginalEventType(),
						systemEventEntryModelImpl.getOriginalClassNameId(),
						systemEventEntryModelImpl.getOriginalClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_E_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_E_C_C,
					args);

				args = new Object[] {
						systemEventEntryModelImpl.getGroupId(),
						systemEventEntryModelImpl.getEventType(),
						systemEventEntryModelImpl.getClassNameId(),
						systemEventEntryModelImpl.getClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_E_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_E_C_C,
					args);
			}
		}

		EntityCacheUtil.putResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
			SystemEventEntryImpl.class, systemEventEntry.getPrimaryKey(),
			systemEventEntry);

		return systemEventEntry;
	}

	protected SystemEventEntry toUnwrappedModel(
		SystemEventEntry systemEventEntry) {
		if (systemEventEntry instanceof SystemEventEntryImpl) {
			return systemEventEntry;
		}

		SystemEventEntryImpl systemEventEntryImpl = new SystemEventEntryImpl();

		systemEventEntryImpl.setNew(systemEventEntry.isNew());
		systemEventEntryImpl.setPrimaryKey(systemEventEntry.getPrimaryKey());

		systemEventEntryImpl.setSystemEventId(systemEventEntry.getSystemEventId());
		systemEventEntryImpl.setGroupId(systemEventEntry.getGroupId());
		systemEventEntryImpl.setCompanyId(systemEventEntry.getCompanyId());
		systemEventEntryImpl.setUserId(systemEventEntry.getUserId());
		systemEventEntryImpl.setUserName(systemEventEntry.getUserName());
		systemEventEntryImpl.setCreateDate(systemEventEntry.getCreateDate());
		systemEventEntryImpl.setEventType(systemEventEntry.getEventType());
		systemEventEntryImpl.setClassNameId(systemEventEntry.getClassNameId());
		systemEventEntryImpl.setClassPK(systemEventEntry.getClassPK());
		systemEventEntryImpl.setClassUuid(systemEventEntry.getClassUuid());

		return systemEventEntryImpl;
	}

	/**
	 * Returns the system event entry with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the system event entry
	 * @return the system event entry
	 * @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchSystemEventEntryException, SystemException {
		SystemEventEntry systemEventEntry = fetchByPrimaryKey(primaryKey);

		if (systemEventEntry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchSystemEventEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return systemEventEntry;
	}

	/**
	 * Returns the system event entry with the primary key or throws a {@link com.liferay.portal.NoSuchSystemEventEntryException} if it could not be found.
	 *
	 * @param systemEventId the primary key of the system event entry
	 * @return the system event entry
	 * @throws com.liferay.portal.NoSuchSystemEventEntryException if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry findByPrimaryKey(long systemEventId)
		throws NoSuchSystemEventEntryException, SystemException {
		return findByPrimaryKey((Serializable)systemEventId);
	}

	/**
	 * Returns the system event entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the system event entry
	 * @return the system event entry, or <code>null</code> if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		SystemEventEntry systemEventEntry = (SystemEventEntry)EntityCacheUtil.getResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
				SystemEventEntryImpl.class, primaryKey);

		if (systemEventEntry == _nullSystemEventEntry) {
			return null;
		}

		if (systemEventEntry == null) {
			Session session = null;

			try {
				session = openSession();

				systemEventEntry = (SystemEventEntry)session.get(SystemEventEntryImpl.class,
						primaryKey);

				if (systemEventEntry != null) {
					cacheResult(systemEventEntry);
				}
				else {
					EntityCacheUtil.putResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
						SystemEventEntryImpl.class, primaryKey,
						_nullSystemEventEntry);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(SystemEventEntryModelImpl.ENTITY_CACHE_ENABLED,
					SystemEventEntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return systemEventEntry;
	}

	/**
	 * Returns the system event entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param systemEventId the primary key of the system event entry
	 * @return the system event entry, or <code>null</code> if a system event entry with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public SystemEventEntry fetchByPrimaryKey(long systemEventId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)systemEventId);
	}

	/**
	 * Returns all the system event entries.
	 *
	 * @return the system event entries
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<SystemEventEntry> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<SystemEventEntry> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
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
	@Override
	public List<SystemEventEntry> findAll(int start, int end,
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

		List<SystemEventEntry> list = (List<SystemEventEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_SYSTEMEVENTENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_SYSTEMEVENTENTRY;

				if (pagination) {
					sql = sql.concat(SystemEventEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<SystemEventEntry>(list);
				}
				else {
					list = (List<SystemEventEntry>)QueryUtil.list(q,
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
	 * Removes all the system event entries from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (SystemEventEntry systemEventEntry : findAll()) {
			remove(systemEventEntry);
		}
	}

	/**
	 * Returns the number of system event entries.
	 *
	 * @return the number of system event entries
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_SYSTEMEVENTENTRY);

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

	/**
	 * Initializes the system event entry persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.portal.util.PropsUtil.get(
						"value.object.listener.com.liferay.portal.model.SystemEventEntry")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<SystemEventEntry>> listenersList = new ArrayList<ModelListener<SystemEventEntry>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<SystemEventEntry>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(SystemEventEntryImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_SYSTEMEVENTENTRY = "SELECT systemEventEntry FROM SystemEventEntry systemEventEntry";
	private static final String _SQL_SELECT_SYSTEMEVENTENTRY_WHERE = "SELECT systemEventEntry FROM SystemEventEntry systemEventEntry WHERE ";
	private static final String _SQL_COUNT_SYSTEMEVENTENTRY = "SELECT COUNT(systemEventEntry) FROM SystemEventEntry systemEventEntry";
	private static final String _SQL_COUNT_SYSTEMEVENTENTRY_WHERE = "SELECT COUNT(systemEventEntry) FROM SystemEventEntry systemEventEntry WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "systemEventEntry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No SystemEventEntry exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No SystemEventEntry exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static Log _log = LogFactoryUtil.getLog(SystemEventEntryPersistenceImpl.class);
	private static SystemEventEntry _nullSystemEventEntry = new SystemEventEntryImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<SystemEventEntry> toCacheModel() {
				return _nullSystemEventEntryCacheModel;
			}
		};

	private static CacheModel<SystemEventEntry> _nullSystemEventEntryCacheModel = new CacheModel<SystemEventEntry>() {
			@Override
			public SystemEventEntry toEntityModel() {
				return _nullSystemEventEntry;
			}
		};
}