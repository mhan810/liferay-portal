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

package com.liferay.portal.ldap.internal.authenticator.configuration;

import aQute.configurable.Configurable;

import com.liferay.portal.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.ldap.configuration.ConfigurationProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.Configuration;
import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"factoryPid=com.liferay.portal.ldap.authenticator.configuration.LDAPAuthConfiguration"
	},
	service = ConfigurationProvider.class
)
public class LDAPAuthConfigurationProviderImpl
	implements ConfigurationProvider<LDAPAuthConfiguration> {

	@Override
	public LDAPAuthConfiguration getConfiguration(long companyId) {
		LDAPAuthConfiguration LDAPAuthConfiguration =
			_ldapAuthConfiguration.get(companyId);

		if (LDAPAuthConfiguration == null) {
			LDAPAuthConfiguration = _ldapAuthConfiguration.get(0L);
		}

		return LDAPAuthConfiguration;
	}

	@Override
	public LDAPAuthConfiguration getConfiguration(long companyId, long index) {
		return getConfiguration(companyId);
	}

	@Override
	public Collection<LDAPAuthConfiguration> getConfigurations(long companyId) {
		List<LDAPAuthConfiguration> LDAPAuthConfigurations = new ArrayList<>();

		LDAPAuthConfiguration LDAPAuthConfiguration = getConfiguration(
			companyId);

		LDAPAuthConfigurations.add(LDAPAuthConfiguration);

		return LDAPAuthConfigurations;
	}

	@Override
	public Class<LDAPAuthConfiguration> getMetatype() {
		return LDAPAuthConfiguration.class;
	}

	@Override
	public synchronized void registerConfiguration(
		Configuration configuration) {

		Dictionary<String, Object> properties = configuration.getProperties();

		LDAPAuthConfiguration LDAPAuthConfiguration =
			Configurable.createConfigurable(getMetatype(), properties);

		long companyId = LDAPAuthConfiguration.companyId();

		_ldapAuthConfiguration.put(companyId, LDAPAuthConfiguration);
	}

	@Override
	public synchronized void unregisterConfiguration(
		Configuration configuration) {

		Dictionary<String, Object> properties = configuration.getProperties();

		LDAPAuthConfiguration LDAPAuthConfiguration =
			Configurable.createConfigurable(getMetatype(), properties);

		long companyId = LDAPAuthConfiguration.companyId();

		_ldapAuthConfiguration.remove(companyId);
	}

	private final Map<Long, LDAPAuthConfiguration>
		_ldapAuthConfiguration = new HashMap<>();

}