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
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.Order;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BaseTermsAggregationTestCase
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
	public void testTerms() throws Exception {
		TermsAggregation termsAggregation = new TermsAggregation(
			"terms", Field.USER_NAME);

		assertSearch(
			indexingTestHelper -> {
				TermsAggregationResult termsAggregationResult = search(
					termsAggregation);

				List<Bucket> buckets = new ArrayList<>(
					termsAggregationResult.getBuckets());

				Assert.assertEquals("Num buckets", 3, buckets.size());

				assertBucket(buckets.get(0), "SomeUser2", 5);
				assertBucket(buckets.get(1), "SomeUser1", 4);
				assertBucket(buckets.get(2), "SomeUser3", 2);
			});
	}

	@Test
	public void testTermsWithOrder() throws Exception {
		TermsAggregation termsAggregation = new TermsAggregation(
			"terms", Field.USER_NAME);

		termsAggregation.addOrders(Order.key(true));

		assertSearch(
			indexingTestHelper -> {
				TermsAggregationResult termsAggregationResult = search(
					termsAggregation);

				List<Bucket> buckets = new ArrayList<>(
					termsAggregationResult.getBuckets());

				Assert.assertEquals("Num buckets", 3, buckets.size());

				assertBucket(buckets.get(0), "SomeUser1", 4);
				assertBucket(buckets.get(1), "SomeUser2", 5);
				assertBucket(buckets.get(2), "SomeUser3", 2);
			});
	}

	protected void assertBucket(
		Bucket bucket, String expectedKey, long expectedCount) {

		Assert.assertEquals(expectedKey, bucket.getKey());
		Assert.assertEquals(expectedCount, bucket.getDocCount());
	}

}