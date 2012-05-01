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

package com.liferay.portal.security;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Quick-and-dirty basic-auth access manager.
 */
public class BasicAuthSecurityAccessManager implements SecurityAccessManager {

	public long accept(HttpServletRequest request, HttpServletResponse response,
					   boolean required) {

		// check if user is already authenticated
		HttpSession session = request.getSession();

		long userId = GetterUtil.getLong(
					(String)session.getAttribute(_AUTHENTICATED_USER));

		if (userId > 0) {
			return userId;
		}

		// checks for basic-auth info
		try {
			userId = PortalUtil.getBasicAuthUserId(request);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (userId == 0) {
			// user is denied
			if (required) {
				response.setHeader(HttpHeaders.WWW_AUTHENTICATE, _BASIC_REALM);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else {
				userId = ACCESS_GRANTED;
			}
		}
		else {
			// user access is granted
			session.setAttribute(_AUTHENTICATED_USER, String.valueOf(userId));
		}

		return userId;
	}
	
	private static final String _AUTHENTICATED_USER =
		BasicAuthSecurityAccessManager.class + "_AUTHENTICATED_USER";
	
	private static final String _BASIC_REALM =
		"Basic realm=\"" + Portal.PORTAL_REALM + '"';
	
	private static Log _log = LogFactoryUtil
		.getLog(BasicAuthSecurityAccessManager.class);

}