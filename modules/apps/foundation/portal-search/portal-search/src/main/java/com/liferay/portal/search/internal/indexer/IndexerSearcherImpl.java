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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.DefaultSearchResultPermissionFilter;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexSearcherHelper;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.SearchResultPermissionFilter;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.search.hits.HitsProcessorRegistry;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.contributor.query.QueryConfigContributor;
import com.liferay.portal.search.indexer.IndexerPermissionPostFilter;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;
import com.liferay.portal.search.indexer.IndexerSearcher;

/**
 * @author Michael C. Han
 */
public class  IndexerSearcherImpl<T extends BaseModel>
	implements IndexerSearcher {

	public IndexerSearcherImpl(
		ModelSearchSettings modelSearchSettings,
		Iterable<QueryConfigContributor> modelQueryConfigContributors,
		IndexerPermissionPostFilter indexerPermissionPostFilter,
		IndexerQueryBuilder indexerQueryBuilder,
		HitsProcessorRegistry hitsProcessorRegistry,
		IndexSearcherHelper indexSearcherHelper,
		Iterable<QueryConfigContributor> queryConfigContributors) {

		_modelSearchSettings = modelSearchSettings;
		_modelQueryConfigContributors = modelQueryConfigContributors;
		_indexerPermissionPostFilter = indexerPermissionPostFilter;
		_indexerQueryBuilder = indexerQueryBuilder;
		_hitsProcessorRegistry = hitsProcessorRegistry;
		_indexSearcherHelper = indexSearcherHelper;
		_queryConfigContributors = queryConfigContributors;
	}

	@Override
	public Hits search(SearchContext searchContext) throws SearchException {
		try {
			_queryConfigContributors.forEach(
				queryConfigContributor ->
					queryConfigContributor.contributeQueryConfigurations(
						_modelSearchSettings, searchContext));

			_modelQueryConfigContributors.forEach(
				modelQueryConfigContributor ->
					modelQueryConfigContributor.contributeQueryConfigurations(
						_modelSearchSettings, searchContext));

			Hits hits = null;

			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if ((permissionChecker != null) &&
				_indexerPermissionPostFilter.isPermissionAware()) {

				if (searchContext.getUserId() == 0) {
					searchContext.setUserId(permissionChecker.getUserId());
				}

				SearchResultPermissionFilter searchResultPermissionFilter =
					new DefaultSearchResultPermissionFilter(
						this::doSearch, permissionChecker);

				hits = searchResultPermissionFilter.search(searchContext);
			}
			else {
				hits = doSearch(searchContext);
			}

			_hitsProcessorRegistry.process(searchContext, hits);

			return hits;
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public long searchCount(SearchContext searchContext)
		throws SearchException {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		//todo diverges from BaseIndexer b/c we are trying to determine if
		//filtered search is necessary

		if ((permissionChecker != null) &&
			_indexerPermissionPostFilter.isPermissionAware()) {

			Hits hits = search(searchContext);

			return hits.getLength();
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setHitsProcessingEnabled(false);
		queryConfig.setScoreEnabled(false);
		queryConfig.setQueryIndexingEnabled(false);
		queryConfig.setQuerySuggestionEnabled(false);

		searchContext.setSearchEngineId(
			_modelSearchSettings.getSearchEngineId());

		BooleanQuery fullQuery = _indexerQueryBuilder.getQuery(
			searchContext);

		fullQuery.setQueryConfig(queryConfig);

		return _indexSearcherHelper.searchCount(searchContext, fullQuery);
	}

	protected Hits doSearch(SearchContext searchContext)
		throws SearchException {

		searchContext.setSearchEngineId(
			_modelSearchSettings.getSearchEngineId());

		Query fullQuery = _indexerQueryBuilder.getQuery(searchContext);

		if (!fullQuery.hasChildren()) {
			BooleanFilter preBooleanFilter = fullQuery.getPreBooleanFilter();

			fullQuery = new MatchAllQuery();

			fullQuery.setPreBooleanFilter(preBooleanFilter);
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		fullQuery.setQueryConfig(queryConfig);

		return _indexSearcherHelper.search(searchContext, fullQuery);
	}

	private final HitsProcessorRegistry _hitsProcessorRegistry;
	private final IndexerPermissionPostFilter _indexerPermissionPostFilter;
	private final IndexerQueryBuilder _indexerQueryBuilder;
	private final IndexSearcherHelper _indexSearcherHelper;
	private final Iterable<QueryConfigContributor>
		_modelQueryConfigContributors;
	private final ModelSearchSettings _modelSearchSettings;
	private final Iterable<QueryConfigContributor> _queryConfigContributors;

}