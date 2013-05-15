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
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSender;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSenderFactory;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.StagedModel;

import java.lang.reflect.Field;

import java.util.Date;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageSenderFactoryImpl
	implements ExportImportMessageSenderFactory {

	public ExportImportMessageSender getExportImportMessageSender(
		Class dataHandlerClass) {

		try {
			ExportImportMessageSender exportImportMessageSender =
				new ExportImportMessageSenderImpl();

			if (PortletDataHandler.class.isAssignableFrom(dataHandlerClass)) {
				exportImportMessageSender.setPortletDataHandlerClassName(
					dataHandlerClass.getSimpleName());
			}
			else if (StagedModelDataHandler.class.isAssignableFrom(
						dataHandlerClass)) {

				Field classNamesField = dataHandlerClass.getDeclaredField(
					"CLASS_NAMES");

				String[] classNames = (String[])classNamesField.get(null);

				if (classNames.length == 1) {
					exportImportMessageSender.setStagedModelClassName(
						classNames[0]);

					return exportImportMessageSender;
				}

				StagedModelDataHandler stagedModelDataHandler =
					(StagedModelDataHandler)dataHandlerClass.newInstance();

				String stagedModelClassName =
					stagedModelDataHandler.getClassName(null);

				exportImportMessageSender.setStagedModelClassName(
					stagedModelClassName);
			}
			else {
				return _dummyExportImportMessageSender;
			}

			return exportImportMessageSender;
		}
		catch (Exception e) {
			return _dummyExportImportMessageSender;
		}
	}

	private static final ExportImportMessageSender
		_dummyExportImportMessageSender =
			new ExportImportMessageSenderImpl() {

			@Override
			public String getPortletDataHandlerClassName() {
				return StringPool.BLANK;
			}

			@Override
			public String getStagedModelClassName() {
				return StringPool.BLANK;
			}

			@Override
			public void send(String action) {
				return;
			}

			@Override
			public void send(String action, String message) {
				return;
			}

			@Override
			public void send(
				String action, Class<? extends StagedModel> stagedModelClass) {

				return;
			}

			@Override
			public void send(
				String action, String message,
				Class<? extends StagedModel> stagedModelClass) {

				return;
			}

			@Override
			public void send(String action, StagedModel stagedModel) {
				return;
			}

			@Override
			public void send(
				String action, String message, StagedModel stagedModel) {

				return;
			}

			@Override
			public void send(
				String action, String message,
				String portletDataHandlerClassName, String stagedModelClassName,
				String stagedModelUuid) {

				return;
			}

			@Override
			public void setPortletDataHandlerClassName(
				String portletDataHandlerClassName) {

				return;
			}

			@Override
			public void setStagedModelClassName(String stagedModelClassName) {
				return;
			}

			@Override
			protected ExportImportMessage createExportImportMessage(
				String action, String message,
				String portletDataHandlerClassName, String stagedModelClassName,
				String stagedModelUuid, Date timestamp) {

				return null;
			}
		};

}