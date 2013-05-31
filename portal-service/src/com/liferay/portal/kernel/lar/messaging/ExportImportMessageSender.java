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
 * @author Michael C. Han
 */
public interface ExportImportMessageSender {

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel);

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel,
		Throwable rootCause);

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId);

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId,
		Throwable rootCause);

}