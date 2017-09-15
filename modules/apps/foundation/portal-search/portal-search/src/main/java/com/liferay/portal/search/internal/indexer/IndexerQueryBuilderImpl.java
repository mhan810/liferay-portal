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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.contributor.query.KeywordQueryContributor;
import com.liferay.portal.search.contributor.query.QueryPreFilterContributor;
import com.liferay.portal.search.contributor.query.SearchContextContributor;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;

/**
 * @author Michael C. Han
 */
public class IndexerQueryBuilderImpl<T extends BaseModel>
	implements IndexerQueryBuilder {

	public IndexerQueryBuilderImpl(
		ModelSearchSettings modelSearchSettings,
		Iterable<KeywordQueryContributor> modelKeywordQueryContributors,
		Iterable<QueryPreFilterContributor> modelQueryPreFilterContributor,
		Iterable<SearchContextContributor> modelSearchContextContributor,
		Iterable<KeywordQueryContributor> keywordQueryContributors,
		Iterable<QueryPreFilterContributor> queryPreFilterContributors,
		Iterable<SearchContextContributor> searchContextContributors,
		Iterable<IndexerPostProcessor> indexerPostProcessors,
		RelatedEntryIndexerRegistry relatedEntryIndexerRegistry) {

		_modelSearchSettings = modelSearchSettings;
		_modelKeywordQueryContributors = modelKeywordQueryContributors;
		_modelQueryPreFilterContributor = modelQueryPreFilterContributor;
		_modelSearchContextContributor = modelSearchContextContributor;
		_keywordQueryContributors = keywordQueryContributors;
		_queryPreFilterContributors = queryPreFilterContributors;
		_searchContextContributors = searchContextContributors;
		_indexerPostProcessors = indexerPostProcessors;
		_relatedEntryIndexerRegistry = relatedEntryIndexerRegistry;
	}

	@Override
	public BooleanQuery getQuery(SearchContext searchContext) {

		try {
			searchContext.setSearchEngineId(
				_modelSearchSettings.getSearchEngineId());

			searchContext.clearFullQueryEntryClassNames();

			for (RelatedEntryIndexer relatedEntryIndexer :
					_relatedEntryIndexerRegistry.getRelatedEntryIndexers()) {

				relatedEntryIndexer.updateFullQuery(searchContext);
			}

			_searchContextContributors.forEach(
				searchContextContributor -> searchContextContributor.contribute(
					_modelSearchSettings.getClassName(), _modelSearchSettings,
					searchContext));

			_modelSearchContextContributor.forEach(
				modelSearchContextContributor ->
					modelSearchContextContributor.contribute(
						_modelSearchSettings.getClassName(),
						_modelSearchSettings, searchContext));

			final BooleanFilter fullQueryBooleanFilter = new BooleanFilter();

			contribute(
				_queryPreFilterContributors, searchContext,
				fullQueryBooleanFilter);

			contribute(
				_modelQueryPreFilterContributor, searchContext,
				fullQueryBooleanFilter);

			BooleanQuery fullQuery = createFullQuery(
				fullQueryBooleanFilter, searchContext);

			fullQuery.setQueryConfig(searchContext.getQueryConfig());

			return fullQuery;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected void contribute(
			Iterable<KeywordQueryContributor> keywordQueryContributors,
			SearchContext searchContext, BooleanQuery keywordBooleanQuery,
			String keywords)
		throws SearchException {

		for (KeywordQueryContributor keywordQueryContributor :
				keywordQueryContributors) {

			keywordQueryContributor.contribute(
				_modelSearchSettings.getClassName(), keywordBooleanQuery,
				keywords, searchContext);
		}
	}

	protected void contribute(
			Iterable<QueryPreFilterContributor> queryPreFilterContributors,
			SearchContext searchContext, BooleanFilter fullQueryBooleanFilter)
		throws SearchException {

		for (QueryPreFilterContributor queryPreFilterContributor :
				queryPreFilterContributors) {

			queryPreFilterContributor.contribute(
				fullQueryBooleanFilter, searchContext);
		}
	}

	protected BooleanQuery createFullQuery(
			BooleanFilter fullQueryBooleanFilter, SearchContext searchContext)
		throws Exception {

		final BooleanQuery keywordBooleanQuery = new BooleanQueryImpl();

		final String keywords = searchContext.getKeywords();

		contribute(
			_keywordQueryContributors, searchContext, keywordBooleanQuery,
			keywords);

		contribute(
			_modelKeywordQueryContributors, searchContext, keywordBooleanQuery,
			keywords);

		for (IndexerPostProcessor indexerPostProcessor :
				_indexerPostProcessors) {

			indexerPostProcessor.postProcessSearchQuery(
				keywordBooleanQuery, fullQueryBooleanFilter, searchContext);
		}

		BooleanQuery fullBooleanQuery = new BooleanQueryImpl();

		if (fullQueryBooleanFilter.hasClauses()) {
			fullBooleanQuery.setPreBooleanFilter(fullQueryBooleanFilter);
		}

		if (keywordBooleanQuery.hasClauses()) {
			fullBooleanQuery.add(keywordBooleanQuery, BooleanClauseOccur.MUST);
		}

		BooleanClause<Query>[] booleanClauses =
			searchContext.getBooleanClauses();

		if (booleanClauses != null) {
			for (BooleanClause<Query> booleanClause : booleanClauses) {
				fullBooleanQuery.add(
					booleanClause.getClause(),
					booleanClause.getBooleanClauseOccur());
			}
		}

		for (IndexerPostProcessor indexerPostProcessor :
				_indexerPostProcessors) {

			indexerPostProcessor.postProcessFullQuery(
				fullBooleanQuery, searchContext);
		}

		return fullBooleanQuery;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexerQueryBuilderImpl.class);

	private final Iterable<IndexerPostProcessor> _indexerPostProcessors;
	private final Iterable<KeywordQueryContributor> _keywordQueryContributors;
	private final Iterable<KeywordQueryContributor>
		_modelKeywordQueryContributors;
	private final Iterable<QueryPreFilterContributor>
		_modelQueryPreFilterContributor;
	private final Iterable<SearchContextContributor>
		_modelSearchContextContributor;
	private final ModelSearchSettings _modelSearchSettings;
	private final Iterable<QueryPreFilterContributor>
		_queryPreFilterContributors;
	private final RelatedEntryIndexerRegistry _relatedEntryIndexerRegistry;
	private final Iterable<SearchContextContributor> _searchContextContributors;

}