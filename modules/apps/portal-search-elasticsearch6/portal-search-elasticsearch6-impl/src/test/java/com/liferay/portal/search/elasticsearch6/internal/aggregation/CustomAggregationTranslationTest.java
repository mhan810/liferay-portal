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

package com.liferay.portal.search.elasticsearch6.internal.aggregation;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseAggregation;
import com.liferay.portal.search.aggregation.CustomAggregation;
import com.liferay.portal.search.spi.aggregation.CustomAggregationTranslatorContributor;
import com.liferay.portal.search.test.util.aggregation.TestCustomAggregationTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael C. Han
 */
public class CustomAggregationTranslationTest {

	@Before
	public void setUp() {
		ElasticsearchAggregationVisitorFixture
			elasticsearchAggregationVisitorFixture =
				new ElasticsearchAggregationVisitorFixture();

		_aggregationTranslator =
			elasticsearchAggregationVisitorFixture.
				getElasticsearchAggregationVisitor();
	}

	@Test
	public void testCustomAggregationTranslation() {
		TestCustomAggregationTranslatorContributorRegistry
			testCustomAggregationTranslatorContributorRegistry =
				TestCustomAggregationTranslatorContributorRegistry.
					getInstance();

		CustomAggregationTranslatorContributor<AggregationBuilder>
			customAggregationTranslatorContributor = Mockito.mock(
				CustomAggregationTranslatorContributor.class);

		Mockito.when(
			customAggregationTranslatorContributor.getAggregationClassName()
		).thenReturn(
			CustomTestAggregation.class.getName()
		);

		AggregationBuilder customAggregationBuilder = Mockito.mock(
			AggregationBuilder.class);

		Mockito.when(
			customAggregationTranslatorContributor.translate(
				Mockito.any(CustomTestAggregation.class),
				Mockito.any(AggregationTranslator.class))
		).thenReturn(
			customAggregationBuilder
		);

		testCustomAggregationTranslatorContributorRegistry.
			registerCustomAggregationTranslatorContributor(
				customAggregationTranslatorContributor);

		CustomAggregation customAggregation = new CustomAggregation(
			"TestName", new CustomTestAggregation("testName"));

		AggregationBuilder aggregationBuilder =
			_aggregationTranslator.translate(customAggregation);

		Assert.assertSame(customAggregationBuilder, aggregationBuilder);
	}

	public class CustomTestAggregation extends BaseAggregation {

		public CustomTestAggregation(String name) {
			super(name);
		}

		@Override
		public <S extends AggregationResult, T> S accept(
			AggregationResultTranslator<S, T> aggregationResultTranslator,
			T aggregationResult) {

			return null;
		}

		@Override
		public <T> T accept(AggregationVisitor<T> aggregationVisitor) {
			return null;
		}

	}

	private AggregationTranslator<AggregationBuilder> _aggregationTranslator;

}