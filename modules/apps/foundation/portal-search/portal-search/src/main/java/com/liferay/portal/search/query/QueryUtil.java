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

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

/**
 * @author Michael C. Han
 */
public class QueryUtil {

	public static void addSearchLocalizedTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws SearchException {

		addSearchTerm(searchQuery, searchContext, field, like);

		String localizedFieldName = Field.getLocalizedName(
			searchContext.getLocale(), field);

		addSearchTerm(searchQuery, searchContext, localizedFieldName, like);
	}

	public static Query addSearchTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws SearchException {

		if (Validator.isNull(field)) {
			return null;
		}

		String value = StringPool.BLANK;

		Serializable serializable = searchContext.getAttribute(field);

		if (serializable != null) {
			Class<?> clazz = serializable.getClass();

			if (clazz.isArray()) {
				value = StringUtil.merge((Object[])serializable);
			}
			else {
				value = GetterUtil.getString(serializable);
			}
		}

		if (Validator.isNotNull(value) &&
			(searchContext.getFacet(field) != null)) {

			return null;
		}

		if (Validator.isNull(value)) {
			value = searchContext.getKeywords();
		}

		if (Validator.isNull(value)) {
			return null;
		}

		Query query = null;

		if (searchContext.isAndSearch()) {
			query = searchQuery.addRequiredTerm(field, value, like);
		}
		else {
			try {
				query = searchQuery.addTerm(field, value, like);
			}
			catch (ParseException pe) {
				throw new SearchException(pe);
			}
		}

		return query;
	}

}