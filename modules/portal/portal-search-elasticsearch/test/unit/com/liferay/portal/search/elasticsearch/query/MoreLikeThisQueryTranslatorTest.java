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

import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.junit.Test;

import com.liferay.portal.kernel.search.MoreLikeThisQuery;
import com.liferay.portal.kernel.search.generic.MoreLikeThisQueryImpl;

/**
 * @author André de Oliveira
 */
public class MoreLikeThisQueryTranslatorTest {

	@Test
	public void testMoreLikeThisQuery() throws Exception {
		
		/*
		String likeText,
		int maxQueryTerms, int minTermFreq, String... fields
		 */
		
		MoreLikeThisQuery query = new MoreLikeThisQueryImpl(
			"Gallindo", 12,1, "name.first", "name.last");

		String expected = 
			"{\"mlt\":{\"fields\":[\"name.first\",\"name.last\"]," +
			"\"like_text\":\"Gallindo\",\"min_term_freq\":1," +
			"\"max_query_terms\":12}";
		
		assertTranslate(expected, query);
	}

	protected void assertTranslate(
			String expected, MoreLikeThisQuery termRangeQuery)
		throws Exception {

		MoreLikeThisQueryBuilder termRangeQueryBuilder = _translator.translate(
			termRangeQuery);

		QueryBuilderTestUtil.assertJsonContains(
			expected, termRangeQueryBuilder);
	}

	private final MoreLikeThisQueryTranslator _translator = 
		new MoreLikeThisQueryTranslator();

}