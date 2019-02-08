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

import com.liferay.portal.search.aggregation.AggregationResult;

/**
 * @author Michael C. Han
 */
public class CustomPipelineAggregation extends BasePipelineAggregation {

	public CustomPipelineAggregation(
		String name, PipelineAggregation pipelineAggregation) {

		super(name);

		_pipelineAggregation = pipelineAggregation;
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

	public PipelineAggregation getPipelineAggregation() {
		return _pipelineAggregation;
	}

	private final PipelineAggregation _pipelineAggregation;

}