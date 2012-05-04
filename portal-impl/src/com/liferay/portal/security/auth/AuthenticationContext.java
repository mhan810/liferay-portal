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

	public AuthenticationConfig getAuthenticationConfig() {
		return _authenticationConfig;
	}

	public Phase getAuthenticationPhase() {
		return _authenticationPhase;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public void setAuthenticationConfig(AuthenticationConfig authenticationConfig) {
		this._authenticationConfig = authenticationConfig;
	}

	public void setAuthenticationPhase(Phase authenticationPhase) {
		this._authenticationPhase = authenticationPhase;
	}

	public void setRequest(HttpServletRequest request) {
		this._request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this._response = response;
	}

	public enum Phase {
		PHASE_1, PHASE_2
	}

	private AuthenticationConfig _authenticationConfig;
	private Phase _authenticationPhase;
	private HttpServletRequest _request;
	private HttpServletResponse _response;
}
