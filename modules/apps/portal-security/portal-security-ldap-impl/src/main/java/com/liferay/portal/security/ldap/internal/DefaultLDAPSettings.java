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

package com.liferay.portal.security.ldap.internal;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.ldap.LDAPSettings;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.ldap.SafeLdapFilterStringUtil;
import com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.security.ldap.configuration.SystemLDAPConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPExportConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPImportConfiguration;
import com.liferay.portal.security.ldap.validator.LDAPFilterValidator;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Edward Han
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = LDAPSettings.class)
public class DefaultLDAPSettings implements LDAPSettings {

	/**
	 * @deprecated As of Mueller (7.2.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public String getAuthSearchFilter(
			long ldapServerId, long companyId, String emailAddress,
			String screenName, String userId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		String filter = ldapServerConfiguration.authSearchFilter();

		if (_log.isDebugEnabled()) {
			_log.debug("Search filter before transformation " + filter);
		}

		filter = StringUtil.replace(
			filter,
			new String[] {
				"@company_id@", "@email_address@", "@screen_name@", "@user_id@"
			},
			new String[] {
				String.valueOf(companyId),
				SafeLdapFilterStringUtil.rfc2254Escape(emailAddress),
				SafeLdapFilterStringUtil.rfc2254Escape(screenName),
				SafeLdapFilterStringUtil.rfc2254Escape(userId)
			});

		_ldapFilterValidator.validate(filter);

		if (_log.isDebugEnabled()) {
			_log.debug("Search filter after transformation " + filter);
		}

		return filter;
	}

	@Override
	public Properties getContactExpandoMappings(
			long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactExpandoMappings = getProperties(
			ldapServerConfiguration.contactCustomMappings());

		LogUtil.debug(_log, contactExpandoMappings);

		return contactExpandoMappings;
	}

	@Override
	public Properties getContactMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactMappings = getProperties(
			ldapServerConfiguration.contactMappings());

		LogUtil.debug(_log, contactMappings);

		return contactMappings;
	}

	@Override
	public String[] getErrorPasswordHistoryKeywords(long companyId) {
		try {
			SystemLDAPConfiguration systemLDAPConfiguration =
				_configurationProvider.getCompanyConfiguration(
					SystemLDAPConfiguration.class, companyId);

			return systemLDAPConfiguration.errorPasswordHistoryKeywords();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Override
	public Properties getGroupMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties groupMappings = getProperties(
			ldapServerConfiguration.groupMappings());

		LogUtil.debug(_log, groupMappings);

		return groupMappings;
	}

	@Override
	public long getPreferredLDAPServerId(long companyId, String screenName) {
		User user = _userLocalService.fetchUserByScreenName(
			companyId, screenName);

		if (user == null) {
			return -1;
		}

		return user.getLdapServerId();
	}

	@Override
	public String getPropertyPostfix(long ldapServerId) {
		return StringPool.PERIOD + ldapServerId;
	}

	@Override
	public Properties getUserExpandoMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactExpandoMappings = getProperties(
			ldapServerConfiguration.userCustomMappings());

		LogUtil.debug(_log, contactExpandoMappings);

		return contactExpandoMappings;
	}

	@Override
	public Properties getUserMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties userMappings = getProperties(
			ldapServerConfiguration.userMappings());

		LogUtil.debug(_log, userMappings);

		return userMappings;
	}

	@Override
	public boolean isExportEnabled(long companyId) {
		try {
			LDAPImportConfiguration ldapImportConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPImportConfiguration.class, companyId);

			boolean defaultImportUserPasswordAutogenerated =
				ldapImportConfiguration.importUserPasswordAutogenerated();

			if (ldapImportConfiguration.importEnabled() &&
				defaultImportUserPasswordAutogenerated) {

				return false;
			}

			LDAPExportConfiguration ldapExportConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPExportConfiguration.class, companyId);

			return ldapExportConfiguration.exportEnabled();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Override
	public boolean isExportGroupEnabled(long companyId) {
		try {
			LDAPExportConfiguration ldapExportConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPExportConfiguration.class, companyId);

			return ldapExportConfiguration.exportGroupEnabled();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Override
	public boolean isImportEnabled(long companyId) {
		try {
			LDAPImportConfiguration ldapImportConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPImportConfiguration.class, companyId);

			return ldapImportConfiguration.importEnabled();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Override
	public boolean isImportOnStartup(long companyId) {
		try {
			LDAPImportConfiguration ldapImportConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPImportConfiguration.class, companyId);

			return ldapImportConfiguration.importOnStartup();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	@Override
	public boolean isPasswordPolicyEnabled(long companyId) {
		try {
			LDAPAuthConfiguration ldapAuthConfiguration =
				_configurationProvider.getCompanyConfiguration(
					LDAPAuthConfiguration.class, companyId);

			return ldapAuthConfiguration.passwordPolicyEnabled();
		}
		catch (ConfigurationException configurationException) {
			throw new SystemException(configurationException);
		}
	}

	protected Properties getProperties(String[] keyValuePairs) {
		Properties properties = new Properties();

		for (String keyValuePair : keyValuePairs) {
			String[] keyValue = StringUtil.split(keyValuePair, CharPool.EQUAL);

			if (ArrayUtil.isEmpty(keyValue)) {
				continue;
			}

			String value = StringPool.BLANK;

			if (keyValue.length == 2) {
				value = keyValue[1];
			}

			properties.put(keyValue[0], value);
		}

		return properties;
	}

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration)",
		unbind = "-"
	)
	protected void setLDAPServerConfigurationProvider(
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPServerConfiguration> ldapServerConfigurationProvider) {

		_ldapServerConfigurationProvider = ldapServerConfigurationProvider;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultLDAPSettings.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private LDAPFilterValidator _ldapFilterValidator;

	private com.liferay.portal.security.ldap.configuration.ConfigurationProvider
		<LDAPServerConfiguration> _ldapServerConfigurationProvider;
	private UserLocalService _userLocalService;

}