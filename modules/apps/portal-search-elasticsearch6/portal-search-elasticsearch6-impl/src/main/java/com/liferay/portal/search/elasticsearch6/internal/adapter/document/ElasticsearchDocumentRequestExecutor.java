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

package com.liferay.portal.search.elasticsearch6.internal.adapter.document;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.DocumentRequestExecutor;
import com.liferay.portal.search.engine.adapter.DocumentResponse;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(
	immediate = true, property = "search.engine.impl=Elasticsearch",
	service = DocumentRequestExecutor.class
)
public class ElasticsearchDocumentRequestExecutor
	implements DocumentRequestExecutor<DocumentResponse> {

	@Override
	public DocumentResponse executeDocumentRequest(
		DeleteByQueryDocumentRequest deleteByQueryDocumentRequest) {

		return deleteByQueryDocumentRequestTranslator.execute(
			deleteByQueryDocumentRequest, elasticsearchConnectionManager);
	}

	@Override
	public DocumentResponse executeDocumentRequest(
		DeleteDocumentRequest deleteDocumentRequest) {

		return deleteDocumentRequestTranslator.execute(
			deleteDocumentRequest, elasticsearchConnectionManager);
	}

	@Override
	public DocumentResponse executeDocumentRequest(
		IndexDocumentRequest indexDocumentRequest) {

		return indexDocumentRequestTranslator.execute(
			indexDocumentRequest, elasticsearchConnectionManager);
	}

	@Override
	public DocumentResponse executeDocumentRequest(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest) {

		return updateByQueryDocumentRequestTranslator.execute(
			updateByQueryDocumentRequest, elasticsearchConnectionManager);
	}

	@Override
	public DocumentResponse executeDocumentRequest(
		UpdateDocumentRequest updateDocumentRequest) {

		return updateDocumentRequestTranslator.execute(
			updateDocumentRequest, elasticsearchConnectionManager);
	}

	@Reference
	protected DeleteByQueryDocumentRequestTranslator
		deleteByQueryDocumentRequestTranslator;

	@Reference
	protected DeleteDocumentRequestTranslator deleteDocumentRequestTranslator;

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

	@Reference
	protected IndexDocumentRequestTranslator indexDocumentRequestTranslator;

	@Reference
	protected UpdateByQueryDocumentRequestTranslator
		updateByQueryDocumentRequestTranslator;

	@Reference
	protected UpdateDocumentRequestTranslator updateDocumentRequestTranslator;

}