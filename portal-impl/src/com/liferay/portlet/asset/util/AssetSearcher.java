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

package com.liferay.portlet.asset.util;

import com.liferay.portal.kernel.search.BaseSearcher;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Eudaldo Alonso
 */
public class AssetSearcher extends BaseSearcher {

	public static Indexer getInstance() {
		return new AssetSearcher();
	}

	public AssetSearcher() {
		setDefaultSelectedFieldNames(
			Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK, Field.UID);
		setFilterSearch(true);
		setPermissionAware(true);
	}

	@Override
	public String[] getSearchClassNames() {
		long[] classNameIds = _assetEntryQuery.getClassNameIds();

		String[] classNames = new String[classNameIds.length];

		for (int i = 0; i < classNames.length; i++) {
			long classNameId = classNameIds[i];

			classNames[i] = PortalUtil.getClassName(classNameId);
		}

		return classNames;
	}

	public void setAssetEntryQuery(AssetEntryQuery assetEntryQuery) {
		_assetEntryQuery = assetEntryQuery;
	}

	protected void addImpossibleTerm(BooleanFilter queryFilter, String field)
		throws Exception {

		queryFilter.addTerm(field, "-1", BooleanClauseOccur.MUST);
	}

	protected void addSearchAllCategories(BooleanFilter queryFilter)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		long[] allCategoryIds = _assetEntryQuery.getAllCategoryIds();

		if (allCategoryIds.length == 0) {
			return;
		}

		long[] filteredAllCategoryIds = AssetUtil.filterCategoryIds(
			permissionChecker, allCategoryIds);

		if (allCategoryIds.length != filteredAllCategoryIds.length) {
			addImpossibleTerm(queryFilter, Field.ASSET_CATEGORY_IDS);

			return;
		}

		BooleanFilter categoryIdsFilter = new BooleanFilter();

		for (long allCategoryId : filteredAllCategoryIds) {
			AssetCategory assetCategory =
				AssetCategoryLocalServiceUtil.fetchAssetCategory(allCategoryId);

			if (assetCategory == null) {
				continue;
			}

			List<Long> categoryIds = new ArrayList<>();

			if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
				categoryIds.addAll(
					AssetCategoryLocalServiceUtil.getSubcategoryIds(
						allCategoryId));
			}

			if (categoryIds.isEmpty()) {
				categoryIds.add(allCategoryId);
			}

			BooleanFilter categoryIdFilter = new BooleanFilter();

			for (long categoryId : categoryIds) {
				categoryIdFilter.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
			}

			categoryIdsFilter.add(categoryIdFilter, BooleanClauseOccur.MUST);
		}

		queryFilter.add(categoryIdsFilter, BooleanClauseOccur.MUST);
	}

	protected void addSearchAllTags(BooleanFilter queryFilter)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		long[][] allTagIdsArray = _assetEntryQuery.getAllTagIdsArray();

		if (allTagIdsArray.length == 0) {
			return;
		}

		BooleanFilter tagIdsArrayFilter = new BooleanFilter();

		for (long[] allTagIds : allTagIdsArray) {
			if (allTagIds.length == 0) {
				continue;
			}

			long[] filteredAllTagIds = AssetUtil.filterTagIds(
				permissionChecker, allTagIds);

			if (allTagIds.length != filteredAllTagIds.length) {
				addImpossibleTerm(queryFilter, Field.ASSET_TAG_IDS);

				return;
			}

			BooleanFilter tagIdsFilter = new BooleanFilter();

			for (long tagId : allTagIds) {
				tagIdsFilter.addTerm(Field.ASSET_TAG_IDS, tagId);
			}

			tagIdsArrayFilter.add(tagIdsFilter, BooleanClauseOccur.MUST);
		}

		queryFilter.add(tagIdsArrayFilter, BooleanClauseOccur.MUST);
	}

	protected void addSearchAnyCategories(BooleanFilter queryFilter)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		long[] anyCategoryIds = _assetEntryQuery.getAnyCategoryIds();

		if (anyCategoryIds.length == 0) {
			return;
		}

		long[] filteredAnyCategoryIds = AssetUtil.filterCategoryIds(
			permissionChecker, anyCategoryIds);

		if (filteredAnyCategoryIds.length == 0) {
			addImpossibleTerm(queryFilter, Field.ASSET_CATEGORY_IDS);

			return;
		}

		BooleanFilter categoryIdsFilter = new BooleanFilter();

		for (long anyCategoryId : filteredAnyCategoryIds) {
			AssetCategory assetCategory =
				AssetCategoryLocalServiceUtil.fetchAssetCategory(anyCategoryId);

			if (assetCategory == null) {
				continue;
			}

			List<Long> categoryIds = new ArrayList<>();

			if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
				categoryIds.addAll(
					AssetCategoryLocalServiceUtil.getSubcategoryIds(
						anyCategoryId));
			}

			if (categoryIds.isEmpty()) {
				categoryIds.add(anyCategoryId);
			}

			for (long categoryId : categoryIds) {
				categoryIdsFilter.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
			}
		}

		queryFilter.add(categoryIdsFilter, BooleanClauseOccur.MUST);
	}

	protected void addSearchAnyTags(BooleanFilter queryFilter)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		long[] anyTagIds = _assetEntryQuery.getAnyTagIds();

		if (anyTagIds.length == 0) {
			return;
		}

		long[] filteredAnyTagIds = AssetUtil.filterTagIds(
			permissionChecker, anyTagIds);

		if (filteredAnyTagIds.length == 0) {
			addImpossibleTerm(queryFilter, Field.ASSET_TAG_IDS);

			return;
		}

		BooleanFilter tagIdsFilter = new BooleanFilter();

		for (long tagId : anyTagIds) {
			tagIdsFilter.addTerm(Field.ASSET_TAG_IDS, tagId);
		}

		queryFilter.add(tagIdsFilter, BooleanClauseOccur.MUST);
	}

	@Override
	protected void addSearchAssetCategoryIds(
			BooleanFilter queryFilter, SearchContext searchContext)
		throws Exception {

		addSearchAllCategories(queryFilter);
		addSearchAnyCategories(queryFilter);
		addSearchNotAnyCategories(queryFilter);
		addSearchNotAllCategories(queryFilter);
	}

	@Override
	protected void addSearchAssetTagNames(
			BooleanFilter queryFilter, SearchContext searchContext)
		throws Exception {

		addSearchAllTags(queryFilter);
		addSearchAnyTags(queryFilter);
		addSearchNotAllTags(queryFilter);
		addSearchNotAnyTags(queryFilter);
	}

	@Override
	protected Map<String, Query> addSearchKeywords(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {

		String keywords = searchContext.getKeywords();

		if (Validator.isNull(keywords)) {
			return Collections.emptyMap();
		}

		Map<String, Query> queries = super.addSearchKeywords(
			searchQuery, searchContext);

		String field = DocumentImpl.getLocalizedName(
			searchContext.getLocale(), "localized_title");

		Query query = searchQuery.addTerm(field, keywords, true);

		queries.put(field, query);

		return queries;
	}

	@Override
	protected void addSearchLayout(
			BooleanFilter queryFilter, SearchContext searchContext)
		throws Exception {

		String layoutUuid = (String)searchContext.getAttribute(
			Field.LAYOUT_UUID);

		if (Validator.isNotNull(layoutUuid)) {
			queryFilter.addRequiredTerm(Field.LAYOUT_UUID, layoutUuid);
		}
	}

	protected void addSearchNotAllCategories(BooleanFilter queryFilter)
		throws Exception {

		long[] notAllCategoryIds = _assetEntryQuery.getNotAllCategoryIds();

		if (notAllCategoryIds.length == 0) {
			return;
		}

		BooleanFilter categoryIdsFilter = new BooleanFilter();

		for (long notAllCategoryId : notAllCategoryIds) {
			AssetCategory assetCategory =
				AssetCategoryLocalServiceUtil.fetchAssetCategory(
					notAllCategoryId);

			if (assetCategory == null) {
				continue;
			}

			List<Long> categoryIds = new ArrayList<>();

			if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
				categoryIds.addAll(
					AssetCategoryLocalServiceUtil.getSubcategoryIds(
						notAllCategoryId));
			}

			if (categoryIds.isEmpty()) {
				categoryIds.add(notAllCategoryId);
			}

			BooleanFilter categoryIdFilter = new BooleanFilter();

			for (long categoryId : categoryIds) {
				categoryIdFilter.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
			}

			categoryIdsFilter.add(categoryIdFilter, BooleanClauseOccur.MUST);
		}

		queryFilter.add(categoryIdsFilter, BooleanClauseOccur.MUST_NOT);
	}

	protected void addSearchNotAllTags(BooleanFilter queryFilter)
		throws Exception {

		long[][] notAllTagIdsArray = _assetEntryQuery.getNotAllTagIdsArray();

		if (notAllTagIdsArray.length == 0) {
			return;
		}

		BooleanFilter tagIdsArrayFilter = new BooleanFilter();

		for (long[] notAllTagIds : notAllTagIdsArray) {
			if (notAllTagIds.length == 0) {
				continue;
			}

			BooleanFilter tagIdsFilter = new BooleanFilter();

			for (long tagId : notAllTagIds) {
				tagIdsFilter.addTerm(Field.ASSET_TAG_IDS, tagId);
			}

			tagIdsArrayFilter.add(tagIdsFilter, BooleanClauseOccur.MUST);
		}

		queryFilter.add(tagIdsArrayFilter, BooleanClauseOccur.MUST_NOT);
	}

	protected void addSearchNotAnyCategories(BooleanFilter queryFilter)
		throws Exception {

		long[] notAnyCategoryIds = _assetEntryQuery.getNotAnyCategoryIds();

		if (notAnyCategoryIds.length == 0) {
			return;
		}

		BooleanFilter categoryIdsFilter = new BooleanFilter();

		for (long notAnyCategoryId : notAnyCategoryIds) {
			AssetCategory assetCategory =
				AssetCategoryLocalServiceUtil.fetchAssetCategory(
					notAnyCategoryId);

			if (assetCategory == null) {
				continue;
			}

			List<Long> categoryIds = new ArrayList<>();

			if (PropsValues.ASSET_CATEGORIES_SEARCH_HIERARCHICAL) {
				categoryIds.addAll(
					AssetCategoryLocalServiceUtil.getSubcategoryIds(
						notAnyCategoryId));
			}

			if (categoryIds.isEmpty()) {
				categoryIds.add(notAnyCategoryId);
			}

			for (long categoryId : categoryIds) {
				categoryIdsFilter.addTerm(Field.ASSET_CATEGORY_IDS, categoryId);
			}
		}

		queryFilter.add(categoryIdsFilter, BooleanClauseOccur.MUST_NOT);
	}

	protected void addSearchNotAnyTags(BooleanFilter queryFilter)
		throws Exception {

		long[] notAnyTagIds = _assetEntryQuery.getNotAnyTagIds();

		if (notAnyTagIds.length == 0) {
			return;
		}

		BooleanFilter tagIdsFilter = new BooleanFilter();

		for (long tagId : _assetEntryQuery.getNotAnyTagIds()) {
			tagIdsFilter.addTerm(Field.ASSET_TAG_IDS, tagId);
		}

		queryFilter.add(tagIdsFilter, BooleanClauseOccur.MUST_NOT);
	}

	@Override
	protected void postProcessFullQuery(
			BooleanQuery fullQuery, SearchContext searchContext)
		throws Exception {

		fullQuery.addRequiredTerm("visible", true);
	}

	private AssetEntryQuery _assetEntryQuery;

}