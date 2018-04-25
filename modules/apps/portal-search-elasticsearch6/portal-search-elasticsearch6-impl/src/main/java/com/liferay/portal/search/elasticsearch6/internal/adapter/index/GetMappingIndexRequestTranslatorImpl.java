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
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexRequest;
import com.liferay.portal.search.engine.adapter.index.GetMappingIndexResponse;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;

import org.osgi.service.component.annotations.Component;

/**
 * @author Dylan Rebelak
 */
@Component(immediate = true, service = GetMappingIndexRequestTranslator.class)
public class GetMappingIndexRequestTranslatorImpl
	implements GetMappingIndexRequestTranslator {

	@Override
	public GetMappingIndexResponse execute(
		GetMappingIndexRequest getMappingIndexRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		GetMappingsRequestBuilder getMappingsRequestBuilder = createdBuilder(
			getMappingIndexRequest, elasticsearchConnectionManager);

		GetMappingsResponse getMappingsResponse =
			getMappingsRequestBuilder.get();

		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>>
			mappings = getMappingsResponse.getMappings();

		ImmutableOpenMap<String, MappingMetaData> indexMapping = mappings.get(
			getMappingIndexRequest.getIndexName());

		MappingMetaData mappingMetaData = indexMapping.get(
			getMappingIndexRequest.getMappingName());

		CompressedXContent mappingContent = mappingMetaData.source();

		return new GetMappingIndexResponse(mappingContent.toString());
	}

	protected GetMappingsRequestBuilder createdBuilder(
		GetMappingIndexRequest getMappingIndexRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		AdminClient adminClient =
			elasticsearchConnectionManager.getAdminClient();

		IndicesAdminClient indicesAdminClient = adminClient.indices();

		GetMappingsRequestBuilder getMappingsRequestBuilder =
			indicesAdminClient.prepareGetMappings(
				getMappingIndexRequest.getIndexName());

		getMappingsRequestBuilder.setTypes(
			getMappingIndexRequest.getMappingName());

		return getMappingsRequestBuilder;
	}

}