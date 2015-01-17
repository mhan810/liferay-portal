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

import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.search.lucene.TermQueryImpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class PortalToElasticsearchQueryTranslatorTest {

	@Test
	public void testTermQuery() {
		TermQuery query = new TermQueryImpl("foo", "bar");
		ElasticsearchQuery elasticsearchQuery = _translator.translate(query);
		String json = elasticsearchQuery.toJson();
		Assert.assertTrue(json.contains("\"query\" : \"foo:bar\""));
	}

	private final PortalToElasticsearchQueryTranslator _translator =
		new PortalToElasticsearchQueryTranslator();

}