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
import com.liferay.portal.search.aggregation.CustomAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.spi.aggregation.CustomAggregationResultTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.CustomAggregationResultTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.Aggregation;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CustomAggregationTranslator.class)
public class CustomAggregationResultTranslatorImpl
	implements CustomAggregationResultTranslator {

	@Override
	public AggregationResult translate(
		CustomAggregation customAggregation, Aggregation aggregation,
		AggregationResultTranslator<AggregationResult, Aggregation>
			aggregationResultTranslator,
		PipelineAggregationResultTranslator
			pipelineAggregationResultTranslator) {

		com.liferay.portal.search.aggregation.Aggregation liferayAggregation =
			customAggregation.getDelegateAggregation();

		Class<?> aggregationClass = liferayAggregation.getClass();

		try {
			CustomAggregationResultTranslatorContributor
				<AggregationResult, Aggregation>
					customAggregationResultTranslatorContributor =
						(CustomAggregationResultTranslatorContributor
							<AggregationResult, Aggregation>)
								_customAggregationResultTranslatorContributorRegistry.
									getCustomAggregationResultTranslatorContributor(
										aggregationClass.getName());

			if (customAggregationResultTranslatorContributor == null) {
				throw new IllegalStateException(
					"No CustomAggregationResultTranslatorContributor found " +
						"for " + aggregationClass);
			}

			AggregationResult aggregationResult =
				customAggregationResultTranslatorContributor.translate(
					liferayAggregation, aggregation,
					aggregationResultTranslator,
					pipelineAggregationResultTranslator);

			return aggregationResult;
		}
		catch (ClassCastException cce) {
			throw new IllegalArgumentException(
				"No CustomAggregationResultTranslatorContributor found for: " +
					aggregationClass,
				cce);
		}
	}

	@Reference(unbind = "-")
	protected void setCustomAggregationResultTranslatorContributorRegistry(
		CustomAggregationResultTranslatorContributorRegistry
			customAggregationResultTranslatorContributorRegistry) {

		_customAggregationResultTranslatorContributorRegistry =
			customAggregationResultTranslatorContributorRegistry;
	}

	private CustomAggregationResultTranslatorContributorRegistry
		_customAggregationResultTranslatorContributorRegistry;

}