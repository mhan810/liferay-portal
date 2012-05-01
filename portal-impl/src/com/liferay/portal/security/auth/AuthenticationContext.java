/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.security.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 */
public class AuthenticationContext {
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private AuthenticationConfig _authenticationConfig;

	public HttpServletRequest getRequest() {
		return _request;
	}

	public void setRequest(HttpServletRequest request) {
		this._request = request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public void setResponse(HttpServletResponse response) {
		this._response = response;
	}

	public AuthenticationConfig getAuthenticationConfig() {
		return _authenticationConfig;
	}

	public void setAuthenticationConfig(AuthenticationConfig authenticationConfig) {
		this._authenticationConfig = authenticationConfig;
	}
}
