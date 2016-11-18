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

package com.liferay.portal.search.indexer;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;

import java.util.Map;

/**
 * @author Michael C. Han
 */
public interface IndexerHelper {

	public void addSearchAssetCategoryIds(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public Filter addSearchClassTypeIds(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception;

	public void addSearchEntryClassNames(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public Map<String, Query> addSearchExpando(
			BooleanQuery searchQuery, ModelIndexer<?> modelIndexer,
			SearchContext searchContext, String keywords)
		throws Exception;

	public void addSearchFolderId(
		BooleanFilter queryBooleanFilter, SearchContext searchContext);

	public void addSearchGroupId(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public Map<String, Query> addSearchKeywords(
			BooleanQuery searchQuery, ModelIndexer<?> modelIndexer,
			SearchContext searchContext)
		throws Exception;

	public void addSearchLayout(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public Map<String, Query> addSearchLocalizedTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception;

	public Query addSearchTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception;

	public void addSearchUserId(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public void addStatus(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception;

	public void deleteDocument(
			long companyId, long field1, ModelIndexer<?> modelIndexer)
		throws Exception;

	public void deleteDocument(
			long companyId, long field1, String field2,
			ModelIndexer<?> modelIndexer)
		throws Exception;

	public void deleteDocument(
			long companyId, String field1, ModelIndexer<?> modelIndexer)
		throws Exception;

	public void deleteDocument(
			long companyId, String field1, String field2,
			ModelIndexer<?> modelIndexer)
		throws Exception;

	public String getExpandoFieldName(
		SearchContext searchContext, ExpandoBridge expandoBridge,
		String attributeName);

}