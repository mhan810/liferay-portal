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

package com.liferay.portal.search.elasticsearch6.internal.aggregation;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.CustomAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;

import org.elasticsearch.search.aggregations.Aggregation;

/**
 * @author Michael C. Han
 */
public interface CustomAggregationResultTranslator {

	public AggregationResult translate(
		CustomAggregation customAggregation, Aggregation aggregation,
		AggregationResultTranslator<AggregationResult, Aggregation>
			aggregationResultTranslator,
		PipelineAggregationResultTranslator
			pipelineAggregationResultTranslator);

}