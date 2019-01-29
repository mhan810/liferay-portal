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
public class RangeAggregation extends BaseFieldAggregation {

	public RangeAggregation(String name, String field) {
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

	public void addRange(Range range) {
		_ranges.add(range);
	}

	public void addRanges(Range... ranges) {
		Collections.addAll(_ranges, ranges);
	}

	public void addUnboundedFrom(Double from) {
		addRange(new Range(from, null));
	}

	public void addUnboundedFrom(String key, Double from) {
		addRange(new Range(key, from, null));
	}

	public void addUnboundedTo(String key, Double to) {
		addRange(new Range(key, null, to));
	}

	public String getFormat() {
		return _format;
	}

	public Boolean getKeyed() {
		return _keyed;
	}

	public List<Range> getRanges() {
		return Collections.unmodifiableList(_ranges);
	}

	public void setFormat(String format) {
		_format = format;
	}

	public void setKeyed(Boolean keyed) {
		_keyed = keyed;
	}

	public void unboundedTo(Double to) {
		addRange(new Range(null, to));
	}

	private String _format;
	private Boolean _keyed;
	private List<Range> _ranges = new ArrayList<>();

}