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

import com.liferay.portal.kernel.search.facet.daterange.DateRangeBuilderWrapper;

import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class DateRangeBuilderWrapperImpl extends DateRangeBuilder
	implements DateRangeBuilderWrapper {

	public DateRangeBuilderWrapperImpl(String name) {
		super(name);
	}

	@Override
	public void addFrom(Object from) {
		addUnboundedFrom(from);
	}

	@Override
	public void addInterval(Object from, Object to) {
		addRange(from, to);
	}

	@Override
	public void addTo(Object to) {
		addUnboundedTo(to);
	}

}