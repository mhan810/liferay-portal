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

package com.liferay.portal.kernel.calendar.auth;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Josef Sustacek
 */
public class PasswordBasedServiceAuth extends AbstractServiceAuth
	implements ServiceAuth {

	public PasswordBasedServiceAuth(
		String serviceLogin, String servicePassword,
		String portalUserId, String portalEmailAddress, String portalFullName) {

		super(portalUserId, portalEmailAddress, portalFullName);

		_serviceLogin = serviceLogin;
		_servicePassword = servicePassword;
	}

	public PasswordBasedServiceAuth(
		String serviceLogin, String servicePassword,
		HttpServletRequest request) {

		super(request);

		_serviceLogin = serviceLogin;
		_servicePassword = servicePassword;
	}

	public PasswordBasedServiceAuth(
		String serviceLogin, String servicePassword,
		PortletRequest request) {

		super(request);

		_serviceLogin = serviceLogin;
		_servicePassword = servicePassword;
	}

	public PasswordBasedServiceAuth(
		String serviceLogin, String servicePassword,
		ThemeDisplay themeDisplay) {

		super(themeDisplay);

		_serviceLogin = serviceLogin;
		_servicePassword = servicePassword;
	}

	public String getServiceLogin() {
		return _serviceLogin;
	}

	public String getServicePassword() {
		return _servicePassword;
	}

	public AuthType getType() {
		return AuthType.LOGIN_AND_PASSWORD;
	}

	@Override
	public String toString() {
		return new StringBundler(11)
			.append("PasswordBasedServiceAuth {")
			.append("serviceLogin='").append(_serviceLogin)
			.append("', servicePassword='***'")
			.append("', portalUserId='").append(getPortalUserId())
			.append("', portalEmailAddress='").append(getPortalEmailAddress())
			.append("', portalFullName='").append(getPortalFullName())
			.append("'}")
			.toString();
	}

	private String _serviceLogin;
	private String _servicePassword;

}
