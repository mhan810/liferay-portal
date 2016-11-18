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

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.util.ExpandoBridgeFactory;
import com.liferay.expando.kernel.util.ExpandoBridgeIndexer;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexSearcherHelper;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.SearchResultPermissionFilter;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.search.hits.HitsProcessorRegistry;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.ModelIndexer;
import com.liferay.portal.search.indexer.PermissionAwareModelIndexer;
import com.liferay.portal.search.indexer.SortableModelIndexer;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Michael C. Han
 */
public class DefaultIndexer<T> implements Indexer<T> {

	public DefaultIndexer(
		ExpandoBridgeFactory expandoBridgeFactory,
		ExpandoBridgeIndexer expandoBridgeIndexer,
		HitsProcessorRegistry hitsProcessorRegistry,
		IndexerRegistry indexerRegistry,
		IndexSearcherHelper indexSearcherHelper,
		IndexStatusManager indexStatusManager,
		IndexWriterHelper indexWriterHelper, ModelIndexer modelIndexer,
		SearchEngineHelper searchEngineHelper, String searchEngineId,
		SearchPermissionChecker searchPermissionChecker,
		ExpandoColumnLocalService expandoColumnLocalService,
		GroupLocalService groupLocalService) {

		_expandoBridgeFactory = expandoBridgeFactory;
		_expandoBridgeIndexer = expandoBridgeIndexer;
		_hitsProcessorRegistry = hitsProcessorRegistry;
		_indexerRegistry = indexerRegistry;
		_indexStatusManager = indexStatusManager;
		_indexSearchHelper = indexSearcherHelper;
		_indexWriterHelper = indexWriterHelper;
		_modelIndexer = modelIndexer;
		_searchEngineHelper = searchEngineHelper;
		_searchEngineId = searchEngineId;
		_searchPermissionChecker = searchPermissionChecker;
		_expandoColumnLocalService = expandoColumnLocalService;
		_groupLocalService = groupLocalService;
	}

	@Override
	public void delete(long companyId, String uid) throws SearchException {
		try {
			_indexWriterHelper.deleteDocument(
				getSearchEngineId(), companyId, uid, _commitImmediately);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public void delete(T object) throws SearchException {
		if (object == null) {
			return;
		}

		try {
			_modelIndexer.delete(object);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}

		if (!(object instanceof Indexer<?>)) {
			return false;
		}

		Indexer<?> indexer = (Indexer<?>)object;

		return Objects.equals(getClassName(), indexer.getClassName());
	}

	@Override
	public String getClassName() {
		return _modelIndexer.getClassName();
	}

	/**
	 * @deprecated As of 3.4.0, replaced by {@link #getSearchClassNames}
	 */
	@Deprecated
	@Override
	public String[] getClassNames() {
		return getSearchClassNames();
	}

	@Override
	public Document getDocument(T object) throws SearchException {
		try {
			Document document = _modelIndexer.getDocument(object);

			for (IndexerPostProcessor indexerPostProcessor :
					_indexerPostProcessors) {

				indexerPostProcessor.postProcessDocument(document, object);
			}

			if (document == null) {
				return null;
			}

			Map<String, Field> fields = document.getFields();

			Field groupIdField = fields.get(Field.GROUP_ID);

			if (groupIdField != null) {
				long groupId = GetterUtil.getLong(groupIdField.getValue());

				addStagingGroupKeyword(document, groupId);
			}

			return document;
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public BooleanFilter getFacetBooleanFilter(
			String className, SearchContext searchContext)
		throws Exception {

		BooleanFilter facetBooleanFilter = new BooleanFilter();

		facetBooleanFilter.addTerm(Field.ENTRY_CLASS_NAME, className);

		if (searchContext.getUserId() > 0) {
			facetBooleanFilter =
				_searchPermissionChecker.getPermissionBooleanFilter(
					searchContext.getCompanyId(), searchContext.getGroupIds(),
					searchContext.getUserId(), className, facetBooleanFilter,
					searchContext);
		}

		return facetBooleanFilter;
	}

	@Override
	public BooleanQuery getFullQuery(SearchContext searchContext)
		throws SearchException {

		try {
			searchContext.setSearchEngineId(getSearchEngineId());

			resetFullQuery(searchContext);

			String[] fullQueryEntryClassNames =
				searchContext.getFullQueryEntryClassNames();

			if (ArrayUtil.isNotEmpty(fullQueryEntryClassNames)) {
				searchContext.setAttribute(
					"relatedEntryClassNames", getSearchClassNames());
			}

			String[] entryClassNames = ArrayUtil.append(
				getSearchClassNames(), fullQueryEntryClassNames);

			searchContext.setEntryClassNames(entryClassNames);

			BooleanFilter fullQueryBooleanFilter = new BooleanFilter();

			addSearchAssetCategoryIds(fullQueryBooleanFilter, searchContext);
			addSearchAssetTagNames(fullQueryBooleanFilter, searchContext);
			addSearchEntryClassNames(fullQueryBooleanFilter, searchContext);
			addSearchFolderId(fullQueryBooleanFilter, searchContext);
			addSearchGroupId(fullQueryBooleanFilter, searchContext);
			addSearchLayout(fullQueryBooleanFilter, searchContext);
			addSearchUserId(fullQueryBooleanFilter, searchContext);

			BooleanQuery fullQuery = createFullQuery(
				fullQueryBooleanFilter, searchContext);

			fullQuery.setQueryConfig(searchContext.getQueryConfig());

			return fullQuery;
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public IndexerPostProcessor[] getIndexerPostProcessors() {
		return _indexerPostProcessors;
	}

	/**
	 * @deprecated As of 3.4.0, replaced by {@link #getClassName}
	 */
	@Deprecated
	@Override
	public String getPortletId() {
		return StringPool.BLANK;
	}

	@Override
	public String[] getSearchClassNames() {
		return new String[] {getClassName()};
	}

	@Override
	public String getSearchEngineId() {
		if (_searchEngineId != null) {
			return _searchEngineId;
		}

		Class<?> clazz = getClass();

		String searchEngineId = GetterUtil.getString(
			PropsUtil.get(
				PropsKeys.INDEX_SEARCH_ENGINE_ID,
				new com.liferay.portal.kernel.configuration.Filter(
					clazz.getName())));

		if (Validator.isNotNull(searchEngineId)) {
			SearchEngine searchEngine = _searchEngineHelper.getSearchEngine(
				searchEngineId);

			if (searchEngine != null) {
				_searchEngineId = searchEngineId;
			}
		}

		if (_searchEngineId == null) {
			_searchEngineId = _searchEngineHelper.getDefaultSearchEngineId();
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Search engine ID for " + clazz.getName() + " is " +
					searchEngineId);
		}

		return _searchEngineId;
	}

	@Override
	public String getSortField(String orderByCol) {
		if (_modelIndexer instanceof SortableModelIndexer) {
			SortableModelIndexer sortableModelIndexer =
				(SortableModelIndexer)_modelIndexer;

			String sortField = sortableModelIndexer.getSortField(orderByCol);

			if (_document.isDocumentSortableTextField(sortField)) {
				return DocumentImpl.getSortableFieldName(sortField);
			}
		}

		return orderByCol;
	}

	@Override
	public String getSortField(String orderByCol, int sortType) {
		if ((sortType == Sort.DOUBLE_TYPE) || (sortType == Sort.FLOAT_TYPE) ||
			(sortType == Sort.INT_TYPE) || (sortType == Sort.LONG_TYPE)) {

			return DocumentImpl.getSortableFieldName(orderByCol);
		}

		return getSortField(orderByCol);
	}

	/**
	 * @deprecated As of 3.4.0, replaced by {@link #getSummary(Document, String,
	 *             PortletRequest, PortletResponse)}
	 */
	@Deprecated
	@Override
	public Summary getSummary(Document document, Locale locale, String snippet)
		throws SearchException {

		return getSummary(document, snippet, null, null);
	}

	@Override
	public Summary getSummary(
			Document document, String snippet, PortletRequest portletRequest,
			PortletResponse portletResponse)
		throws SearchException {

		try {
			Locale locale = getLocale(portletRequest);

			Summary summary = _modelIndexer.getSummary(
				document, locale, snippet, portletRequest, portletResponse);

			for (IndexerPostProcessor indexerPostProcessor :
					_indexerPostProcessors) {

				indexerPostProcessor.postProcessSummary(
					summary, document, locale, snippet);
			}

			return summary;
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, getClassName());
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, String entryClassName,
			long entryClassPK, String actionId)
		throws Exception {

		if (_modelIndexer instanceof PermissionAwareModelIndexer) {
			PermissionAwareModelIndexer permissionAwareModelIndexer =
				(PermissionAwareModelIndexer)_modelIndexer;

			return permissionAwareModelIndexer.hasPermission(
				permissionChecker, entryClassName, entryClassPK, actionId);
		}

		return true;
	}

	@Override
	public boolean isCommitImmediately() {
		return _commitImmediately;
	}

	@Override
	public boolean isFilterSearch() {
		return _filterSearch;
	}

	@Override
	public boolean isIndexerEnabled() {
		String className = getClassName();

		if (_indexerEnabled == null) {
			String indexerEnabled = PropsUtil.get(
				PropsKeys.INDEXER_ENABLED,
				new com.liferay.portal.kernel.configuration.Filter(className));

			_indexerEnabled = GetterUtil.getBoolean(indexerEnabled, true);

			return _indexerEnabled;
		}

		return _indexerEnabled;
	}

	@Override
	public boolean isPermissionAware() {
		return _permissionAware;
	}

	public boolean isSelectAllLocales() {
		return _selectAllLocales;
	}

	@Override
	public boolean isStagingAware() {
		return _stagingAware;
	}

	@Override
	public boolean isVisible(long classPK, int status) throws Exception {
		if (_modelIndexer instanceof PermissionAwareModelIndexer) {
			PermissionAwareModelIndexer permissionAwareModelIndexer =
				(PermissionAwareModelIndexer)_modelIndexer;

			return permissionAwareModelIndexer.isVisible(classPK, status);
		}

		return true;
	}

	@Override
	public boolean isVisibleRelatedEntry(long classPK, int status)
		throws Exception {

		if (_modelIndexer instanceof PermissionAwareModelIndexer) {
			PermissionAwareModelIndexer permissionAwareModelIndexer =
				(PermissionAwareModelIndexer)_modelIndexer;

			return permissionAwareModelIndexer.isVisibleRelatedEntry(
				classPK, status);
		}

		return true;
	}

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		_modelIndexer.postProcessContextBooleanFilter(
			contextBooleanFilter, searchContext);
	}

	/**
	 * @deprecated As of 3.4.0, replaced by {@link
	 *             #postProcessContextBooleanFilter(BooleanFilter,
	 *             SearchContext)}
	 */
	@Deprecated
	@Override
	public void postProcessContextQuery(
			BooleanQuery contextQuery, SearchContext searchContext)
		throws Exception {
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		String keywords = searchContext.getKeywords();

		if (Validator.isNull(keywords)) {
			addSearchTerm(searchQuery, searchContext, Field.DESCRIPTION, false);
			addSearchTerm(searchQuery, searchContext, Field.TITLE, false);
			addSearchTerm(searchQuery, searchContext, Field.USER_NAME, false);
		}
	}

	/**
	 * @deprecated As of 3.4.0, replaced by {@link
	 *             #postProcessSearchQuery(BooleanQuery, BooleanFilter,
	 *             SearchContext)}
	 */
	@Deprecated
	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {
	}

	@Override
	public void registerIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		List<IndexerPostProcessor> indexerPostProcessorsList =
			ListUtil.fromArray(_indexerPostProcessors);

		indexerPostProcessorsList.add(indexerPostProcessor);

		_indexerPostProcessors = indexerPostProcessorsList.toArray(
			new IndexerPostProcessor[indexerPostProcessorsList.size()]);
	}

	@Override
	public void reindex(Collection<T> collection) {
		if (_indexStatusManager.isIndexReadOnly() || !isIndexerEnabled() ||
			collection.isEmpty()) {

			return;
		}

		for (T element : collection) {
			try {
				reindex(element);
			}
			catch (SearchException se) {
				_log.error("Unable to index object: " + element, se);
			}
		}
	}

	@Override
	public void reindex(String className, long classPK) throws SearchException {
		try {
			if (_indexStatusManager.isIndexReadOnly() || !isIndexerEnabled() ||
				(classPK <= 0)) {

				return;
			}

			_modelIndexer.reindex(className, classPK);
		}
		catch (NoSuchModelException nsme) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to index " + className + " " + classPK, nsme);
			}
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public void reindex(String[] ids) throws SearchException {
		long companyThreadLocalCompanyId = CompanyThreadLocal.getCompanyId();

		try {
			if (_indexStatusManager.isIndexReadOnly() || !isIndexerEnabled()) {
				return;
			}

			if (ids.length > 0) {
				long companyId = GetterUtil.getLong(ids[0]);

				CompanyThreadLocal.setCompanyId(companyId);
			}

			_modelIndexer.reindex(ids);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
		finally {
			CompanyThreadLocal.setCompanyId(companyThreadLocalCompanyId);
		}
	}

	@Override
	public void reindex(T object) throws SearchException {
		try {
			if (_indexStatusManager.isIndexReadOnly() || !isIndexerEnabled()) {
				return;
			}

			if (object == null) {
				return;
			}

			_modelIndexer.reindex(object);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public Hits search(SearchContext searchContext) throws SearchException {
		try {
			Hits hits = null;

			QueryConfig queryConfig = searchContext.getQueryConfig();

			addDefaultHighlightFieldNames(queryConfig);

			if (ArrayUtil.isEmpty(queryConfig.getSelectedFieldNames())) {
				addDefaultSelectedFieldNames(searchContext);
			}

			addFacetSelectedFieldNames(searchContext, queryConfig);

			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if ((permissionChecker != null) && isFilterSearch()) {
				if (searchContext.getUserId() == 0) {
					searchContext.setUserId(permissionChecker.getUserId());
				}

				SearchResultPermissionFilter searchResultPermissionFilter =
					new DefaultSearchResultPermissionFilter(
						this, permissionChecker);

				hits = searchResultPermissionFilter.search(searchContext);
			}
			else {
				hits = doSearch(searchContext);
			}

			processHits(searchContext, hits);

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
	public Hits search(
			SearchContext searchContext, String... selectedFieldNames)
		throws SearchException {

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSelectedFieldNames(selectedFieldNames);

		return search(searchContext);
	}

	@Override
	public long searchCount(SearchContext searchContext)
		throws SearchException {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if ((permissionChecker != null) && isFilterSearch()) {
			Hits hits = search(searchContext);

			return hits.getLength();
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setHitsProcessingEnabled(false);
		queryConfig.setScoreEnabled(false);
		queryConfig.setQueryIndexingEnabled(false);
		queryConfig.setQuerySuggestionEnabled(false);

		searchContext.setSearchEngineId(getSearchEngineId());

		BooleanQuery fullQuery = getFullQuery(searchContext);

		fullQuery.setQueryConfig(queryConfig);

		return _indexSearchHelper.searchCount(searchContext, fullQuery);
	}

	public void setCommitImmediately(boolean commitImmediately) {
		_commitImmediately = commitImmediately;
	}

	@Override
	public void setIndexerEnabled(boolean indexerEnabled) {
		_indexerEnabled = indexerEnabled;
	}

	public void setSelectAllLocales(boolean selectAllLocales) {
		_selectAllLocales = selectAllLocales;
	}

	@Override
	public void unregisterIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		List<IndexerPostProcessor> indexerPostProcessorsList =
			ListUtil.fromArray(_indexerPostProcessors);

		indexerPostProcessorsList.remove(indexerPostProcessor);

		_indexerPostProcessors = indexerPostProcessorsList.toArray(
			new IndexerPostProcessor[indexerPostProcessorsList.size()]);
	}

	protected void addDefaultHighlightFieldNames(QueryConfig queryConfig) {
		queryConfig.addHighlightFieldNames(Field.ASSET_CATEGORY_TITLES);

		if (queryConfig.isHighlightEnabled()) {
			queryConfig.addHighlightFieldNames(
				Field.CONTENT, Field.DESCRIPTION, Field.TITLE);
		}
	}

	protected void addDefaultSelectedFieldNames(SearchContext searchContext) {
		QueryConfig queryConfig = searchContext.getQueryConfig();

		Set<String> selectedFieldNames = null;

		if (!ArrayUtil.isEmpty(getDefaultSelectedFieldNames())) {
			selectedFieldNames = SetUtil.fromArray(
				getDefaultSelectedFieldNames());

			if (searchContext.isIncludeAttachments() ||
				searchContext.isIncludeDiscussions()) {

				selectedFieldNames.add(Field.CLASS_NAME_ID);
				selectedFieldNames.add(Field.CLASS_PK);
			}
		}

		if (!ArrayUtil.isEmpty(getDefaultSelectedLocalizedFieldNames())) {
			if (selectedFieldNames == null) {
				selectedFieldNames = new HashSet<>();
			}

			if (isSelectAllLocales()) {
				addSelectedLocalizedFieldNames(
					selectedFieldNames,
					LocaleUtil.toLanguageIds(
						LanguageUtil.getSupportedLocales()));
			}
			else {
				addSelectedLocalizedFieldNames(
					selectedFieldNames,
					LocaleUtil.toLanguageId(queryConfig.getLocale()));
			}
		}

		if ((selectedFieldNames != null) && !selectedFieldNames.isEmpty()) {
			queryConfig.setSelectedFieldNames(
				selectedFieldNames.toArray(
					new String[selectedFieldNames.size()]));
		}
	}

	/**
	 * @deprecated As of 3.4.0
	 */
	@Deprecated
	protected void addFacetClause(
			SearchContext searchContext, BooleanFilter facetBooleanFilter,
			Collection<Facet> facets)
		throws ParseException {
	}

	protected void addFacetSelectedFieldNames(
		SearchContext searchContext, QueryConfig queryConfig) {

		String[] selectedFieldNames = queryConfig.getSelectedFieldNames();

		if (ArrayUtil.isEmpty(selectedFieldNames) ||
			(selectedFieldNames.length == 1) &&
			selectedFieldNames[0].equals(Field.ANY)) {

			return;
		}

		Set<String> selectedFieldNameSet = SetUtil.fromArray(
			selectedFieldNames);

		Map<String, Facet> facets = searchContext.getFacets();

		selectedFieldNameSet.addAll(facets.keySet());

		selectedFieldNames = selectedFieldNameSet.toArray(
			new String[selectedFieldNameSet.size()]);

		queryConfig.setSelectedFieldNames(selectedFieldNames);
	}

	protected void addSearchAssetCategoryIds(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.ASSET_CATEGORY_IDS);
		multiValueFacet.setStatic(true);
		multiValueFacet.setValues(searchContext.getAssetCategoryIds());

		searchContext.addFacet(multiValueFacet);
	}

	protected void addSearchAssetTagNames(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.ASSET_TAG_NAMES);
		multiValueFacet.setStatic(true);
		multiValueFacet.setValues(searchContext.getAssetTagNames());

		searchContext.addFacet(multiValueFacet);
	}

	protected void addSearchEntryClassNames(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		Facet facet = new AssetEntriesFacet(searchContext);

		facet.setStatic(true);

		searchContext.addFacet(facet);
	}

	protected Map<String, Query> addSearchExpando(
			BooleanQuery searchQuery, SearchContext searchContext,
			String keywords)
		throws Exception {

		Map<String, Query> expandoQueries = new HashMap<>();

		ExpandoBridge expandoBridge = _expandoBridgeFactory.getExpandoBridge(
			searchContext.getCompanyId(), getClassName());

		Set<String> attributeNames = SetUtil.fromEnumeration(
			expandoBridge.getAttributeNames());

		for (String attributeName : attributeNames) {
			UnicodeProperties properties = expandoBridge.getAttributeProperties(
				attributeName);

			int indexType = GetterUtil.getInteger(
				properties.getProperty(ExpandoColumnConstants.INDEX_TYPE));

			if ((indexType != ExpandoColumnConstants.INDEX_TYPE_NONE) &&
				Validator.isNotNull(keywords)) {

				String fieldName = getExpandoFieldName(
					searchContext, expandoBridge, attributeName);

				boolean like = false;

				if (indexType == ExpandoColumnConstants.INDEX_TYPE_TEXT) {
					like = true;
				}

				if (searchContext.isAndSearch()) {
					Query query = searchQuery.addRequiredTerm(
						fieldName, keywords, like);

					expandoQueries.put(attributeName, query);
				}
				else {
					Query query = searchQuery.addTerm(
						fieldName, keywords, like);

					expandoQueries.put(attributeName, query);
				}
			}
		}

		return expandoQueries;
	}

	protected void addSearchFolderId(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.TREE_PATH);
		multiValueFacet.setStatic(true);

		long[] folderIds = searchContext.getFolderIds();

		if (ArrayUtil.isNotEmpty(folderIds)) {
			folderIds = ArrayUtil.remove(folderIds, _DEFAULT_FOLDER_ID);

			multiValueFacet.setValues(folderIds);
		}

		searchContext.addFacet(multiValueFacet);
	}

	protected void addSearchGroupId(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		Facet facet = new ScopeFacet(searchContext);

		facet.setStatic(true);

		searchContext.addFacet(facet);
	}

	protected Map<String, Query> addSearchKeywords(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {

		String keywords = searchContext.getKeywords();

		if (Validator.isNull(keywords)) {
			return Collections.emptyMap();
		}

		addSearchExpando(searchQuery, searchContext, keywords);

		addSearchLocalizedTerm(
			searchQuery, searchContext, Field.ASSET_CATEGORY_TITLES,
			searchContext.isLike());

		return searchQuery.addTerms(
			Field.KEYWORDS, keywords, searchContext.isLike());
	}

	protected void addSearchLayout(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.LAYOUT_UUID);
		multiValueFacet.setStatic(true);

		searchContext.addFacet(multiValueFacet);
	}

	protected Map<String, Query> addSearchLocalizedTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception {

		Map<String, Query> queries = new HashMap<>();

		Query query = addSearchTerm(searchQuery, searchContext, field, like);

		queries.put(field, query);

		String localizedFieldName = DocumentImpl.getLocalizedName(
			searchContext.getLocale(), field);

		Query localizedQuery = addSearchTerm(
			searchQuery, searchContext, localizedFieldName, like);

		queries.put(localizedFieldName, localizedQuery);

		return queries;
	}

	protected Query addSearchTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception {

		if (Validator.isNull(field)) {
			return null;
		}

		String value = null;

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
		else {
			value = GetterUtil.getString(serializable);
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
			query = searchQuery.addTerm(field, value, like);
		}

		return query;
	}

	protected void addSearchUserId(
			BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.USER_ID);
		multiValueFacet.setStatic(true);

		long userId = GetterUtil.getLong(
			searchContext.getAttribute(Field.USER_ID));

		if (userId > 0) {
			multiValueFacet.setValues(new long[] {userId});
		}

		searchContext.addFacet(multiValueFacet);
	}

	protected void addSelectedLocalizedFieldNames(
		Set<String> selectedFieldNames, String... languageIds) {

		for (String defaultLocalizedSelectedFieldName :
				getDefaultSelectedLocalizedFieldNames()) {

			selectedFieldNames.add(defaultLocalizedSelectedFieldName);

			for (String languageId : languageIds) {
				String localizedFieldName = LocalizationUtil.getLocalizedName(
					defaultLocalizedSelectedFieldName, languageId);

				selectedFieldNames.add(localizedFieldName);
			}
		}
	}

	protected void addStagingGroupKeyword(Document document, long groupId) {
		if (!isStagingAware()) {
			return;
		}

		document.addKeyword(Field.STAGING_GROUP, isStagingGroup(groupId));
	}

	protected BooleanQuery createFullQuery(
			BooleanFilter fullQueryBooleanFilter, SearchContext searchContext)
		throws Exception {

		BooleanQuery searchQuery = new BooleanQueryImpl();

		addSearchKeywords(searchQuery, searchContext);
		postProcessSearchQuery(
			searchQuery, fullQueryBooleanFilter, searchContext);

		for (IndexerPostProcessor indexerPostProcessor :
				_indexerPostProcessors) {

			indexerPostProcessor.postProcessSearchQuery(
				searchQuery, fullQueryBooleanFilter, searchContext);
		}

		doPostProcessSearchQuery(this, searchQuery, searchContext);

		Map<String, Facet> facets = searchContext.getFacets();

		BooleanFilter facetBooleanFilter = new BooleanFilter();

		for (Facet facet : facets.values()) {
			BooleanClause<Filter> filterBooleanClause =
				facet.getFacetFilterBooleanClause();

			if (filterBooleanClause != null) {
				facetBooleanFilter.add(
					filterBooleanClause.getClause(),
					filterBooleanClause.getBooleanClauseOccur());
			}
		}

		addFacetClause(searchContext, facetBooleanFilter, facets.values());

		if (facetBooleanFilter.hasClauses()) {
			fullQueryBooleanFilter.add(
				facetBooleanFilter, BooleanClauseOccur.MUST);
		}

		BooleanQuery fullBooleanQuery = new BooleanQueryImpl();

		if (fullQueryBooleanFilter.hasClauses()) {
			fullBooleanQuery.setPreBooleanFilter(fullQueryBooleanFilter);
		}

		if (searchQuery.hasClauses()) {
			fullBooleanQuery.add(searchQuery, BooleanClauseOccur.MUST);
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

		for (IndexerPostProcessor indexerPostProcessor :
				_indexerPostProcessors) {

			indexerPostProcessor.postProcessFullQuery(
				fullBooleanQuery, searchContext);
		}

		return fullBooleanQuery;
	}

	/**
	 * @deprecated As of 3.4.0, added strictly to support backwards
	 *             compatibility of {@link
	 *             Indexer#postProcessSearchQuery(BooleanQuery, SearchContext)}
	 */
	@Deprecated
	protected void doPostProcessSearchQuery(
			Indexer<?> indexer, BooleanQuery searchQuery,
			SearchContext searchContext)
		throws Exception {
	}

	protected Hits doSearch(SearchContext searchContext)
		throws SearchException {

		searchContext.setSearchEngineId(getSearchEngineId());

		Query fullQuery = getFullQuery(searchContext);

		if (!fullQuery.hasChildren()) {
			BooleanFilter preBooleanFilter = fullQuery.getPreBooleanFilter();

			fullQuery = new MatchAllQuery();

			fullQuery.setPreBooleanFilter(preBooleanFilter);
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		fullQuery.setQueryConfig(queryConfig);

		return _indexSearchHelper.search(searchContext, fullQuery);
	}

	protected String[] getDefaultSelectedFieldNames() {
		return _defaultSelectedFieldNames;
	}

	protected String[] getDefaultSelectedLocalizedFieldNames() {
		return _defaultSelectedLocalizedFieldNames;
	}

	protected String getExpandoFieldName(
		SearchContext searchContext, ExpandoBridge expandoBridge,
		String attributeName) {

		ExpandoColumn expandoColumn =
			_expandoColumnLocalService.getDefaultTableColumn(
				expandoBridge.getCompanyId(), expandoBridge.getClassName(),
				attributeName);

		String fieldName = _expandoBridgeIndexer.encodeFieldName(attributeName);

		if (expandoColumn.getType() ==
				ExpandoColumnConstants.STRING_LOCALIZED) {

			fieldName = DocumentImpl.getLocalizedName(
				searchContext.getLocale(), fieldName);
		}

		return fieldName;
	}

	protected Locale getLocale(PortletRequest portletRequest) {
		if (portletRequest != null) {
			return portletRequest.getLocale();
		}

		return LocaleUtil.getMostRelevantLocale();
	}

	protected Group getSiteGroup(long groupId) {
		Group group = null;

		try {
			group = _groupLocalService.getGroup(groupId);

			if (group.isLayout()) {
				group = group.getParentGroup();
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get site group", pe);
			}
		}

		return group;
	}

	protected boolean isStagingGroup(long groupId) {
		Group group = getSiteGroup(groupId);

		if (group == null) {
			return false;
		}

		return group.isStagingGroup();
	}

	protected void postProcessFullQuery(
			BooleanQuery fullQuery, SearchContext searchContext)
		throws Exception {
	}

	protected void processHits(SearchContext searchContext, Hits hits)
		throws SearchException {

		_hitsProcessorRegistry.process(searchContext, hits);
	}

	protected void resetFullQuery(SearchContext searchContext) {
		searchContext.clearFullQueryEntryClassNames();

		for (Indexer<?> indexer : _indexerRegistry.getIndexers()) {
			if (indexer instanceof RelatedEntryIndexer) {
				RelatedEntryIndexer relatedEntryIndexer =
					(RelatedEntryIndexer)indexer;

				relatedEntryIndexer.updateFullQuery(searchContext);
			}
		}
	}

	protected void setDefaultSelectedFieldNames(
		String... defaultLocalizedFieldNames) {

		_defaultSelectedFieldNames = defaultLocalizedFieldNames;
	}

	protected void setDefaultSelectedLocalizedFieldNames(
		String... defaultLocalizedFieldNames) {

		_defaultSelectedLocalizedFieldNames = defaultLocalizedFieldNames;
	}

	protected void setFilterSearch(boolean filterSearch) {
		_filterSearch = filterSearch;
	}

	protected void setPermissionAware(boolean permissionAware) {
		_permissionAware = permissionAware;
	}

	protected void setStagingAware(boolean stagingAware) {
		_stagingAware = stagingAware;
	}

	private static final long _DEFAULT_FOLDER_ID = 0L;

	private static final Log _log = LogFactoryUtil.getLog(DefaultIndexer.class);

	private boolean _commitImmediately;
	private String[] _defaultSelectedFieldNames;
	private String[] _defaultSelectedLocalizedFieldNames;
	private final Document _document = new DocumentImpl();
	private final ExpandoBridgeFactory _expandoBridgeFactory;
	private final ExpandoBridgeIndexer _expandoBridgeIndexer;
	private final ExpandoColumnLocalService _expandoColumnLocalService;
	private boolean _filterSearch;
	private final GroupLocalService _groupLocalService;
	private final HitsProcessorRegistry _hitsProcessorRegistry;
	private Boolean _indexerEnabled;
	private IndexerPostProcessor[] _indexerPostProcessors =
		new IndexerPostProcessor[0];
	private final IndexerRegistry _indexerRegistry;
	private final IndexSearcherHelper _indexSearchHelper;
	private final IndexStatusManager _indexStatusManager;
	private final IndexWriterHelper _indexWriterHelper;
	private final ModelIndexer<T> _modelIndexer;
	private boolean _permissionAware;
	private final SearchEngineHelper _searchEngineHelper;
	private String _searchEngineId;
	private final SearchPermissionChecker _searchPermissionChecker;
	private boolean _selectAllLocales;
	private boolean _stagingAware = true;

}