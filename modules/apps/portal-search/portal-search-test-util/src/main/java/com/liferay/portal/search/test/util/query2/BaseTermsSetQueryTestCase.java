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

package com.liferay.portal.search.test.util.query2;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.TermsSetQuery;
import com.liferay.portal.search.script.Script;
import com.liferay.portal.search.sort.FieldSort;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public abstract class BaseTermsSetQueryTestCase extends BaseIndexingTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();

		addDocument(
			document -> {
				document.addKeyword(
					Field.USER_NAME, new String[] {"SomeUser1", "SomeUser2"});
				document.addNumber(Field.PRIORITY, 2);
			});
		addDocument(
			document -> {
				document.addKeyword(
					Field.USER_NAME,
					new String[] {
						"SomeUser1", "SomeUser2", "SomeUser3", "SomeUser4"
					});
				document.addNumber(Field.PRIORITY, 3);
			});
		addDocument(
			document -> {
				document.addKeyword(
					Field.USER_NAME,
					new String[] {
						"SomeUser2", "SomeUser3", "SomeUser4", "SomeUser5"
					});
				document.addNumber(Field.PRIORITY, 2);
			});
		addDocument(
			document -> {
				document.addKeyword(
					Field.USER_NAME,
					new String[] {
						"SomeUser3", "SomeUser4", "SomeUser5", "SomeUser6"
					});
				document.addNumber(Field.PRIORITY, 3);
			});
		addDocument(
			document -> {
				document.addKeyword(
					Field.USER_NAME,
					new String[] {
						"SomeUser6", "SomeUser7", "SomeUser8", "SomeUser9"
					});
				document.addNumber(Field.PRIORITY, 2);
			});
	}

	@Test
	public void testTermsSetQueryWithField() {
		assertSearch(
			indexingTestHelper -> {
				List<Object> termsSet = new ArrayList<>();

				termsSet.add("SomeUser2");
				termsSet.add("SomeUser3");
				termsSet.add("SomeUser4");

				TermsSetQuery termsSetQuery = new TermsSetQuery(
					Field.USER_NAME, termsSet);

				termsSetQuery.setMinimumShouldMatchField(Field.PRIORITY);

				SearchSearchRequest searchSearchRequest =
					new SearchSearchRequest();

				searchSearchRequest.setIndexNames("_all");
				searchSearchRequest.setQuery(termsSetQuery);

				FieldSort fieldSort = new FieldSort(Field.UID);

				fieldSort.setSortOrder(SortOrder.ASC);

				searchSearchRequest.addSorts(fieldSort);

				SearchEngineAdapter searchEngineAdapter =
					getSearchEngineAdapter();

				SearchSearchResponse searchSearchResponse =
					searchEngineAdapter.execute(searchSearchRequest);

				SearchHits searchHits = searchSearchResponse.getSearchHits();

				Assert.assertEquals("Total hits", 2, searchHits.getTotalHits());

				List<SearchHit> searchHitList = searchHits.getSearchHits();

				Assert.assertEquals("Retrieved hits", 2, searchHitList.size());

				SearchHit searchHit1 = searchHitList.get(0);

				Document document1 = searchHit1.getDocument();

				List<String> document1Values = new ArrayList<>();

				document1Values.add("SomeUser1");
				document1Values.add("SomeUser2");
				document1Values.add("SomeUser3");
				document1Values.add("SomeUser4");

				Assert.assertEquals(
					document1Values, document1.getFieldValues(Field.USER_NAME));

				SearchHit searchHit2 = searchHitList.get(1);

				Document document2 = searchHit2.getDocument();

				List<String> document2Values = new ArrayList<>();

				document2Values.add("SomeUser2");
				document2Values.add("SomeUser3");
				document2Values.add("SomeUser4");
				document2Values.add("SomeUser5");

				Assert.assertEquals(
					document2Values, document2.getFieldValues(Field.USER_NAME));
			});
	}

	@Test
	public void testTermsSetQueryWithScript() {
		assertSearch(
			indexingTestHelper -> {
				List<Object> termsSet = new ArrayList<>();

				termsSet.add("SomeUser2");
				termsSet.add("SomeUser3");
				termsSet.add("SomeUser4");

				TermsSetQuery termsSetQuery = new TermsSetQuery(
					Field.USER_NAME, termsSet);

				Script script = new Script(
					"painless",
					"Math.min(params.num_terms, doc['priority'].value)");

				termsSetQuery.setMinimumShouldMatchScript(script);

				SearchSearchRequest searchSearchRequest =
					new SearchSearchRequest();

				searchSearchRequest.setIndexNames("_all");
				searchSearchRequest.setQuery(termsSetQuery);

				FieldSort fieldSort = new FieldSort(Field.UID);

				fieldSort.setSortOrder(SortOrder.ASC);

				searchSearchRequest.addSorts(fieldSort);

				SearchEngineAdapter searchEngineAdapter =
					getSearchEngineAdapter();

				SearchSearchResponse searchSearchResponse =
					searchEngineAdapter.execute(searchSearchRequest);

				SearchHits searchHits = searchSearchResponse.getSearchHits();

				Assert.assertEquals("Total hits", 2, searchHits.getTotalHits());

				List<SearchHit> searchHitList = searchHits.getSearchHits();

				Assert.assertEquals("Retrieved hits", 2, searchHitList.size());

				SearchHit searchHit1 = searchHitList.get(0);

				Document document1 = searchHit1.getDocument();

				List<String> document1Values = new ArrayList<>();

				document1Values.add("SomeUser1");
				document1Values.add("SomeUser2");
				document1Values.add("SomeUser3");
				document1Values.add("SomeUser4");

				Assert.assertEquals(
					document1Values, document1.getFieldValues(Field.USER_NAME));

				SearchHit searchHit2 = searchHitList.get(1);

				Document document2 = searchHit2.getDocument();

				List<String> document2Values = new ArrayList<>();

				document2Values.add("SomeUser2");
				document2Values.add("SomeUser3");
				document2Values.add("SomeUser4");
				document2Values.add("SomeUser5");

				Assert.assertEquals(
					document2Values, document2.getFieldValues(Field.USER_NAME));
			});
	}

}