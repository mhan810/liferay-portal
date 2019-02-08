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

import com.liferay.portal.search.aggregation.pipeline.CustomPipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CustomPipelineAggregationTranslator.class)
public class CustomPipelineAggregationTranslatorImpl
	implements CustomPipelineAggregationTranslator {

	@Override
	public PipelineAggregationBuilder translate(
		CustomPipelineAggregation customPipelineAggregation,
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		PipelineAggregation delegatePipelineAggregation =
			customPipelineAggregation.getPipelineAggregation();

		Class<?> aggregationClass = delegatePipelineAggregation.getClass();

		try {
			CustomPipelineAggregationTranslatorContributor
				<PipelineAggregationBuilder>
					customPipelineAggregationTranslatorContributor =
						(CustomPipelineAggregationTranslatorContributor
							<PipelineAggregationBuilder>)
								_customPipelineAggregationTranslatorContributorRegistry.
									getCustomPipelineAggregationTranslatorContributor(
										aggregationClass.getName());

			if (customPipelineAggregationTranslatorContributor == null) {
				throw new IllegalStateException(
					"No CustomPipelineAggregationTranslatorContributor found " +
						"for: " + aggregationClass);
			}

			PipelineAggregationBuilder pipelineAggregationBuilder =
				customPipelineAggregationTranslatorContributor.translate(
					delegatePipelineAggregation, pipelineAggregationTranslator);

			return pipelineAggregationBuilder;
		}
		catch (ClassCastException cce) {
			throw new IllegalArgumentException(
				"No CustomPipelineAggregationTranslatorContributor found " +
					"for: " + aggregationClass,
				cce);
		}
	}

	@Reference(unbind = "-")
	protected void setCustomPipelineAggregationTranslatorContributorRegistry(
		CustomPipelineAggregationTranslatorContributorRegistry
			customAggregationTranslatorContributorRegistry) {

		_customPipelineAggregationTranslatorContributorRegistry =
			customAggregationTranslatorContributorRegistry;
	}

	private CustomPipelineAggregationTranslatorContributorRegistry
		_customPipelineAggregationTranslatorContributorRegistry;

}