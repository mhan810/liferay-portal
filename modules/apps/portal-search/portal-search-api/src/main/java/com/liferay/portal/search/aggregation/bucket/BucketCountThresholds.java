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

/**
 * @author Michael C. Han
 */
@ProviderType
public class BucketCountThresholds {

	public BucketCountThresholds(
		long minDocCount, long shardMinDocCount, int requiredSize,
		int shardSize) {

		_minDocCount = minDocCount;
		_shardMinDocCount = shardMinDocCount;
		_requiredSize = requiredSize;
		_shardSize = shardSize;
	}

	public long getMinDocCount() {
		return _minDocCount;
	}

	public int getRequiredSize() {
		return _requiredSize;
	}

	public long getShardMinDocCount() {
		return _shardMinDocCount;
	}

	public int getShardSize() {
		return _shardSize;
	}

	private final long _minDocCount;
	private final int _requiredSize;
	private final long _shardMinDocCount;
	private final int _shardSize;

}