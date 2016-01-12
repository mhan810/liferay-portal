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

package com.liferay.portal.ldap.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.exportimport.UserImporter;
import com.liferay.portal.security.ldap.LDAPSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true)
public class CompanyLDAPInitializer {

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY, unbind = "removeCompany"
	)
	protected void addCompany(Company company) {
		if (!_ldapSettings.isImportOnStartup(company.getCompanyId())) {
			return;
		}

		try {
			_userImporter.importUsers(company.getCompanyId());
		}
		catch (Exception e) {
			_log.error(
				"Unable to import users for company: " + company.getCompanyId(),
				e);
		}
	}

	protected void removeCompany(Company company) {
	}

	@Reference(unbind = "-")
	protected void setLdapSettings(LDAPSettings ldapSettings) {
		_ldapSettings = ldapSettings;
	}

	@Reference(unbind = "-")
	protected void setUserImporter(UserImporter userImporter) {
		_userImporter = userImporter;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CompanyLDAPInitializer.class);

	private volatile LDAPSettings _ldapSettings;
	private volatile UserImporter _userImporter;

}