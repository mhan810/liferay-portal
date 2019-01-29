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

package com.liferay.portal.search.aggregation.metrics;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.BaseAggregationResult;
import com.liferay.portal.search.geolocation.GeoLocationPoint;

/**
 * @author Michael C. Han
 */
@ProviderType
public class GeoCentroidAggregationResult
	extends BaseAggregationResult implements AggregationResult {

	public GeoCentroidAggregationResult(
		String name, GeoLocationPoint centroid, long count) {

		super(name);

		_centroid = centroid;
		_count = count;
	}

	public GeoLocationPoint getCentroid() {
		return _centroid;
	}

	public long getCount() {
		return _count;
	}

	private final GeoLocationPoint _centroid;
	private final long _count;

}