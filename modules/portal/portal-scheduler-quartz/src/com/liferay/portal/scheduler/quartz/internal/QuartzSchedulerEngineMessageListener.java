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

package com.liferay.portal.scheduler.quartz.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.proxy.ProxyMessageListener;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Edward C. Han
 */
@Component(
	immediate = true, property = {"destination.name=liferay/scheduler_engine"},
	service = MessageListener.class
)
public class QuartzSchedulerEngineMessageListener extends ProxyMessageListener {

	@Override
	public void setManager(Object manager) {
		if (manager instanceof SchedulerEngine) {
			setSchedulerEngine((SchedulerEngine)manager);
		}
		else {
			if (_log.isErrorEnabled()) {
				_log.error(
					"Manager type " + manager.getClass().getName() +
					" must implement " + SchedulerEngine.class.getName());
			}
		}
	}

	@Reference
	public void setSchedulerEngine(SchedulerEngine schedulerEngine) {
		super.setManager(schedulerEngine);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		QuartzSchedulerEngineMessageListener.class);

}