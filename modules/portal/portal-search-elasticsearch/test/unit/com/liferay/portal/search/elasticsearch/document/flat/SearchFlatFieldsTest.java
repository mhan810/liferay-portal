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

package com.liferay.portal.search.elasticsearch.document.flat;

import static org.junit.Assert.assertEquals;

import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.search.elasticsearch.document.BaseElasticsearchTest;
import com.liferay.portal.search.elasticsearch.util.RetryAssertExecutor;

import java.util.concurrent.Callable;

import org.junit.Test;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class SearchFlatFieldsTest extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testSearchByLanguageIdEN() throws Exception {
		indexDocument(_EN);

		assertSearch("languageId", _EN, 1);
	}	
	
	@Test
	public void testSearchByLanguageIdPT() throws Exception {
		indexDocument(_PT);

		assertSearch("languageId", _PT, 1);
	}
	
	@Test
	public void testSearchArticleId() throws Exception {

		indexDocument(_PT);
		indexDocument(_EN);
		assertSearch("articleId", _ARTICLE_ID, 2);
	}
	
	
	protected Hits assertSearch(
			final String field, final String value, final int expected) 
		throws Exception {

		RetryAssertExecutor retry = new RetryAssertExecutor(1000);
		
		return retry.execute(new Callable<Hits>() {

			@Override
			public Hits call() throws Exception {
				Hits hits = search(field, value);
				assertEquals(expected, hits.getLength());
				return hits;
			}
		});		
	}
	
	protected DocumentImpl createDocument(String locale) {
		DocumentImpl document = createDocumentWithRequiredData();

		document.addKeyword("languageId",locale);
		document.addKeyword("articleId",_ARTICLE_ID);

		return document;
	}

	protected void indexDocument(String locale) throws Exception {
		DocumentImpl document = createDocument(locale);

		updateDocument(document);
	}
	
	private static final String _ARTICLE_ID = "123";

	private static final String _EN = "en";

	private static final String _PT = "pt";

}