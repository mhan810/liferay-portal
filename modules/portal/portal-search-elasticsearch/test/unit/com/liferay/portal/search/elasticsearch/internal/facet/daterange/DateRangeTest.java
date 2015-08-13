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

package com.liferay.portal.search.elasticsearch.internal.facet.daterange;

import static org.junit.Assert.assertEquals;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.search.elasticsearch.facet.hits.AggregatedHit;
import com.liferay.portal.search.elasticsearch.internal.facet.AggregationsTest;
import com.liferay.portal.search.elasticsearch.internal.facet.hits.AggregatedHitsImpl;
import com.liferay.portal.search.elasticsearch.internal.facet.hits.DateRangeAggregatedHit;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange.Bucket;

import org.junit.Test;
public class DateRangeTest extends AggregationsTest {

	@Test
	public void testAfterDate() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet = createDateRangeFacet(searchContext);

		String dateAsString = createDate(1);

		dateRangeFacet.setFrom(dateAsString);

		configureSearchContext(searchContext, dateRangeFacet);

		List<AggregatedHit> aggregatedHits = getAggregationHits(searchContext);

		assertEquals(1, aggregatedHits.size());

		DateRangeAggregatedHit dateRangeAggregatedHit =
			(DateRangeAggregatedHit)aggregatedHits.get(0);

		assertEquals(1, dateRangeAggregatedHit.getBuckets().size());
		Bucket bucket =
			(InternalDateRange.Bucket)
				dateRangeAggregatedHit.getBuckets().toArray()[0];
		assertEquals(30, bucket.getDocCount());
	}

	@Test
	public void testBeforeDate() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet = createDateRangeFacet(searchContext);

		String dateAsString = createDate(31);

		dateRangeFacet.setTo(dateAsString);

		configureSearchContext(searchContext, dateRangeFacet);

		List<AggregatedHit> aggregatedHits = getAggregationHits(searchContext);

		assertEquals(1, aggregatedHits.size());

		DateRangeAggregatedHit dateRangeAggregatedHit =
			(DateRangeAggregatedHit)aggregatedHits.get(0);

		assertEquals(1, dateRangeAggregatedHit.getBuckets().size());
		Bucket bucket =
			(InternalDateRange.Bucket)
				dateRangeAggregatedHit.getBuckets().toArray()[0];
		assertEquals(31, bucket.getDocCount());
	}

	@Test
	public void testBetweenDates() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet01 = createDateRangeFacet(searchContext);

		String fromAsString = createDate(1);
		String toAsString = createDate(15);

		dateRangeFacet01.setFrom(fromAsString);
		dateRangeFacet01.setTo(toAsString);

		configureSearchContext(searchContext, dateRangeFacet01);

		List<AggregatedHit> aggregatedHits = getAggregationHits(searchContext);

		assertEquals(1, aggregatedHits.size());

		DateRangeAggregatedHit dateRangeAggregatedHit =
			(DateRangeAggregatedHit)aggregatedHits.get(0);

		assertEquals(1, dateRangeAggregatedHit.getBuckets().size());

		Bucket bucket01 =
			(InternalDateRange.Bucket)
				dateRangeAggregatedHit.getBuckets().toArray()[0];

		assertEquals(14, bucket01.getDocCount());
	}

	@Override
	protected void clearEnviroment() throws Exception {
		FileUtils.forceDelete(new File("tmp/"));
	}

	protected void configureSearchContext(
		SearchContext searchContext, DateRangeFacet... dateRangeFacets) {

		for (DateRangeFacet dateRangeFacet : dateRangeFacets) {
			searchContext.addFacet(dateRangeFacet);
		}

		searchContext.setAttribute(
			"INCLUDE_ONLY_AGGREGATION_BUCKETS", Boolean.TRUE);
	}

	protected String createDate(int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");

		String dateAsString = formatDate.format(calendar.getTime());
		return dateAsString;
	}

	protected DateRangeFacet createDateRangeFacet(SearchContext searchContext) {
		DateRangeFacet dateRangeFacet = new DateRangeFacet(searchContext);
		dateRangeFacet.getFacetConfiguration().setFieldName(Field.CREATE_DATE);
		return dateRangeFacet;
	}

	protected List<AggregatedHit> getAggregationHits(
			SearchContext searchContext)
		throws SearchException {

		TermQuery termQuery = new TermQueryImpl(
			Field.ENTRY_CLASS_NAME, ENTRY_CLASS_NAME);

		AggregatedHitsImpl hits = (AggregatedHitsImpl)indexSearcher.search(
			searchContext, termQuery);

		List<AggregatedHit> aggregatedHits = hits.getAggregatedHits();
		return aggregatedHits;
	}

	@Override
	protected void initEnviroment() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		for (int i = 1; i <= 31; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, i);
			indexWriter.addDocument(
				getSearchContext(), newDocument(calendar.getTime()));
		}

		Thread.sleep(10 * 1000);
	}

}