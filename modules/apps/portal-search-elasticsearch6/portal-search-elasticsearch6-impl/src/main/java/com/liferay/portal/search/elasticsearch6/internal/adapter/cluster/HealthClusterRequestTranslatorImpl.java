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

package com.liferay.portal.search.elasticsearch6.internal.adapter.cluster;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.rest.RestStatus;

/**
 * @author Dylan Rebelak
 */
public class HealthClusterRequestTranslatorImpl implements HealthClusterRequestTranslator{
	@Override
	public HealthClusterResponse execute(
		HealthClusterRequest healthClusterRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		ClusterHealthRequestBuilder clusterHealthRequestBuilder =
			createBuilder(healthClusterRequest, elasticsearchConnectionManager);

		ClusterHealthResponse clusterHealthResponse =
			clusterHealthRequestBuilder.get();

		RestStatus status = clusterHealthResponse.status();

		String health = clusterHealthResponse.toString();

		return new HealthClusterResponse(health, status.getStatus());
	}

	protected ClusterHealthRequestBuilder createBuilder(
		HealthClusterRequest healthClusterRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		ClusterAdminClient clusterAdminClient =
			elasticsearchConnectionManager.getClusterAdminClient();

		return clusterAdminClient.prepareHealth(
			healthClusterRequest.getIndexName());
	}
}
