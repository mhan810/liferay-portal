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

import com.liferay.portal.kernel.lar.ExportImportMessageSenderFactoryUtil;
import com.liferay.portal.kernel.lar.PortletDataHandlerProxy;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageSender;

import java.lang.reflect.Method;

import javax.portlet.PortletPreferences;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mate Thurzo
 */
public class PortletDataHandlerMessageAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object[] arguments = methodInvocation.getArguments();

		com.liferay.portal.kernel.lar.PortletDataContext portletDataContext =
			(com.liferay.portal.kernel.lar.PortletDataContext)arguments[0];
		String portletId = (String)arguments[1];
		PortletPreferences portletPreferences =
			(PortletPreferences)arguments[2];

		String data = null;

		Method method = methodInvocation.getMethod();

		String methodName = method.getName();

		if (methodName.equals("importData")) {
			data = (String)arguments[3];
		}

		PortletDataHandlerProxy proxy =
			(PortletDataHandlerProxy)methodInvocation.getThis();

		try {
			_exportImportMessageSender.send(
				"start - " + methodName, proxy.getPortletDataHandler(),
				System.currentTimeMillis());

			return methodInvocation.proceed();
		}
		finally {
			_exportImportMessageSender.send(
				"stop - " + methodName, proxy.getPortletDataHandler(),
				System.currentTimeMillis());
		}
	}

	private static final ExportImportMessageSender _exportImportMessageSender =
		ExportImportMessageSenderFactoryUtil.getExportImportMessageSender();

}