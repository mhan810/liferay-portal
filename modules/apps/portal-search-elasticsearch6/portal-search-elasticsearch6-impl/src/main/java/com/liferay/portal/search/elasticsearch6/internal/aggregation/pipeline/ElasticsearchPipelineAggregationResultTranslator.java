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
import com.liferay.portal.search.aggregation.metrics.ExtendedStatsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.PercentilesAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.AvgBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.AvgBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.BucketScriptPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.BucketScriptPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.CumulativeSumPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.CumulativeSumPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.DerivativePipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MaxBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MaxBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.MinBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MinBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.MovingFunctionPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.MovingFunctionPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
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

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	property = "search.engine.impl=Elasticsearch",
	service = PipelineAggregationResultTranslator.class
)
public class ElasticsearchPipelineAggregationResultTranslator
	implements PipelineAggregationResultTranslator
		<AggregationResult, Aggregation> {

	@Override
	public AggregationResult translate(
		AvgBucketPipelineAggregation avgAggregation, Aggregation aggregation) {

		BucketMetricValue bucketMetricValue = (BucketMetricValue)aggregation;

		AvgBucketPipelineAggregationResult avgBucketPipelineAggregationResult =
			new AvgBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		avgBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return avgBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		BucketScriptPipelineAggregation bucketScriptPipelineAggregation,
		Aggregation aggregationResult) {

		SimpleValue simpleValue = (SimpleValue)aggregationResult;

		return new BucketScriptPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		CumulativeSumPipelineAggregation cumulativeSumPipelineAggregation,
		Aggregation aggregationResult) {

		SimpleValue simpleValue = (SimpleValue)aggregationResult;

		return new CumulativeSumPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		DerivativePipelineAggregation derivativePipelineAggregation,
		Aggregation aggregationResult) {

		Derivative derivative = (Derivative)aggregationResult;

		return new DerivativePipelineAggregationResult(
			derivative.getName(), derivative.normalizedValue());
	}

	@Override
	public AggregationResult translate(
		ExtendedStatsBucketPipelineAggregation
			extendedStatsBucketPipelineAggregation,
		Aggregation aggregationResult) {

		ExtendedStatsBucket extendedStatsBucket =
			(ExtendedStatsBucket)aggregationResult;

		return new ExtendedStatsAggregationResult(
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
		Aggregation aggregationResult) {

		BucketMetricValue bucketMetricValue =
			(BucketMetricValue)aggregationResult;

		MaxBucketPipelineAggregationResult maxBucketPipelineAggregationResult =
			new MaxBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		maxBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return maxBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		MinBucketPipelineAggregation minBucketPipelineAggregation,
		Aggregation aggregationResult) {

		BucketMetricValue bucketMetricValue =
			(BucketMetricValue)aggregationResult;

		MinBucketPipelineAggregationResult minBucketPipelineAggregationResult =
			new MinBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		minBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return minBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		MovingFunctionPipelineAggregation movingFunctionPipelineAggregation,
		Aggregation aggregationResult) {

		SimpleValue simpleValue = (SimpleValue)aggregationResult;

		return new MovingFunctionPipelineAggregationResult(
			simpleValue.getName(), simpleValue.value());
	}

	@Override
	public AggregationResult translate(
		PercentilesBucketPipelineAggregation
			percentilesBucketPipelineAggregation,
		Aggregation aggregationResult) {

		PercentilesBucket percentilesBucket =
			(PercentilesBucket)aggregationResult;

		PercentilesAggregationResult percentilesAggregationResult =
			new PercentilesAggregationResult(percentilesBucket.getName());

		double[] percents = percentilesBucketPipelineAggregation.getPercents();

		for (double percent : percents) {
			double percentile = percentilesBucket.percentile(percent);

			percentilesAggregationResult.addPercentile(percent, percentile);
		}

		return percentilesAggregationResult;
	}

	@Override
	public AggregationResult translate(
		StatsBucketPipelineAggregation statsBucketPipelineAggregation,
		Aggregation aggregationResult) {

		StatsBucket statsBucket = (StatsBucket)aggregationResult;

		return new StatsBucketPipelineAggregationResult(
			statsBucket.getName(), statsBucket.getAvg(), statsBucket.getCount(),
			statsBucket.getMin(), statsBucket.getMax(), statsBucket.getSum());
	}

	@Override
	public AggregationResult translate(
		SumBucketPipelineAggregation sumBucketPipelineAggregation,
		Aggregation aggregationResult) {

		BucketMetricValue bucketMetricValue =
			(BucketMetricValue)aggregationResult;

		SumBucketPipelineAggregationResult sumBucketPipelineAggregationResult =
			new SumBucketPipelineAggregationResult(
				bucketMetricValue.getName(), bucketMetricValue.value());

		sumBucketPipelineAggregationResult.setKeys(bucketMetricValue.keys());

		return sumBucketPipelineAggregationResult;
	}

}