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

package com.liferay.portal.security.ldap.internal.upgrade;

import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.ldap.LDAPSettings;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.security.ldap.internal.upgrade.v1_0_0.UpgradeLDAPProperties;
import com.liferay.portal.security.ldap.internal.upgrade.v2_0_0.UpgradeLDAPPropertiesToSettings;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = UpgradeStepRegistrator.class)
public class LDAPImplUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.0", "1.0.0", new DummyUpgradeStep());

		registry.register(
			"0.0.1", "1.0.0",
			new UpgradeLDAPProperties(
				_companyLocalService, _configurationProvider,
				_ldapServerConfigurationProvider, _ldapSettings, _prefsProps,
				_props));

		registry.register(
			"1.0.0", "2.0.0",
			new UpgradeLDAPPropertiesToSettings(
				_configurationAdmin, _configurationProvider));
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration)"
	)
	private com.liferay.portal.security.ldap.configuration.ConfigurationProvider
		<LDAPServerConfiguration> _ldapServerConfigurationProvider;

	@Reference
	private LDAPSettings _ldapSettings;

	@Reference
	private PrefsProps _prefsProps;

	@Reference
	private Props _props;

}