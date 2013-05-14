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

package com.liferay.portal.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.ExportImportTask;
import com.liferay.portal.kernel.util.ExportImportTaskFactory;
import com.liferay.portal.lar.LayoutsExportTask;
import com.liferay.portal.lar.PortletExportTask;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.BackgroundTaskConstants;

/**
 * @author Daniel Kocsis
 */
public class ExportImportTaskFactoryImpl implements ExportImportTaskFactory {

	@Override
	public ExportImportTask getExportTask(BackgroundTask backgroundTask)
		throws PortalException {

		String taskType = backgroundTask.getType();

		ExportImportTask exportTask = null;

		if (taskType.equals(BackgroundTaskConstants.TASK_TYPE_EXPORT_LAYOUTS)) {
			exportTask = new LayoutsExportTask(backgroundTask);
		}
		else if (taskType.equals(
					BackgroundTaskConstants.TASK_TYPE_EXPORT_PORTLET)) {

			exportTask = new PortletExportTask(backgroundTask);
		}

		return exportTask;
	}

}