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

package com.liferay.portal.kernel.lar.messaging;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.model.StagedModel;

import java.io.Serializable;

/**
 * @author Mate Thurzo
 * @author Michael C. Han
 */
public class ExportImportMessage extends Message implements Serializable {

	public static final String VALUE_KEY_DESCRIPTION = "description";
	public static final String VALUE_KEY_EXPORT_IMPORT_ACTION =
		"exportImportAction";
	public static final String VALUE_KEY_EXPORT_IMPORT_STATUS =
		"exportImportStatus";
	public static final String VALUE_KEY_MODEL_CLASS_NAME = "modelClassName";
	public static final String VALUE_KEY_PORTLET_ID = "portletId";
	public static final String VALUE_KEY_ROOT_CAUSE = "rootCause";
	public static final String VALUE_KEY_TIMESTAMP = "timestamp";
	public static final String VALUE_KEY_UUID = "uuid";

	public static ExportImportMessage createExportImportMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel) {

		ExportImportMessage exportImportMessage = new ExportImportMessage();

		exportImportMessage.setExportImportAction(exportImportAction);

		exportImportMessage.setExportImportStatus(exportImportStatus);

		exportImportMessage.setModelClassName(stagedModel.getModelClassName());

		exportImportMessage.setUuid(stagedModel.getUuid());

		exportImportMessage.setTimestamp(System.currentTimeMillis());

		return exportImportMessage;
	}

	public static ExportImportMessage createExportImportMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel,
		Throwable rootCause) {

		ExportImportMessage exportImportMessage = createExportImportMessage(
			exportImportAction, exportImportStatus, stagedModel);

		exportImportMessage.setRootCause(rootCause);

		return exportImportMessage;
	}

	public static ExportImportMessage createExportImportMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId) {

		ExportImportMessage exportImportMessage = new ExportImportMessage();

		exportImportMessage.setExportImportAction(exportImportAction);

		exportImportMessage.setExportImportStatus(exportImportStatus);

		exportImportMessage.setPortletId(portletId);

		exportImportMessage.setTimestamp(System.currentTimeMillis());

		return exportImportMessage;
	}

	public static ExportImportMessage createExportImportMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId,
		Throwable rootCause) {

		ExportImportMessage exportImportMessage = createExportImportMessage(
			exportImportAction, exportImportStatus, portletId);

		exportImportMessage.setRootCause(rootCause);

		return exportImportMessage;
	}

	public ExportImportAction getExportImportAction() {
		return (ExportImportAction)get(VALUE_KEY_EXPORT_IMPORT_ACTION);
	}

	public ExportImportStatus getExportImportStatus() {
		return (ExportImportStatus)get(VALUE_KEY_EXPORT_IMPORT_STATUS);
	}

	public void setDescription(String description) {
		put(VALUE_KEY_DESCRIPTION, description);
	}

	public void setExportImportAction(ExportImportAction exportImportAction) {
		put(VALUE_KEY_EXPORT_IMPORT_ACTION, exportImportAction);
	}

	public void setExportImportStatus(ExportImportStatus exportImportStatus) {
		put(VALUE_KEY_EXPORT_IMPORT_STATUS, exportImportStatus);
	}

	public void setModelClassName(String modelClassName) {
		put(VALUE_KEY_MODEL_CLASS_NAME, modelClassName);
	}

	public void setPortletId(String portletId) {
		put(VALUE_KEY_PORTLET_ID, portletId);
	}

	public void setRootCause(Throwable rootCause) {
		put(VALUE_KEY_ROOT_CAUSE, rootCause);
	}

	public void setTimestamp(long timestamp) {
		put(VALUE_KEY_TIMESTAMP, timestamp);
	}

	public void setUuid(String uuid) {
		put(VALUE_KEY_UUID, uuid);
	}

}