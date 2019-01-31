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
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregationResult;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BasePercentilesPipelineAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testPercentiles() throws Exception {
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

		PercentilesBucketPipelineAggregation
			percentilesBucketPipelineAggregation =
				new PercentilesBucketPipelineAggregation(
					"percentiles", "histogram>sum");

		assertSearch(
			indexingTestHelper -> {
				Map<String, AggregationResult> aggregationResults = search(
					histogramAggregation, percentilesBucketPipelineAggregation);

				PercentilesBucketPipelineAggregationResult
					percentilesBucketPipelineAggregationResult =
						(PercentilesBucketPipelineAggregationResult)
							aggregationResults.get("percentiles");

				Assert.assertNotNull(
					percentilesBucketPipelineAggregationResult);

				Map<Double, Double> percentiles =
					percentilesBucketPipelineAggregationResult.getPercentiles();

				Assert.assertEquals(
					"Total number of buckets", 7, percentiles.size(), 0);
				Assert.assertEquals("1%", 10, percentiles.get(1.0), 0);
				Assert.assertEquals("5%", 10, percentiles.get(5.0), 0);
				Assert.assertEquals("25.%", 20, percentiles.get(25.0), 0);
				Assert.assertEquals("50%", 35, percentiles.get(50.0), 0);
				Assert.assertEquals("75%", 60, percentiles.get(75.0), 0);
				Assert.assertEquals("95%", 85, percentiles.get(95.0), 0);
				Assert.assertEquals("99%", 85, percentiles.get(99.0), 0);
			});
	}

	@Test
	public void testPercentilesWithSpecifiedPercents() throws Exception {
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

		PercentilesBucketPipelineAggregation
			percentilesBucketPipelineAggregation =
				new PercentilesBucketPipelineAggregation(
					"percentiles", "histogram>sum");

		percentilesBucketPipelineAggregation.setPercents(25.0, 50.0, 75.0);

		assertSearch(
			indexingTestHelper -> {
				Map<String, AggregationResult> aggregationResults = search(
					histogramAggregation, percentilesBucketPipelineAggregation);

				PercentilesBucketPipelineAggregationResult
					percentilesBucketPipelineAggregationResult =
						(PercentilesBucketPipelineAggregationResult)
							aggregationResults.get("percentiles");

				Assert.assertNotNull(
					percentilesBucketPipelineAggregationResult);

				Map<Double, Double> percentiles =
					percentilesBucketPipelineAggregationResult.getPercentiles();

				Assert.assertEquals(
					"Total number of buckets", 3, percentiles.size(), 0);
				Assert.assertEquals("25%", 20, percentiles.get(25.0), 0);
				Assert.assertEquals("50%", 35, percentiles.get(50.0), 0);
				Assert.assertEquals("75%", 60, percentiles.get(75.0), 0);
			});
	}

}