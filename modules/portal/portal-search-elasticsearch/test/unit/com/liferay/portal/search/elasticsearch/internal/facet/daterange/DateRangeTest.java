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
import static org.junit.Assert.assertTrue;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.search.facet.daterange.DateRangeFacet;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.search.elasticsearch.internal.facet.AggregationsTest;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Client;

import org.junit.Test;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class DateRangeTest extends AggregationsTest {

	@Test
	public void testAfterDate() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet = createDateRangeFacet(searchContext);

		String dateAsString = createDate(1);

		dateRangeFacet.addUnboundedFrom(dateAsString);

		configureSearchContext(searchContext, dateRangeFacet);

		getFacetCollectors(searchContext);

		Facet facet = searchContext.getFacet(dateRangeFacet.getFieldName());
		FacetCollector facetCollector = facet.getFacetCollector();
		List<TermCollector> termCollectors = facetCollector.getTermCollectors();

		assertEquals(1, termCollectors.size());
		assertEquals(30, termCollectors.get(0).getFrequency());
	}

	@Test
	public void testBeforeDate() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet = createDateRangeFacet(searchContext);

		String dateAsString = createDate(31);

		dateRangeFacet.addUnboundedTo(dateAsString);

		configureSearchContext(searchContext, dateRangeFacet);

		getFacetCollectors(searchContext);

		Facet facet = searchContext.getFacet(dateRangeFacet.getFieldName());
		FacetCollector facetCollector = facet.getFacetCollector();
		List<TermCollector> termCollectors = facetCollector.getTermCollectors();

		assertEquals(1, termCollectors.size());
		assertEquals(31, termCollectors.get(0).getFrequency());
	}

	@Test
	public void testBetweenDates() throws Exception {
		SearchContext searchContext = getSearchContext();

		DateRangeFacet dateRangeFacet = createDateRangeFacet(searchContext);

		dateRangeFacet.addRange(createDate(1), createDate(15));
		dateRangeFacet.addRange(createDate(16), createDate(31));

		configureSearchContext(searchContext, dateRangeFacet);

		getFacetCollectors(searchContext);

		Facet facet = searchContext.getFacet(dateRangeFacet.getFieldName());
		FacetCollector facetCollector = facet.getFacetCollector();
		List<TermCollector> termCollectors = facetCollector.getTermCollectors();

		assertEquals(2, termCollectors.size());
		assertTrue(
			14 == termCollectors.get(0).getFrequency() ||
				15 == termCollectors.get(0).getFrequency());
		assertTrue(
			14 == termCollectors.get(1).getFrequency() ||
				15 == termCollectors.get(1).getFrequency());
	}

	@Override
	protected void clearEnviroment() throws Exception {
		Client client = connectionManager.getClient();

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
			COMPANY_ID + "");
		client.admin().indices().delete(deleteIndexRequest).actionGet();

		FileUtils.forceDelete(new File("tmp/"));
	}

	protected void configureSearchContext(
		SearchContext searchContext, DateRangeFacet... dateRangeFacets) {

		for (DateRangeFacet dateRangeFacet : dateRangeFacets) {
			searchContext.addFacet(dateRangeFacet);
		}

		searchContext.setAggregationsOnly(true);
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

	protected void getFacetCollectors(SearchContext searchContext)
		throws SearchException {

		TermQuery termQuery = new TermQueryImpl(
			Field.ENTRY_CLASS_NAME, ENTRY_CLASS_NAME);

		indexSearcher.search(searchContext, termQuery);
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