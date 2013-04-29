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

import com.liferay.portal.kernel.lar.messaging.ExportImportMessageHandlerUtil;
import com.liferay.portal.model.StagedModel;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();

		String methodName = method.getName();

		if (!methodName.equals("exportStagedModel") &&
			!methodName.equals("importStagedModel")) {

			return methodInvocation.proceed();
		}

		Object[] arguments = methodInvocation.getArguments();

		com.liferay.portal.kernel.lar.PortletDataContext portletDataContext =
			(com.liferay.portal.kernel.lar.PortletDataContext)arguments[0];
		StagedModel stagedModel = (StagedModel)arguments[1];

		ExportImportMessageHandlerUtil.started(portletDataContext, stagedModel);

		try {
			methodInvocation.proceed();
		}
		catch (Exception e) {
			ExportImportMessageHandlerUtil.error(
				portletDataContext, stagedModel);

			throw e;
		}

		ExportImportMessageHandlerUtil.finished(
			portletDataContext, stagedModel);

		return null;
	}

}