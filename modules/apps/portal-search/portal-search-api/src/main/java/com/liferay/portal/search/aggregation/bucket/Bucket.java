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

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.AggregationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@ProviderType
public class Bucket {

	public Bucket(String key, long docCount) {
		_key = key;
		_docCount = docCount;
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

	public long getDocCount() {
		return _docCount;
	}

	public String getKey() {
		return _key;
	}

	@Override
	public String toString() {

		// Purposely same string representation as java.util.Map.Entry

		return _key + "=" + _docCount;
	}

	private Map<String, AggregationResult> _childrenAggregationResults =
		new HashMap<>();
	private final long _docCount;
	private final String _key;

}