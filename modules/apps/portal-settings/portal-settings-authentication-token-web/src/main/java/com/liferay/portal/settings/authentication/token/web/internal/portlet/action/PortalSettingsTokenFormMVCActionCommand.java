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

package com.liferay.portal.settings.authentication.token.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.sso.token.constants.TokenConstants;
import com.liferay.portal.settings.portlet.action.BasePortalSettingsFormMVCActionCommand;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;
import com.liferay.portal.security.sso.token.security.auth.TokenLocation;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Greenwald
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + PortalSettingsPortletKeys.PORTAL_SETTINGS,
		"mvc.command.name=/portal_settings/token"
	},
	service = MVCActionCommand.class
)
public class PortalSettingsTokenFormMVCActionCommand
	extends BasePortalSettingsFormMVCActionCommand {

	@Override
	protected void doValidateForm(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		boolean tokenEnabled = ParamUtil.getBoolean(actionRequest, "token--enabled");

		if (!tokenEnabled) {
			return;
		}

		String tokenAuthenticationCookies = ParamUtil.getString(actionRequest, "token--authenticationCookies");
		String tokenLogoutURL = ParamUtil.getString(actionRequest, "token--logoutRedirectURL");
		String tokenUserTokenName = ParamUtil.getString(actionRequest, "token--userTokenName");
		String tokenLocation = ParamUtil.getString(actionRequest, "token--tokenLocation");

		// Validate tokenAuthenticationCookies
		if (Validator.isNull(tokenAuthenticationCookies)) {
			SessionErrors.add(actionRequest, "userAuthenticationCookiesInvalid");
		}

		// Validate tokenLogoutURL
		if (!Validator.isUrl(tokenLogoutURL)) {
			SessionErrors.add(actionRequest, "tokenLogoutURLInvalid");
		}

		// Validate tokenUserTokenName
		if (Validator.isNull(tokenUserTokenName)) {
			SessionErrors.add(actionRequest, "userTokenNameInvalid");
		}

		// Validate tokenLocation
		if (Validator.isNull(tokenLocation)) {
			SessionErrors.add(actionRequest, "userTokenLocationInvalid");
		}

		// Validate the tokenLocation enum values (COOKIE, REQUEST, REQUEST_HEADER, SESSION)
		boolean isTokenLocationValid = false;
		for (TokenLocation t : TokenLocation.values()) {
			if (tokenLocation.equals(t.name())) {
				isTokenLocationValid = true;
				break;
			}
		}

		if (!isTokenLocationValid) {
			SessionErrors.add(actionRequest, "userTokenLocationInvalid");
		}
	}

	@Override
	protected String getParameterNamespace() {
		return "token--";
	}

	@Override
	protected String getSettingsId() {
		return TokenConstants.SERVICE_NAME;
	}

}