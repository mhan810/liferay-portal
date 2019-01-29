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

package com.liferay.portal.search.aggregation.pipeline;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.BaseAggregationResult;

/**
 * @author Michael C. Han
 */
@ProviderType
public class BaseBucketPipelineAggregationResult extends BaseAggregationResult {

	public BaseBucketPipelineAggregationResult(String name, double value) {
		super(name);

		_value = value;
	}

	public String[] getKeys() {
		return _keys;
	}

	public double getValue() {
		return _value;
	}

	public void setKeys(String[] keys) {
		_keys = keys;
	}

	private String[] _keys;
	private final double _value;

}