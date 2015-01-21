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

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Query;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
public class BooleanQueryTranslator 
implements QueryTranslator {

	@Override
	public ElasticsearchQuery translate(Query originalQuery) {
		
		BooleanQuery query = (BooleanQuery)originalQuery;
		
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		for (BooleanClause clause : query.clauses()) {
			_addClause(clause, queryBuilder);
		}

		String queryString = queryBuilder.toString();

		ElasticsearchQuery elasticsearchQuery = new ElasticsearchQuery(query);
		
		elasticsearchQuery.setQueryBuilder(queryBuilder);
		elasticsearchQuery.setQueryString(queryString);
		
		return elasticsearchQuery;
	}

	private void _addClause(BooleanClause clause, BoolQueryBuilder boolQuery) {
		QueryBuilder queryBuilder = _toQueryBuilder(clause);

		BooleanClauseOccur occur = clause.getBooleanClauseOccur();

		if (occur.equals(BooleanClauseOccur.MUST)) {
			boolQuery.must(queryBuilder);
			return;
		}

		if (occur.equals(BooleanClauseOccur.MUST_NOT)) {
			boolQuery.mustNot(queryBuilder);
			return;
		}

		if (occur.equals(BooleanClauseOccur.SHOULD)) {
			boolQuery.should(queryBuilder);
			return;
		}

		throw new IllegalArgumentException();
	}

	private QueryBuilder _toQueryBuilder(BooleanClause clause) {
		PortalToElasticsearchQueryTranslator translator =
			new PortalToElasticsearchQueryTranslator();

		ElasticsearchQuery elasticsearchQuery = translator.translate(
			clause.getQuery());

		return elasticsearchQuery.getQueryBuilder();
	}

}