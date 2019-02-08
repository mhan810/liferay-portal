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
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseAggregation;
import com.liferay.portal.search.aggregation.CustomAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.CustomPipelineAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.CustomPipelineAggregationResultTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.ElasticsearchPipelineAggregationResultTranslator;
import com.liferay.portal.search.spi.aggregation.CustomAggregationResultTranslatorContributor;
import com.liferay.portal.search.test.util.aggregation.TestCustomAggregationResultTranslatorContributorRegistry;
import com.liferay.portal.search.test.util.aggregation.pipeline.TestCustomPipelineAggregationResultTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.Aggregation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael C. Han
 */
public class CustomAggregationResultTranslationTest {

	@Before
	public void setUp() {
		CustomAggregationResultTranslator customAggregationResultTranslator =
			new CustomAggregationResultTranslatorImpl() {
				{
					setCustomAggregationResultTranslatorContributorRegistry(
						TestCustomAggregationResultTranslatorContributorRegistry.
							getInstance());
				}
			};

		CustomPipelineAggregationResultTranslator
			customPipelineAggregationResultTranslator =
				new CustomPipelineAggregationResultTranslatorImpl() {
					{
						setCustomPipelineAggregationResultTranslatorContributorRegistry(
							TestCustomPipelineAggregationResultTranslatorContributorRegistry.
								getInstance());
					}
				};

		ElasticsearchPipelineAggregationResultTranslator
			elasticsearchPipelineAggregationResultTranslator =
				new ElasticsearchPipelineAggregationResultTranslator(
					customPipelineAggregationResultTranslator);

		_aggregationResultTranslator =
			new ElasticsearchAggregationResultTranslator(
				customAggregationResultTranslator,
				elasticsearchPipelineAggregationResultTranslator);
	}

	@Test
	public void testCustomAggregationTranslation() {
		TestCustomAggregationResultTranslatorContributorRegistry
			testCustomAggregationResultTranslatorContributorRegistry =
				TestCustomAggregationResultTranslatorContributorRegistry.
					getInstance();

		CustomAggregationResultTranslatorContributor
			<AggregationResult, Aggregation>
				customAggregationResultTranslatorContributor = Mockito.mock(
					CustomAggregationResultTranslatorContributor.class);

		Mockito.when(
			customAggregationResultTranslatorContributor.
				getAggregationClassName()
		).thenReturn(
			CustomTestAggregation.class.getName()
		);

		AggregationResult customAggregationResult = Mockito.mock(
			AggregationResult.class);

		Mockito.when(
			customAggregationResultTranslatorContributor.translate(
				Mockito.any(CustomTestAggregation.class),
				Mockito.mock(Aggregation.class),
				Mockito.any(AggregationResultTranslator.class),
				Mockito.any(PipelineAggregationResultTranslator.class))
		).thenReturn(
			customAggregationResult
		);

		testCustomAggregationResultTranslatorContributorRegistry.
			registerCustomAggregationResultTranslatorContributor(
				customAggregationResultTranslatorContributor);

		CustomAggregation customAggregation = new CustomAggregation(
			"TestName", new CustomTestAggregation("testName"));

		AggregationResult aggregationResult =
			_aggregationResultTranslator.translate(
				customAggregation, Mockito.mock(Aggregation.class));

		Assert.assertSame(customAggregationResult, aggregationResult);
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

	private AggregationResultTranslator<AggregationResult, Aggregation>
		_aggregationResultTranslator;

}