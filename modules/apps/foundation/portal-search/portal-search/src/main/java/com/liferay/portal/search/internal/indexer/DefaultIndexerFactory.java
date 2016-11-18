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

import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.util.ExpandoBridgeFactory;
import com.liferay.expando.kernel.util.ExpandoBridgeIndexer;
import com.liferay.portal.kernel.search.IndexSearcherHelper;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.hits.HitsProcessorRegistry;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.IndexerFactory;
import com.liferay.portal.search.indexer.IndexerPropertyKeys;
import com.liferay.portal.search.indexer.ModelIndexer;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = IndexerFactory.class)
public class DefaultIndexerFactory implements IndexerFactory {

	@Override
	public Indexer<?> create(
		String searchEngineId, ModelIndexer<?> modelIndexer,
		Map<String, Object> properties) {

		DefaultIndexer defaultIndexer = new DefaultIndexer(
			expandoBridgeFactory, expandoBridgeIndexer, hitsProcessorRegistry,
			indexerRegistry, indexSearcherHelper, indexStatusManager,
			indexWriterHelper, modelIndexer, searchEngineHelper, searchEngineId,
			searchPermissionChecker, expandoColumnLocalService,
			groupLocalService);

		boolean commitImmediately = GetterUtil.getBoolean(
			properties.get(IndexerPropertyKeys.COMMIT_IMMEDIATELY));

		defaultIndexer.setCommitImmediately(commitImmediately);

		String[] defaultSelectedFieldNames = (String[])properties.get(
			IndexerPropertyKeys.DEFAULT_SELECTED_FIELD_NAMES);

		if (ArrayUtil.isNotEmpty(defaultSelectedFieldNames)) {
			defaultIndexer.setDefaultSelectedFieldNames(
				defaultSelectedFieldNames);
		}

		String[] defaultSelectedLocalizedFieldNames = (String[])properties.get(
			IndexerPropertyKeys.DEFAULT_SELECTED_LOCALIZED_FIELD_NAMES);

		if (ArrayUtil.isNotEmpty(defaultSelectedLocalizedFieldNames)) {
			defaultIndexer.setDefaultSelectedLocalizedFieldNames(
				defaultSelectedLocalizedFieldNames);
		}

		boolean filterSearch = GetterUtil.getBoolean(
			properties.get(IndexerPropertyKeys.FILTER_SEARCH));

		defaultIndexer.setFilterSearch(filterSearch);

		boolean permissionAware = GetterUtil.getBoolean(
			properties.get(IndexerPropertyKeys.PERMISSION_AWARE));

		defaultIndexer.setPermissionAware(permissionAware);

		boolean selectAllLocales = GetterUtil.getBoolean(
			properties.get(IndexerPropertyKeys.SELECT_ALL_LOCALES));

		defaultIndexer.setSelectAllLocales(selectAllLocales);

		boolean stagingAware = GetterUtil.getBoolean(
			properties.get(IndexerPropertyKeys.STAGING_AWARE), true);

		defaultIndexer.setStagingAware(stagingAware);

		return defaultIndexer;
	}

	@Reference
	protected ExpandoBridgeFactory expandoBridgeFactory;

	@Reference
	protected ExpandoBridgeIndexer expandoBridgeIndexer;

	@Reference
	protected ExpandoColumnLocalService expandoColumnLocalService;

	@Reference
	protected GroupLocalService groupLocalService;

	@Reference
	protected HitsProcessorRegistry hitsProcessorRegistry;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected IndexSearcherHelper indexSearcherHelper;

	@Reference
	protected IndexStatusManager indexStatusManager;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	@Reference
	protected SearchEngineHelper searchEngineHelper;

	@Reference
	protected SearchPermissionChecker searchPermissionChecker;

}