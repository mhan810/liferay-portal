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

package com.liferay.portal.servlet.filters.secure;

import com.liferay.portal.security.auth.AuthenticationConfig;
import com.liferay.portal.security.auth.HttpBasicPortalAuthenticator;
import com.liferay.portal.security.auth.ParameterAutoLogin;
import com.liferay.portal.security.auth.PortalAuthenticator;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tomas Polesovsky
 */
public class ServletFilterAuthenticationLoader {

	public AuthenticationConfig load(HttpServletRequest request,
		FilterConfig filterConfig){

		AuthenticationConfig result = new AuthenticationConfig();

		// TODO: load from configuration - filter config,
		// based on request URL, portal.properties ...

		if(request.getRequestURI().equals("/api/jsonws") ||
			request.getRequestURI().equals("/api/jsonws/")){

			// no auth - user is already signed or not
			result.setRemoteAccess(true);
		} else
		if(request.getRequestURI().startsWith("/api/jsonws/")){
			result.getOptionalAuthenticators().add(new ParameterAutoLogin());
			result.getOptionalAuthenticators().add(
				new HttpBasicPortalAuthenticator());

			result.setRemoteAccess(true);
		} else
		if(request.getRequestURI().startsWith("/api/atom/")){
			result.getRequiredAuthenticators().add(
				new HttpBasicPortalAuthenticator());

			result.setRemoteAccess(true);
		}

			return result;
	}
}
