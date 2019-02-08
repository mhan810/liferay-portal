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

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.CustomAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.spi.aggregation.CustomAggregationTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.CustomAggregationTranslatorContributorRegistry;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CustomAggregationTranslator.class)
public class CustomAggregationTranslatorImpl
	implements CustomAggregationTranslator {

	@Override
	public AggregationBuilder translate(
		CustomAggregation customAggregation,
		AggregationTranslator<AggregationBuilder> aggregationTranslator,
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		Aggregation delegateAggregation =
			customAggregation.getDelegateAggregation();

		Class<?> aggregationClass = delegateAggregation.getClass();

		try {
			CustomAggregationTranslatorContributor<AggregationBuilder>
				customAggregationTranslatorContributor =
					(CustomAggregationTranslatorContributor<AggregationBuilder>)
						_customAggregationTranslatorContributorRegistry.
							getCustomAggregationTranslatorContributor(
								aggregationClass.getName());

			if (customAggregationTranslatorContributor == null) {
				throw new IllegalStateException(
					"No CustomAggregationTranslatorContributor found for: " +
						aggregationClass);
			}

			AggregationBuilder aggregationBuilder =
				customAggregationTranslatorContributor.translate(
					delegateAggregation, aggregationTranslator);

			return _baseAggregationTranslator.translate(
				aggregationBuilder, delegateAggregation, aggregationTranslator,
				pipelineAggregationTranslator);
		}
		catch (ClassCastException cce) {
			throw new IllegalArgumentException(
				"No CustomAggregationTranslatorContributor found for: " +
					aggregationClass,
				cce);
		}
	}

	@Reference(unbind = "-")
	protected void setCustomAggregationTranslatorContributorRegistry(
		CustomAggregationTranslatorContributorRegistry
			customAggregationTranslatorContributorRegistry) {

		_customAggregationTranslatorContributorRegistry =
			customAggregationTranslatorContributorRegistry;
	}

	private final BaseAggregationTranslator _baseAggregationTranslator =
		new BaseAggregationTranslator();
	private CustomAggregationTranslatorContributorRegistry
		_customAggregationTranslatorContributorRegistry;

}