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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Josef Sustacek
 */
public abstract class AbstractServiceAuth implements ServiceAuth {

	AbstractServiceAuth(
		String portalUserId, String portalEmailAddress, String portalFullName) {

		_portalUserId = portalUserId;
		_portalEmailAddress = portalEmailAddress;
		_portalFullName = portalFullName;
	}

	AbstractServiceAuth(PortletRequest request) {

		_checkNotNull(request, "request");

		ThemeDisplay themeDisplay =
			(ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);

		_initFields(themeDisplay);
	}


	AbstractServiceAuth(HttpServletRequest request) {

		_checkNotNull(request, "request");

		ThemeDisplay themeDisplay =
			(ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);

		_initFields(themeDisplay);
	}

	AbstractServiceAuth(ThemeDisplay themeDisplay) {

		_checkNotNull(themeDisplay, "themeDisplay");

		_initFields(themeDisplay);
	}

	private void _checkNotNull(Object object, String objectName)
		throws IllegalArgumentException{

		if(object == null) {
			throw new IllegalArgumentException("Argument " + objectName +
				" cannot be null");
		}
	}

	private void _initFields(ThemeDisplay themeDisplay) {

		if(themeDisplay != null) {

			if(themeDisplay.isSignedIn()) {

				User user = themeDisplay.getUser();

				_portalUserId = String.valueOf(user.getUserId());
				_portalEmailAddress = user.getEmailAddress();
				_portalFullName = user.getFullName();

			}
			else {

				_portalUserId = StringPool.BLANK;
				_portalEmailAddress = StringPool.BLANK;
				_portalFullName = RoleConstants.GUEST;
			}
		}
		else {
			throw new IllegalArgumentException(
				"ThemeDisplay has to be set as request attribute in order " +
					"to create ServiceAuth");
		}
	}

	public String getPortalUserId() {
		return _portalUserId;
	}

	public String getPortalEmailAddress() {
		return _portalEmailAddress;
	}

	public String getPortalFullName() {
		return _portalFullName;
	}

	private String _portalUserId;
	private String _portalEmailAddress;
	private String _portalFullName;

}
