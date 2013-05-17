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
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.model.StagedModel;

import java.lang.reflect.Method;

import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mate Thurzo
 */
public class StagedModelDataHandlerMessageAdvice extends BaseExportImportAdvice
	implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();

		String methodName = method.getName();

		if (!_ADVICE_METHOD_NAMES.contains(methodName)) {
			return methodInvocation.proceed();
		}

		Object[] arguments = methodInvocation.getArguments();

		StagedModel stagedModel = (StagedModel)arguments[1];

		ExportImportMessageSender exportImportMessageSender =
			getExportImportMessageSender(methodInvocation);

		String errorMessage = null;

		try {
			String messageAction = getMessageAction(methodName, true);

			exportImportMessageSender.send(messageAction, stagedModel);

			return methodInvocation.proceed();
		}
		catch (Exception e) {
			errorMessage = e.getMessage();

			throw e;
		}
		finally {
			String messageAction = getMessageAction(methodName, false);

			exportImportMessageSender.send(
				messageAction, errorMessage, stagedModel);
		}
	}

	private static final Set<String> _ADVICE_METHOD_NAMES = SetUtil.fromArray(
		new String[] {"exportStagedModel", "importStagedlocalModel"});

}