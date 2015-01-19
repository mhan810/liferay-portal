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
import org.elasticsearch.index.query.RangeQueryBuilder;

import com.liferay.portal.kernel.search.generic.TermRangeQueryImpl;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class TermRangeQueryTranslator {

	public RangeQueryBuilder translate(TermRangeQueryImpl query) {

		return QueryBuilders
            .rangeQuery(query.getField())
            .from(query.getLowerTerm())
            .to(query.getUpperTerm())
            .includeLower(query.includesLower())
            .includeUpper(query.includesUpper());
	}

}