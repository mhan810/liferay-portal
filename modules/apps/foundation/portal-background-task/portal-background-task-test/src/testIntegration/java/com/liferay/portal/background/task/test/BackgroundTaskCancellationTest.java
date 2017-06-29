/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.background.task.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutorRegistryUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatus;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusMessageTranslator;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.Serializable;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Daniel Kocsis
 */
@RunWith(Arquillian.class)
public class BackgroundTaskCancellationTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		BackgroundTaskExecutorRegistryUtil.registerBackgroundTaskExecutor(
			"MockBackgroundTaskExecutor", new MockBackgroundTaskExecutor());
	}

	@After
	public void tearDown() throws Exception {
		BackgroundTaskExecutorRegistryUtil.unregisterBackgroundTaskExecutor(
			"MockBackgroundTaskExecutor");
	}

	@Test
	public void testTaskCancellation() throws Exception {
		BackgroundTask backgroundTask =
			BackgroundTaskLocalServiceUtil.addBackgroundTask(
				TestPropsValues.getUserId(), _group.getGroupId(),
				RandomTestUtil.randomString(), "MockBackgroundTaskExecutor",
				new HashMap<String, Serializable>(),
				ServiceContextTestUtil.getServiceContext());

		_startBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

		BackgroundTaskManagerUtil.interruptBackgroundTask(
			backgroundTask.getBackgroundTaskId());

		_interruptBackgroundTaskSignal.countDown();

		_interruptBackgroundTaskSignalSignal.await(10, TimeUnit.SECONDS);

		backgroundTask =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTaskWithoutCaching(
				backgroundTask.getBackgroundTaskId());

		Assert.assertEquals(
			BackgroundTaskConstants.STATUS_CANCELLED,
			backgroundTask.getStatus());

		Assert.assertFalse(backgroundTask.isCompleted());
	}

	@DeleteAfterTestRun
	private Group _group;

	private final CountDownLatch _interruptBackgroundTaskSignal =
		new CountDownLatch(1);
	private final CountDownLatch _interruptBackgroundTaskSignalSignal =
		new CountDownLatch(1);
	private final CountDownLatch _startBackgroundTaskSignal =
		new CountDownLatch(1);

	private class MockBackgroundTaskExecutor implements BackgroundTaskExecutor {

		@Override
		public BackgroundTaskExecutor clone() {
			return new MockBackgroundTaskExecutor();
		}

		@Override
		public BackgroundTaskResult execute(
				com.liferay.portal.kernel.backgroundtask.BackgroundTask
					backgroundTask)
			throws Exception {

			try {
				_startBackgroundTaskSignal.countDown();

				_interruptBackgroundTaskSignal.await(10, TimeUnit.SECONDS);

				BackgroundTaskManagerUtil.interruptCurrentBackgroundTask();

				return BackgroundTaskResult.SUCCESS;
			}
			finally {
				_interruptBackgroundTaskSignalSignal.countDown();
			}
		}

		@Override
		public String generateLockKey(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask) {

			return "MockBackgroundTaskExecutor";
		}

		@Override
		public BackgroundTaskDisplay getBackgroundTaskDisplay(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask) {

			return null;
		}

		@Override
		public BackgroundTaskStatusMessageTranslator
			getBackgroundTaskStatusMessageTranslator() {

			return new BackgroundTaskStatusMessageTranslator() {

				@Override
				public void translate(
					BackgroundTaskStatus backgroundTaskStatus,
					Message message) {
				}

			};
		}

		@Override
		public int getIsolationLevel() {
			return BackgroundTaskConstants.ISOLATION_LEVEL_CLASS;
		}

		@Override
		public String handleException(
			com.liferay.portal.kernel.backgroundtask.BackgroundTask
				backgroundTask, Exception e) {

			Assert.fail();

			return StringPool.BLANK;
		}

		@Override
		public boolean isSerial() {
			return Boolean.TRUE;
		}

	}

}