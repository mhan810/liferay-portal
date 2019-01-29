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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@ProviderType
public class PercentileRanksAggregationResult
	extends BaseAggregationResult implements AggregationResult {

	public PercentileRanksAggregationResult(String name) {
		super(name);
	}

	public void addPercentile(double value, double percentile) {
		_percentiles.put(value, percentile);
	}

	public Map<Double, Double> getPercentiles() {
		return Collections.unmodifiableMap(_percentiles);
	}

	private Map<Double, Double> _percentiles = new HashMap<>();

}