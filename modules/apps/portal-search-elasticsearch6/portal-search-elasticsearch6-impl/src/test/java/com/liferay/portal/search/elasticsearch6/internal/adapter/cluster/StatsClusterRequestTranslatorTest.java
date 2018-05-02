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

import com.liferay.portal.search.elasticsearch6.internal.adapter.index.GetFieldMappingIndexRequestTranslatorTest;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequest;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class StatsClusterRequestTranslatorTest {

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
	public void testClusterRequestTranslation() {
		StatsClusterRequest statsClusterRequest =
			new StatsClusterRequest(_INDEX_NAME);

		StatsClusterRequestTranslatorImpl statsClusterRequestTranslator =
			new StatsClusterRequestTranslatorImpl();

		ClusterStatsRequestBuilder clusterStatsRequestBuilder =
			statsClusterRequestTranslator.createBuilder(
				statsClusterRequest, _elasticsearchConnectionManager);

		ClusterStatsRequest clusterStatsRequest =
			clusterStatsRequestBuilder.request();

		Assert.assertNotNull(clusterStatsRequest);
	}

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;

	private static final String _INDEX_NAME = "test_request_index";
}
