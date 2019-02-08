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

package com.liferay.portal.search.elasticsearch6.internal.query2;

import com.liferay.portal.search.query.BaseQueryImpl;
import com.liferay.portal.search.query.CustomQuery;
import com.liferay.portal.search.query.QueryTranslator;
import com.liferay.portal.search.query.QueryVisitor;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributor;
import com.liferay.portal.search.test.util.query2.TestCustomQueryTranslatorContributorRegistry;

import org.elasticsearch.index.query.QueryBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael C. Han
 */
public class CustomQueryTranslationTest {

	@Before
	public void setUp() {
		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		_queryTranslator =
			elasticsearchQueryTranslatorFixture.
				getElasticsearchQueryTranslator();
	}

	@Test
	public void testCustomQueryTranslation() {
		TestCustomQueryTranslatorContributorRegistry
			testCustomQueryTranslatorContributorRegistry =
				TestCustomQueryTranslatorContributorRegistry.getInstance();

		CustomQueryTranslatorContributor<QueryBuilder>
			customQueryTranslatorContributor = Mockito.mock(
				CustomQueryTranslatorContributor.class);

		Mockito.when(
			customQueryTranslatorContributor.getQueryClassName()
		).thenReturn(
			CustomTestQuery.class.getName()
		);

		QueryBuilder customQueryBuilder = Mockito.mock(QueryBuilder.class);

		Mockito.when(
			customQueryTranslatorContributor.translate(
				Mockito.any(CustomTestQuery.class),
				Mockito.any(QueryTranslator.class))
		).thenReturn(
			customQueryBuilder
		);

		testCustomQueryTranslatorContributorRegistry.
			registerCustomQueryTranslatorContributor(
				customQueryTranslatorContributor);

		CustomQuery customQuery = new CustomQuery(new CustomTestQuery());

		QueryBuilder queryBuilder = _queryTranslator.translate(customQuery);

		Assert.assertSame(customQueryBuilder, queryBuilder);
	}

	public class CustomTestQuery extends BaseQueryImpl {

		@Override
		public <T> T accept(QueryVisitor<T> queryVisitor) {
			return null;
		}

	}

	private QueryTranslator<QueryBuilder> _queryTranslator;

}