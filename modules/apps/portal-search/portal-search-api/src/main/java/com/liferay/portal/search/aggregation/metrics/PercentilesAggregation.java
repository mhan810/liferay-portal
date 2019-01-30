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
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseFieldAggregation;

/**
 * @author Michael C. Han
 */
@ProviderType
public class PercentilesAggregation extends BaseFieldAggregation {

	public PercentilesAggregation(String name, String field) {
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

	public Integer getCompression() {
		return _compression;
	}

	public Integer getHdrSignificantValueDigits() {
		return _hdrSignificantValueDigits;
	}

	public Boolean getKeyed() {
		return _keyed;
	}

	public PercentilesMethod getPercentilesMethod() {
		return _percentilesMethod;
	}

	public double[] getPercents() {
		return _percents;
	}

	public void setCompression(Integer compression) {
		_compression = compression;
	}

	public void setHdrSignificantValueDigits(
		Integer hdrSignificantValueDigits) {

		_hdrSignificantValueDigits = hdrSignificantValueDigits;
	}

	public void setKeyed(Boolean keyed) {
		_keyed = keyed;
	}

	public void setPercentilesMethod(PercentilesMethod percentilesMethod) {
		_percentilesMethod = percentilesMethod;
	}

	public void setPercents(double... percents) {
		_percents = percents;
	}

	private Integer _compression;
	private Integer _hdrSignificantValueDigits;
	private Boolean _keyed;
	private PercentilesMethod _percentilesMethod;
	private double[] _percents;

}