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

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.document.DefaultElasticsearchDocumentFactory;
import com.liferay.portal.search.elasticsearch6.internal.document.ElasticsearchDocumentFactory;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.test.util.indexing.DocumentFixture;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class IndexDocumentRequestExecutorTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			DeleteDocumentRequestExecutorTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);

		_documentFixture.setUp();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
		_documentFixture.tearDown();
	}

	@Test
	public void testDocumentRequestTranslation() throws IOException {
		String id = "1";
		Document document = new DocumentImpl();

		document.addKeyword(Field.UID, id);
		document.addKeyword(Field.TYPE, _MAPPING_NAME);

		IndexDocumentRequest indexDocumentRequest = new IndexDocumentRequest(
			_INDEX_NAME, document);

		IndexDocumentRequestExecutorImpl indexDocumentRequestTranslator =
			new IndexDocumentRequestExecutorImpl();

		IndexRequestBuilder indexRequestBuilder =
			indexDocumentRequestTranslator.createBuilder(
				indexDocumentRequest, _elasticsearchConnectionManager);

		IndexRequest request = indexRequestBuilder.request();

		String index = request.index();
		String type = request.type();
		String requestId = request.id();
		String source = XContentHelper.convertToJson(
			request.source(), false, XContentType.JSON);

		Assert.assertEquals(_INDEX_NAME, index);
		Assert.assertEquals(_MAPPING_NAME, type);
		Assert.assertEquals(id, requestId);

		ElasticsearchDocumentFactory elasticsearchDocumentFactory =
			new DefaultElasticsearchDocumentFactory();

		Assert.assertEquals(
			elasticsearchDocumentFactory.getElasticsearchDocument(document),
			source);
	}

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _MAPPING_NAME = "testMapping";

	private final DocumentFixture _documentFixture = new DocumentFixture();
	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;

}