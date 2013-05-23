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

import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public interface ExportImportMessageSender {

	public static final String MESSAGE_ACTION_DELETE = "delete";

	public static final String MESSAGE_ACTION_EXPORT_STARTED = "export-started";

	public static final String MESSAGE_ACTION_EXPORT_STOPPED = "export-stopped";

	public static final String MESSAGE_ACTION_IMPORT_STARTED = "import-started";

	public static final String MESSAGE_ACTION_IMPORT_STOPPED = "import-stopped";

	public void send(String action);

	public void send(
		String action, Class<? extends StagedModel> stagedModelClass);

	public void send(String action, StagedModel stagedModel);

	public void send(String action, String message);

	public void send(
		String action, String message,
		Class<? extends StagedModel> stagedModelClass);

	public void send(String action, String message, StagedModel stagedModel);

	public void send(
		String action, String message, String portletDataHandlerClassName,
		String stagedModelClassName, String stagedModelUuid);

	public void setPortletDataHandlerClassName(
		String portletDataHandlerClassName);

	public void setStagedModelClassName(String stagedModelClassName);

}