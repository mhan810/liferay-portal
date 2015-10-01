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

package com.liferay.portal.ldap.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Michael C. Han
 */
@Meta.OCD(
	factory = true,
	id = "com.liferay.portal.ldap.cfgadm.LDAPServerConfiguration"
)
public interface LDAPServerConfiguration {

	@Meta.AD(deflt = "", required = false)
	public String authSearchFilter();

	@Meta.AD(deflt = "", required = false)
	public String baseDn();

	@Meta.AD(deflt = "", required = false)
	public String baseProviderUrl();

	@Meta.AD(deflt = "0", required = false)
	public long companyId();

	@Meta.AD(deflt = "", required = false)
	public String contactCustomMappings();

	@Meta.AD(deflt = "", required = false)
	public String contactMappings();

	@Meta.AD(deflt = "", required = false)
	public String[] groupDefaultObjectClasses();

	@Meta.AD(deflt = "", required = false)
	public String groupMappings();

	@Meta.AD(deflt = "", required = false)
	public String groupsDn();

	@Meta.AD(deflt = "", required = false)
	public String importGroupSearchFilter();

	@Meta.AD(deflt = "", required = false)
	public String importUserSearchFilter();

	@Meta.AD(deflt = "", required = false)
	public String securityCredentials();

	@Meta.AD(deflt = "", required = false)
	public String securityPrincipal();

	@Meta.AD(deflt = "0", required = false)
	public long serverId();

	@Meta.AD(deflt = "", required = false)
	public String serverName();

	@Meta.AD(deflt = "", required = false)
	public String userCustomMappings();

	@Meta.AD(deflt = "", required = false)
	public String[] userDefaultObjectClasses();

	@Meta.AD(deflt = "", required = false)
	public String userMappings();

	@Meta.AD(deflt = "", required = false)
	public String usersDn();

}