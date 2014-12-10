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

package com.liferay.portal.security.ldap;

import com.liferay.portal.model.User;
import com.liferay.portal.security.exportimport.UserImporterUtil;

import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

/**
 * Use UserImporterUtil or LDAPUserImporterUtil instead
 *
 * @deprecated
 * @author Edward Han
 */
@Deprecated
public class PortalLDAPImporterUtil {

	public static void importFromLDAP() throws Exception {
		UserImporterUtil.importUsers();
	}

	public static void importFromLDAP(long companyId) throws Exception {
		UserImporterUtil.importUsers(companyId);
	}

	public static void importFromLDAP(long ldapServerId, long companyId)
		throws Exception {

		UserImporterUtil.importUsers(ldapServerId, companyId);
	}

	public static User importLDAPUser(
			long ldapServerId, long companyId, LdapContext ldapContext,
			Attributes attributes, String password)
		throws Exception {

		return LDAPUserImporterUtil.importUser(
			ldapServerId, companyId, ldapContext, attributes, password);
	}

	public static User importLDAPUser(
			long ldapServerId, long companyId, String emailAddress,
			String screenName)
		throws Exception {

		return UserImporterUtil.importUser(
			ldapServerId, companyId, emailAddress, screenName);
	}

	public static User importLDAPUser(
			long companyId, String emailAddress, String screenName)
		throws Exception {

		return UserImporterUtil.importUser(
			companyId, emailAddress, screenName);
	}

	public static User importLDAPUserByScreenName(
			long companyId, String screenName)
		throws Exception {

		return UserImporterUtil.importUserByScreenName(
			companyId, screenName);
	}

}