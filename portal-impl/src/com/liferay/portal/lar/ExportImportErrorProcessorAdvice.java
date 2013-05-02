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

import com.liferay.portal.model.StagedModel;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.core.annotation.Order;

/**
 * @author Mate Thurzo
 */
@Order(1)
public class ExportImportErrorProcessorAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object[] arguments = methodInvocation.getArguments();

		com.liferay.portal.kernel.lar.PortletDataContext portletDataContext =
			(com.liferay.portal.kernel.lar.PortletDataContext)arguments[0];
		StagedModel stagedModel = (StagedModel)arguments[1];

		try {
			methodInvocation.proceed();
		}
		catch (Exception e) {
			processException(portletDataContext, stagedModel, e);

			throw e;
		}

		return null;
	}

	protected void processException(
		com.liferay.portal.kernel.lar.PortletDataContext portletDataContext,
		StagedModel stagedModel, Exception exception) {

		//TODO put error information to the portlet data context
		//TODO or more likely an error registry?

		return;
	}

}