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

package com.liferay.portal.security.sso.token.internal.verify;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsDescriptor;
import com.liferay.portal.kernel.settings.SettingsException;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.sso.token.constants.LegacyTokenPropsKeys;
import com.liferay.portal.security.sso.token.constants.TokenConstants;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.verify.VerifyProcess;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ValidatorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Brian Greenwald
 */
@Component(
	immediate = true,
	property = {"verify.process.name=com.liferay.portal.security.sso.token.shibboleth"},
	service = VerifyProcess.class
)
public class ShibbolethCompanySettingsVerifyProcess extends VerifyProcess {

	protected void addShibbolethTokenKeys(
		Dictionary<String, String> dictionary, long companyId) {

		dictionary.put(
			TokenConstants.AUTH_ENABLED,
				_prefsProps.getString(
					companyId, LegacyTokenPropsKeys.SHIBBOLETH_AUTH_ENABLED,
					StringPool.FALSE));

		dictionary.put(
			TokenConstants.IMPORT_FROM_LDAP,
				_prefsProps.getString(
					companyId, LegacyTokenPropsKeys.SHIBBOLETH_IMPORT_FROM_LDAP,
					StringPool.FALSE));

		dictionary.put(
			TokenConstants.LOGOUT_REDIRECT_URL,
				_prefsProps.getString(
					companyId, LegacyTokenPropsKeys.SHIBBOLETH_LOGOUT_URL,
					"/Shibboleth.sso/Logout"));

		dictionary.put(
			TokenConstants.USER_HEADER,
				_prefsProps.getString(
					companyId, LegacyTokenPropsKeys.SHIBBOLETH_USER_HEADER,
					"SHIBBOLETH_USER_EMAIL"));
	}

	@Override
	protected void doVerify() throws Exception {
		verifyTokenProperties();
	}

	protected CompanyLocalService getCompanyLocalService() {
		return _companyLocalService;
	}

	protected String getSettingsId() {
		return TokenConstants.SERVICE_NAME;
	}

	protected SettingsFactory getSettingsFactory() {
		return _settingsFactory;
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setPrefsProps(PrefsProps prefsProps) {
		_prefsProps = prefsProps;
	}

	@Reference(unbind = "-")
	protected void setSettingsFactory(SettingsFactory settingsFactory) {
		_settingsFactory = settingsFactory;
	}

	protected void storeSettings(
			long companyId, String settingsId,
			Dictionary<String, String> dictionary)
		throws IOException, SettingsException, ValidatorException {

		Settings settings = _settingsFactory.getSettings(
			new CompanyServiceSettingsLocator(
				companyId, settingsId));

		ModifiableSettings modifiableSettings =
			settings.getModifiableSettings();

		SettingsDescriptor settingsDescriptor =
			_settingsFactory.getSettingsDescriptor(getSettingsId());

		for (String name : settingsDescriptor.getAllKeys()) {
			String value = GetterUtil.getString(dictionary.get(name));

			String oldValue = settings.getValue(name, null);

			if (!value.equals(oldValue)) {
				modifiableSettings.setValue(name, value);
			}
		}

		modifiableSettings.store();
	}

	protected Dictionary<String, String> getPropertyValues(long companyId)
		throws IOException, SettingsException, ValidatorException {

		Dictionary<String, String> dictionary = new HashMapDictionary<>();

		boolean shibbolethEnabled = _prefsProps.getBoolean(
			companyId, LegacyTokenPropsKeys.SHIBBOLETH_AUTH_ENABLED, false);

		if (shibbolethEnabled) {
			addShibbolethTokenKeys(dictionary, companyId);
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Adding Shibboleth Token configuration for company " +
					companyId +	" with properties: " + dictionary);
		}

		return dictionary;
	}

	protected void verifyTokenProperties() throws Exception {
		List<Company> companies = getCompanyLocalService().getCompanies(false);

		for (Company company : companies) {
			long companyId = company.getCompanyId();

			Dictionary<String, String> dictionary =
				getPropertyValues(companyId);

			storeSettings(companyId, getSettingsId(), dictionary);

			Set<String> keys = new HashSet<>();

			keys.addAll(Arrays.asList(LegacyTokenPropsKeys.LEGACY_TOKEN_KEYS));

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Removing preference keys " + keys + " for company " +
					companyId);
			}

			getCompanyLocalService().removePreferences(
				companyId, keys.toArray(new String[keys.size()]));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ShibbolethCompanySettingsVerifyProcess.class);

	private volatile CompanyLocalService _companyLocalService;
	private volatile PrefsProps _prefsProps;
	private volatile SettingsFactory _settingsFactory;

}