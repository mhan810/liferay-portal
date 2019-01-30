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

import com.liferay.portal.search.aggregation.BaseAggregationResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@ProviderType
public class BaseBucketAggregationResult extends BaseAggregationResult {

	public BaseBucketAggregationResult(String name) {
		super(name);
	}

	public void addBucket(Bucket bucket) {
		_buckets.put(bucket.getKey(), bucket);
	}

	public Bucket getBucket(String key) {
		return _buckets.get(key);
	}

	public Collection<Bucket> getBuckets() {
		return Collections.unmodifiableCollection(_buckets.values());
	}

	private Map<String, Bucket> _buckets = new LinkedHashMap<>();

}