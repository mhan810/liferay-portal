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

import com.liferay.portal.kernel.lar.ExportImportMessage;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSender;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.model.StagedModel;

import java.util.Date;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageSenderImpl
	implements ExportImportMessageSender {

	public String getPortletDataHandlerClassName() {
		return _portletDataHandlerClassName;
	}

	public String getStagedModelClassName() {
		return _stagedModelClassName;
	}

	public void send(String action) {
		send(
			action, null, _portletDataHandlerClassName, _stagedModelClassName,
			null);
	}

	public void send(
		String action, Class<? extends StagedModel> stagedModelClass) {

		send(
			action, null, _portletDataHandlerClassName,
			stagedModelClass.getSimpleName(), null);
	}

	public void send(String action, StagedModel stagedModel) {
		send(action, null, null, _stagedModelClassName, stagedModel.getUuid());
	}

	public void send(String action, String message) {
		send(
			action, message, _portletDataHandlerClassName,
			_stagedModelClassName, null);
	}

	public void send(
		String action, String message,
		Class<? extends StagedModel> stagedModelClass) {

		send(
			action, message, _portletDataHandlerClassName,
			stagedModelClass.getSimpleName(), null);
	}

	public void send(String action, String message, StagedModel stagedModel) {
		send(
			action, message, _portletDataHandlerClassName,
			stagedModel.getModelClassName(), stagedModel.getUuid());
	}

	public void send(
		String action, String message, String portletDataHandlerClassName,
		String stagedModelClassName, String stagedModelUuid) {

		Date timestamp = new Date();

		ExportImportMessage exportImportMessage = createExportImportMessage(
			action, message, portletDataHandlerClassName, stagedModelClassName,
			stagedModelUuid, timestamp);

		MessageBusUtil.sendMessage(
			DestinationNames.EXPORT_IMPORT, exportImportMessage);
	}

	public void setPortletDataHandlerClassName(
		String portletDataHandlerClassName) {

		_portletDataHandlerClassName = portletDataHandlerClassName;
	}

	public void setStagedModelClassName(String stagedModelClassName) {
		_stagedModelClassName = stagedModelClassName;
	}

	protected ExportImportMessage createExportImportMessage(
		String action, String message, String portletDataHandlerClassName,
		String stagedModelClassName, String stagedModelUuid, Date timestamp) {

		ExportImportMessage exportImportMessage = new ExportImportMessage();

		exportImportMessage.setAction(action);
		exportImportMessage.setMessage(message);
		exportImportMessage.setPortletDataHandlerClassName(
			portletDataHandlerClassName);
		exportImportMessage.setStagedModelClassName(stagedModelClassName);
		exportImportMessage.setStagedModelUuid(stagedModelUuid);
		exportImportMessage.setTimestamp(timestamp);

		return exportImportMessage;
	}

	private String _portletDataHandlerClassName;
	private String _stagedModelClassName;

}