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

import com.liferay.portal.ldap.configuration.LDAPServerConfiguration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Iván Zaera
 */
@Component(
	configurationPid = "com.liferay.portal.ldap.cfgadm.LDAPServerConfiguration",
	immediate = true
)
public class LDAPServerConfigurationHolder implements LDAPServerConfiguration {

	@Override
	public String authSearchFilter() {
		return _ldapServerConfiguration.authSearchFilter();
	}

	@Override
	public String baseDn() {
		return _ldapServerConfiguration.baseDn();
	}

	@Override
	public String baseProviderUrl() {
		return _ldapServerConfiguration.baseProviderUrl();
	}

	@Override
	public long companyId() {
		return _ldapServerConfiguration.companyId();
	}

	@Override
	public String contactCustomMappings() {
		return _ldapServerConfiguration.contactCustomMappings();
	}

	@Override
	public String contactMappings() {
		return _ldapServerConfiguration.contactMappings();
	}

	@Override
	public String[] groupDefaultObjectClasses() {
		return _ldapServerConfiguration.groupDefaultObjectClasses();
	}

	@Override
	public String groupMappings() {
		return _ldapServerConfiguration.groupMappings();
	}

	@Override
	public String groupsDn() {
		return _ldapServerConfiguration.groupsDn();
	}

	@Override
	public String importGroupSearchFilter() {
		return _ldapServerConfiguration.importGroupSearchFilter();
	}

	@Override
	public String importUserSearchFilter() {
		return _ldapServerConfiguration.importUserSearchFilter();
	}

	@Override
	public String securityCredentials() {
		return _ldapServerConfiguration.securityCredentials();
	}

	@Override
	public String securityPrincipal() {
		return _ldapServerConfiguration.securityPrincipal();
	}

	@Override
	public long serverId() {
		return _ldapServerConfiguration.serverId();
	}

	@Override
	public String serverName() {
		return _ldapServerConfiguration.serverName();
	}

	@Override
	public String userCustomMappings() {
		return _ldapServerConfiguration.userCustomMappings();
	}

	@Override
	public String[] userDefaultObjectClasses() {
		return _ldapServerConfiguration.userDefaultObjectClasses();
	}

	@Override
	public String userMappings() {
		return _ldapServerConfiguration.userMappings();
	}

	@Override
	public String usersDn() {
		return _ldapServerConfiguration.usersDn();
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_ldapServerConfiguration = Configurable.createConfigurable(
			LDAPServerConfiguration.class, properties);
	}

	private volatile LDAPServerConfiguration _ldapServerConfiguration;

}