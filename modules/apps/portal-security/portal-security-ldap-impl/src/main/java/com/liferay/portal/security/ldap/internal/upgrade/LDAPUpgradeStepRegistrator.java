package com.liferay.portal.security.ldap.internal.upgrade;

import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.ldap.LDAPSettings;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.PrefsProps;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.security.ldap.internal.upgrade.v1_0_0.UpgradeLDAPProperties;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = UpgradeStepRegistrator.class)
public class LDAPUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.0", "1.0.0",
			new UpgradeLDAPProperties(
				_companyLocalService, _configurationProvider,
				_ldapServerConfigurationProvider, _ldapSettings, _prefsProps,
				_props));

	}

	@Reference
	private CompanyLocalService _companyLocalService;

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