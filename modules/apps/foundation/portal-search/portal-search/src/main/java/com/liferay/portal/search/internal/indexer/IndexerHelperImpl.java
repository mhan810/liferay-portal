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
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.indexer.IndexerHelper;
import com.liferay.portal.search.indexer.ModelIndexer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = IndexerHelper.class)
public class IndexerHelperImpl implements IndexerHelper {

	@Override
	public void addStatus(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		int[] statuses = GetterUtil.getIntegerValues(
			searchContext.getAttribute(Field.STATUS), null);

		if (ArrayUtil.isEmpty(statuses)) {
			int status = GetterUtil.getInteger(
				searchContext.getAttribute(Field.STATUS),
				WorkflowConstants.STATUS_APPROVED);

			statuses = new int[] {status};
		}

		if (!ArrayUtil.contains(statuses, WorkflowConstants.STATUS_ANY)) {
			TermsFilter statusesTermsFilter = new TermsFilter(Field.STATUS);

			statusesTermsFilter.addValues(ArrayUtil.toStringArray(statuses));

			contextBooleanFilter.add(
				statusesTermsFilter, BooleanClauseOccur.MUST);
		}
		else {
			contextBooleanFilter.addTerm(
				Field.STATUS, String.valueOf(WorkflowConstants.STATUS_IN_TRASH),
				BooleanClauseOccur.MUST_NOT);
		}
	}

	@Override
	public void addSearchAssetCategoryIds(BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.ASSET_CATEGORY_IDS);
		multiValueFacet.setStatic(true);
		multiValueFacet.setValues(searchContext.getAssetCategoryIds());

		searchContext.addFacet(multiValueFacet);
	}

	@Override
	public Filter addSearchClassTypeIds(BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		long[] classTypeIds = searchContext.getClassTypeIds();

		if (ArrayUtil.isEmpty(classTypeIds)) {
			return null;
		}

		TermsFilter classTypeIdsTermsFilter = new TermsFilter(
			Field.CLASS_TYPE_ID);

		classTypeIdsTermsFilter.addValues(
			ArrayUtil.toStringArray(classTypeIds));

		return contextBooleanFilter.add(
			classTypeIdsTermsFilter, BooleanClauseOccur.MUST);
	}

	@Override
	public void addSearchEntryClassNames(BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		Facet facet = new AssetEntriesFacet(searchContext);

		facet.setStatic(true);

		searchContext.addFacet(facet);
	}

	@Override
	public Map<String, Query> addSearchExpando(
			BooleanQuery searchQuery, ModelIndexer<?> modelIndexer,
			SearchContext searchContext, String keywords)
		throws Exception {

		Map<String, Query> expandoQueries = new HashMap<>();

		ExpandoBridge expandoBridge = expandoBridgeFactory.getExpandoBridge(
			searchContext.getCompanyId(), modelIndexer.getClassName());

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

	@Override
	public void addSearchFolderId(BooleanFilter queryBooleanFilter, SearchContext searchContext) {
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

	@Override
	public void addSearchGroupId(BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		Facet facet = new ScopeFacet(searchContext);

		facet.setStatic(true);

		searchContext.addFacet(facet);
	}

	@Override
	public Map<String, Query> addSearchKeywords(
			BooleanQuery searchQuery, ModelIndexer<?> modelIndexer,
			SearchContext searchContext)
		throws Exception {

		String keywords = searchContext.getKeywords();

		if (Validator.isNull(keywords)) {
			return Collections.emptyMap();
		}

		addSearchExpando(searchQuery, modelIndexer, searchContext, keywords);

		addSearchLocalizedTerm(
			searchQuery, searchContext, Field.ASSET_CATEGORY_TITLES,
			searchContext.isLike());

		return searchQuery.addTerms(
			Field.KEYWORDS, keywords, searchContext.isLike());
	}

	@Override
	public void addSearchLayout(BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

		MultiValueFacet multiValueFacet = new MultiValueFacet(searchContext);

		multiValueFacet.setFieldName(Field.LAYOUT_UUID);
		multiValueFacet.setStatic(true);

		searchContext.addFacet(multiValueFacet);
	}

	@Override
	public Map<String, Query> addSearchLocalizedTerm(
			BooleanQuery searchQuery, SearchContext searchContext,
			String field, boolean like)
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

	@Override
	public Query addSearchTerm(BooleanQuery searchQuery, SearchContext searchContext, String field, boolean like)
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


	@Override
	public void addSearchUserId(BooleanFilter queryBooleanFilter, SearchContext searchContext)
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

	@Override
	public String getExpandoFieldName(
		SearchContext searchContext, ExpandoBridge expandoBridge,
		String attributeName) {

		ExpandoColumn expandoColumn =
			expandoColumnLocalService.getDefaultTableColumn(
				expandoBridge.getCompanyId(), expandoBridge.getClassName(),
				attributeName);

		String fieldName = expandoBridgeIndexer.encodeFieldName(
			attributeName);

		if (expandoColumn.getType() ==
			ExpandoColumnConstants.STRING_LOCALIZED) {

			fieldName = DocumentImpl.getLocalizedName(
				searchContext.getLocale(), fieldName);
		}

		return fieldName;
	}

	@Override
	public void deleteDocument(
			long companyId, long field1, ModelIndexer<?> modelIndexer)
		throws Exception {

		deleteDocument(companyId, String.valueOf(field1), modelIndexer);
	}

	@Override
	public void deleteDocument(
			long companyId, long field1, String field2,
			ModelIndexer<?> modelIndexer)
		throws Exception {

		deleteDocument(companyId, String.valueOf(field1), field2, modelIndexer);
	}

	@Override
	public void deleteDocument(
			long companyId, String field1, ModelIndexer<?> modelIndexer)
		throws Exception {

		Document document = new DocumentImpl();

		document.addUID(modelIndexer.getClassName(), field1);

		Indexer<?> indexer = indexerRegistry.nullSafeGetIndexer(
			modelIndexer.getClassName());

		indexWriterHelper.deleteDocument(
			indexer.getSearchEngineId(), companyId, document.get(Field.UID),
			indexer.isCommitImmediately());
	}

	@Override
	public void deleteDocument(
			long companyId, String field1, String field2,
			ModelIndexer<?> modelIndexer)
		throws Exception {

		Document document = new DocumentImpl();

		document.addUID(modelIndexer.getClassName(), field1, field2);

		Indexer<?> indexer = indexerRegistry.nullSafeGetIndexer(
			modelIndexer.getClassName());

		indexWriterHelper.deleteDocument(
			indexer.getSearchEngineId(), companyId, document.get(Field.UID),
			indexer.isCommitImmediately());
	}

	@Reference
	protected ExpandoBridgeFactory expandoBridgeFactory;

	@Reference
	protected ExpandoBridgeIndexer expandoBridgeIndexer;

	@Reference
	protected ExpandoColumnLocalService expandoColumnLocalService;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	private static final long _DEFAULT_FOLDER_ID = 0L;

}
