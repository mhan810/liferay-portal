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
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseFieldAggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DateHistogramAggregation extends BaseFieldAggregation {

	public DateHistogramAggregation(String name, String field) {
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

	public void addOrders(Order... orders) {
		Collections.addAll(_orders, orders);
	}

	public String getDateHistogramInterval() {
		return _dateHistogramInterval;
	}

	public Long getInterval() {
		return _interval;
	}

	public Boolean getKeyed() {
		return _keyed;
	}

	public Long getMaxBound() {
		return _maxBound;
	}

	public Long getMinBound() {
		return _minBound;
	}

	public Long getMinDocCount() {
		return _minDocCount;
	}

	public Long getOffset() {
		return _offset;
	}

	public List<Order> getOrders() {
		return Collections.unmodifiableList(_orders);
	}

	public void setBounds(Long minBound, Long maxBound) {
		_minBound = minBound;
		_maxBound = maxBound;
	}

	public void setDateHistogramInterval(String dateHistogramInterval) {
		_dateHistogramInterval = dateHistogramInterval;
	}

	public void setInterval(Long interval) {
		_interval = interval;
	}

	public void setKeyed(Boolean keyed) {
		_keyed = keyed;
	}

	public void setMinDocCount(Long minDocCount) {
		_minDocCount = minDocCount;
	}

	public void setOffset(Long offset) {
		_offset = offset;
	}

	private String _dateHistogramInterval;
	private Long _interval;
	private Boolean _keyed;
	private Long _maxBound;
	private Long _minBound;
	private Long _minDocCount;
	private Long _offset;
	private List<Order> _orders = new ArrayList<>();

}