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

package com.liferay.portal.search.test.util.aggregation.metrics;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregationResult;
import com.liferay.portal.search.aggregation.metrics.PercentilesMethod;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rafael Praxedes
 */
public abstract class BasePercentileRanksAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testPercentileRanksAggregationHDR() throws Exception {
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));

		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 20));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 30));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 40));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 50));

		double[] values = {10, 20, 30, 40, 50};

		PercentileRanksAggregation percentileRanksAggregation =
			new PercentileRanksAggregation(
				"percentileRanks", Field.PRIORITY, values);

		percentileRanksAggregation.setPercentilesMethod(PercentilesMethod.HDR);

		assertSearch(
			indexingTestHelper -> {
				PercentileRanksAggregationResult
					percentileRanksAggregationResult = search(
						percentileRanksAggregation);

				Map<Double, Double> percentilesMap =
					percentileRanksAggregationResult.getPercentiles();

				double[] percents = {50, 62.5, 75, 87.5, 100};

				for (int i = 0; i < values.length; i++) {
					Assert.assertEquals(
						percents[i], percentilesMap.get(values[i]), 0);
				}
			});
	}

	@Test
	public void testPercentileRanksAggregationTDigest() throws Exception {
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 10));

		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 20));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 30));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 40));
		addDocument(DocumentCreationHelpers.singleNumber(Field.PRIORITY, 50));

		double[] values = {10, 20, 30, 40, 50};

		PercentileRanksAggregation percentileRanksAggregation =
			new PercentileRanksAggregation(
				"percentileRanks", Field.PRIORITY, 10, 20, 30, 40, 50);

		percentileRanksAggregation.setPercentilesMethod(
			PercentilesMethod.TDIGEST);

		assertSearch(
			indexingTestHelper -> {
				PercentileRanksAggregationResult
					percentileRanksAggregationResult = search(
						percentileRanksAggregation);

				Map<Double, Double> percentilesMap =
					percentileRanksAggregationResult.getPercentiles();

				double[] percents = {37.5, 56.25, 68.75, 81.25, 100};

				for (int i = 0; i < values.length; i++) {
					Assert.assertEquals(
						percents[i], percentilesMap.get(values[i]), 0);
				}
			});
	}

}