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

package com.liferay.portal.search.test.util.aggregation.pipeline;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributorRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class TestCustomPipelineAggregationResultTranslatorContributorRegistry
	implements CustomPipelineAggregationResultTranslatorContributorRegistry {

	public static
		TestCustomPipelineAggregationResultTranslatorContributorRegistry
		getInstance() {

		return _instance;
	}

	@Override
	public CustomPipelineAggregationResultTranslatorContributor
		<? extends AggregationResult, ?>
			getCustomPipelineAggregationResultTranslatorContributor(
				String aggregationClassName) {

		return _customPipelineAggregationTranslatorContributors.get(
			aggregationClassName);
	}

	public void registerCustomPipelineAggregationResultTranslatorContributor(
		CustomPipelineAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>
				customPipelineAggregationTranslatorContributor) {

		_customPipelineAggregationTranslatorContributors.put(
			customPipelineAggregationTranslatorContributor.
				getAggregationClassName(),
			customPipelineAggregationTranslatorContributor);
	}

	private static final
		TestCustomPipelineAggregationResultTranslatorContributorRegistry
		_instance =
			new TestCustomPipelineAggregationResultTranslatorContributorRegistry();

	private final Map
		<String,
		 CustomPipelineAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>>
				_customPipelineAggregationTranslatorContributors =
					new HashMap<>();

}