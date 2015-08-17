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

package com.liferay.portal.kernel.search.facet.daterange;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.BaseFacet;
import com.liferay.portal.kernel.search.filter.Filter;

import java.util.ArrayList;
import java.util.List;
public class DateRangeFacet extends BaseFacet {

	public DateRangeFacet(SearchContext searchContext) {
		super(searchContext);
	}

	public void addRange(Object from, Object to) {
		getRanges().add(new BoundedRange(from, to));
	}

	public void addUnboundedFrom(Object from) {
		getRanges().add(new UnboundedFrom(from));
	}

	public void addUnboundedTo(Object to) {
		getRanges().add(new UnboundedTo(to));
	}

	public List<Range> getRanges() {
		return _ranges;
	}

	@Override
	protected BooleanClause<Filter> doGetFacetFilterBooleanClause() {
		return null;
	}

	private final List<Range> _ranges = new ArrayList<>();

}