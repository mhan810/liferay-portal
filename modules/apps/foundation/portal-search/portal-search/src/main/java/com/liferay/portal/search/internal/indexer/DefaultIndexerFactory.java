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
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.IndexerFactory;
import com.liferay.portal.search.indexer.ModelIndexer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = IndexerFactory.class)
public class DefaultIndexerFactory implements IndexerFactory {

	@Override
	public Indexer<?> create(
		String searchEngineId, ModelIndexer<?> modelIndexer) {

		Indexer<?> indexer = new DefaultIndexer(
			expandoBridgeFactory, expandoBridgeIndexer, hitsProcessorRegistry,
			indexerRegistry, indexSearcherHelper, indexStatusManager,
			indexWriterHelper, modelIndexer, searchEngineHelper, searchEngineId,
			searchPermissionChecker, expandoColumnLocalService,
			groupLocalService);

		return indexer;
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