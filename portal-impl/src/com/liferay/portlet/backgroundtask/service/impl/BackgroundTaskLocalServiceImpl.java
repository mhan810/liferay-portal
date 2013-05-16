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

package com.liferay.portlet.backgroundtask.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackRegistryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.backgroundtask.model.BackgroundTask;
import com.liferay.portlet.backgroundtask.model.BackgroundTaskConstants;
import com.liferay.portlet.backgroundtask.service.base.BackgroundTaskLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Daniel Kocsis
 * @author Michael C. Han
 */
public class BackgroundTaskLocalServiceImpl
	extends BackgroundTaskLocalServiceBaseImpl {

	public BackgroundTask addBackgroundTask(
			long userId, long groupId, Class backgroundTaskExecutorClass,
			String name, String data, ServiceContext serviceContext)
		throws PortalException, SystemException {

		return addBackgroundTask(
			userId, groupId, backgroundTaskExecutorClass, StringPool.BLANK,
			name, data, serviceContext);
	}

	public BackgroundTask addBackgroundTask(
			long userId, long groupId, Class backgroundTaskExecutorClass,
			String servletContextName, String name, String data,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);
		Date now = new Date();

		final long backgroundTaskId = counterLocalService.increment();

		BackgroundTask backgroundTask = backgroundTaskPersistence.create(
			backgroundTaskId);

		backgroundTask.setGroupId(groupId);
		backgroundTask.setCompanyId(user.getCompanyId());
		backgroundTask.setUserId(userId);
		backgroundTask.setUserName(user.getFullName());
		backgroundTask.setCreateDate(serviceContext.getCreateDate(now));
		backgroundTask.setModifiedDate(serviceContext.getModifiedDate(now));

		// Model attributes

		backgroundTask.setClassName(backgroundTaskExecutorClass.getName());
		backgroundTask.setServletContextName(servletContextName);
		backgroundTask.setName(name);
		backgroundTask.setData(data);

		// Status

		backgroundTask.setStatus(BackgroundTaskConstants.STATUS_NEW);

		backgroundTaskPersistence.update(backgroundTask);

		TransactionCommitCallbackRegistryUtil.registerCallback(
			new Callable<Void>() {

			public Void call() throws Exception {
				Message message = new Message();
				message.put("backgroundTaskId", backgroundTaskId);

				MessageBusUtil.sendMessage(
					DestinationNames.BACKGROUND_TASK_EXECUTOR, message);

				return null;
			}

		});

		return backgroundTask;
	}

	public List<BackgroundTask> getBackgroundTasks(long groupId, String type)
		throws SystemException {

		return backgroundTaskPersistence.findByG_C(groupId, type);
	}

	public List<BackgroundTask> getBackgroundTasks(
			long groupId, String type, int status)
		throws SystemException {

		return backgroundTaskPersistence.findByG_C_S(groupId, type, status);
	}

	public BackgroundTask updateBackgroundTask(
			long taskId, int status, String name, String data,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		Date now = new Date();

		BackgroundTask backgroundTask =
			backgroundTaskPersistence.findByPrimaryKey(taskId);

		backgroundTask.setModifiedDate(serviceContext.getModifiedDate(now));
		backgroundTask.setStatus(status);
		backgroundTask.setName(name);
		backgroundTask.setData(data);

		if ((status == BackgroundTaskConstants.STATUS_SUCCESSFUL) ||
			(status == BackgroundTaskConstants.STATUS_FAILED)) {

			backgroundTask.setCompletedDate(now);
		}

		backgroundTaskPersistence.update(backgroundTask);

		return backgroundTask;
	}

}