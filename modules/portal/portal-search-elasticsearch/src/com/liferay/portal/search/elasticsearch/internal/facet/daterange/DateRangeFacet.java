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

package com.liferay.portal.search.elasticsearch.internal.facet.daterange;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.BaseFacet;
import com.liferay.portal.kernel.search.filter.Filter;
public class DateRangeFacet extends BaseFacet {

	public DateRangeFacet(SearchContext searchContext) {
		super(searchContext);
	}

	public Object getFrom() {
		return _from;
	}

	public Object getTo() {
		return _to;
	}

	public void setFrom(Object from) {
		_from = from;
	}

	public void setTo(Object to) {
		_to = to;
	}

	@Override
	protected BooleanClause<Filter> doGetFacetFilterBooleanClause() {

		// TODO Auto-generated method stub

		return null;
	}

	private Object _from;
	private Object _to;

}