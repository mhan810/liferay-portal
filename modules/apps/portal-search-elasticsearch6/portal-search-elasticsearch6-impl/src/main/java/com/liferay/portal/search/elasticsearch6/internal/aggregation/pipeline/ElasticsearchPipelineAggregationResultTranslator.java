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
import com.liferay.portal.search.aggregation.pipeline.AvgBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.AvgBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.BucketScriptPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.BucketScriptPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.CumulativeSumPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.CumulativeSumPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.MaxBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MaxBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.MinBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MinBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.MovingFunctionPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MovingFunctionPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.aggregation.pipeline.SerialDiffPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.SerialDiffPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.StatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.StatsBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.SumBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.SumBucketPipelineAggregationResult;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.pipeline.SimpleValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.BucketMetricValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.percentile.PercentilesBucket;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.StatsBucket;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.extended.ExtendedStatsBucket;
import org.elasticsearch.search.aggregations.pipeline.derivative.Derivative;

/**
 * @author Michael C. Han
 */
public class ElasticsearchPipelineAggregationResultTranslator
	implements PipelineAggregationResultTranslator
		<AggregationResult, Aggregation> {

	@Override
	public AggregationResult translate(
		AvgBucketPipelineAggregation avgAggregation, Aggregation aggregation) {

		SimpleValue simpleValue = (SimpleValue)aggregation;

		AvgBucketPipelineAggregationResult avgBucketPipelineAggregationResult =
			new AvgBucketPipelineAggregationResult(
				simpleValue.getName(), simpleValue.value());

		return avgBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		BucketScriptPipelineAggregation bucketScriptPipelineAggregation,
		Aggregation aggregation) {

		SimpleValue simpleValue = (SimpleValue)aggregation;

		return new BucketScriptPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		CumulativeSumPipelineAggregation cumulativeSumPipelineAggregation,
		Aggregation aggregation) {

		SimpleValue simpleValue = (SimpleValue)aggregation;

		return new CumulativeSumPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		DerivativePipelineAggregation derivativePipelineAggregation,
		Aggregation aggregation) {

		Derivative derivative = (Derivative)aggregation;

		return new DerivativePipelineAggregationResult(
			derivative.getName(), derivative.normalizedValue());
	}

	@Override
	public AggregationResult translate(
		ExtendedStatsBucketPipelineAggregation
			extendedStatsBucketPipelineAggregation,
		Aggregation aggregation) {

		ExtendedStatsBucket extendedStatsBucket =
			(ExtendedStatsBucket)aggregation;

		return new ExtendedStatsBucketPipelineAggregationResult(
			extendedStatsBucket.getName(), extendedStatsBucket.getAvg(),
			extendedStatsBucket.getCount(), extendedStatsBucket.getMin(),
			extendedStatsBucket.getMax(), extendedStatsBucket.getSum(),
			extendedStatsBucket.getSumOfSquares(),
			extendedStatsBucket.getVariance(),
			extendedStatsBucket.getStdDeviation());
	}

	@Override
	public AggregationResult translate(
		MaxBucketPipelineAggregation maxBucketPipelineAggregation,
		Aggregation aggregation) {

		BucketMetricValue bucketMetricValue = (BucketMetricValue)aggregation;

		MaxBucketPipelineAggregationResult maxBucketPipelineAggregationResult =
			new MaxBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		maxBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return maxBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		MinBucketPipelineAggregation minBucketPipelineAggregation,
		Aggregation aggregation) {

		BucketMetricValue bucketMetricValue = (BucketMetricValue)aggregation;

		MinBucketPipelineAggregationResult minBucketPipelineAggregationResult =
			new MinBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		minBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return minBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		MovingFunctionPipelineAggregation movingFunctionPipelineAggregation,
		Aggregation aggregation) {

		SimpleValue simpleValue = (SimpleValue)aggregation;

		return new MovingFunctionPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		PercentilesBucketPipelineAggregation
			percentilesBucketPipelineAggregation,
		Aggregation aggregation) {

		PercentilesBucket percentilesBucket = (PercentilesBucket)aggregation;

		PercentilesBucketPipelineAggregationResult
			percentilesBucketPipelineAggregationResult =
				new PercentilesBucketPipelineAggregationResult(
					percentilesBucket.getName());

		percentilesBucket.forEach(
			percentile ->
				percentilesBucketPipelineAggregationResult.addPercentile(
					percentile.getPercent(), percentile.getValue()));

		return percentilesBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		SerialDiffPipelineAggregation serialDiffPipelineAggregation,
		Aggregation aggregationResult) {

		SimpleValue simpleValue = (SimpleValue)aggregationResult;

		SerialDiffPipelineAggregationResult
			serialDiffPipelineAggregationResult =
				new SerialDiffPipelineAggregationResult(
					simpleValue.getName(), simpleValue.value());

		return serialDiffPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		StatsBucketPipelineAggregation statsBucketPipelineAggregation,
		Aggregation aggregation) {

		StatsBucket statsBucket = (StatsBucket)aggregation;

		return new StatsBucketPipelineAggregationResult(
			statsBucket.getName(), statsBucket.getAvg(), statsBucket.getCount(),
			statsBucket.getMin(), statsBucket.getMax(), statsBucket.getSum());
	}

	@Override
	public AggregationResult translate(
		SumBucketPipelineAggregation sumBucketPipelineAggregation,
		Aggregation aggregation) {

		SimpleValue simpleValue = (SimpleValue)aggregation;

		SumBucketPipelineAggregationResult sumBucketPipelineAggregationResult =
			new SumBucketPipelineAggregationResult(
				simpleValue.getName(), simpleValue.value());

		return sumBucketPipelineAggregationResult;
	}

}