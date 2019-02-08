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

package com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.pipeline.CustomPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;

import org.elasticsearch.search.aggregations.Aggregation;

/**
 * @author Michael C. Han
 */
public interface CustomPipelineAggregationResultTranslator {

	public AggregationResult translate(
		CustomPipelineAggregation customPipelineAggregation,
		Aggregation aggregationResult,
		PipelineAggregationResultTranslator<AggregationResult, Aggregation>
			pipelineAggregationResultTranslator);

}