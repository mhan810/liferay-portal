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

package com.liferay.portal.search.test.util.aggregation.bucket;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregationResult;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregationResult;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BaseFilterAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testFilter() throws Exception {
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 1);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 2);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 3);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 4);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 5);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 6);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 7);
			});

		Query termQuery = new TermQuery(Field.USER_NAME, "SomeUser1");

		FilterAggregation filterAggregation = new FilterAggregation(
			"filter", termQuery);

		SumAggregation sumAggregation = new SumAggregation(
			"sum", Field.PRIORITY);

		filterAggregation.addChildAggregation(sumAggregation);

		assertSearch(
			indexingTestHelper -> {
				FilterAggregationResult filterAggregationResult = search(
					filterAggregation);

				Assert.assertEquals(
					"Filtered aggregation results", 3,
					filterAggregationResult.getDocCount());

				Map<String, AggregationResult> childrenAggregationResults =
					filterAggregationResult.getChildrenAggregationResults();

				Assert.assertEquals(
					"Number of children aggregation results", 1,
					childrenAggregationResults.size());

				SumAggregationResult sumAggregationResult2 =
					(SumAggregationResult)childrenAggregationResults.get("sum");

				Assert.assertEquals(
					"SumUser1 total priorities", 6,
					sumAggregationResult2.getValue(), 0);
			});
	}

}