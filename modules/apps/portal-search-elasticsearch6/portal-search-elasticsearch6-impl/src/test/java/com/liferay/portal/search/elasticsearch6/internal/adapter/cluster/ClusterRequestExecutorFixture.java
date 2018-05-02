package com.liferay.portal.search.elasticsearch6.internal.adapter.cluster;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;

/**
 * @author Dylan Rebelak
 */
public class ClusterRequestExecutorFixture {

	public ClusterRequestExecutorFixture(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
	}

	public ElasticsearchClusterRequestExecutor createExecutor() {
		ElasticsearchClusterRequestExecutor clusterRequestExecutor =
			new ElasticsearchClusterRequestExecutor();

		clusterRequestExecutor.elasticsearchConnectionManager =
			_elasticsearchConnectionManager;

		clusterRequestExecutor.healthClusterRequestTranslator =
			new HealthClusterRequestTranslatorImpl();
		clusterRequestExecutor.stateClusterRequestTranslator =
			new StateClusterRequestTranslatorImpl();
		clusterRequestExecutor.statsClusterRequestTranslator =
			new StatsClusterRequestTranslatorImpl();

		return clusterRequestExecutor;
	}

	private final ElasticsearchConnectionManager
		_elasticsearchConnectionManager;
}
