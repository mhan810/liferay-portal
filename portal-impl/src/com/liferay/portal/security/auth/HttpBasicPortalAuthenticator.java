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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Based on {@link com.liferay.portal.servlet.filters.secure.SecureFilter}
 *
 * @author Tomas Polesovsky
 */
public class HttpBasicPortalAuthenticator implements PortalAuthenticator {

	public AuthenticationResult authenticate(
			AuthenticationContext authCtx)
		throws AuthException {

		AuthenticationResult result = new AuthenticationResult();

		try {
			long userId = PortalUtil.getBasicAuthUserId(authCtx.getRequest());
			if (userId > 0) {
				result.setState(AuthenticationResult.State.SUCCESS);
				result.setUserId(userId);

				//TODO: rewrite and insert into AuthenticationConfig/Context -
				// it is used only from PortalRequestProcessor
				HttpSession session = authCtx.getRequest().getSession();
				session.setAttribute(WebKeys.BASIC_AUTH_ENABLED, Boolean.TRUE);
			}
			else {
				result.setState(AuthenticationResult.State.IN_PROGRESS);

				HttpServletResponse response = authCtx.getResponse();
				response.setHeader(HttpHeaders.WWW_AUTHENTICATE, _BASIC_REALM);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		catch (Exception e) {
			throw new AuthException(e.getMessage(), e);
		}

		return result;
	}

	private static final String _BASIC_REALM =
		"Basic realm=\"" + Portal.PORTAL_REALM + "\"";

	private static Log _log =
		LogFactoryUtil.getLog(HttpBasicPortalAuthenticator.class);
}
