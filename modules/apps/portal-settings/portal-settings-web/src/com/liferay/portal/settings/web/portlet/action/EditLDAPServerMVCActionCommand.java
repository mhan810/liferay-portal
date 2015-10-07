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

package com.liferay.portal.settings.web.portlet.action;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.portal.kernel.ldap.DuplicateLDAPServerNameException;
import com.liferay.portal.kernel.ldap.LDAPFilterException;
import com.liferay.portal.kernel.ldap.LDAPServerNameException;
import com.liferay.portal.kernel.ldap.LDAPUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.CompanyService;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.WebKeys;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ryan Park
 * @author Philip Jones
 */
@Component(
	property = {
		"javax.portlet.name=" + PortalSettingsPortletKeys.PORTAL_SETTINGS,
		"mvc.command.name=/portal_settings/edit_ldap_server"
	},
	service = MVCActionCommand.class

)
public class EditLDAPServerMVCActionCommand extends BaseMVCActionCommand {

	@Override
	public void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateLDAPServer(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteLDAPServer(actionRequest);
			}

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			sendRedirect(actionRequest, actionResponse, redirect);
		}
		catch (Exception e) {
			String mvcPath = "/edit_ldap_server.jsp";

			if (e instanceof DuplicateLDAPServerNameException ||
				e instanceof LDAPFilterException ||
				e instanceof LDAPServerNameException) {

				SessionErrors.add(actionRequest, e.getClass());
			}
			else if (e instanceof PrincipalException) {
				SessionErrors.add(actionRequest, e.getClass());

				mvcPath = "/error.jsp";
			}
			else {
				throw e;
			}

			actionResponse.setRenderParameter("mvcPath", mvcPath);
		}
	}

	protected UnicodeProperties addLDAPServer(
			long companyId, UnicodeProperties properties)
		throws Exception {

		String defaultPostfix = StringPool.PERIOD + "0";

		Set<String> defaultKeys = new HashSet<>(_KEYS.length);

		for (String key : _KEYS) {
			defaultKeys.add(key + defaultPostfix);
		}

		long ldapServerId = _counterLocalService.increment();

		String postfix = StringPool.PERIOD + ldapServerId;

		Set<String> keysSet = properties.keySet();

		String[] keys = keysSet.toArray(new String[keysSet.size()]);

		for (String key : keys) {
			if (defaultKeys.contains(key)) {
				String value = properties.remove(key);

				if (key.equals("ldap.security.credentials" + defaultPostfix) &&
					value.equals(Portal.TEMP_OBFUSCATION_VALUE)) {

					value = PrefsPropsUtil.getString(
						"ldap.security.credentials");
				}

				properties.setProperty(
					key.replace(defaultPostfix, postfix), value);
			}
		}

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			companyId, true);

		String ldapServerIds = portletPreferences.getValue(
			"ldap.server.ids", StringPool.BLANK);

		ldapServerIds = StringUtil.add(
			ldapServerIds, String.valueOf(ldapServerId));

		properties.setProperty("ldap.server.ids", ldapServerIds);

		return properties;
	}

	protected void deleteLDAPServer(ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long ldapServerId = ParamUtil.getLong(actionRequest, "ldapServerId");

		// Remove portletPreferences

		String postfix = StringPool.PERIOD + ldapServerId;

		String[] keys = new String[_KEYS.length];

		for (int i = 0; i < _KEYS.length; i++) {
			keys[i] = _KEYS[i] + postfix;
		}

		_companyService.removePreferences(themeDisplay.getCompanyId(), keys);

		// Update portletPreferences

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			themeDisplay.getCompanyId(), true);

		UnicodeProperties properties = new UnicodeProperties();

		String ldapServerIds = portletPreferences.getValue(
			"ldap.server.ids", StringPool.BLANK);

		ldapServerIds = StringUtil.removeFromList(
			ldapServerIds, String.valueOf(ldapServerId));

		properties.put("ldap.server.ids", ldapServerIds);

		_companyService.updatePreferences(
			themeDisplay.getCompanyId(), properties);
	}

	@Reference(unbind = "-")
	protected void setCompanyService(CompanyService companyService) {
		_companyService = companyService;
	}

	@Reference(unbind = "-")
	protected void setCounterLocalService(
		CounterLocalService counterLocalService) {

		_counterLocalService = counterLocalService;
	}

	protected void updateLDAPServer(ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long ldapServerId = ParamUtil.getLong(actionRequest, "ldapServerId");

		UnicodeProperties properties = PropertiesParamUtil.getProperties(
			actionRequest, "settings--");

		validateLDAPServerName(
			ldapServerId, themeDisplay.getCompanyId(), properties);

		validateSearchFilters(actionRequest);

		if (ldapServerId <= 0) {
			properties = addLDAPServer(themeDisplay.getCompanyId(), properties);
		}

		_companyService.updatePreferences(
			themeDisplay.getCompanyId(), properties);
	}

	protected void validateLDAPServerName(
			long ldapServerId, long companyId, UnicodeProperties properties)
		throws Exception {

		String ldapServerName = properties.getProperty(
			"ldap.server.name." + ldapServerId);

		if (Validator.isNull(ldapServerName)) {
			throw new LDAPServerNameException();
		}

		long[] existingLDAPServerIds = StringUtil.split(
			PrefsPropsUtil.getString(companyId, "ldap.server.ids"), 0L);

		for (long existingLDAPServerId : existingLDAPServerIds) {
			if (ldapServerId == existingLDAPServerId) {
				continue;
			}

			String existingLDAPServerName = PrefsPropsUtil.getString(
				companyId, "ldap.server.name." + existingLDAPServerId);

			if (ldapServerName.equals(existingLDAPServerName)) {
				throw new DuplicateLDAPServerNameException();
			}
		}
	}

	protected void validateSearchFilters(ActionRequest actionRequest)
		throws Exception {

		String userFilter = ParamUtil.getString(
			actionRequest, "importUserSearchFilter");

		LDAPUtil.validateFilter(userFilter, "importUserSearchFilter");

		String groupFilter = ParamUtil.getString(
			actionRequest, "importGroupSearchFilter");

		LDAPUtil.validateFilter(groupFilter, "importGroupSearchFilter");
	}

	private static final String[] _KEYS = {
		"ldap.auth.search.filter", "ldap.base.dn", "ldap.base.provider.url",
		"ldap.contact.custom.mappings", "ldap.contact.mappings",
		"ldap.group.default.object.classes", "ldap.group.mappings",
		"ldap.groups.dn", "ldap.import.group.search.filter",
		"ldap.import.user.search.filter", "ldap.security.credentials",
		"ldap.security.principal", "ldap.server.name",
		"ldap.user.custom.mappings", "ldap.user.default.object.classes",
		"ldap.user.mappings", "ldap.users.dn"
	};

	private CompanyService _companyService;
	private CounterLocalService _counterLocalService;

}