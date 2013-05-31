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

import com.liferay.portal.kernel.lar.messaging.ExportImportAction;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessage;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSender;
import com.liferay.portal.kernel.lar.messaging.ExportImportStatus;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;
import com.liferay.portal.model.StagedModel;

/**
 * @author Michael C. Han
 */
public class ExportImportMessageSenderImpl
	implements ExportImportMessageSender {

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel) {

		ExportImportMessage message =
			ExportImportMessage.createExportImportMessage(
				exportImportAction, exportImportStatus, stagedModel);

		_singleDestinationMessageSender.send(message);
	}

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, StagedModel stagedModel,
		Throwable rootCause) {

		ExportImportMessage message =
			ExportImportMessage.createExportImportMessage(
				exportImportAction, exportImportStatus, stagedModel, rootCause);

		_singleDestinationMessageSender.send(message);
	}

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId) {

		ExportImportMessage message =
			ExportImportMessage.createExportImportMessage(
				exportImportAction, exportImportStatus, portletId);

		_singleDestinationMessageSender.send(message);
	}

	public void sendMessage(
		ExportImportAction exportImportAction,
		ExportImportStatus exportImportStatus, String portletId,
		Throwable rootCause) {

		ExportImportMessage message =
			ExportImportMessage.createExportImportMessage(
				exportImportAction, exportImportStatus, portletId, rootCause);

		_singleDestinationMessageSender.send(message);
	}

	public void setSingleDestinationMessageSender(
		SingleDestinationMessageSender singleDestinationMessageSender) {

		_singleDestinationMessageSender = singleDestinationMessageSender;
	}

	private static SingleDestinationMessageSender
		_singleDestinationMessageSender;

}