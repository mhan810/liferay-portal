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

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class MoreLikeThisQueryTranslator {

	public MoreLikeThisQueryBuilder translate(MoreLikeThisQuery query) {

		return QueryBuilders.moreLikeThisQuery(query.getFields())
			.likeText(query.getLikeText())
			.minTermFreq(query.getMinTermFreq())
			.maxQueryTerms(query.getMaxQueryTerms());
	}

}