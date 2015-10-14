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
	id = "com.liferay.portal.ldap.configuration.SystemLDAPConfiguration",
	localization = "content/Language"
)
public interface SystemLDAPConfiguration {

	@Meta.AD(
		deflt =
			"com.sun.jndi.ldap.connect.pool=true|com.sun.jndi.ldap.connect.timeout=500|com.sun.jndi.ldap.read.timeout=15000",
		required = false
	)
	public String[] connectionProperties();

	@Meta.AD(deflt = "com.sun.jndi.ldap.LdapCtxFactory", required = false)
	public String factoryInitial();

	@Meta.AD(deflt = "1000", required = false)
	public int pageSize();

	@Meta.AD(deflt = "1000", required = false)
	public int rangeSize();

	@Meta.AD(
		deflt = "follow", optionValues = {"follow", "ignore", "throws"},
		required = false
	)
	public String referral();

}