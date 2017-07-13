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

package com.liferay.calendar.search.facet.asset;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.search.facet.asset.AssetEntriesFacetBooleanFilterBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"entry.class.name=com.liferay.calendar.model.CalendarBooking"},
	service = AssetEntriesFacetBooleanFilterBuilder.class
)
public class AssetEntriesFacetBooleanFilterBuilderImpl
	implements AssetEntriesFacetBooleanFilterBuilder {

	@Override
	public BooleanFilter getFacetBooleanFilter(
		String entryClassName, SearchContext searchContext) {

		BooleanFilter facetBooleanFilter = new BooleanFilter();

		facetBooleanFilter.addTerm(
			Field.ENTRY_CLASS_NAME, CalendarBooking.class.getName());

		if (searchContext.getUserId() > 0) {
			facetBooleanFilter =
				searchPermissionChecker.getPermissionBooleanFilter(
					searchContext.getCompanyId(), searchContext.getGroupIds(),
					searchContext.getUserId(), Calendar.class.getName(),
					facetBooleanFilter, searchContext);
		}

		return facetBooleanFilter;
	}

	@Reference
	protected SearchPermissionChecker searchPermissionChecker;

}