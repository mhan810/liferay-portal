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

package com.liferay.portal.search.elasticsearch6.internal.query2;

import com.liferay.portal.search.query.CustomQuery;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.QueryTranslator;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributor;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributorRegistry;

import org.elasticsearch.index.query.QueryBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CustomQueryTranslator.class)
public class CustomQueryTranslatorImpl implements CustomQueryTranslator {

	@Override
	public QueryBuilder translate(
		CustomQuery customQuery,
		QueryTranslator<QueryBuilder> queryTranslator) {

		Query delegateQuery = customQuery.getDelegate();

		Class<?> queryClass = delegateQuery.getClass();

		try {
			CustomQueryTranslatorContributor<QueryBuilder>
				customQueryTranslatorContributor =
					(CustomQueryTranslatorContributor<QueryBuilder>)
						_customQueryTranslatorContributorRegistry.
							getCustomQueryTranslatorContributor(
								queryClass.getName());

			if (customQueryTranslatorContributor == null) {
				throw new IllegalStateException(
					"No  CustomQueryTranslator found for: " + queryClass);
			}

			return customQueryTranslatorContributor.translate(
				delegateQuery, queryTranslator);
		}
		catch (ClassCastException cce) {
			throw new IllegalArgumentException(
				"No  CustomQueryTranslator found for: " + queryClass, cce);
		}
	}

	@Reference(unbind = "-")
	protected void setCustomQueryTranslatorContributorRegistry(
		CustomQueryTranslatorContributorRegistry
			customQueryTranslatorContributorRegistry) {

		_customQueryTranslatorContributorRegistry =
			customQueryTranslatorContributorRegistry;
	}

	private CustomQueryTranslatorContributorRegistry
		_customQueryTranslatorContributorRegistry;

}