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
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.index.UpdateDocumentIndexWriter;
import com.liferay.portal.search.indexer.BaseModelDocumentFactory;
import com.liferay.portal.search.permission.SearchPermissionDocumentContributor;
import com.liferay.portal.search.permission.SearchPermissionIndexWriter;

import java.util.Arrays;
import java.util.List;

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
		BaseModel<?> baseModel, long companyId, String searchEngineId,
		boolean commitImmediately) {

		Document document = createBasePermissionDocument(baseModel);

		if (document != null) {
			searchPermissionDocumentContributor.addPermissionFields(
				companyId, document);

			updateDocumentIndexWriter.updateDocumentPartially(
				searchEngineId, companyId, document, commitImmediately);
		}
		else {
			_log.error(
				StringBundler.concat(
					"Unable to update permissions for ",
					baseModel.getModelClassName(), " with primaryKey ",
					String.valueOf(baseModel.getPrimaryKeyObj())),
				new Exception());
		}
	}

	protected Document createBasePermissionDocument(BaseModel<?> baseModel) {
		//todo this is super expensive.
		Indexer<BaseModel<?>> indexer = indexerRegistry.getIndexer(
			baseModel.getModelClassName());

		if (indexer != null) {
			try {
				Document indexerDocument = indexer.getDocument(baseModel);
				Document document = new DocumentImpl();

				document.addKeyword(
					Field.ENTRY_CLASS_NAME,
					indexerDocument.get(Field.ENTRY_CLASS_NAME));
				document.addKeyword(
					Field.ENTRY_CLASS_PK,
					indexerDocument.get(Field.ENTRY_CLASS_PK));
				document.addKeyword(
					Field.GROUP_ID, indexerDocument.get(Field.GROUP_ID));
				document.addKeyword(Field.UID, indexerDocument.get(Field.UID));

				List<String> relatedPermissionFieldNames = Arrays.asList(
					Field.RELATED_ENTRY, Field.CLASS_NAME_ID, Field.CLASS_PK);

				for (String relatedPermissionFieldName :
						relatedPermissionFieldNames) {

					if (indexerDocument.hasField(relatedPermissionFieldName)) {
						document.addKeyword(
							relatedPermissionFieldName,
							indexerDocument.get(relatedPermissionFieldName));
					}
				}

				return document;
			}
			catch (SearchException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"Unable to create document with indexer for ",
							baseModel.getModelClassName()),
						se);
				}
			}
		}

		return null;
	}

	@Reference
	protected BaseModelDocumentFactory baseModelDocumentFactory;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected SearchPermissionDocumentContributor
		searchPermissionDocumentContributor;

	@Reference
	protected UpdateDocumentIndexWriter updateDocumentIndexWriter;

	private static final Log _log = LogFactoryUtil.getLog(
		SearchPermissionIndexWriterImpl.class);

}