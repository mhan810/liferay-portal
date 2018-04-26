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

/**
 * @author Dylan Rebelak
 */
public class DocumentRequestExecutorFixture {

	public DocumentRequestExecutorFixture(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
	}

	public ElasticsearchDocumentRequestExecutor createExecutor() {
		ElasticsearchDocumentRequestExecutor documentRequestExecutor =
			new ElasticsearchDocumentRequestExecutor();

		documentRequestExecutor.elasticsearchConnectionManager =
			_elasticsearchConnectionManager;

		documentRequestExecutor.deleteByQueryDocumentRequestTranslator =
			new DeleteByQueryDocumentRequestTranslatorImpl();
		documentRequestExecutor.deleteDocumentRequestTranslator =
			new DeleteDocumentRequestTranslatorImpl();
		documentRequestExecutor.indexDocumentRequestTranslator =
			new IndexDocumentRequestTranslatorImpl();
		documentRequestExecutor.updateByQueryDocumentRequestTranslator =
			new UpdateByQueryDocumentRequestTranslatorImpl();
		documentRequestExecutor.updateDocumentRequestTranslator =
			new UpdateDocumentRequestTranslatorImpl();

		return documentRequestExecutor;
	}

	private final ElasticsearchConnectionManager
		_elasticsearchConnectionManager;

}