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

package com.liferay.portal.search.test.util.aggregation.pipeline.sum;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.pipeline.SumBucketPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.SumBucketPipelineAggregationResult;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public abstract class BaseSumBucketPipelineAggregationTestCase
	extends BaseAggregationTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();

		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 1);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 2);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser3");
				document.addNumber(Field.PRIORITY, 3);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 4);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 5);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser3");
				document.addNumber(Field.PRIORITY, 6);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 7);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 8);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser1");
				document.addNumber(Field.PRIORITY, 9);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 10);
			});
		addDocument(
			document -> {
				document.addKeyword(Field.USER_NAME, "SomeUser2");
				document.addNumber(Field.PRIORITY, 11);
			});
	}

	@Test
	public void testSumBucketPipelineAggregationResult() throws Exception {
		SumAggregation sumAggregation = new SumAggregation(
			"sum", Field.PRIORITY);

		TermsAggregation termsAggregation = new TermsAggregation(
			"terms", Field.USER_NAME);

		termsAggregation.addChildAggregation(sumAggregation);

		SumBucketPipelineAggregation sumBucketPipelineAggregation =
			new SumBucketPipelineAggregation("sumBucket", "terms>sum");

		Map<String, AggregationResult> aggregationResultMap = search(
			termsAggregation, sumBucketPipelineAggregation);

		SumBucketPipelineAggregationResult sumBucketPipelineAggregationResult =
			(SumBucketPipelineAggregationResult)
				aggregationResultMap.get("sumBucket");

		Assert.assertEquals(
			66, sumBucketPipelineAggregationResult.getValue(), 0);
	}

}