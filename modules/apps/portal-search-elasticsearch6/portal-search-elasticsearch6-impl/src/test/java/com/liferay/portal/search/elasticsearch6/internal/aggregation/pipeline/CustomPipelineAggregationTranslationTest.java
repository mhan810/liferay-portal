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
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationVisitor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationTranslatorContributor;
import com.liferay.portal.search.test.util.aggregation.pipeline.TestCustomPipelineAggregationTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael C. Han
 */
public class CustomPipelineAggregationTranslationTest {

	@Before
	public void setUp() {
		ElasticsearchPipelineAggregationVisitorFixture
			elasticsearchPipelineAggregationVisitorFixture =
				new ElasticsearchPipelineAggregationVisitorFixture();

		_pipelineAggregationTranslator =
			elasticsearchPipelineAggregationVisitorFixture.
				getElasticsearchPipelineAggregationVisitor();
	}

	@Test
	public void testCustomPipelineAggregationTranslation() {
		TestCustomPipelineAggregationTranslatorContributorRegistry
			testCustomPipelineAggregationTranslatorContributorRegistry =
				TestCustomPipelineAggregationTranslatorContributorRegistry.
					getInstance();

		CustomPipelineAggregationTranslatorContributor
			<PipelineAggregationBuilder>
				customPipelineAggregationTranslatorContributor = Mockito.mock(
					CustomPipelineAggregationTranslatorContributor.class);

		Mockito.when(
			customPipelineAggregationTranslatorContributor.
				getPipelineAggregationClassName()
		).thenReturn(
			CustomTestPipelineAggregation.class.getName()
		);

		PipelineAggregationBuilder customPipelineAggregationBuilder =
			Mockito.mock(PipelineAggregationBuilder.class);

		Mockito.when(
			customPipelineAggregationTranslatorContributor.translate(
				Mockito.any(CustomTestPipelineAggregation.class),
				Mockito.any(PipelineAggregationTranslator.class))
		).thenReturn(
			customPipelineAggregationBuilder
		);

		testCustomPipelineAggregationTranslatorContributorRegistry.
			registerCustomPipelineAggregationTranslatorContributor(
				customPipelineAggregationTranslatorContributor);

		CustomPipelineAggregation customPipelineAggregation =
			new CustomPipelineAggregation(
				"TestName", new CustomTestPipelineAggregation("testName"));

		PipelineAggregationBuilder pipelineAggregationBuilder =
			_pipelineAggregationTranslator.translate(customPipelineAggregation);

		Assert.assertSame(
			customPipelineAggregationBuilder, pipelineAggregationBuilder);
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

	private PipelineAggregationTranslator<PipelineAggregationBuilder>
		_pipelineAggregationTranslator;

}