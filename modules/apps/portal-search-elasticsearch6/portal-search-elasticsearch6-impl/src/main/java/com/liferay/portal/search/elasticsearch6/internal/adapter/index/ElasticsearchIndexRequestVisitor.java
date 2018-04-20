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
import com.liferay.portal.search.engine.adapter.IndexRequestVisitor;
import com.liferay.portal.search.engine.adapter.index.GetFieldMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.PutMappingIndexRequest;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(
	immediate = true, property = "search.engine.impl=Elasticsearch",
	service = IndexRequestVisitor.class
)
public class ElasticsearchIndexRequestVisitor
	implements IndexRequestVisitor<ActionRequestBuilder> {

	@Override
	public GetFieldMappingsRequestBuilder visitIndexRequest(
		GetFieldMappingIndexRequest getFieldMappingIndexRequest) {

		return getFieldMappingIndexRequestTranslator.translate(
			getFieldMappingIndexRequest, this, elasticsearchConnectionManager);
	}

	@Override
	public GetMappingsRequestBuilder visitIndexRequest(
		GetMappingIndexRequest getMappingIndexRequest) {

		return getMappingIndexRequestTranslator.translate(
			getMappingIndexRequest, this, elasticsearchConnectionManager);
	}

	@Override
	public PutMappingRequestBuilder visitIndexRequest(
		PutMappingIndexRequest putMappingIndexRequest) {

		return putMappingIndexRequestTranslator.translate(
			putMappingIndexRequest, this, elasticsearchConnectionManager);
	}

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

	@Reference
	protected GetFieldMappingIndexRequestTranslator
		getFieldMappingIndexRequestTranslator;

	@Reference
	protected GetMappingIndexRequestTranslator getMappingIndexRequestTranslator;

	@Reference
	protected PutMappingIndexRequestTranslator putMappingIndexRequestTranslator;

}