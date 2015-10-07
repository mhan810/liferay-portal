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
import com.liferay.portal.ldap.configuration.LDAPCompanyConfiguration;
import com.liferay.portal.ldap.configuration.LDAPCompanyConfigurationProvider;

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
public class LDAPCompanyConfigurationProviderImpl
	implements LDAPCompanyConfigurationProvider {

	@Override
	public LDAPCompanyConfiguration getLDAPCompanyConfiguration(
		long companyId) {

		LDAPCompanyConfiguration ldapCompanyConfiguration =
			_ldapCompanyConfigurations.getService(companyId);

		if (ldapCompanyConfiguration == null) {
			ldapCompanyConfiguration = _ldapCompanyConfigurations.getService(
				0L);
		}

		if (ldapCompanyConfiguration == null) {
			ldapCompanyConfiguration = _defaultLDAPCompanyConfiguration;
		}

		return ldapCompanyConfiguration;
	}

	@Activate
	protected void activate(BundleContext bundleContext)
		throws InvalidSyntaxException {

		_ldapCompanyConfigurations = ServiceTrackerMapFactory.singleValueMap(
			bundleContext, LDAPCompanyConfiguration.class, null,
			new ServiceReferenceMapper<Long, LDAPCompanyConfiguration>() {

				@Override
				public void map(
					ServiceReference<LDAPCompanyConfiguration> serviceReference,
					Emitter<Long> emitter) {

					Long companyId = (Long)serviceReference.getProperty(
						"companyId");

					if (companyId == null) {
						emitter.emit(0L);
					}
					else {
						emitter.emit(companyId);
					}
				}
			});

		_ldapCompanyConfigurations.open();
	}

	@Deactivate
	protected void deactivate() {
		_ldapCompanyConfigurations.close();
		_ldapCompanyConfigurations = null;
	}

	private final LDAPCompanyConfiguration _defaultLDAPCompanyConfiguration =
		Configurable.createConfigurable(
			LDAPCompanyConfiguration.class, Collections.emptyMap());
	private ServiceTrackerMap<Long, LDAPCompanyConfiguration>
		_ldapCompanyConfigurations;

}