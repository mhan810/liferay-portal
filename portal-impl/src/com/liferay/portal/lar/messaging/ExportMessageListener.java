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

package com.liferay.portal.lar.messaging;

import com.liferay.portal.kernel.lar.ExportImportTask;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.ExportImportTaskFactoryUtil;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;

/**
 * @author Daniel Kocsis
 */
public class ExportMessageListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		long backgroundTaskId = (Long)message.getPayload();

		BackgroundTask backgroundTask =
			BackgroundTaskLocalServiceUtil.fetchBackgroundTask(
				backgroundTaskId);

		if (backgroundTask == null) {
			_log.error(
				"Unable to find export task with id " + backgroundTaskId);

			return;
		}

		ExportImportTask exportTask = ExportImportTaskFactoryUtil.getExportTask(
			backgroundTask);

		try {
			exportTask.run();
		}
		catch (Exception e) {
			_log.error("Unable to run export task", e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ExportMessageListener.class);

}