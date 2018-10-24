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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.document;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.document.BulkableDocumentRequestTranslator;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class DeleteDocumentRequestExecutorTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			DeleteByQueryDocumentRequestExecutorTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testDocumentRequestTranslationWithNoRefresh() {
		doTestDocumentRequestTranslation(false);
	}

	@Test
	public void testDocumentRequestTranslationWithRefresh() {
		doTestDocumentRequestTranslation(true);
	}

	protected void doTestDocumentRequestTranslation(boolean refresh) {
		DeleteDocumentRequest deleteDocumentRequest = new DeleteDocumentRequest(
			_INDEX_NAME, "uid");

		deleteDocumentRequest.setType("type");
		deleteDocumentRequest.setRefresh(refresh);

		BulkableDocumentRequestTranslator
			<DeleteRequestBuilder, IndexRequestBuilder, UpdateRequestBuilder,
			 BulkRequestBuilder>
				elasticsearchBulkableDocumentRequestTranslator =
					new ElasticsearchBulkableDocumentRequestTranslator() {
						{
							elasticsearchConnectionManager =
								_elasticsearchConnectionManager;
						}
					};

		DeleteRequestBuilder deleteRequestBuilder =
			elasticsearchBulkableDocumentRequestTranslator.translate(
				deleteDocumentRequest, null);

		DeleteRequest deleteRequest = deleteRequestBuilder.request();

		Assert.assertArrayEquals(
			new String[] {_INDEX_NAME}, deleteRequest.indices());

		WriteRequest.RefreshPolicy expectedRefreshPolicy = null;

		if (deleteDocumentRequest.isRefresh()) {
			expectedRefreshPolicy = WriteRequest.RefreshPolicy.IMMEDIATE;
		}
		else {
			expectedRefreshPolicy = WriteRequest.RefreshPolicy.NONE;
		}

		Assert.assertEquals(
			expectedRefreshPolicy, deleteRequest.getRefreshPolicy());

		Assert.assertEquals(
			deleteDocumentRequest.getType(), deleteRequest.type());

		Assert.assertEquals(deleteDocumentRequest.getUid(), deleteRequest.id());
	}

	private static final String _INDEX_NAME = "test_request_index";

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;

}