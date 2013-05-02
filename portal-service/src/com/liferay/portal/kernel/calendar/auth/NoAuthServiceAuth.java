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

import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Used to authenticate to calendar service, when no credentials are needed.
 * This class still provides human-redable insformation abotu the user making
 * the e.g. booking (<code>portalUserId</code>, <code>portalEmailAddress</code>,
 * <code>portalFullName</code>) that can be used in for example note of the
 * booking (in target booking system).
 *
 * @author Josef Sustacek
 */
public class NoAuthServiceAuth extends AbstractServiceAuth
	implements ServiceAuth {

	public NoAuthServiceAuth(
		String portalUserId, String portalEmailAddress, String portalFullName) {

		super(portalUserId, portalEmailAddress, portalFullName);
	}

	public NoAuthServiceAuth(HttpServletRequest request) {

		super(request);
	}

	public NoAuthServiceAuth(PortletRequest request) {

		super(request);
	}

	public NoAuthServiceAuth(ThemeDisplay themeDisplay) {

		super(themeDisplay);
	}

	public AuthType getType() {
		return AuthType.NONE;
	}

	@Override
	public String toString() {
		return " NoAuthServiceAuth{}";
	}

}
