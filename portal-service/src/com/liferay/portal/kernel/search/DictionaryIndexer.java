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
public interface DictionaryIndexer {

	public static final String FILTER_TYPE_DICTIONARY = "dictionary";

	public String getUID(
		long companyId, long[] groupIds, Locale locale, String keywords);

	public void indexDictionary(
		SearchContext searchContext, InputStream inputStream)
		throws SearchException;

	public void indexDictionary(
			long companyId, long[] groupIds, Locale locale,
			InputStream inputStream)
		throws SearchException;

}