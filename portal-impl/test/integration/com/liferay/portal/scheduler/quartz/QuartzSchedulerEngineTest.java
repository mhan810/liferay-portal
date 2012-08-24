/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.scheduler.quartz;

import com.liferay.portal.kernel.scheduler.CronTrigger;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.quartz.Trigger;
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class QuartzSchedulerEngineTest {

	@Test
	public void testCronTriggerFireTime() throws Exception {
		CronTrigger cronTrigger = new CronTrigger(
			"jobName", "groupName", "0/1 * * * * ?");

		Trigger trigger1 = _quartzSchedulerEngine.getQuartzTrigger(cronTrigger);

		Date nextFireTime1 = trigger1.getFireTimeAfter(trigger1.getStartTime());

		Thread.sleep(1000);

		Trigger trigger2 = _quartzSchedulerEngine.getQuartzTrigger(cronTrigger);

		Date nextFireTime2 = trigger2.getFireTimeAfter(trigger2.getStartTime());

		if (nextFireTime1.equals(nextFireTime2)) {
			Assert.fail();
		}
	}

	private QuartzSchedulerEngine _quartzSchedulerEngine =
		new QuartzSchedulerEngine();

}