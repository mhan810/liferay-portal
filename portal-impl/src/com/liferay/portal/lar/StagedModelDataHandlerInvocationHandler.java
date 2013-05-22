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

import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.messaging.ExportImportAction;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSenderUtil;
import com.liferay.portal.kernel.lar.messaging.ExportImportStatus;
import com.liferay.portal.model.StagedModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mate Thurzo
 * @author Michael C. Han
 */
public class StagedModelDataHandlerInvocationHandler
	implements InvocationHandler {

	public StagedModelDataHandlerInvocationHandler(
		StagedModelDataHandler stagedModelDataHandler) {

		_stagedModelDataHandler = stagedModelDataHandler;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		String methodName = method.getName();

		ExportImportAction exportImportAction = null;

		if (methodName.equals(_METHOD_NAME_EXPORT_STAGED_MODEL)) {
			exportImportAction = ExportImportAction.EXPORT;
		}
		else if (methodName.equals(_METHOD_NAME_IMPORT_STAGED_MODEL)) {
			exportImportAction = ExportImportAction.IMPORT;
		}

		StagedModel stagedModel = null;

		if (exportImportAction != null) {
			stagedModel = (StagedModel)args[1];

			ExportImportMessageSenderUtil.sendMessage(
				exportImportAction, ExportImportStatus.START, stagedModel);
		}

		try {
			Object returnValue = method.invoke(_stagedModelDataHandler, args);

			if (exportImportAction != null) {
				ExportImportMessageSenderUtil.sendMessage(
					exportImportAction, ExportImportStatus.END, stagedModel);
			}

			return returnValue;
		}
		catch (InvocationTargetException ite) {
			Throwable rootCause = ite.getCause();

			if (exportImportAction != null) {
				ExportImportMessageSenderUtil.sendMessage(
					exportImportAction, ExportImportStatus.ERROR, stagedModel,
					rootCause);
			}

			throw rootCause;
		}
	}

	private static final String _METHOD_NAME_EXPORT_STAGED_MODEL =
		"exportStagedModel";

	private static final String _METHOD_NAME_IMPORT_STAGED_MODEL =
		"importStagedModel";

	private StagedModelDataHandler _stagedModelDataHandler;

}