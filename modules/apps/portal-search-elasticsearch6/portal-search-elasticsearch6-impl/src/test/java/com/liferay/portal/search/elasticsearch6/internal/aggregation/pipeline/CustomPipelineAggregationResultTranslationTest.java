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

package com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.pipeline.BasePipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.CustomPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationVisitor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributor;
import com.liferay.portal.search.test.util.aggregation.pipeline.TestCustomPipelineAggregationResultTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.Aggregation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael C. Han
 */
public class CustomPipelineAggregationResultTranslationTest {

	@Before
	public void setUp() {
		CustomPipelineAggregationResultTranslator
			customPipelineAggregationResultTranslator =
				new CustomPipelineAggregationResultTranslatorImpl() {
					{
						setCustomPipelineAggregationResultTranslatorContributorRegistry(
							TestCustomPipelineAggregationResultTranslatorContributorRegistry.
								getInstance());
					}
				};

		_pipelineAggregationResultTranslator =
			new ElasticsearchPipelineAggregationResultTranslator(
				customPipelineAggregationResultTranslator);
	}

	@Test
	public void testCustomAggregationTranslation() {
		TestCustomPipelineAggregationResultTranslatorContributorRegistry
			testCustomPipelineAggregationResultTranslatorContributorRegistry =
				TestCustomPipelineAggregationResultTranslatorContributorRegistry.
					getInstance();

		CustomPipelineAggregationResultTranslatorContributor
			<AggregationResult, Aggregation>
				customPipelineAggregationResultTranslatorContributor =
					Mockito.mock(
						CustomPipelineAggregationResultTranslatorContributor.
							class);

		Mockito.when(
			customPipelineAggregationResultTranslatorContributor.
				getAggregationClassName()
		).thenReturn(
			CustomTestPipelineAggregation.class.getName()
		);

		AggregationResult customAggregationResult = Mockito.mock(
			AggregationResult.class);

		Mockito.when(
			customPipelineAggregationResultTranslatorContributor.translate(
				Mockito.any(CustomTestPipelineAggregation.class),
				Mockito.mock(Aggregation.class),
				Mockito.any(PipelineAggregationResultTranslator.class))
		).thenReturn(
			customAggregationResult
		);

		testCustomPipelineAggregationResultTranslatorContributorRegistry.
			registerCustomPipelineAggregationResultTranslatorContributor(
				customPipelineAggregationResultTranslatorContributor);

		CustomPipelineAggregation customPipelineAggregation =
			new CustomPipelineAggregation(
				"TestName", new CustomTestPipelineAggregation("testName"));

		AggregationResult aggregationResult =
			_pipelineAggregationResultTranslator.translate(
				customPipelineAggregation, Mockito.mock(Aggregation.class));

		Assert.assertSame(customAggregationResult, aggregationResult);
	}

	public class CustomTestPipelineAggregation extends BasePipelineAggregation {

		public CustomTestPipelineAggregation(String name) {
			super(name);
		}

		@Override
		public <S extends AggregationResult, T> S accept(
			PipelineAggregationResultTranslator<S, T>
				pipelineAggregationResultTranslator,
			T aggregationResult) {

			return null;
		}

		@Override
		public <T> T accept(
			PipelineAggregationVisitor<T> pipelineAggregationVisitor) {

			return null;
		}

	}

	private PipelineAggregationResultTranslator<AggregationResult, Aggregation>
		_pipelineAggregationResultTranslator;

}