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

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineHelperUtil;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;
import com.liferay.portal.search.permission.SearchPermissionFilterContributor;
import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;
import com.liferay.portal.search.spi.model.query.contributor.QueryPreFilterContributor;
import com.liferay.portal.search.spi.model.query.contributor.SearchContextContributor;
import com.liferay.portal.search.spi.model.query.contributor.helper.KeywordQueryContributorHelper;
import com.liferay.portal.search.spi.model.query.contributor.helper.SearchContextContributorHelper;
import com.liferay.portal.search.spi.model.registrar.ModelSearchSettings;

import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Michael C. Han
 */
public class IndexerQueryBuilderImpl<T extends BaseModel<?>>
	implements IndexerQueryBuilder {

	public IndexerQueryBuilderImpl(
		ModelSearchSettings modelSearchSettings,
		Iterable<KeywordQueryContributor> modelKeywordQueryContributors,
		Iterable<SearchContextContributor> modelSearchContextContributor,
		Iterable<KeywordQueryContributor> keywordQueryContributors,
		Iterable<QueryPreFilterContributor> queryPreFilterContributors,
		Iterable<SearchContextContributor> searchContextContributors,
		Iterable<SearchPermissionFilterContributor>
			searchPermissionFilterContributors,
		IndexerPostProcessorsHolder indexerPostProcessorsHolder,
		RelatedEntryIndexerRegistry relatedEntryIndexerRegistry) {

		_modelSearchSettings = modelSearchSettings;
		_modelKeywordQueryContributors = modelKeywordQueryContributors;
		_modelSearchContextContributors = modelSearchContextContributor;
		_keywordQueryContributors = keywordQueryContributors;
		_queryPreFilterContributors = queryPreFilterContributors;
		_searchContextContributors = searchContextContributors;
		_searchPermissionFilterContributors =
			searchPermissionFilterContributors;
		_indexerPostProcessorsHolder = indexerPostProcessorsHolder;
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

			contributeSearchContext(searchContext);

			final BooleanFilter fullQueryBooleanFilter = new BooleanFilter();

			contributePreFilter(fullQueryBooleanFilter, searchContext);

			BooleanQuery fullQuery = createFullQuery(
				fullQueryBooleanFilter, searchContext);

			fullQuery.setQueryConfig(searchContext.getQueryConfig());

			return fullQuery;
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected void contribute(
		Iterable<KeywordQueryContributor> keywordQueryContributors,
		SearchContext searchContext, BooleanQuery booleanQuery,
		String keywords) {

		for (KeywordQueryContributor keywordQueryContributor :
				keywordQueryContributors) {

			keywordQueryContributor.contribute(
				keywords, booleanQuery,
				new KeywordQueryContributorHelper() {

					@Override
					public String getClassName() {
						return _modelSearchSettings.getClassName();
					}

					@Override
					public Stream<String> getSearchClassNamesStream() {
						return Stream.of(
							_modelSearchSettings.getSearchClassNames());
					}

					@Override
					public SearchContext getSearchContext() {
						return searchContext;
					}

				});
		}
	}

	protected void contribute(
			Iterable<QueryPreFilterContributor> queryPreFilterContributors,
			BooleanFilter fullQueryBooleanFilter, SearchContext searchContext)
		throws Exception {

		for (QueryPreFilterContributor queryPreFilterContributor :
				queryPreFilterContributors) {

			queryPreFilterContributor.contribute(
				fullQueryBooleanFilter, searchContext);
		}
	}

	protected void contributePreFilter(
			BooleanFilter fullQueryBooleanFilter, SearchContext searchContext)
		throws Exception {

		contribute(
			_queryPreFilterContributors, fullQueryBooleanFilter, searchContext);

		Bundle bundle = FrameworkUtil.getBundle(getClass());

		BundleContext bundleContext = bundle.getBundleContext();

		for (String entryClassName : searchContext.getEntryClassNames()) {
			BooleanFilter entryClassNameBooleanFilter = new BooleanFilter();

			entryClassNameBooleanFilter.addRequiredTerm(
				Field.ENTRY_CLASS_NAME, entryClassName);

			_addPermissionFilter(
				entryClassNameBooleanFilter, entryClassName, searchContext);

			ServiceTrackerList
				<QueryPreFilterContributor, QueryPreFilterContributor>
					entryClassNameModelQueryPreFilterContributors =
						ServiceTrackerListFactory.open(
							bundleContext, QueryPreFilterContributor.class,
							"(indexer.class.name=" + entryClassName + ")");

			contribute(
				entryClassNameModelQueryPreFilterContributors,
				entryClassNameBooleanFilter, searchContext);

			fullQueryBooleanFilter.add(
				entryClassNameBooleanFilter, BooleanClauseOccur.SHOULD);

			entryClassNameModelQueryPreFilterContributors.close();
		}
	}

	protected void contributeSearchContext(SearchContext searchContext) {
		SearchContextContributorHelper searchContextContributorHelper =
			new SearchContextContributorHelper() {

				@Override
				public String[] getSearchClassNames() {
					return _modelSearchSettings.getSearchClassNames();
				}

			};

		_searchContextContributors.forEach(
			searchContextContributor -> {
				searchContextContributor.contribute(
					searchContext, searchContextContributorHelper);
			});

		_modelSearchContextContributors.forEach(
			modelSearchContextContributor ->
				modelSearchContextContributor.contribute(
					searchContext, searchContextContributorHelper));
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

		postProcessSearchQuery(
			keywordBooleanQuery, fullQueryBooleanFilter, searchContext);

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

		postProcessFullQuery(fullBooleanQuery, searchContext);

		return fullBooleanQuery;
	}

	protected void postProcessFullQuery(
		BooleanQuery booleanQuery, SearchContext searchContext) {

		Stream<IndexerPostProcessor> stream =
			_indexerPostProcessorsHolder.stream();

		stream.forEach(
			indexerPostProcessor -> {
				try {
					indexerPostProcessor.postProcessFullQuery(
						booleanQuery, searchContext);
				}
				catch (RuntimeException re) {
					throw re;
				}
				catch (Exception e) {
					throw new SystemException(e);
				}
			});
	}

	protected void postProcessSearchQuery(
		BooleanQuery booleanQuery, BooleanFilter booleanFilter,
		SearchContext searchContext) {

		Stream<IndexerPostProcessor> stream =
			_indexerPostProcessorsHolder.stream();

		stream.forEach(
			indexerPostProcessor -> {
				try {
					indexerPostProcessor.postProcessSearchQuery(
						booleanQuery, booleanFilter, searchContext);
				}
				catch (RuntimeException re) {
					throw re;
				}
				catch (Exception e) {
					throw new SystemException(e);
				}
			});
	}

	private void _addPermissionFilter(
			BooleanFilter booleanFilter, String entryClassName,
			SearchContext searchContext)
		throws Exception {

		if (searchContext.getUserId() == 0) {
			return;
		}

		SearchPermissionChecker searchPermissionChecker =
			SearchEngineHelperUtil.getSearchPermissionChecker();

		Optional<String> permissionedEntryClassNameOptional =
			_getPermissionedEntryClassName(entryClassName);

		String permissionedEntryClassName =
			permissionedEntryClassNameOptional.orElse(entryClassName);

		searchPermissionChecker.getPermissionBooleanFilter(
			searchContext.getCompanyId(), searchContext.getGroupIds(),
			searchContext.getUserId(), permissionedEntryClassName,
			booleanFilter, searchContext);
	}

	private Optional<String> _getPermissionedEntryClassName(
		String entryClassName) {

		for (SearchPermissionFilterContributor
				searchPermissionFilterContributor :
					_searchPermissionFilterContributors) {

			Optional<String> permissionedEntryClassNameOptional =
				searchPermissionFilterContributor.
					getParentEntryClassNameOptional(entryClassName);

			if ((permissionedEntryClassNameOptional != null) &&
				permissionedEntryClassNameOptional.isPresent()) {

				return permissionedEntryClassNameOptional;
			}
		}

		return Optional.empty();
	}

	private final IndexerPostProcessorsHolder _indexerPostProcessorsHolder;
	private final Iterable<KeywordQueryContributor> _keywordQueryContributors;
	private final Iterable<KeywordQueryContributor>
		_modelKeywordQueryContributors;
	private final Iterable<SearchContextContributor>
		_modelSearchContextContributors;
	private final ModelSearchSettings _modelSearchSettings;
	private final Iterable<QueryPreFilterContributor>
		_queryPreFilterContributors;
	private final RelatedEntryIndexerRegistry _relatedEntryIndexerRegistry;
	private final Iterable<SearchContextContributor> _searchContextContributors;
	private final Iterable<SearchPermissionFilterContributor>
		_searchPermissionFilterContributors;

}