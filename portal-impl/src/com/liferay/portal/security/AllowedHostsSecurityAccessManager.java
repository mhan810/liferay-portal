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
import com.liferay.portal.security.auth.AuthSettingsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link SecurityAccessManager Security access manager} that disables certain
 * blacklisted host addresses.
 * <p>
 * This is also an example of access manager that does not authenticate users.
 */
public class AllowedHostsSecurityAccessManager
	implements SecurityAccessManager {

	public long accept(HttpServletRequest request, HttpServletResponse response) {
		String remoteAddr = request.getRemoteAddr();

		if (AuthSettingsUtil.isAccessAllowed(request, _hostsAllowed)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Access allowed for " + remoteAddr);
			}
			return ACCESS_GRANTED;
		}

		if (_log.isWarnEnabled()) {
			_log.warn("Access denied for " + remoteAddr);
		}

		try {
			response.sendError(
				HttpServletResponse.SC_FORBIDDEN,
				"Access denied for " + remoteAddr);
		}
		catch (IOException e) {
			_log.error(e);
		}

		return ACCESS_DENIED;
	}

	private static Log _log =
		LogFactoryUtil.getLog(AllowedHostsSecurityAccessManager.class);

	private Set<String> _hostsAllowed = new HashSet<String>();

}