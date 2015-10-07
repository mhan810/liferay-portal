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

package com.liferay.portal.ldap.impl;

import com.liferay.portal.kernel.ldap.LDAPUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.ldap.configuration.LDAPCompanyConfiguration;
import com.liferay.portal.ldap.configuration.LDAPCompanyConfigurationProvider;
import com.liferay.portal.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.ldap.configuration.LDAPServerConfigurationProvider;
import com.liferay.portal.model.User;
import com.liferay.portal.security.ldap.LDAPSettings;
import com.liferay.portal.service.UserLocalService;

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

	@Override
	public String getAuthSearchFilter(
			long ldapServerId, long companyId, String emailAddress,
			String screenName, String userId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

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
				String.valueOf(companyId), emailAddress, screenName, userId
			});

		LDAPUtil.validateFilter(filter);

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
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

		Properties contactExpandoMappings = PropertiesUtil.load(
			ldapServerConfiguration.contactCustomMappings());

		LogUtil.debug(_log, contactExpandoMappings);

		return contactExpandoMappings;
	}

	@Override
	public Properties getContactMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

		Properties contactMappings = PropertiesUtil.load(
			ldapServerConfiguration.contactMappings());

		LogUtil.debug(_log, contactMappings);

		return contactMappings;
	}

	@Override
	public Properties getGroupMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

		Properties groupMappings = PropertiesUtil.load(
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
	public Properties getUserExpandoMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

		Properties userExpandoMappings = PropertiesUtil.load(
			ldapServerConfiguration.userCustomMappings());

		LogUtil.debug(_log, userExpandoMappings);

		return userExpandoMappings;
	}

	@Override
	public Properties getUserMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getLDAPServerConfiguration(
				ldapServerId);

		Properties userMappings = PropertiesUtil.load(
			ldapServerConfiguration.userMappings());

		LogUtil.debug(_log, userMappings);

		return userMappings;
	}

	@Override
	public boolean isExportEnabled(long companyId) {
		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurationProvider.getLDAPCompanyConfiguration(
				companyId);

		boolean defaultImportUserPasswordAutogenerated =
			ldapCompanyConfiguration.importUserPasswordAutogenerated();

		if (isImportEnabled(companyId) &&
			defaultImportUserPasswordAutogenerated) {

			return false;
		}

		return ldapCompanyConfiguration.exportEnabled();
	}

	@Override
	public boolean isExportGroupEnabled(long companyId) {
		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurationProvider.getLDAPCompanyConfiguration(
				companyId);

		return ldapCompanyConfiguration.exportGroupEnabled();
	}

	@Override
	public boolean isImportEnabled(long companyId) {
		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurationProvider.getLDAPCompanyConfiguration(
				companyId);

		return ldapCompanyConfiguration.importEnabled();
	}

	@Override
	public boolean isImportOnStartup(long companyId) {
		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurationProvider.getLDAPCompanyConfiguration(
				companyId);

		return ldapCompanyConfiguration.importOnStartup();
	}

	@Override
	public boolean isPasswordPolicyEnabled(long companyId) {
		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurationProvider.getLDAPCompanyConfiguration(
				companyId);

		return ldapCompanyConfiguration.passwordPolicyEnabled();
	}

	@Reference
	protected void setLDAPCompanyConfigurationProvider(
		LDAPCompanyConfigurationProvider ldapCompanyConfigurationProvider) {

		_ldapCompanyConfigurationProvider = ldapCompanyConfigurationProvider;
	}

	@Reference
	protected void setLDAPServerConfigurationProvider(
		LDAPServerConfigurationProvider ldapServerConfigurationProvider) {

		_ldapServerConfigurationProvider = ldapServerConfigurationProvider;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultLDAPSettings.class);

	private LDAPCompanyConfigurationProvider _ldapCompanyConfigurationProvider;
	private LDAPServerConfigurationProvider _ldapServerConfigurationProvider;
	private UserLocalService _userLocalService;

}