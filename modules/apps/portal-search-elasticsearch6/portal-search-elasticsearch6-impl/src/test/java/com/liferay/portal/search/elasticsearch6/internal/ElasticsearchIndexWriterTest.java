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

package com.liferay.portal.search.elasticsearch6.internal;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.index.IndexNameBuilder;
import com.liferay.portal.search.elasticsearch6.internal.util.DocumentTypes;
import com.liferay.portal.search.test.util.IdempotentRetryAssert;
import com.liferay.portal.search.test.util.indexing.DocumentFixture;

import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class ElasticsearchIndexWriterTest {

	@Before
	public void setUp() throws Exception {
		_documentFixture.setUp();

		_elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchIndexWriterTest.class.getSimpleName());

		_elasticsearchIndexingFixture = new ElasticsearchIndexingFixture(
			_elasticsearchFixture, _COMPANY_ID);

		_elasticsearchIndexingFixture.setUp();

		_indexWriter = _elasticsearchIndexingFixture.getIndexWriter();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchIndexingFixture.tearDown();
	}

	@Test
	public void testDeleteEntityDocuments() throws Exception {
		String documentSource1 =
			"{\"" + Field.ENTRY_CLASS_NAME + "\":\"user\"}";
		String documentSource2 =
			"{\"" + Field.ENTRY_CLASS_NAME + "\":\"content\"}";
		String documentSource3 =
			"{\"" + Field.ENTRY_CLASS_NAME + "\":\"user\"}";

		_indexDocument(documentSource1, "1");
		_indexDocument(documentSource2, "2");
		_indexDocument(documentSource3, "3");

		IndicesAdminClient indicesAdminClient =
			_elasticsearchFixture.getIndicesAdminClient();

		IndexNameBuilder indexNameBuilder =
			_elasticsearchIndexingFixture.getIndexNameBuilder();

		RefreshRequestBuilder refreshRequestBuilder =
			indicesAdminClient.prepareRefresh(
				indexNameBuilder.getIndexName(_COMPANY_ID));

		refreshRequestBuilder.get();

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(_COMPANY_ID);

		_indexWriter.deleteEntityDocuments(searchContext, "user");

		IdempotentRetryAssert.retryAssert(
			3, TimeUnit.SECONDS,
			() -> {
				GetResponse getResponse1 = _getDocument("1");

				Assert.assertFalse(getResponse1.isExists());

				GetResponse getResponse2 = _getDocument("2");

				Assert.assertTrue(getResponse2.isExists());

				GetResponse getResponse3 = _getDocument("3");

				Assert.assertFalse(getResponse3.isExists());

				return null;
			});
	}

	private GetResponse _getDocument(String id) {
		Client client = _elasticsearchFixture.getClient();

		IndexNameBuilder indexNameBuilder =
			_elasticsearchIndexingFixture.getIndexNameBuilder();

		GetRequestBuilder getRequestBuilder = client.prepareGet(
			indexNameBuilder.getIndexName(_COMPANY_ID), DocumentTypes.LIFERAY,
			id);

		getRequestBuilder.setId(id);

		return getRequestBuilder.get();
	}

	private void _indexDocument(String documentSource, String id) {
		Client client = _elasticsearchFixture.getClient();

		IndexNameBuilder indexNameBuilder =
			_elasticsearchIndexingFixture.getIndexNameBuilder();

		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(
			indexNameBuilder.getIndexName(_COMPANY_ID), DocumentTypes.LIFERAY);

		indexRequestBuilder.setId(id);
		indexRequestBuilder.setSource(documentSource, XContentType.JSON);

		indexRequestBuilder.get();
	}

	private static final long _COMPANY_ID = 1;

	private final DocumentFixture _documentFixture = new DocumentFixture();
	private ElasticsearchFixture _elasticsearchFixture;
	private ElasticsearchIndexingFixture _elasticsearchIndexingFixture;
	private IndexWriter _indexWriter;

}