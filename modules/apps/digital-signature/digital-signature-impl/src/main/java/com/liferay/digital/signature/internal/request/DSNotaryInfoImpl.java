/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.digital.signature.internal.request;

import com.liferay.digital.signature.request.DSNotaryInfo;

/**
 * @author Michael C. Han
 */
public class DSNotaryInfoImpl implements DSNotaryInfo {

	public DSNotaryInfoImpl(String host, String name, String email) {
		_host = host;
		_name = name;
		_email = email;
	}

	@Override
	public String getEmail() {
		return _email;
	}

	@Override
	public String getHost() {
		return _host;
	}

	@Override
	public String getName() {
		return _name;
	}

	private final String _email;
	private final String _host;
	private final String _name;

}