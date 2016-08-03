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

package com.liferay.portal.search.verify;

import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.verify.VerifyProcess;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(immediate = true, service = VerifyProcess.class)
public class ReindexBackgroundTaskVerifyProcess extends VerifyProcess {

	@Activate
	@Override
	protected void doVerify() throws Exception {
		List<BackgroundTask> reindexBackgroundTasks =
			_backgroundTaskManager.getBackgroundTasks(
				CompanyConstants.SYSTEM,
				"com.liferay.portal.search.internal.background.task." +
					"ReindexPortalBackgroundTaskExecutor",
				BackgroundTaskConstants.STATUS_IN_PROGRESS);

		reindexBackgroundTasks.addAll(
			_backgroundTaskManager.getBackgroundTasks(
				CompanyConstants.SYSTEM,
				"com.liferay.portal.search.internal.background.task." +
					"ReindexSingleIndexerBackgroundTaskExecutor",
				BackgroundTaskConstants.STATUS_IN_PROGRESS));

		for (BackgroundTask backgroundTask : reindexBackgroundTasks) {
			_backgroundTaskManager.deleteBackgroundTask(
				backgroundTask.getBackgroundTaskId());
		}
	}

	@Reference(unbind = "-")
	protected void setBackgroundTaskManager(
		BackgroundTaskManager backgroundTaskManager) {

		_backgroundTaskManager = backgroundTaskManager;
	}

	private BackgroundTaskManager _backgroundTaskManager;

}