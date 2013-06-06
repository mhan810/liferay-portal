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
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.SystemEventEntry;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class SystemEventEntryPersistenceTest {
	@After
	public void tearDown() throws Exception {
		Map<Serializable, BasePersistence<?>> basePersistences = _transactionalPersistenceAdvice.getBasePersistences();

		Set<Serializable> primaryKeys = basePersistences.keySet();

		for (Serializable primaryKey : primaryKeys) {
			BasePersistence<?> basePersistence = basePersistences.get(primaryKey);

			try {
				basePersistence.remove(primaryKey);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("The model with primary key " + primaryKey +
						" was already deleted");
				}
			}
		}

		_transactionalPersistenceAdvice.reset();
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEventEntry systemEventEntry = _persistence.create(pk);

		Assert.assertNotNull(systemEventEntry);

		Assert.assertEquals(systemEventEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SystemEventEntry newSystemEventEntry = addSystemEventEntry();

		_persistence.remove(newSystemEventEntry);

		SystemEventEntry existingSystemEventEntry = _persistence.fetchByPrimaryKey(newSystemEventEntry.getPrimaryKey());

		Assert.assertNull(existingSystemEventEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSystemEventEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEventEntry newSystemEventEntry = _persistence.create(pk);

		newSystemEventEntry.setGroupId(ServiceTestUtil.nextLong());

		newSystemEventEntry.setCompanyId(ServiceTestUtil.nextLong());

		newSystemEventEntry.setUserId(ServiceTestUtil.nextLong());

		newSystemEventEntry.setUserName(ServiceTestUtil.randomString());

		newSystemEventEntry.setCreateDate(ServiceTestUtil.nextDate());

		newSystemEventEntry.setEventType(ServiceTestUtil.nextInt());

		newSystemEventEntry.setClassNameId(ServiceTestUtil.nextLong());

		newSystemEventEntry.setClassPK(ServiceTestUtil.nextLong());

		newSystemEventEntry.setClassUuid(ServiceTestUtil.randomString());

		_persistence.update(newSystemEventEntry);

		SystemEventEntry existingSystemEventEntry = _persistence.findByPrimaryKey(newSystemEventEntry.getPrimaryKey());

		Assert.assertEquals(existingSystemEventEntry.getSystemEventId(),
			newSystemEventEntry.getSystemEventId());
		Assert.assertEquals(existingSystemEventEntry.getGroupId(),
			newSystemEventEntry.getGroupId());
		Assert.assertEquals(existingSystemEventEntry.getCompanyId(),
			newSystemEventEntry.getCompanyId());
		Assert.assertEquals(existingSystemEventEntry.getUserId(),
			newSystemEventEntry.getUserId());
		Assert.assertEquals(existingSystemEventEntry.getUserName(),
			newSystemEventEntry.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingSystemEventEntry.getCreateDate()),
			Time.getShortTimestamp(newSystemEventEntry.getCreateDate()));
		Assert.assertEquals(existingSystemEventEntry.getEventType(),
			newSystemEventEntry.getEventType());
		Assert.assertEquals(existingSystemEventEntry.getClassNameId(),
			newSystemEventEntry.getClassNameId());
		Assert.assertEquals(existingSystemEventEntry.getClassPK(),
			newSystemEventEntry.getClassPK());
		Assert.assertEquals(existingSystemEventEntry.getClassUuid(),
			newSystemEventEntry.getClassUuid());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SystemEventEntry newSystemEventEntry = addSystemEventEntry();

		SystemEventEntry existingSystemEventEntry = _persistence.findByPrimaryKey(newSystemEventEntry.getPrimaryKey());

		Assert.assertEquals(existingSystemEventEntry, newSystemEventEntry);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchSystemEventEntryException");
		}
		catch (NoSuchSystemEventEntryException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		try {
			_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				getOrderByComparator());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SystemEventEntry",
			"systemEventId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true, "eventType",
			true, "classNameId", true, "classPK", true, "classUuid", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SystemEventEntry newSystemEventEntry = addSystemEventEntry();

		SystemEventEntry existingSystemEventEntry = _persistence.fetchByPrimaryKey(newSystemEventEntry.getPrimaryKey());

		Assert.assertEquals(existingSystemEventEntry, newSystemEventEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEventEntry missingSystemEventEntry = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSystemEventEntry);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SystemEventEntryActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SystemEventEntry systemEventEntry = (SystemEventEntry)object;

					Assert.assertNotNull(systemEventEntry);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SystemEventEntry newSystemEventEntry = addSystemEventEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEventEntry.class,
				SystemEventEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("systemEventId",
				newSystemEventEntry.getSystemEventId()));

		List<SystemEventEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SystemEventEntry existingSystemEventEntry = result.get(0);

		Assert.assertEquals(existingSystemEventEntry, newSystemEventEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEventEntry.class,
				SystemEventEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("systemEventId",
				ServiceTestUtil.nextLong()));

		List<SystemEventEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SystemEventEntry newSystemEventEntry = addSystemEventEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEventEntry.class,
				SystemEventEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"systemEventId"));

		Object newSystemEventId = newSystemEventEntry.getSystemEventId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("systemEventId",
				new Object[] { newSystemEventId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingSystemEventId = result.get(0);

		Assert.assertEquals(existingSystemEventId, newSystemEventId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEventEntry.class,
				SystemEventEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"systemEventId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("systemEventId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected SystemEventEntry addSystemEventEntry() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEventEntry systemEventEntry = _persistence.create(pk);

		systemEventEntry.setGroupId(ServiceTestUtil.nextLong());

		systemEventEntry.setCompanyId(ServiceTestUtil.nextLong());

		systemEventEntry.setUserId(ServiceTestUtil.nextLong());

		systemEventEntry.setUserName(ServiceTestUtil.randomString());

		systemEventEntry.setCreateDate(ServiceTestUtil.nextDate());

		systemEventEntry.setEventType(ServiceTestUtil.nextInt());

		systemEventEntry.setClassNameId(ServiceTestUtil.nextLong());

		systemEventEntry.setClassPK(ServiceTestUtil.nextLong());

		systemEventEntry.setClassUuid(ServiceTestUtil.randomString());

		_persistence.update(systemEventEntry);

		return systemEventEntry;
	}

	private static Log _log = LogFactoryUtil.getLog(SystemEventEntryPersistenceTest.class);
	private SystemEventEntryPersistence _persistence = (SystemEventEntryPersistence)PortalBeanLocatorUtil.locate(SystemEventEntryPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}