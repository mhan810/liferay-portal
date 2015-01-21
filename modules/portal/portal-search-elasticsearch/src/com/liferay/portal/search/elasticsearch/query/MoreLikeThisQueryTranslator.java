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

import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.liferay.portal.kernel.search.MoreLikeThisQuery;
import com.liferay.portal.kernel.search.Query;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class MoreLikeThisQueryTranslator implements QueryTranslator {

	@Override
	public ElasticsearchQuery translate(Query originalQuery) {

		MoreLikeThisQuery query = (MoreLikeThisQuery)originalQuery;
		
		MoreLikeThisQueryBuilder queryBuilder = 
		QueryBuilders.moreLikeThisQuery(query.getFields())
			.likeText(query.getLikeText())
			.minTermFreq(query.getMinTermFreq())
			.maxQueryTerms(query.getMaxQueryTerms());
		
		String queryString = queryBuilder.toString();

		ElasticsearchQuery elasticsearchQuery = new ElasticsearchQuery(query);
		
		elasticsearchQuery.setQueryBuilder(queryBuilder);
		elasticsearchQuery.setQueryString(queryString);
		
		return elasticsearchQuery;
	}

}