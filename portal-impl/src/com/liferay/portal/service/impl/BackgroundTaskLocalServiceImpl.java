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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.BackgroundTaskConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.base.BackgroundTaskLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;

/**
 * @author Daniel Kocsis
 */
public class BackgroundTaskLocalServiceImpl
	extends BackgroundTaskLocalServiceBaseImpl {

	public BackgroundTask addTask(
			long userId, long groupId, String type, String name, String data,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);
		Date now = new Date();

		long backgroundTaskId = counterLocalService.increment();

		BackgroundTask backgroundTask = backgroundTaskPersistence.create(
			backgroundTaskId);

		backgroundTask.setGroupId(groupId);
		backgroundTask.setCompanyId(user.getCompanyId());
		backgroundTask.setUserId(userId);
		backgroundTask.setUserName(user.getFullName());
		backgroundTask.setCreateDate(serviceContext.getCreateDate(now));
		backgroundTask.setModifiedDate(serviceContext.getModifiedDate(now));

		// Model attributes

		backgroundTask.setType(type);
		backgroundTask.setName(name);
		backgroundTask.setData(data);

		// Status

		backgroundTask.setStatus(BackgroundTaskConstants.STATUS_NEW);

		backgroundTaskPersistence.update(backgroundTask);

		return backgroundTask;
	}

	public List<BackgroundTask> getTasks(long groupId, String type)
		throws SystemException {

		return backgroundTaskPersistence.findByG_T(groupId, type);
	}

	public List<BackgroundTask> getTasks(long groupId, String type, int status)
		throws SystemException {

		return backgroundTaskPersistence.findByG_T_S(groupId, type, status);
	}

	public BackgroundTask updateTask(
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

		if ((status == BackgroundTaskConstants.STATUS_SUCCEDED) ||
			(status == BackgroundTaskConstants.STATUS_FAILED)) {

			backgroundTask.setCompletedDate(now);
		}

		backgroundTaskPersistence.update(backgroundTask);

		return backgroundTask;
	}

}