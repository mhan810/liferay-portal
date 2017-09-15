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
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;
import com.liferay.portal.search.indexer.IndexerPermissionPostFilter;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;
import com.liferay.portal.search.indexer.IndexerSearcher;
import com.liferay.portal.search.indexer.IndexerSummaryBuilder;
import com.liferay.portal.search.indexer.IndexerWriter;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Michael C. Han
 */
public class DefaultIndexer<T extends BaseModel> implements Indexer<T> {

	public DefaultIndexer(
		ModelSearchSettings modelSearchSettings,
		IndexerDocumentBuilder indexerDocumentBuilder,
		IndexerSearcher indexerSearcher, IndexerWriter<T> indexerWriter,
		IndexerPermissionPostFilter indexerPermissionPostFilter,
		IndexerQueryBuilder indexerQueryBuilder,
		IndexerSummaryBuilder indexerSummaryBuilder) {

		_modelSearchSettings = modelSearchSettings;
		_indexerDocumentBuilder = indexerDocumentBuilder;
		_indexerSearcher = indexerSearcher;
		_indexerWriter = indexerWriter;
		_indexerPermissionPostFilter = indexerPermissionPostFilter;
		_indexerQueryBuilder = indexerQueryBuilder;
		_indexerSummaryBuilder = indexerSummaryBuilder;
	}

	@Override
	public void delete(long companyId, String uid) throws SearchException {
		_indexerWriter.delete(companyId, uid);
	}

	@Override
	public void delete(T baseModel) throws SearchException {
		_indexerWriter.delete(baseModel);
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
		return _modelSearchSettings.getClassName();
	}

	/**
	 * @deprecated As of 3.8.0, replaced by {@link #getSearchClassNames}
	 */
	@Deprecated
	@Override
	public String[] getClassNames() {
		return getSearchClassNames();
	}

	@Override
	public Document getDocument(T baseModel) throws SearchException {
		return _indexerDocumentBuilder.getDocument(baseModel);
	}

	@Override
	public BooleanFilter getFacetBooleanFilter(
			String className, SearchContext searchContext)
		throws Exception {

		//todo WILL BE REPLACED

		return null;
	}

	@Override
	public BooleanQuery getFullQuery(SearchContext searchContext)
		throws SearchException {

		return _indexerQueryBuilder.getQuery(searchContext);
	}

	@Override
	public IndexerPostProcessor[] getIndexerPostProcessors() {
		return _modelSearchSettings.getIndexerPostProcessorsArray();
	}

	/**
	 * @deprecated As of 3.8.0
	 * @return
	 */
	@Deprecated
	@Override
	public String getPortletId() {
		return StringPool.BLANK;
	}

	@Override
	public String[] getSearchClassNames() {
		return _modelSearchSettings.getSearchClassNames();
	}

	@Override
	public String getSearchEngineId() {
		//todo WE PROBABLY WANT TO DROP THIS FEATURE

		return SearchEngineHelper.SYSTEM_ENGINE_ID;
	}

	/**
	 * @deprecated As of 7.1.0
	 * @param orderByCol
	 * @return
	 */
	@Deprecated
	@Override
	public String getSortField(String orderByCol) {
		//todo NO NEED TO IMPLEMENT

		return StringPool.BLANK;
	}

	/**
	 * @deprecated As of 7.1.0
	 * @param orderByCol
	 * @param sortType
	 * @return
	 */
	@Deprecated
	@Override
	public String getSortField(String orderByCol, int sortType) {
		//todo NO NEED TO IMPLEMENT

		return StringPool.BLANK;
	}

	/**
	 * @deprecated since 7.0.0
	 * @param document
	 * @param locale
	 * @param snippet
	 * @return
	 * @throws SearchException
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

		return _indexerSummaryBuilder.getSummary(
			document, snippet, portletRequest, portletResponse);
	}

	@Override
	public int hashCode() {
		String[] searchClassNames = getSearchClassNames();

		StringBundler sb = new StringBundler(searchClassNames.length);

		for (String searchClassName : searchClassNames) {
			sb.append(searchClassName);
		}

		return HashUtil.hash(0, sb.toString());
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, String entryClassName,
			long entryClassPK, String actionId)
		throws Exception {

		return _indexerPermissionPostFilter.hasPermission(
			permissionChecker, entryClassName, entryClassPK, actionId);
	}

	@Override
	public boolean isCommitImmediately() {
		return _modelSearchSettings.isCommitImmediately();
	}

	/**
	 * todo Filter search is no
	 * @return
	 */
	@Override
	public boolean isFilterSearch() {
		return isPermissionAware();
	}

	@Override
	public boolean isIndexerEnabled() {
		return _indexerWriter.isEnabled();
	}

	@Override
	public boolean isPermissionAware() {
		return _indexerPermissionPostFilter.isPermissionAware();
	}

	@Override
	public boolean isStagingAware() {
		return _modelSearchSettings.isStagingAware();
	}

	@Override
	public boolean isVisible(long classPK, int status) throws Exception {
		return _indexerPermissionPostFilter.isVisible(classPK, status);
	}

	@Override
	public boolean isVisibleRelatedEntry(long classPK, int status)
		throws Exception {

		return true;
	}

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		//todo probably get rid of this method
		//_indexerQueryBuilder.postProcessContextBooleanFilter(
		//		contextBooleanFilter, searchContext);
	}

	/**
	 * @deprecated As of 7.1.0
	 * @param contextQuery
	 * @param searchContext
	 * @throws Exception
	 */
	@Deprecated
	@Override
	public void postProcessContextQuery(
			BooleanQuery contextQuery, SearchContext searchContext)
		throws Exception {

		//todo SHOULD NOT NEED TO BE IMPLEMENTED
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		//todo this isn't necessary anymore. Replaced w/ KeywordQueryContributor
	}

	/**
	 * @deprecated As of 7.1.0
	 * @param searchQuery
	 * @param searchContext
	 * @throws Exception
	 */
	@Deprecated
	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {

		//todo SHOULD NOT NEED TO BE IMPLEMENTED
	}

	@Override
	public void registerIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		_modelSearchSettings.registerIndexerPostProcessor(indexerPostProcessor);
	}

	@Override
	public void reindex(Collection<T> objects) throws SearchException {
		_indexerWriter.reindex(objects);
	}

	@Override
	public void reindex(String className, long classPK) throws SearchException {
		_indexerWriter.reindex(classPK);
	}

	@Override
	public void reindex(String[] ids) throws SearchException {
		_indexerWriter.reindex(ids);
	}

	@Override
	public void reindex(T baseModel) throws SearchException {
		_indexerWriter.reindex(baseModel);
	}

	@Override
	public Hits search(SearchContext searchContext) throws SearchException {
		return _indexerSearcher.search(searchContext);
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

		return _indexerSearcher.searchCount(searchContext);
	}

	@Override
	public void setIndexerEnabled(boolean indexerEnabled) {
		_indexerWriter.setEnabled(indexerEnabled);
	}

	@Override
	public void unregisterIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		_modelSearchSettings.unregisterIndexerPostProcessor(
			indexerPostProcessor);
	}

	private final IndexerDocumentBuilder _indexerDocumentBuilder;
	private final IndexerPermissionPostFilter _indexerPermissionPostFilter;
	private final IndexerQueryBuilder _indexerQueryBuilder;
	private final IndexerSearcher _indexerSearcher;
	private final IndexerSummaryBuilder _indexerSummaryBuilder;
	private final IndexerWriter<T> _indexerWriter;
	private final ModelSearchSettings _modelSearchSettings;

}