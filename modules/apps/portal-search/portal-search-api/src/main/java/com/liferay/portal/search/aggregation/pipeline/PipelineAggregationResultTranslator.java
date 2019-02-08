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
public interface PipelineAggregationResultTranslator
	<S extends AggregationResult, T> {

	public S translate(
		AvgBucketPipelineAggregation avgAggregation, T aggregationResult);

	public S translate(
		BucketScriptPipelineAggregation bucketScriptPipelineAggregation,
		T aggregationResult);

	public S translate(
		CumulativeSumPipelineAggregation cumulativeSumPipelineAggregation,
		T aggregationResult);

	public S translate(
		CustomPipelineAggregation customPipelineAggregation,
		T aggregationResult);

	public S translate(
		DerivativePipelineAggregation derivativePipelineAggregation,
		T aggregationResult);

	public S translate(
		ExtendedStatsBucketPipelineAggregation
			extendedStatsBucketPipelineAggregation,
		T aggregationResult);

	public S translate(
		MaxBucketPipelineAggregation maxBucketPipelineAggregation,
		T aggregationResult);

	public S translate(
		MinBucketPipelineAggregation minBucketPipelineAggregation,
		T aggregationResult);

	public S translate(
		MovingFunctionPipelineAggregation movingFunctionPipelineAggregation,
		T aggregationResult);

	public S translate(
		PercentilesBucketPipelineAggregation
			percentilesBucketPipelineAggregation,
		T aggregationResult);

	public S translate(
		SerialDiffPipelineAggregation serialDiffPipelineAggregation,
		T aggregationResult);

	public S translate(
		StatsBucketPipelineAggregation statsBucketPipelineAggregation,
		T aggregationResult);

	public S translate(
		SumBucketPipelineAggregation sumBucketPipelineAggregation,
		T aggregationResult);

}