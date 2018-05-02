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
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.xpack.watcher.watch.Payload;

import java.io.IOException;

/**
 * @author Dylan Rebelak
 */
public class StatsClusterRequestTranslatorImpl implements StatsClusterRequestTranslator{
	@Override
	public StatsClusterResponse execute(
		StatsClusterRequest statsClusterRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		ClusterStatsRequestBuilder clusterStatsRequestBuilder =
			createBuilder(statsClusterRequest, elasticsearchConnectionManager);

		ClusterStatsResponse clusterStatsResponse =
			clusterStatsRequestBuilder.get();

		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder();

			xContentBuilder.startObject();

			xContentBuilder = clusterStatsResponse.toXContent(xContentBuilder,
				Payload.XContent.EMPTY_PARAMS);

			xContentBuilder.endObject();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return new StatsClusterResponse(xContentBuilder.toString());
	}

	protected ClusterStatsRequestBuilder createBuilder(
		StatsClusterRequest statsClusterRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		ClusterAdminClient clusterAdminClient =
			elasticsearchConnectionManager.getClusterAdminClient();

		return clusterAdminClient.prepareClusterStats();
	}
}
