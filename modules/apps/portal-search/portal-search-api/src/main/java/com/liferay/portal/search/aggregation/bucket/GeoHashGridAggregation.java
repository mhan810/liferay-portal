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
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseFieldAggregation;

/**
 * @author Michael C. Han
 */
public class GeoHashGridAggregation extends BaseFieldAggregation {

	public GeoHashGridAggregation(String name, String field) {
		super(name, field);
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		AggregationResultTranslator<S, T> aggregationResultTranslator,
		T aggregationResult) {

		return aggregationResultTranslator.translate(this, aggregationResult);
	}

	@Override
	public <T> T accept(AggregationVisitor<T> aggregationVisitor) {
		return aggregationVisitor.visit(this);
	}

	public Integer getPrecision() {
		return _precision;
	}

	public Integer getShardSize() {
		return _shardSize;
	}

	public Integer getSize() {
		return _size;
	}

	public void setPrecision(Integer precision) {
		_precision = precision;
	}

	public void setShardSize(Integer shardSize) {
		_shardSize = shardSize;
	}

	public void setSize(Integer size) {
		_size = size;
	}

	private Integer _precision;
	private Integer _shardSize;
	private Integer _size;

}