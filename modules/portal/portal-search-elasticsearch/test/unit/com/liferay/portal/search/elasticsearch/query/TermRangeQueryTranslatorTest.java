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

import com.liferay.portal.kernel.search.generic.TermRangeQueryImpl;

import org.elasticsearch.index.query.RangeQueryBuilder;

import org.junit.Test;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class TermRangeQueryTranslatorTest {

	@Test
	public void testTermRangeQuery() throws Exception {
		TermRangeQueryImpl query = new TermRangeQueryImpl(
			"name", "André", "Miguel",true, false);

		assertTranslate(
			"{\"range\":{\"name\":{\"from\":\"André\",\"to\":\"Miguel\"," +
			"\"include_lower\":true,\"include_upper\":false}}}", 
			query);
	}

	protected void assertTranslate(
			String expected, TermRangeQueryImpl termRangeQuery)
		throws Exception {

		RangeQueryBuilder termRangeQueryBuilder = _translator.translate(
			termRangeQuery);

		QueryBuilderTestUtil.assertJsonContains(
			expected, termRangeQueryBuilder);
	}

	private final TermRangeQueryTranslator _translator = 
		new TermRangeQueryTranslator();

}