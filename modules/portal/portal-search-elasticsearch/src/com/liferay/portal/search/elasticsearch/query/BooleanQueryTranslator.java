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

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author André de Oliveira
 */
public class BooleanQueryTranslator {

	public BoolQueryBuilder translate(BooleanQueryImpl booleanQuery) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

		for (BooleanClause clause : booleanQuery.clauses()) {
			_addClause(clause, boolQuery);
		}

		return boolQuery;
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