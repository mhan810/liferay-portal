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

/**
 * @author Michael C. Han
 */
@ProviderType
public class SignificantTextsAggregationResult
	extends BaseBucketAggregationResult implements AggregationResult {

	public SignificantTextsAggregationResult(
		String name, long errorDocCounts, long otherDocCounts) {

		super(name);

		_errorDocCounts = errorDocCounts;
		_otherDocCounts = otherDocCounts;
	}

	public long getErrorDocCounts() {
		return _errorDocCounts;
	}

	public long getOtherDocCounts() {
		return _otherDocCounts;
	}

	private final long _errorDocCounts;
	private final long _otherDocCounts;

}