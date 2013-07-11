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

package com.liferay.portal.kernel.backgroundtask;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.Lock;
import com.liferay.portal.service.LockLocalServiceUtil;

/**
 * @author Michael C. Han
 * @author Zsolt Berentey
 */
public class SerialBackgroundTaskExecutor implements BackgroundTaskExecutor {

	public SerialBackgroundTaskExecutor(
		BackgroundTaskExecutor backgroundTaskExecutor) {

		_backgroundTaskExecutor = backgroundTaskExecutor;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		Lock lock = null;

		String owner =
			backgroundTask.getName() + StringPool.POUND +
				backgroundTask.getBackgroundTaskId();

		try {
			if (isSerial()) {
				while (true) {
					try {
						lock = LockLocalServiceUtil.lock(
							BackgroundTaskExecutor.class.getName(),
							backgroundTask.getTaskExecutorClassName(), owner,
							false);

						break;
					}
					catch (Exception e) {
						continue;
					}
				}

				if (!lock.isNew()) {
					throw new DuplicateLockException(lock);
				}
			}

			return _backgroundTaskExecutor.execute(backgroundTask);
		}
		finally {
			if (lock != null) {
				LockLocalServiceUtil.unlock(
					BackgroundTaskExecutor.class.getName(),
					backgroundTask.getTaskExecutorClassName(), owner, false);
			}
		}
	}

	@Override
	public String handleException(BackgroundTask backgroundTask, Exception e) {
		return _backgroundTaskExecutor.handleException(backgroundTask, e);
	}

	@Override
	public boolean isSerial() {
		return _backgroundTaskExecutor.isSerial();
	}

	private BackgroundTaskExecutor _backgroundTaskExecutor;

}