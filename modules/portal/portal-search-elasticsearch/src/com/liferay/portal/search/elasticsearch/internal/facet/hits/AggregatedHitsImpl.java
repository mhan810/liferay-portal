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

package com.liferay.portal.search.elasticsearch.internal.facet.hits;

import com.liferay.portal.kernel.search.HitsImpl;
import com.liferay.portal.search.elasticsearch.facet.hits.AggregatedHit;
import com.liferay.portal.search.elasticsearch.facet.hits.DateRangeHitTransformer;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange;
public class AggregatedHitsImpl extends HitsImpl {

	public void addAggregation(InternalDateRange dateRange) {
		DateRangeAggregatedHit dateRangeAggregatedHit =
			_dateRangeHitTransformer.transform(dateRange);

		_aggregatedHits.add(dateRangeAggregatedHit);
	}

	public List<AggregatedHit> getAggregatedHits() {
		return _aggregatedHits;
	}

	private final List<AggregatedHit> _aggregatedHits = new ArrayList<>();
	private final DateRangeHitTransformer _dateRangeHitTransformer =
		new DateRangeHitTransformerImpl();

}