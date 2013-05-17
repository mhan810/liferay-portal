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

import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.util.ServiceBeanMethodInvocationFactoryUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Mate Thurzo
 */
public class PortletDataHandlerInvocationHandler implements InvocationHandler {

	public PortletDataHandlerInvocationHandler() {
	}

	public PortletDataHandlerInvocationHandler(PortletDataHandler delegate) {
		_delegate = delegate;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		return ServiceBeanMethodInvocationFactoryUtil.proceed(
			_delegate, PortletDataHandler.class, method, args, _adviceNames);
	}

	public void setAdviceNames(String[] adviceNames) {
		_adviceNames = adviceNames;
	}

	private static String[] _adviceNames;

	private PortletDataHandler _delegate;

}