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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search2;

import com.liferay.portal.search.engine.adapter.search2.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.CountSearchResponse;
import com.liferay.portal.search.engine.adapter.search2.MultisearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.MultisearchSearchResponse;
import com.liferay.portal.search.engine.adapter.search2.SearchRequestExecutor;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	property = "search.engine.impl=Elasticsearch",
	service = SearchRequestExecutor.class
)
public class ElasticsearchSearchRequestExecutor
	implements SearchRequestExecutor {

	@Override
	public CountSearchResponse executeSearchRequest(
		CountSearchRequest countSearchRequest) {

		return null;
	}

	@Override
	public MultisearchSearchResponse executeSearchRequest(
		MultisearchSearchRequest multisearchSearchRequest) {

		return null;
	}

	@Override
	public SearchSearchResponse executeSearchRequest(
		SearchSearchRequest searchSearchRequest) {

		return null;
	}

}