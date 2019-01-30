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

package com.liferay.portal.search.test.util.aggregation.bucket.daterange;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.DateRangeAggregation;
import com.liferay.portal.search.aggregation.bucket.RangeAggregationResult;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public abstract class BaseDateRangeAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testDateRanges() throws Exception {
		addDocument(getDate("2016-02-01T00:00:00"));
		addDocument(getDate("2016-02-02T00:00:00"));
		addDocument(getDate("2017-02-02T00:00:00"));
		addDocument(getDate("2017-02-03T00:00:00"));
		addDocument(getDate("2018-02-03T00:00:00"));
		addDocument(getDate("2018-02-03T00:00:00"));
		addDocument(getDate("2019-02-05T00:00:00"));

		DateRangeAggregation dateRangeAggregation = new DateRangeAggregation(
			"date_range", Field.EXPIRATION_DATE);

		dateRangeAggregation.setFormat("yyyyMMdd");

		dateRangeAggregation.addRange("Before 2017", "20160101", "20161231");
		dateRangeAggregation.addRange("2017", "20170101", "20171231");
		dateRangeAggregation.addRange("After 2017", "20180101", "20191231");

		RangeAggregationResult rangeAggregationResult = search(
			dateRangeAggregation);

		List<Bucket> buckets = new ArrayList<>(
			rangeAggregationResult.getBuckets());

		Assert.assertEquals("Num buckets", 2, buckets.size());

		assertBucket(buckets.get(0), "Before 2017", 2);
		assertBucket(buckets.get(1), "2017", 2);
		assertBucket(buckets.get(2), "After 2017", 3);
	}

	@Test
	public void testDateRangesWithKeys() throws Exception {
		addDocument(getDate("2016-02-01T00:00:00"));
		addDocument(getDate("2016-02-02T00:00:00"));
		addDocument(getDate("2017-02-02T00:00:00"));
		addDocument(getDate("2017-02-03T00:00:00"));
		addDocument(getDate("2018-02-03T00:00:00"));
		addDocument(getDate("2018-02-03T00:00:00"));
		addDocument(getDate("2019-02-05T00:00:00"));

		DateRangeAggregation dateRangeAggregation = new DateRangeAggregation(
			"date_range", Field.EXPIRATION_DATE);

		dateRangeAggregation.setFormat("yyyyMMdd");
		dateRangeAggregation.setKeyed(true);

		dateRangeAggregation.addRange("20160101", "20161231");
		dateRangeAggregation.addRange("20170101", "20171231");
		dateRangeAggregation.addRange("20180101", null);

		RangeAggregationResult rangeAggregationResult = search(
			dateRangeAggregation);

		List<Bucket> buckets = new ArrayList<>(
			rangeAggregationResult.getBuckets());

		Assert.assertEquals(buckets.toString(), 3, buckets.size());

		assertBucket(buckets.get(0), "20160101-20161231", 2);
		assertBucket(buckets.get(1), "20170101-20171231", 2);
		assertBucket(buckets.get(2), "20180101-*", 3);
	}

	protected void addDocument(Date date) throws Exception {
		addDocument(
			DocumentCreationHelpers.singleDate(Field.EXPIRATION_DATE, date));
	}

	protected void assertBucket(
		Bucket bucket, String expectedKey, long expectedCount) {

		Assert.assertEquals(expectedKey, bucket.getKey());
		Assert.assertEquals(expectedCount, bucket.getDocCount());
	}

	protected Date getDate(String date) {
		LocalDateTime localDateTime = LocalDateTime.parse(date);

		ZonedDateTime zonedDateTime = ZonedDateTime.of(
			localDateTime, ZoneId.systemDefault());

		return Date.from(zonedDateTime.toInstant());
	}

}