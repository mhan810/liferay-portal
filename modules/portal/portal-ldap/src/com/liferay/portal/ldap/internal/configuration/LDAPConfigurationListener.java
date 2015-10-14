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

package com.liferay.portal.ldap.internal.configuration;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.ldap.configuration.ConfigurationProvider;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = ConfigurationListener.class)
public class LDAPConfigurationListener implements ConfigurationListener {

	@Override
	public void configurationEvent(ConfigurationEvent configurationEvent) {
		String factoryPid = configurationEvent.getFactoryPid();

		if (Validator.isNull(factoryPid) ||
			!_configurationProviders.containsKey(factoryPid)) {

			return;
		}

		ConfigurationProvider<?> configurationProvider =
			_configurationProviders.get(factoryPid);

		ServiceReference<ConfigurationAdmin> serviceReference =
			configurationEvent.getReference();

		ConfigurationAdmin configurationAdmin = _bundleContext.getService(
			serviceReference);

		try {
			Configuration configuration = configurationAdmin.getConfiguration(
				configurationEvent.getPid());

			if (configurationEvent.getType() == ConfigurationEvent.CM_DELETED) {
				configurationProvider.unregisterConfiguration(configuration);
			}
			else {
				configurationProvider.registerConfiguration(configuration);
			}
		}
		catch (IOException e) {
			throw new SystemException(
				"Could not load configuration: " + configurationEvent.getPid(),
				e);
		}
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		_bundleContext = componentContext.getBundleContext();
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext = null;

		_configurationProviders.clear();
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected synchronized void setConfigurationProvider(
		ConfigurationProvider<?> configurationProvider,
		Map<String, Object> properties) {

		String factoryPid = MapUtil.getString(properties, "factoryPid");

		if (Validator.isNull(factoryPid)) {
			throw new IllegalArgumentException(
				"No factoryPids specified for ConfigurationProvider " +
					configurationProvider);
		}

		_configurationProviders.put(factoryPid, configurationProvider);
	}

	protected synchronized void unsetConfigurationProvider(
		ConfigurationProvider<?> configurationProvider,
		Map<String, Object> properties) {

		String factoryPid = MapUtil.getString(properties, "factoryPid");

		_configurationProviders.remove(factoryPid);
	}

	private BundleContext _bundleContext;
	private final Map<String, ConfigurationProvider<?>>
		_configurationProviders = new HashMap<>();

}