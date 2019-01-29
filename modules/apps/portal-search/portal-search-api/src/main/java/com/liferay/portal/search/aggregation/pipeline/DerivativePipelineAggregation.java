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

import com.liferay.portal.search.aggregation.AggregationResult;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DerivativePipelineAggregation
	extends BasePipelineAggregation implements PipelineAggregation {

	public DerivativePipelineAggregation(String name, String bucketsPath) {
		super(name);

		_bucketsPath = bucketsPath;
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		PipelineAggregationResultTranslator<S, T>
			pipelineAggregationResultTranslator,
		T aggregationResult) {

		return pipelineAggregationResultTranslator.translate(
			this, aggregationResult);
	}

	@Override
	public <T> T accept(
		PipelineAggregationVisitor<T> pipelineAggregationVisitor) {

		return pipelineAggregationVisitor.visit(this);
	}

	public String getBucketsPath() {
		return _bucketsPath;
	}

	public String getFormat() {
		return _format;
	}

	public GapPolicy getGapPolicy() {
		return _gapPolicy;
	}

	public String getUnit() {
		return _unit;
	}

	public void setFormat(String format) {
		_format = format;
	}

	public void setGapPolicy(GapPolicy gapPolicy) {
		_gapPolicy = gapPolicy;
	}

	public void setUnit(String unit) {
		_unit = unit;
	}

	private final String _bucketsPath;
	private String _format;
	private GapPolicy _gapPolicy;
	private String _unit;

}