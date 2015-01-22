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

package com.liferay.portal.search.elasticsearch.query;

import org.junit.Test;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
public class BooleanQueryTranslatorTest {

	@Test
	public void testBooleanQuery() throws Exception {
		BooleanQueryImpl query = new BooleanQueryImpl();

		query.addTerm("Luke", "Darth", false, BooleanClauseOccur.MUST);
		query.addTerm("Leia", "Han", false, BooleanClauseOccur.SHOULD);
		query.addTerm("Jar", "Jar", false, BooleanClauseOccur.MUST_NOT);

		assertTranslate(
			"{\"bool\":{" +
				"\"must\":{\"term\":{\"Luke\":\"Darth\"}}," +
				"\"must_not\":{\"term\":{\"Jar\":\"Jar\"}}," +
				"\"should\":{\"term\":{\"Leia\":\"Han\"}}" +
				"}}",
			query);
	}

	protected void assertTranslate(
			String expected, BooleanQuery originalQuery)
		throws Exception {

		ElasticsearchQuery elasticsearchQuery = 
			_translator.translate(originalQuery);

		String jsonQuery = elasticsearchQuery.getQueryString();
		
		QueryBuilderTestUtil.assertJsonContains(expected, jsonQuery);
	}

	private final BooleanQueryTranslator _translator =
		new BooleanQueryTranslator();

}