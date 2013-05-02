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

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessage;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageFactoryUtil;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageHandler;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageLevel;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageHandlerImpl
	implements ExportImportMessageHandler {

	public static final String MESSAGE_ERROR = LanguageUtil.get(
		LocaleThreadLocal.getThemeDisplayLocale(), "error");
	public static final String MESSAGE_FINISHED = LanguageUtil.get(
		LocaleThreadLocal.getThemeDisplayLocale(), "finished");
	public static final String MESSAGE_STARTED = LanguageUtil.get(
		LocaleThreadLocal.getThemeDisplayLocale(), "started");

	public void error(
		PortletDataContext portletDataContext, StagedModel stagedModel) {

		ExportImportMessage errorMessage =
			ExportImportMessageFactoryUtil.getErrorMessage(
				stagedModel, MESSAGE_ERROR);

		sendMessage(errorMessage);
	}

	public void exportFinished(PortletDataContext portletDataContext) {

		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage("Export finished");

		sendMessage(message);
	}

	public void exportStarted(PortletDataContext portletDataContext) {

		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage("Export started");

		sendMessage(message);
	}

	public void finished(
		PortletDataContext portletDataContext, StagedModel stagedModel) {

		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage(
				stagedModel, MESSAGE_FINISHED);

		sendMessage(message);
	}

	public void importFinished(PortletDataContext portletDataContext) {
		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage("Import finished");

		sendMessage(message);
	}

	public void importStarted(PortletDataContext portletDataContext) {
		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage("Import started");

		sendMessage(message);
	}

	public void sendMessage(ExportImportMessage message) {
		if (checkMessageLevel(message)) {
			return;
		}

		if (Validator.isNotNull(message.getDestination())) {
			MessageBusUtil.sendMessage(message.getDestination(), message);
		}
	}

	public void started(
		PortletDataContext portletDataContext, StagedModel stagedModel) {

		ExportImportMessage message =
			ExportImportMessageFactoryUtil.getInfoMessage(
				stagedModel, MESSAGE_STARTED);

		sendMessage(message);
	}

	private boolean checkMessageLevel(ExportImportMessage message) {
		return _EXPORT_IMPORT_MESSAGE_LEVEL_THRESHOLD.ordinal() <
			message.getMessageLevel().ordinal();
	}

	private static final ExportImportMessageLevel
		_EXPORT_IMPORT_MESSAGE_LEVEL_THRESHOLD = ExportImportMessageLevel.INFO;

}