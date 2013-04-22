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

package com.liferay.portal.kernel.calendar.service;

import com.liferay.portal.kernel.calendar.auth.ServiceAuth;

/**
 * Common implementation of Calendar service, keeping the
 * <code>serviceTargetId</code> and <code>serviceAuth</code> as private fields,
 * so that the actual service methods can use this information to fetch data
 * from external system.
 *
 * @author Josef Sustacek
 */
public class AbstractServiceImpl {

	public AbstractServiceImpl(String serviceTargetId, ServiceAuth serviceAuth) {
		_serviceTargetId = serviceTargetId;
		_serviceAuth = serviceAuth;
	}

	public ServiceAuth getServiceAuth() {
		return _serviceAuth;
	}

	public String getServiceTargetId() {
		return _serviceTargetId;
	}

	private ServiceAuth _serviceAuth;
	private String _serviceTargetId;

}
