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

package com.liferay.portal.search.aggregation.bucket;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.BaseAggregationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class BaseHierarchicalAggregationResult extends BaseAggregationResult {

	public BaseHierarchicalAggregationResult(String name) {
		super(name);
	}

	public void addChildAggregationResult(AggregationResult aggregationResult) {
		_childrenAggregationResults.put(
			aggregationResult.getName(), aggregationResult);
	}

	public void addChildAggregationResults(
		List<AggregationResult> aggregationResults) {

		aggregationResults.forEach(
			aggregationResult ->
				_childrenAggregationResults.put(
					aggregationResult.getName(), aggregationResult));
	}

	public Map<String, AggregationResult> getChildrenAggregationResults() {
		return Collections.unmodifiableMap(_childrenAggregationResults);
	}

	private Map<String, AggregationResult> _childrenAggregationResults =
		new HashMap<>();

}