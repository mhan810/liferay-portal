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

package com.liferay.portal.search.internal.permission;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.indexer.BaseModelDocumentFactory;
import com.liferay.portal.search.indexer.BaseModelRetriever;
import com.liferay.portal.search.permission.SearchPermissionDocumentContributor;
import com.liferay.portal.search.permission.SearchPermissionIndexWriter;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = SearchPermissionIndexWriter.class)
public class SearchPermissionIndexWriterImpl
	implements SearchPermissionIndexWriter {

	@Override
	public void updatePermissionFields(
		long companyId, String resourceClassName, String resourceClassPK) {

		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				resourceClassName);

		if (persistedModelLocalService == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"PersistedModelLocalService does not exist for class: " +
						resourceClassName);
			}

			return;
		}

		Optional<BaseModel> baseModelOptional =
			_baseModelRetriever.fetchBaseModel(
				resourceClassName, GetterUtil.getLong(resourceClassPK));

		if (!baseModelOptional.isPresent()) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						resourceClassName, " not found with key: ",
						resourceClassPK));
			}

			return;
		}

		Document document = _baseModelDocumentFactory.createDocument(
			baseModelOptional.get());

		_searchPermissionDocumentContributor.addPermissionFields(
			companyId, document);

		Indexer<?> indexer = _indexerRegistry.nullSafeGetIndexer(
			resourceClassName);

		indexer.partiallyUpdateDocument(companyId, document);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchPermissionIndexWriterImpl.class);

	@Reference
	private BaseModelDocumentFactory _baseModelDocumentFactory;

	@Reference
	private BaseModelRetriever _baseModelRetriever;

	@Reference
	private IndexerRegistry _indexerRegistry;

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

	@Reference
	private SearchPermissionDocumentContributor
		_searchPermissionDocumentContributor;

}