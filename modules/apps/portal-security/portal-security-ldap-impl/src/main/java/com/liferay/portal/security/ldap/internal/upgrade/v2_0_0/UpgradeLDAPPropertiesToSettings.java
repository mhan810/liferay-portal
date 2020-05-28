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

package com.liferay.portal.security.ldap.internal.upgrade.v2_0_0;

import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.security.ldap.configuration.SystemLDAPConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPExportConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPImportConfiguration;

import java.util.Dictionary;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class UpgradeLDAPPropertiesToSettings implements UpgradeStep {

	public UpgradeLDAPPropertiesToSettings(
		CompanyLocalService companyLocalService,
		ConfigurationProvider configurationProvider,
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPAuthConfiguration> ldapAuthConfigurationProvider,
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPExportConfiguration> ldapExportConfigurationProvider,
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPImportConfiguration> ldapImportConfigurationProvider,
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPServerConfiguration> systemLDAPConfigurationProvider) {

		_companyLocalService = companyLocalService;
		_configurationProvider = configurationProvider;
		_ldapExportConfigurationProvider = ldapExportConfigurationProvider;
		_ldapImportConfigurationProvider = ldapImportConfigurationProvider;
		_ldapAuthConfigurationProvider = ldapAuthConfigurationProvider;
		_systemLDAPConfigurationProvider = systemLDAPConfigurationProvider;
	}

	@Override
	public void upgrade(DBProcessContext dbProcessContext)
		throws UpgradeException {

		List<Company> companies = _companyLocalService.getCompanies();

		try {
			for (Company company : companies) {
				long companyId = company.getCompanyId();

				transferConfigurationProperties(
					companyId, _ldapAuthConfigurationProvider,
					LDAPAuthConfiguration.class);

				transferConfigurationProperties(
					companyId, _ldapImportConfigurationProvider,
					LDAPImportConfiguration.class);

				transferConfigurationProperties(
					companyId, _ldapImportConfigurationProvider,
					LDAPImportConfiguration.class);

				transferConfigurationProperties(
					companyId, _systemLDAPConfigurationProvider,
					SystemLDAPConfiguration.class);
			}
		}
		catch (ConfigurationException configurationException) {
			throw new UpgradeException(configurationException);
		}
	}

	protected void transferConfigurationProperties(
			long companyId,
			com.liferay.portal.security.ldap.configuration.ConfigurationProvider
				<?> configurationProvider,
			Class<?> configurationClass)
		throws ConfigurationException {

		Dictionary<String, Object> ldapExportProperties =
			configurationProvider.getConfigurationProperties(companyId);

		_configurationProvider.saveCompanyConfiguration(
			configurationClass, companyId, ldapExportProperties);
	}

	private final CompanyLocalService _companyLocalService;
	private final ConfigurationProvider _configurationProvider;
	private final
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPAuthConfiguration> _ldapAuthConfigurationProvider;
	private final
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPExportConfiguration> _ldapExportConfigurationProvider;
	private final
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPImportConfiguration> _ldapImportConfigurationProvider;
	private final
		com.liferay.portal.security.ldap.configuration.ConfigurationProvider
			<LDAPServerConfiguration> _systemLDAPConfigurationProvider;

}