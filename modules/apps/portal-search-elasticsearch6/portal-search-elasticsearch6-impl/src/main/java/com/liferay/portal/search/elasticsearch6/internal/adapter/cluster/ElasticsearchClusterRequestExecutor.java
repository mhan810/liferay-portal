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
import com.liferay.portal.search.engine.adapter.ClusterRequestExecutor;
import com.liferay.portal.search.engine.adapter.ClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(
	immediate = true, property = "search.engine.impl=Elasticsearch",
	service = ClusterRequestExecutor.class
)
public class ElasticsearchClusterRequestExecutor implements
	ClusterRequestExecutor<ClusterResponse> {

	@Override
	public HealthClusterResponse executeClusterRequest(
		HealthClusterRequest healthClusterRequest) {
		return healthClusterRequestTranslator.execute(healthClusterRequest, elasticsearchConnectionManager);
	}

	@Override
	public StateClusterResponse executeClusterRequest(
		StateClusterRequest stateClusterRequest) {
		return stateClusterRequestTranslator.execute(stateClusterRequest, elasticsearchConnectionManager);
	}

	@Override
	public StatsClusterResponse executeClusterRequest(
		StatsClusterRequest statsClusterRequest) {
		return statsClusterRequestTranslator.execute(statsClusterRequest, elasticsearchConnectionManager);
	}

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

	@Reference
	protected HealthClusterRequestTranslator healthClusterRequestTranslator;
	@Reference
	protected StateClusterRequestTranslator stateClusterRequestTranslator;
	@Reference
	protected StatsClusterRequestTranslator statsClusterRequestTranslator;
}
