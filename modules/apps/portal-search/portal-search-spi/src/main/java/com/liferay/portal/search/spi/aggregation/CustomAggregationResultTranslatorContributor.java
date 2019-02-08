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

package com.liferay.portal.search.spi.aggregation;

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;

/**
 * @author Michael C. Han
 */
public interface CustomAggregationResultTranslatorContributor
	<S extends AggregationResult, T> {

	public String getAggregationClassName();

	public AggregationResult translate(
		Aggregation customAggregation, T aggregation,
		AggregationResultTranslator<S, T> aggregationResultTranslator,
		PipelineAggregationResultTranslator
			pipelineAggregationResultTranslator);

}