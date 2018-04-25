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

package com.liferay.portal.search.elasticsearch6.internal.adapter.index;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexRequest;

import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class GetFieldMappingIndexRequestTranslatorTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			GetFieldMappingIndexRequestTranslatorTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testIndexRequestTranslation() {
		GetFieldMappingIndexRequest getFieldMappingIndexRequest =
			new GetFieldMappingIndexRequest(
				_INDEX_NAME, _MAPPING_NAME, new String[] {_FIELD_NAME});

		GetFieldMappingIndexRequestTranslatorImpl
			getFieldMappingIndexRequestTranslator =
				new GetFieldMappingIndexRequestTranslatorImpl();

		GetFieldMappingsRequestBuilder getFieldMappingsRequestBuilder =
			getFieldMappingIndexRequestTranslator.createBuilder(
				getFieldMappingIndexRequest, _elasticsearchConnectionManager);

		GetFieldMappingsRequest request =
			getFieldMappingsRequestBuilder.request();

		String[] indices = request.indices();
		String[] types = request.types();
		String[] fields = request.fields();

		Assert.assertArrayEquals(new String[] {_INDEX_NAME}, indices);
		Assert.assertArrayEquals(new String[] {_MAPPING_NAME}, types);
		Assert.assertArrayEquals(new String[] {_FIELD_NAME}, fields);
	}

	private static final String _FIELD_NAME = "testField";

	private static final String _INDEX_NAME = "test_request_index";

	private static final String _MAPPING_NAME = "testMapping";

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;

}