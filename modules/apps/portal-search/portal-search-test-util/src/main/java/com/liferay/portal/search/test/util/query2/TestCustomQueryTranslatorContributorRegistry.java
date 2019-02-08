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

package com.liferay.portal.search.test.util.query2;

import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributor;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributorRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class TestCustomQueryTranslatorContributorRegistry
	implements CustomQueryTranslatorContributorRegistry {

	public static TestCustomQueryTranslatorContributorRegistry getInstance() {
		return _instance;
	}

	@Override
	public CustomQueryTranslatorContributor<?>
		getCustomQueryTranslatorContributor(String queryClassName) {

		return _customQueryTranslatorContributors.get(queryClassName);
	}

	public void registerCustomQueryTranslatorContributor(
		CustomQueryTranslatorContributor<?> customQueryTranslatorContributor) {

		_customQueryTranslatorContributors.put(
			customQueryTranslatorContributor.getQueryClassName(),
			customQueryTranslatorContributor);
	}

	private static final TestCustomQueryTranslatorContributorRegistry
		_instance = new TestCustomQueryTranslatorContributorRegistry();

	private final Map<String, CustomQueryTranslatorContributor<?>>
		_customQueryTranslatorContributors = new HashMap<>();

}