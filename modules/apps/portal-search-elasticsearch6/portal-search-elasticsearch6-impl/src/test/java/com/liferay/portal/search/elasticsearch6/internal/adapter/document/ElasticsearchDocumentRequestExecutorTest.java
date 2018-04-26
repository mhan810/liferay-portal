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
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Dylan Rebelak
 */
public class ElasticsearchDocumentRequestExecutorTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		_elasticsearchDocumentRequestExecutor =
			new ElasticsearchDocumentRequestExecutor();

		_elasticsearchDocumentRequestExecutor.elasticsearchConnectionManager =
			_elasticsearchConnectionManager;

		_elasticsearchDocumentRequestExecutor.
			deleteByQueryDocumentRequestTranslator =
				_deleteByQueryDocumentRequestTranslator;
		_elasticsearchDocumentRequestExecutor.deleteDocumentRequestTranslator =
			_deleteDocumentRequestTranslator;
		_elasticsearchDocumentRequestExecutor.indexDocumentRequestTranslator =
			_indexDocumentRequestTranslator;
		_elasticsearchDocumentRequestExecutor.
			updateByQueryDocumentRequestTranslator =
				_updateByQueryDocumentRequestTranslator;
		_elasticsearchDocumentRequestExecutor.updateDocumentRequestTranslator =
			_updateDocumentRequestTranslator;
	}

	@Test
	public void testExecuteDeleteByQueryDocumentRequest() {
		DeleteByQueryDocumentRequest deleteByQueryDocumentRequest =
			new DeleteByQueryDocumentRequest(null, null);

		_elasticsearchDocumentRequestExecutor.executeDocumentRequest(
			deleteByQueryDocumentRequest);

		Mockito.verify(
			_deleteByQueryDocumentRequestTranslator
		).execute(
			deleteByQueryDocumentRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteDeleteDocumentRequest() {
		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			null, null, null);

		_elasticsearchDocumentRequestExecutor.executeDocumentRequest(
			deleteDocumentRequest);

		Mockito.verify(
			_deleteDocumentRequestTranslator
		).execute(
			deleteDocumentRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteIndexDocumentRequest() {
		IndexDocumentRequest indexDocumentRequest = new IndexDocumentRequest(
			null, null);

		_elasticsearchDocumentRequestExecutor.executeDocumentRequest(
			indexDocumentRequest);

		Mockito.verify(
			_indexDocumentRequestTranslator
		).execute(
			indexDocumentRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteUpdateByQueryDocumentRequest() {
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest =
			new UpdateByQueryDocumentRequest(null, null, null);

		_elasticsearchDocumentRequestExecutor.executeDocumentRequest(
			updateByQueryDocumentRequest);

		Mockito.verify(
			_updateByQueryDocumentRequestTranslator
		).execute(
			updateByQueryDocumentRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteUpdateDocumentRequest() {
		UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest(
			null, null, null);

		_elasticsearchDocumentRequestExecutor.executeDocumentRequest(
			updateDocumentRequest);

		Mockito.verify(
			_updateDocumentRequestTranslator
		).execute(
			updateDocumentRequest, _elasticsearchConnectionManager
		);
	}

	@Mock
	private DeleteByQueryDocumentRequestTranslator
		_deleteByQueryDocumentRequestTranslator;

	@Mock
	private DeleteDocumentRequestTranslator _deleteDocumentRequestTranslator;

	@Mock
	private ElasticsearchConnectionManager _elasticsearchConnectionManager;

	private ElasticsearchDocumentRequestExecutor
		_elasticsearchDocumentRequestExecutor;

	@Mock
	private IndexDocumentRequestTranslator _indexDocumentRequestTranslator;

	@Mock
	private UpdateByQueryDocumentRequestTranslator
		_updateByQueryDocumentRequestTranslator;

	@Mock
	private UpdateDocumentRequestTranslator _updateDocumentRequestTranslator;

}