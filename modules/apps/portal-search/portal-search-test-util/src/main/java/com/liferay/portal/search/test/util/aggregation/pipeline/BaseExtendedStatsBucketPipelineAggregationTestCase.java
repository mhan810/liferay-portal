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

package com.liferay.portal.search.test.util.aggregation.pipeline;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.ExtendedStatsBucketPipelineAggregationResult;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BaseExtendedStatsBucketPipelineAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testStatsBucketPipeline() throws Exception {
		for (int i = 1; i <= 20; i++) {
			addDocument(
				DocumentCreationHelpers.singleNumber(Field.PRIORITY, i));
		}

		HistogramAggregation histogramAggregation = new HistogramAggregation(
			"histogram", Field.PRIORITY);

		histogramAggregation.setMinDocCount(1L);
		histogramAggregation.setInterval(5.0);

		SumAggregation sumAggregation = new SumAggregation(
			"sum", Field.PRIORITY);

		histogramAggregation.addChildAggregation(sumAggregation);

		ExtendedStatsBucketPipelineAggregation
			extendedStatsBucketPipelineAggregation =
				new ExtendedStatsBucketPipelineAggregation(
					"extended_stats_bucket", "histogram>sum");

		assertSearch(
			indexingTestHelper -> {
				Map<String, AggregationResult> aggregationResults = search(
					histogramAggregation,
					extendedStatsBucketPipelineAggregation);

				ExtendedStatsBucketPipelineAggregationResult
					extendedStatsBucketPipelineAggregationResult =
						(ExtendedStatsBucketPipelineAggregationResult)
							aggregationResults.get("extended_stats_bucket");

				Assert.assertNotNull(
					extendedStatsBucketPipelineAggregationResult);

				Assert.assertEquals(
					"Avg summged in buckets", 42,
					extendedStatsBucketPipelineAggregationResult.getAvg(), 0);
				Assert.assertEquals(
					"Total count in buckets", 5,
					extendedStatsBucketPipelineAggregationResult.getCount(), 0);
				Assert.assertEquals(
					"Max summed priority in buckets", 85,
					extendedStatsBucketPipelineAggregationResult.getMax(), 0);
				Assert.assertEquals(
					"Min summed priority in buckets", 10,
					extendedStatsBucketPipelineAggregationResult.getMin(), 0);
				Assert.assertEquals(
					"Summed priority in buckets", 210,
					extendedStatsBucketPipelineAggregationResult.getSum(), 0);
				Assert.assertEquals(
					"Std deviation of summed priority in buckets",
					27.313000567495326,
					extendedStatsBucketPipelineAggregationResult.
						getStdDeviation(),
					0);
				Assert.assertEquals(
					"Sum of squares of summed priority in buckets", 12550,
					extendedStatsBucketPipelineAggregationResult.
						getSumOfSquares(),
					0);
				Assert.assertEquals(
					"Variance of summed priority in buckets", 746,
					extendedStatsBucketPipelineAggregationResult.getVariance(),
					0);
			});
	}

}