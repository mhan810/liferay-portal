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
import com.liferay.portal.search.aggregation.pipeline.CustomPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.Aggregation;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CustomPipelineAggregationResultTranslator.class)
public class CustomPipelineAggregationResultTranslatorImpl
	implements CustomPipelineAggregationResultTranslator {

	@Override
	public AggregationResult translate(
		CustomPipelineAggregation customPipelineAggregation,
		Aggregation aggregation,
		PipelineAggregationResultTranslator<AggregationResult, Aggregation>
			pipelineAggregationResultTranslator) {

		PipelineAggregation delegatePipelineAggregation =
			customPipelineAggregation.getPipelineAggregation();

		Class<?> aggregationClass = delegatePipelineAggregation.getClass();

		try {
			CustomPipelineAggregationResultTranslatorContributor
				<AggregationResult, Aggregation>
					customPipelineAggregationTranslatorContributor =
						(CustomPipelineAggregationResultTranslatorContributor
							<AggregationResult, Aggregation>)
								_customPipelineAggregationResultTranslatorContributorRegistry.
									getCustomPipelineAggregationResultTranslatorContributor(
										aggregationClass.getName());

			if (customPipelineAggregationTranslatorContributor == null) {
				throw new IllegalStateException(
					"No CustomPipelineAggregationResultTranslatorContributor " +
						"found for: " + aggregationClass);
			}

			AggregationResult aggregationResult =
				customPipelineAggregationTranslatorContributor.translate(
					delegatePipelineAggregation, aggregation,
					pipelineAggregationResultTranslator);

			return aggregationResult;
		}
		catch (ClassCastException cce) {
			throw new IllegalArgumentException(
				"No CustomPipelineAggregationResultTranslatorContributor " +
					"found for: " + aggregationClass,
				cce);
		}
	}

	@Reference(unbind = "-")
	protected void
		setCustomPipelineAggregationResultTranslatorContributorRegistry(
			CustomPipelineAggregationResultTranslatorContributorRegistry
				customAggregationTranslatorContributorRegistry) {

		_customPipelineAggregationResultTranslatorContributorRegistry =
			customAggregationTranslatorContributorRegistry;
	}

	private CustomPipelineAggregationResultTranslatorContributorRegistry
		_customPipelineAggregationResultTranslatorContributorRegistry;

}