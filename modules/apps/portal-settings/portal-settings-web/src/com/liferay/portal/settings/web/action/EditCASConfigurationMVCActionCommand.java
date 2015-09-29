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

package com.liferay.portal.settings.web.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsDescriptor;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.sso.cas.constants.CASConstants;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;
import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
@Component(
		property = {
			"javax.portlet.name=" + PortalSettingsPortletKeys.PORTAL_SETTINGS,
			"mvc.command.name=/portal_settings/edit_company_cas_configuration"
		},
		service = MVCActionCommand.class
	)
public class EditCASConfigurationMVCActionCommand
	extends BaseMVCActionCommand implements MVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		validateCAS(actionRequest);

		if (SessionErrors.isEmpty(actionRequest)) {
			updateCASSettings(actionRequest);
		}
	}

	protected Settings getSettings(ActionRequest actionRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return SettingsFactoryUtil.getSettings(
			new CompanyServiceSettingsLocator(
				themeDisplay.getCompanyId(), _SETTINGS_ID));
	}

	protected void validateCAS(ActionRequest actionRequest) {
		boolean casEnabled = ParamUtil.getBoolean(
			actionRequest, "cas--" + CASConstants.CAS_AUTH_ENABLED + "--");

		if (!casEnabled) {
			return;
		}

		String casLoginURL = ParamUtil.getString(
			actionRequest, "cas--" + CASConstants.CAS_LOGIN_URL + "--");
		String casLogoutURL = ParamUtil.getString(
			actionRequest, "cas--" + CASConstants.CAS_LOGOUT_URL + "--");
		String casServerName = ParamUtil.getString(
			actionRequest, "cas--" + CASConstants.CAS_SERVER_NAME + "--");
		String casServerURL = ParamUtil.getString(
			actionRequest, "cas--" + CASConstants.CAS_SERVER_URL + "--");
		String casServiceURL = ParamUtil.getString(
			actionRequest, "cas--" + CASConstants.CAS_SERVICE_URL + "--");
		String casNoSuchUserRedirectURL = ParamUtil.getString(
			actionRequest,
			"cas--" + CASConstants.CAS_NO_SUCH_USER_REDIRECT_URL + "--");

		if (!Validator.isUrl(casLoginURL)) {
			SessionErrors.add(actionRequest, "casLoginURLInvalid");
		}

		if (!Validator.isUrl(casLogoutURL)) {
			SessionErrors.add(actionRequest, "casLogoutURLInvalid");
		}

		if (Validator.isNull(casServerName)) {
			SessionErrors.add(actionRequest, "casServerNameInvalid");
		}

		if (!Validator.isUrl(casServerURL)) {
			SessionErrors.add(actionRequest, "casServerURLInvalid");
		}

		if (Validator.isNotNull(casServiceURL) &&
			!Validator.isUrl(casServiceURL)) {

			SessionErrors.add(actionRequest, "casServiceURLInvalid");
		}

		if (Validator.isNotNull(casNoSuchUserRedirectURL) &&
			!Validator.isUrl(casNoSuchUserRedirectURL)) {

			SessionErrors.add(actionRequest, "casNoSuchUserURLInvalid");
		}
	}

	private void updateCASSettings(ActionRequest actionRequest)
		throws Exception {

		Settings settings = getSettings(actionRequest);
		ModifiableSettings modifiableSettings =
			settings.getModifiableSettings();

		SettingsDescriptor settingsDescriptor =
			SettingsFactoryUtil.getSettingsDescriptor(_SETTINGS_ID);

		for (String name : settingsDescriptor.getAllKeys()) {
			String oldValue = settings.getValue(name, null);
			String value = ParamUtil.getString(
				actionRequest, "cas--" + name + "--", oldValue);

			modifiableSettings.setValue(name, value);
		}

		modifiableSettings.store();
	}

	private static final String _SETTINGS_ID = CASConstants.SERVICE_NAME;

}