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

package com.liferay.portal.backgroundtask.internal;

import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.BackgroundTaskLocalService;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.backgroundtask.util.comparator.BackgroundTaskCompletionDateComparator;
import com.liferay.portlet.backgroundtask.util.comparator.BackgroundTaskCreateDateComparator;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskManagerImpl
	implements BackgroundTaskManager<com.liferay.portal.model.BackgroundTask> {

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		addBackgroundTask(
			long userId, long groupId, String name,
			String[] servletContextNames, Class<?> taskExecutorClass,
			Map<String, Serializable> taskContextMap,
			ServiceContext serviceContext)
		throws PortalException {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.addBackgroundTask(
				userId, groupId, name, servletContextNames, taskExecutorClass,
				taskContextMap, serviceContext);

		return new BackgroundTaskImpl(backgroundTask);
	}

	public void addBackgroundTaskAttachment(
			long userId, long backgroundTaskId, String fileName, File file)
		throws PortalException {

		_backgroundTaskLocalService.addBackgroundTaskAttachment(
			userId, backgroundTaskId, fileName, file);
	}

	public void addBackgroundTaskAttachment(
			long userId, long backgroundTaskId, String fileName,
			InputStream inputStream)
		throws PortalException {

		_backgroundTaskLocalService.addBackgroundTaskAttachment(
			userId, backgroundTaskId, fileName, inputStream);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		amendBackgroundTask(
			long backgroundTaskId, Map<String, Serializable> taskContextMap,
			int status, ServiceContext serviceContext) {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.amendBackgroundTask(
				backgroundTaskId, taskContextMap, status, serviceContext);

		return new BackgroundTaskImpl(backgroundTask);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		amendBackgroundTask(
			long backgroundTaskId, Map<String, Serializable> taskContextMap,
			int status, String statusMessage, ServiceContext serviceContext) {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.amendBackgroundTask(
				backgroundTaskId, taskContextMap, status, statusMessage,
				serviceContext);

		return new BackgroundTaskImpl(backgroundTask);
	}

	public void cleanUpBackgroundTask(
		BackgroundTask<com.liferay.portal.model.BackgroundTask> backgroundTask,
		int status) {

		_backgroundTaskLocalService.cleanUpBackgroundTask(
			backgroundTask.getBackgroundTaskId(), status);
	}

	public void cleanUpBackgroundTasks() {
		_backgroundTaskLocalService.cleanUpBackgroundTasks();
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
			deleteBackgroundTask(long backgroundTaskId)
		throws PortalException {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.deleteBackgroundTask(backgroundTaskId);

		return new BackgroundTaskImpl(backgroundTask);
	}

	public void deleteCompanyBackgroundTasks(long companyId)
		throws PortalException {

		_backgroundTaskLocalService.deleteCompanyBackgroundTasks(companyId);
	}

	public void deleteGroupBackgroundTasks(long groupId)
		throws PortalException {

		_backgroundTaskLocalService.deleteGroupBackgroundTasks(groupId);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		fetchBackgroundTask(long backgroundTaskId) {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.fetchBackgroundTask(backgroundTaskId);

		return new BackgroundTaskImpl(backgroundTask);
	}

	@Override
	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		fetchFirstBackgroundTask(
			long groupId, String taskExecutorClassName, boolean completed,
			OrderByComparator<BackgroundTask> orderByComparator) {

		com.liferay.portal.model.BackgroundTask backgroundTaskModel =
			_backgroundTaskLocalService.fetchFirstBackgroundTask(
				groupId, taskExecutorClassName, completed,
				convert(orderByComparator));

		return new BackgroundTaskImpl(backgroundTaskModel);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		fetchFirstBackgroundTask(String taskExecutorClassName, int status) {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.fetchFirstBackgroundTask(
				taskExecutorClassName, status);

		return new BackgroundTaskImpl(backgroundTask);
	}

	@Override
	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		fetchFirstBackgroundTask(
			String taskExecutorClassName, int status,
			OrderByComparator<BackgroundTask> orderByComparator) {

		com.liferay.portal.model.BackgroundTask backgroundTaskModel =
			_backgroundTaskLocalService.fetchFirstBackgroundTask(
				taskExecutorClassName, status, convert(orderByComparator));

		return new BackgroundTaskImpl(backgroundTaskModel);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
			getBackgroundTask(long backgroundTaskId)
		throws PortalException {

		com.liferay.portal.model.BackgroundTask backgroundTask =
			_backgroundTaskLocalService.getBackgroundTask(backgroundTaskId);

		return new BackgroundTaskImpl(backgroundTask);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(long groupId, int status) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(groupId, status);

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(long groupId, String taskExecutorClassName) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassName);

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			long groupId, String taskExecutorClassName, int status) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassName, status);

		return convert(backgroundTaskModels);
	}

	@Override
	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			long groupId, String taskExecutorClassName, int start, int end,
			OrderByComparator<BackgroundTask> orderByComparator) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassName, start, end,
				convert(orderByComparator));

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(long groupId, String[] taskExecutorClassNames) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassNames);

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			long groupId, String[] taskExecutorClassNames, int status) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassNames, status);

		return convert(backgroundTaskModels);
	}

	@Override
	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			long groupId, String[] taskExecutorClassNames, int start, int end,
			OrderByComparator<BackgroundTask> orderByComparator) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				groupId, taskExecutorClassNames, start, end,
				convert(orderByComparator));

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(String taskExecutorClassName, int status) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				taskExecutorClassName, status);

		return convert(backgroundTaskModels);
	}

	@Override
	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			String taskExecutorClassName, int status, int start, int end,
			OrderByComparator<BackgroundTask> orderByComparator) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				taskExecutorClassName, status, start, end,
				convert(orderByComparator));

		return convert(backgroundTaskModels);
	}

	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(String[] taskExecutorClassNames, int status) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				taskExecutorClassNames, status);

		return convert(backgroundTaskModels);
	}

	@Override
	public List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		getBackgroundTasks(
			String[] taskExecutorClassNames, int status, int start, int end,
			OrderByComparator<BackgroundTask> orderByComparator) {

		List<com.liferay.portal.model.BackgroundTask> backgroundTaskModels =
			_backgroundTaskLocalService.getBackgroundTasks(
				taskExecutorClassNames, status, start, end,
				convert(orderByComparator));

		return convert(backgroundTaskModels);
	}

	public int getBackgroundTasksCount(
		long groupId, String taskExecutorClassName) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, taskExecutorClassName);
	}

	public int getBackgroundTasksCount(
		long groupId, String taskExecutorClassName, boolean completed) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, taskExecutorClassName, completed);
	}

	public int getBackgroundTasksCount(
		long groupId, String name, String taskExecutorClassName) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, name, taskExecutorClassName);
	}

	public int getBackgroundTasksCount(
		long groupId, String name, String taskExecutorClassName,
		boolean completed) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, name, taskExecutorClassName, completed);
	}

	public int getBackgroundTasksCount(
		long groupId, String[] taskExecutorClassNames) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, taskExecutorClassNames);
	}

	public int getBackgroundTasksCount(
		long groupId, String[] taskExecutorClassNames, boolean completed) {

		return _backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, taskExecutorClassNames, completed);
	}

	public String getBackgroundTaskStatusJSON(long backgroundTaskId) {
		return _backgroundTaskLocalService.getBackgroundTaskStatusJSON(
			backgroundTaskId);
	}

	public void resumeBackgroundTask(long backgroundTaskId) {
		_backgroundTaskLocalService.resumeBackgroundTask(backgroundTaskId);
	}

	public void triggerBackgroundTask(long backgroundTaskId) {
		_backgroundTaskLocalService.triggerBackgroundTask(backgroundTaskId);
	}

	public BackgroundTask<com.liferay.portal.model.BackgroundTask>
		updateBackgroundTask(
			BackgroundTask<com.liferay.portal.model.BackgroundTask>
				backgroundTask) {

		com.liferay.portal.model.BackgroundTask backgroundTaskModel =
			backgroundTask.getModel();

		_backgroundTaskLocalService.triggerBackgroundTask(
			backgroundTaskModel.getBackgroundTaskId());

		return new BackgroundTaskImpl(backgroundTaskModel);
	}

	protected List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
		convert(
			List<com.liferay.portal.model.BackgroundTask>
				backgroundTaskModels) {

		List<BackgroundTask<com.liferay.portal.model.BackgroundTask>>
			backgroundTasks = new ArrayList<>(backgroundTaskModels.size());

		for (com.liferay.portal.model.BackgroundTask backgroundTaskModel :
				backgroundTaskModels) {

			backgroundTasks.add(new BackgroundTaskImpl(backgroundTaskModel));
		}

		return backgroundTasks;
	}

	protected OrderByComparator<com.liferay.portal.model.BackgroundTask>
		convert(OrderByComparator<BackgroundTask> orderByComparator) {

		if (orderByComparator instanceof
				BackgroundTaskCompletionDateComparator) {

			return new com.liferay.portal.backgroundtask.internal.comparator.
				BackgroundTaskCompletionDateComparator(
					orderByComparator.isAscending());
		}
		else if (orderByComparator instanceof
					BackgroundTaskCreateDateComparator) {

			return new com.liferay.portal.backgroundtask.internal.comparator.
				BackgroundTaskCreateDateComparator(
					orderByComparator.isAscending());
		}
		else {
			if (_log.isInfoEnabled()) {
				_log.info(
					"No comparator available to convert: " +
						orderByComparator.getClass().getName());
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BackgroundTaskManagerImpl.class);

	@BeanReference(type = BackgroundTaskLocalService.class)
	private BackgroundTaskLocalService _backgroundTaskLocalService;

}