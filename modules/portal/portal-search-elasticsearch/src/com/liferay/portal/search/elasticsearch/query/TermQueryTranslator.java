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

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.TermQuery;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
public class TermQueryTranslator implements QueryTranslator {
	
	@Override
	public ElasticsearchQuery translate(Query originalQuery) {
		
		TermQuery query = (TermQuery)originalQuery;
		
		QueryTerm queryTerm = query.getQueryTerm();

		TermQueryBuilder queryBuilder = 
			QueryBuilders.termQuery(queryTerm.getField(), queryTerm.getValue());
		
		String queryString = queryBuilder.toString();

		ElasticsearchQuery elasticsearchQuery = new ElasticsearchQuery(query);
		
		elasticsearchQuery.setQueryBuilder(queryBuilder);
		elasticsearchQuery.setQueryString(queryString);
		
		return elasticsearchQuery;
	}

}