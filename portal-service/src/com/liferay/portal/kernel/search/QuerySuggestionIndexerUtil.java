
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.search;

import java.io.InputStream;

import java.util.Locale;

/**
 * @author David Mendez Gonzalez
 */
public class QuerySuggestionIndexerUtil {

	public static void indexQuerySuggestion(SearchContext searchContext)
		throws SearchException {

		_querySuggestionIndexer.indexQuerySuggestion(searchContext);
	}

	public static void indexQuerySuggestions(
			long companyId, long[] groupIds, Locale locale,
			InputStream inputStream)
		throws SearchException {

		_querySuggestionIndexer.indexQuerySuggestions(
			companyId, groupIds, locale, inputStream);
	}

	public void setQuerySuggestionIndexer(
		QuerySuggestionIndexer querySuggestionIndexer) {

		_querySuggestionIndexer = querySuggestionIndexer;
	}

	private static QuerySuggestionIndexer _querySuggestionIndexer;

}