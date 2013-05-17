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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSender;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSenderFactoryUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mate Thurzo
 */
public abstract class BaseExportImportAdvice implements MethodInterceptor {

	protected ExportImportMessageSender getExportImportMessageSender(
		MethodInvocation methodInvocation) {

		Object advisedDataHandler = methodInvocation.getThis();

		return ExportImportMessageSenderFactoryUtil.
			getExportImportMessageSender(advisedDataHandler.getClass());
	}

	protected String getMessageAction(String methodName, boolean started) {
		boolean export = true;

		if (_IMPORT_METHOD_NAMES.contains(methodName)) {
			export = false;
		}

		String action = null;

		if (export && started) {
			action = ExportImportMessageSender.MESSAGE_ACTION_EXPORT_STARTED;
		}
		else if (export && !started) {
			action = ExportImportMessageSender.MESSAGE_ACTION_EXPORT_STOPPED;
		}
		else if (!export && started) {
			action = ExportImportMessageSender.MESSAGE_ACTION_IMPORT_STARTED;
		}
		else if (!export && !started) {
			action = ExportImportMessageSender.MESSAGE_ACTION_IMPORT_STOPPED;
		}

		return action;
	}

	private static final Set<String> _IMPORT_METHOD_NAMES = SetUtil.fromArray(
		new String[]{"importData", "importStagedModel"});

}