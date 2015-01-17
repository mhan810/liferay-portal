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

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

/**
 * @author André de Oliveira
 */
public class PortalToElasticsearchQueryTranslator {

	public ElasticsearchQuery translate(Query query) {
		return new ElasticsearchQuery(_translateQuery(query));
	}

	private QueryStringQueryBuilder _fallbackToQueryString(Query query) {
		return QueryBuilders.queryString(query.toString());
	}

	private QueryBuilder _translateBooleanQuery(BooleanQueryImpl booleanQuery) {
		BooleanQueryTranslator booleanQueryTranslator =
			new BooleanQueryTranslator();

		return booleanQueryTranslator.translate(booleanQuery);
	}

	private QueryBuilder _translateQuery(Query query) {
		if (query instanceof BooleanQueryImpl) {
			return _translateBooleanQuery((BooleanQueryImpl)query);
		}

		if (query instanceof TermQueryImpl) {
			return _translateTermQuery((TermQueryImpl)query);
		}

		return _fallbackToQueryString(query);
	}

	private QueryBuilder _translateTermQuery(TermQueryImpl termQuery) {
		TermQueryTranslator termQueryTranslator = new TermQueryTranslator();

		return termQueryTranslator.translate(termQuery);
	}

}