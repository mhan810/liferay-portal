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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.security.ldap.configuration.SystemLDAPConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPExportConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPImportConfiguration;

import java.util.Dictionary;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Michael C. Han
 */
public class UpgradeLDAPPropertiesToSettings implements UpgradeStep {

	public UpgradeLDAPPropertiesToSettings(
		ConfigurationAdmin configurationAdmin,
		ConfigurationProvider configurationProvider) {

		_configurationAdmin = configurationAdmin;
		_configurationProvider = configurationProvider;
	}

	@Override
	public void upgrade(DBProcessContext dbProcessContext)
		throws UpgradeException {

		try {
			transferConfigurationProperties(LDAPAuthConfiguration.class);
			transferConfigurationProperties(LDAPExportConfiguration.class);
			transferConfigurationProperties(LDAPImportConfiguration.class);
			transferConfigurationProperties(SystemLDAPConfiguration.class);
		}
		catch (Exception exception) {
			throw new UpgradeException(exception);
		}
	}

	protected void transferConfigurationProperties(Class<?> configurationClass)
		throws Exception {

		String filterString = StringBundler.concat(
			"(service.factoryPid=", configurationClass.getName(), ")");

		Configuration[] configurations = _configurationAdmin.listConfigurations(
			filterString);

		for (Configuration configuration : configurations) {
			Dictionary<String, Object> properties =
				configuration.getProperties();

			Long companyId = (Long)properties.get("companyId");

			if (companyId == null) {
				continue;
			}

			_configurationProvider.saveCompanyConfiguration(
				configurationClass, companyId, properties);
		}
	}

	private final ConfigurationAdmin _configurationAdmin;
	private final ConfigurationProvider _configurationProvider;

}