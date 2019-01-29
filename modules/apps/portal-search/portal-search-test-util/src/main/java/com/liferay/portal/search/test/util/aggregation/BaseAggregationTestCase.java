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

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchResponse;
import com.liferay.portal.search.query.MatchAllQuery;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;

import java.util.Map;

import org.junit.Assert;

/**
 * @author Rafael Praxedes
 */
public abstract class BaseAggregationTestCase extends BaseIndexingTestCase {

	protected <T extends AggregationResult> T search(Aggregation aggregation) {
		SearchSearchResponse searchSearchResponse =
			getSearchEngineAdapter().execute(
				new SearchSearchRequest() {
					{
						putAggregation(aggregation);
						setIndexNames(new String[] {"_all"});
						setQuery(new MatchAllQuery());
						setSelectedFieldNames();
					}
				});

		Map<String, AggregationResult> aggregationResultMap =
			searchSearchResponse.getAggregationResultMap();

		Assert.assertNotNull(aggregationResultMap);

		return (T)aggregationResultMap.get(aggregation.getName());
	}

}