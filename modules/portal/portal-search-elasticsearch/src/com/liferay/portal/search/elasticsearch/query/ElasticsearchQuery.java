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

package com.liferay.portal.search.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;


/**
 * @author André de Oliveira
 * @author Michael C. Han
 */
public class ElasticsearchQuery implements Query {

	public ElasticsearchQuery(Query query) {
		_originalQuery = query;
	}
	
	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}
	
	@Override
	public QueryConfig getQueryConfig() {
		return _originalQuery.getQueryConfig();
	}

	public String getQueryString() {
		return _queryString;
	}

	@Override
	public Object getWrappedQuery() {
		return _originalQuery;
	}
	
	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	
	@Override
	public void setQueryConfig(QueryConfig queryConfig) {
		_originalQuery.setQueryConfig(queryConfig);
	}

	public void setQueryString(String queryString) {
		_queryString = queryString;
	}

	@Override
	public String toString() {
		return _queryString;
	}

	private final Query _originalQuery;
	private QueryBuilder queryBuilder;
	private transient String _queryString;
}