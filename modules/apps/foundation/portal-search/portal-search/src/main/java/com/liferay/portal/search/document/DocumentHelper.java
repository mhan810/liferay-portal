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

package com.liferay.portal.search.document;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.util.StringPool;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public interface DocumentHelper {

	public void addAttachmentOwnerKey(
		Document document, long classNameId, long classPK);

	public void addEntryKey(Document document, String className, long classPK);

	public void addAssetFields(
		Document document, String className, long classPK);

	public void addTrashFields(Document document, TrashedModel trashedModel);

	public void addLocalizedField(
		Document document, String field, Locale siteDefaultLocale,
		Map<Locale, String> map);

	public void addAssetCategoryTitles(
		Document document, String field, List<AssetCategory> assetCategories);

	public void addAssetTagNames(
		BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception;

	public void addSearchAssetCategoryTitles(
		Document document, String field, List<AssetCategory> assetCategories);

	public Document getBaseModelDocument(
		String portletId, BaseModel<?> baseModel);

	public Document getBaseModelDocument(
		String portletId, BaseModel<?> baseModel,
		BaseModel<?> workflowedBaseModel);

	public Locale getSnippetLocale(Document document, Locale locale);

	public Summary createSummary(Document document);

	public Summary createSummary(
		Document document, String titleField, String contentField);

	public Map<Locale, String> populateMap(
		AssetEntry assetEntry, Map<Locale, String> map);

}
