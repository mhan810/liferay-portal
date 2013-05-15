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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.ExportImportTask;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.model.BackgroundTask;

/**
 * @author Daniel Kocsis
 */
public class ExportImportTaskFactoryUtil {

	public static ExportImportTaskFactory getExportImportTaskFactory() {
		PortalRuntimePermission.checkGetBeanProperty(
			ExportImportTaskFactory.class);

		return _exportImportTaskFactory;
	}

	public static ExportImportTask getExportTask(BackgroundTask backgroundTask)
		throws PortalException {

		return getExportImportTaskFactory().getExportTask(backgroundTask);
	}

	public void setExportImportTaskFactory(
		ExportImportTaskFactory exportImportTaskFactory) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_exportImportTaskFactory = exportImportTaskFactory;
	}

	private static ExportImportTaskFactory _exportImportTaskFactory;

}