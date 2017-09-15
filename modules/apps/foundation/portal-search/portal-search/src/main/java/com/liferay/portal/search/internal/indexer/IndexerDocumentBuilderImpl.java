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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ResourcedModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.search.DocumentHelper;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.stream.Stream;

/**
 * @author Michael C. Han
 */
public class IndexerDocumentBuilderImpl implements IndexerDocumentBuilder {

	public IndexerDocumentBuilderImpl(
		Iterable<ModelDocumentContributor> modelDocumentContributors,
		Iterable<DocumentContributor> documentContributors,
		IndexerPostProcessorsHolder indexerPostProcessorsHolder) {

		_modelDocumentContributors = modelDocumentContributors;
		_documentContributors = documentContributors;
		_indexerPostProcessorsHolder = indexerPostProcessorsHolder;
	}

	@Override
	public <T extends BaseModel<?>> Document getDocument(T baseModel) {
		Document document = (Document)_document.clone();

		String className = baseModel.getModelClassName();

		Tuple classPKResourcePrimKeyTuple = getClassPKResourcePrimKey(
			baseModel);

		long classPK = (Long)classPKResourcePrimKeyTuple.getObject(0);

		String uid = doGetDocumentUID(className, classPK);

		document.addKeyword(Field.UID, uid);

		DocumentHelper documentHelper = new DocumentHelper(document);

		long resourcePrimKey = (Long)classPKResourcePrimKeyTuple.getObject(1);

		documentHelper.setEntryKey(className, classPK);

		if (resourcePrimKey > 0) {
			document.addKeyword(Field.ROOT_ENTRY_CLASS_PK, resourcePrimKey);
		}

		_documentContributors.forEach(
			(DocumentContributor documentContributor) ->
				documentContributor.contribute(document, baseModel));

		_modelDocumentContributors.forEach(
			(ModelDocumentContributor modelDocumentContributor) ->
				modelDocumentContributor.contribute(document, baseModel));

		postProcessDocument(document, baseModel);

		return document;
	}

	@Override
	public <T extends BaseModel<?>> String getDocumentUID(T baseModel) {
		Tuple classPKResourcePrimKey = getClassPKResourcePrimKey(baseModel);

		long classPK = (Long)classPKResourcePrimKey.getObject(0);

		String className = baseModel.getModelClassName();

		return doGetDocumentUID(className, classPK);
	}

	protected String doGetDocumentUID(String className, long classPK) {
		String uid = Field.getUID(className, String.valueOf(classPK));

		return uid;
	}

	protected Tuple getClassPKResourcePrimKey(BaseModel<?> baseModel) {
		long classPK = 0;
		long resourcePrimKey = 0;

		if (baseModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)baseModel;

			classPK = resourcedModel.getResourcePrimKey();
			resourcePrimKey = resourcedModel.getResourcePrimKey();
		}
		else {
			classPK = (Long)baseModel.getPrimaryKeyObj();
		}

		Tuple tuple = new Tuple(classPK, resourcePrimKey);

		return tuple;
	}

	protected <T extends BaseModel<?>> void postProcessDocument(
		Document document, T baseModel) {

		Stream<IndexerPostProcessor> stream =
			_indexerPostProcessorsHolder.stream();

		stream.forEach(
			indexerPostProcessor -> {
				try {
					indexerPostProcessor.postProcessDocument(
						document, baseModel);
				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Error post processing document: " + document, e);
					}
				}
			});
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexerDocumentBuilderImpl.class);

	private Document _document = new DocumentImpl();
	private final Iterable<DocumentContributor> _documentContributors;
	private final IndexerPostProcessorsHolder _indexerPostProcessorsHolder;
	private final Iterable<ModelDocumentContributor> _modelDocumentContributors;

}