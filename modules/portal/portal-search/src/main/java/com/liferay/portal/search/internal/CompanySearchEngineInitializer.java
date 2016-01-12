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

package com.liferay.portal.search.internal;

import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.model.Company;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true)
public class CompanySearchEngineInitializer {

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY, unbind = "removeCompany"
	)
	protected void addCompany(Company company) {
		_searchEngineHelper.initialize(company.getCompanyId());
	}

	protected void removeCompany(Company company) {
	}

	@Reference(unbind = "-")
	protected void setSearchEngineHelper(
		SearchEngineHelper searchEngineHelper) {

		_searchEngineHelper = searchEngineHelper;
	}

	private volatile SearchEngineHelper _searchEngineHelper;

}