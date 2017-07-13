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

package com.liferay.portal.search.internal.facet.asset;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactory;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.facet.asset.AssetEntriesFacet;
import com.liferay.portal.search.facet.asset.AssetEntriesFacetBooleanFilterBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = AssetEntriesFacet.class)
public class AssetEntriesFacetImpl
	extends MultiValueFacet implements AssetEntriesFacet {

	public AssetEntriesFacetImpl(SearchContext searchContext) {
		super(searchContext);

		setFieldName(Field.ENTRY_CLASS_NAME);

		initFacetClause();
	}

	@Override
	public AssetEntriesFacet newInstance(SearchContext searchContext) {
		return new AssetEntriesFacetImpl(searchContext);
	}

	@Override
	public void setFacetConfiguration(FacetConfiguration facetConfiguration) {
		super.setFacetConfiguration(facetConfiguration);

		initFacetClause();
	}

	@Override
	protected BooleanClause<Filter> doGetFacetFilterBooleanClause() {
		SearchContext searchContext = getSearchContext();

		String[] entryClassNames = searchContext.getEntryClassNames();

		BooleanFilter facetFilter = new BooleanFilter();

		for (String entryClassName : entryClassNames) {
			Indexer<?> indexer = _indexerRegistry.getIndexer(entryClassName);

			if (indexer == null) {
				continue;
			}

			String searchEngineId = searchContext.getSearchEngineId();

			if (!searchEngineId.equals(indexer.getSearchEngineId())) {
				continue;
			}

			try {
				AssetEntriesFacetBooleanFilterBuilder
					assetEntriesFacetBooleanFilterBuilder =
						_assetEntriesFacetBooleanFilterBuilders.get(
							entryClassName);

				if (assetEntriesFacetBooleanFilterBuilder == null) {
					assetEntriesFacetBooleanFilterBuilder =
						_defaultAssetEntriesFacetBooleanFilterBuilder;
				}

				BooleanFilter indexerBooleanFilter =
					assetEntriesFacetBooleanFilterBuilder.getFacetBooleanFilter(
						entryClassName, searchContext);

				if ((indexerBooleanFilter == null) ||
					!indexerBooleanFilter.hasClauses()) {

					continue;
				}

				BooleanFilter entityBooleanFilter = new BooleanFilter();

				entityBooleanFilter.add(
					indexerBooleanFilter, BooleanClauseOccur.MUST);

				indexer.postProcessContextBooleanFilter(
					entityBooleanFilter, searchContext);

				for (IndexerPostProcessor indexerPostProcessor :
						indexer.getIndexerPostProcessors()) {

					indexerPostProcessor.postProcessContextBooleanFilter(
						entityBooleanFilter, searchContext);
				}

				if (indexer.isStagingAware()) {
					if (!searchContext.isIncludeLiveGroups() &&
						searchContext.isIncludeStagingGroups()) {

						entityBooleanFilter.addRequiredTerm(
							Field.STAGING_GROUP, true);
					}
					else if (searchContext.isIncludeLiveGroups() &&
							 !searchContext.isIncludeStagingGroups()) {

						entityBooleanFilter.addRequiredTerm(
							Field.STAGING_GROUP, false);
					}
				}

				if (entityBooleanFilter.hasClauses()) {
					facetFilter.add(
						entityBooleanFilter, BooleanClauseOccur.SHOULD);
				}
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		if (!facetFilter.hasClauses()) {
			return null;
		}

		return _booleanClauseFactory.createFilter(
			facetFilter, BooleanClauseOccur.MUST);
	}

	protected void initFacetClause() {
		SearchContext searchContext = getSearchContext();

		FacetConfiguration facetConfiguration = getFacetConfiguration();

		JSONObject dataJSONObject = facetConfiguration.getData();

		String[] entryClassNames = null;

		if (dataJSONObject.has("values")) {
			JSONArray valuesJSONArray = dataJSONObject.getJSONArray("values");

			entryClassNames = new String[valuesJSONArray.length()];

			for (int i = 0; i < valuesJSONArray.length(); i++) {
				entryClassNames[i] = valuesJSONArray.getString(i);
			}
		}

		if (ArrayUtil.isEmpty(entryClassNames)) {
			entryClassNames = searchContext.getEntryClassNames();
		}

		if (!isStatic()) {
			String[] entryClassNameParam = StringUtil.split(
				GetterUtil.getString(
					searchContext.getAttribute(getFieldName())));

			if (ArrayUtil.isNotEmpty(entryClassNameParam)) {
				entryClassNames = entryClassNameParam;
			}
		}

		if (ArrayUtil.isEmpty(entryClassNames)) {
			entryClassNames = _searchEngineHelper.getEntryClassNames();

			if (!dataJSONObject.has("values")) {
				JSONArray entriesJSONArray = _jsonFactory.createJSONArray();

				for (String entryClassName : entryClassNames) {
					entriesJSONArray.put(entryClassName);
				}

				dataJSONObject.put("values", entriesJSONArray);
			}
		}

		searchContext.setEntryClassNames(entryClassNames);
	}

	protected void removeAssetEntriesFacetBooleanFilterBuilder(
		AssetEntriesFacetBooleanFilterBuilder
			assetEntriesFacetBooleanFilterBuilder,
		Map<String, Object> properties) {

		String entryClassName = GetterUtil.getString(
			properties, "entry.class.name");

		if (Validator.isNull(entryClassName)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No entry.class.name property specified for : " +
						assetEntriesFacetBooleanFilterBuilder);
			}

			return;
		}

		_assetEntriesFacetBooleanFilterBuilders.put(
			entryClassName, assetEntriesFacetBooleanFilterBuilder);
	}

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "!(entry.class.name=default)",
		unbind = "removeAssetEntriesFacetBooleanFilterBuilder"
	)
	protected void setAssetEntriesFacetBooleanFilterBuilder(
		AssetEntriesFacetBooleanFilterBuilder
			assetEntriesFacetBooleanFilterBuilder,
		Map<String, Object> properties) {

		String entryClassName = GetterUtil.getString(
			properties, "entry.class.name");

		if (Validator.isNull(entryClassName)) {
			throw new IllegalStateException(
				"No entry.class.name property specified for : " +
					assetEntriesFacetBooleanFilterBuilder);
		}

		_assetEntriesFacetBooleanFilterBuilders.put(
			entryClassName, assetEntriesFacetBooleanFilterBuilder);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetEntriesFacetImpl.class);

	private Map<String, AssetEntriesFacetBooleanFilterBuilder>
		_assetEntriesFacetBooleanFilterBuilders = new ConcurrentHashMap<>();

	@Reference
	private BooleanClauseFactory _booleanClauseFactory;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(entry.class.name=default)"
	)
	private AssetEntriesFacetBooleanFilterBuilder
		_defaultAssetEntriesFacetBooleanFilterBuilder;

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private SearchEngineHelper _searchEngineHelper;

}