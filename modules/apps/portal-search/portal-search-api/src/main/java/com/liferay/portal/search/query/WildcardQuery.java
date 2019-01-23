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

package com.liferay.portal.search.query;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringBundler;

/**
 * @author Michael C. Han
 */
@ProviderType
public class WildcardQuery extends BaseQueryImpl implements Query {

	public WildcardQuery(QueryTerm queryTerm) {
		_queryTerm = queryTerm;
	}

	public WildcardQuery(String field, String value) {
		this(new QueryTerm(field, value));
	}

	@Override
	public <T> T accept(QueryVisitor<T> queryVisitor) {
		return queryVisitor.visit(this);
	}

	public QueryTerm getQueryTerm() {
		return _queryTerm;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{className=");

		Class<?> clazz = getClass();

		sb.append(clazz.getSimpleName());

		sb.append(", queryTerm=");
		sb.append(_queryTerm);
		sb.append("}");

		return sb.toString();
	}

	private final QueryTerm _queryTerm;

}