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

package com.liferay.portal.search.elasticsearch6.internal.adapter;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.elasticsearch6.internal.adapter.cluster.ClusterRequestExecutorFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.cluster.ClusterHealthStatus;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.ClusterAdminClient;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class ElasticsearchSearchEngineAdapterClusterRequestTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchSearchEngineAdapterIndexRequestTest.class.
				getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);

		AdminClient adminClient =
			_elasticsearchConnectionManager.getAdminClient();

		CreateIndexRequestBuilder createIndexRequestBuilder =
			adminClient.indices().prepareCreate(_INDEX_NAME);

		createIndexRequestBuilder.get();

		_clusterAdminClient =
			_elasticsearchConnectionManager.getClusterAdminClient();

		_configureSearchEngineAdapter();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testExecuteHealthClusterRequest() throws JSONException {
		HealthClusterRequest healthClusterRequest = new HealthClusterRequest(
			_INDEX_NAME);

		HealthClusterResponse healthClusterResponse =
			_searchEngineAdapter.execute(healthClusterRequest);

		Assert.assertEquals(
			ClusterHealthStatus.GREEN,
			healthClusterResponse.getClusterHealthStatus());

		String healthStatusMessage =
			healthClusterResponse.getHealthStatusMessage();

		JSONFactory jsonFactory = new JSONFactoryImpl();

		JSONObject jsonObject = jsonFactory.createJSONObject(
			healthStatusMessage);

		Assert.assertEquals(
			"LiferayElasticsearchCluster",
			jsonObject.getString("cluster_name"));
		Assert.assertEquals("5", jsonObject.getString("active_shards"));
	}

	@Test
	public void testExecuteStateClusterRequest() throws JSONException {
		StateClusterRequest stateClusterRequest = new StateClusterRequest(
			_INDEX_NAME);

		StateClusterResponse stateClusterResponse =
			_searchEngineAdapter.execute(stateClusterRequest);

		String state = stateClusterResponse.getStateMessage();

		System.out.println("STATE >>>");
		System.out.println(state);

		/* JSONFactory jsonFactory = new JSONFactoryImpl();

		JSONObject jsonObject = jsonFactory.createJSONObject(state);

		System.out.println("JSON >>>");
		System.out.println(jsonObject);*/
	}

	@Test
	public void testExecuteStatsClusterRequest() throws JSONException {
		StatsClusterRequest statsClusterRequest = new StatsClusterRequest(
			_INDEX_NAME);

		StatsClusterResponse statsClusterResponse =
			_searchEngineAdapter.execute(statsClusterRequest);

		Assert.assertEquals(
			ClusterHealthStatus.GREEN,
			statsClusterResponse.getClusterHealthStatus());

		String statusMessage = statsClusterResponse.getStatsMessage();

		JSONFactory jsonFactory = new JSONFactoryImpl();

		JSONObject jsonObject = jsonFactory.createJSONObject(statusMessage);

		Assert.assertEquals(
			"1", jsonObject.getJSONObject("indices").getString("count"));
	}

	private void _configureSearchEngineAdapter() {
		_searchEngineAdapter = new ElasticsearchSearchEngineAdapterImpl();

		ClusterRequestExecutorFixture clusterRequestExecutorFixture =
			new ClusterRequestExecutorFixture(_elasticsearchConnectionManager);

		((ElasticsearchSearchEngineAdapterImpl)_searchEngineAdapter).
			clusterRequestExecutor =
				clusterRequestExecutorFixture.createExecutor();
	}

	private static final String _INDEX_NAME = "test_request_index";

	private ClusterAdminClient _clusterAdminClient;
	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;
	private SearchEngineAdapter _searchEngineAdapter;

}