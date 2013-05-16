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

package com.liferay.portlet.backgroundtask.executor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portlet.backgroundtask.model.BackgroundTask;

/**
 * @author Daniel Kocsis
 */
public class BaseBackgroundTaskExecutor implements BackgroundTaskExecutor {

	public void execute(BackgroundTask backgroundTask) {
		long startTime = 0;

		if (_log.isInfoEnabled()) {
			_log.info(
				"Executing background task " +
					backgroundTask.getBackgroundTaskId());

			startTime = System.currentTimeMillis();
		}

		try {
			doExecute(backgroundTask);
		}
		catch (Exception e) {
			_log.error(
				"Unable to execute background task with id " +
					backgroundTask.getBackgroundTaskId());
		}
		finally {
			if (_log.isInfoEnabled()) {
				long duration = System.currentTimeMillis() - startTime;

				_log.info(
					"Executed background task in " +
						Time.getDuration(duration));
			}
		}
	}

	protected void doExecute(BackgroundTask backgroundTask) throws Exception {
		return;
	}

	private static Log _log = LogFactoryUtil.getLog(
		BaseBackgroundTaskExecutor.class);

}