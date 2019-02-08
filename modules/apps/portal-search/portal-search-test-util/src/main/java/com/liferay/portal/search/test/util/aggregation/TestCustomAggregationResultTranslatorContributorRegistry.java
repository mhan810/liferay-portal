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

package com.liferay.portal.search.test.util.aggregation;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.spi.aggregation.CustomAggregationResultTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.CustomAggregationResultTranslatorContributorRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class TestCustomAggregationResultTranslatorContributorRegistry
	implements CustomAggregationResultTranslatorContributorRegistry {

	public static TestCustomAggregationResultTranslatorContributorRegistry
		getInstance() {

		return _instance;
	}

	@Override
	public CustomAggregationResultTranslatorContributor
		<? extends AggregationResult, ?>
			getCustomAggregationResultTranslatorContributor(
				String queryClassName) {

		return _customAggregationTranslatorContributors.get(queryClassName);
	}

	public void registerCustomAggregationResultTranslatorContributor(
		CustomAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>
				customAggregationTranslatorContributor) {

		_customAggregationTranslatorContributors.put(
			customAggregationTranslatorContributor.getAggregationClassName(),
			customAggregationTranslatorContributor);
	}

	private static final
		TestCustomAggregationResultTranslatorContributorRegistry _instance =
			new TestCustomAggregationResultTranslatorContributorRegistry();

	private final Map
		<String,
		 CustomAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>>
				_customAggregationTranslatorContributors = new HashMap<>();

}