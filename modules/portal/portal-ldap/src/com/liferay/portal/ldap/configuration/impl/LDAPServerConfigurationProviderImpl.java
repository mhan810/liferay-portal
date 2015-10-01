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

package com.liferay.portal.ldap.configuration.impl;

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.osgi.service.tracker.map.ServiceReferenceMapper;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.ldap.configuration.LDAPServerConfigurationProvider;

import java.util.Collections;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Iván Zaera
 */
@Component
public class LDAPServerConfigurationProviderImpl
	implements LDAPServerConfigurationProvider {

	@Override
	public LDAPServerConfiguration getLDAPServerConfiguration(
		long ldapServerId) {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurations.getService(ldapServerId);

		if (ldapServerConfiguration == null) {
			ldapServerConfiguration = _defaultLDAPServerConfiguration;
		}

		return ldapServerConfiguration;
	}

	@Activate
	protected void activate(BundleContext bundleContext)
		throws InvalidSyntaxException {

		_ldapServerConfigurations = ServiceTrackerMapFactory.singleValueMap(
			bundleContext, LDAPServerConfiguration.class, null,
			new ServiceReferenceMapper<Long, LDAPServerConfiguration>() {

				@Override
				public void map(
					ServiceReference<LDAPServerConfiguration> serviceReference,
					Emitter<Long> emitter) {

					Long ldapServerId = (Long)serviceReference.getProperty(
						"ldapServerId");

					if (ldapServerId == null) {
						emitter.emit(0L);
					}
					else {
						emitter.emit(ldapServerId);
					}
				}
			}
		);

		_ldapServerConfigurations.open();
	}

	@Deactivate
	protected void deactivate() {
		_ldapServerConfigurations.close();
		_ldapServerConfigurations = null;
	}

	private final LDAPServerConfiguration _defaultLDAPServerConfiguration =
		Configurable.createConfigurable(
			LDAPServerConfiguration.class, Collections.emptyMap());
	private ServiceTrackerMap<Long, LDAPServerConfiguration>
		_ldapServerConfigurations;

}