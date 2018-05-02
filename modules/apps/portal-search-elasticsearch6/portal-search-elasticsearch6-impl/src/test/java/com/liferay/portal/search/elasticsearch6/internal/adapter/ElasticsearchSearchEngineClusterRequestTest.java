package com.liferay.portal.search.elasticsearch6.internal.adapter;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.elasticsearch6.internal.adapter.cluster.ClusterRequestExecutorFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.ClusterResponse;
import com.liferay.portal.search.engine.adapter.SearchEngine;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterResponse;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.rest.RestStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class ElasticsearchSearchEngineClusterRequestTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchSearchEngineIndexRequestTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);

		AdminClient adminClient =
			_elasticsearchConnectionManager.getAdminClient();

		CreateIndexRequestBuilder createIndexRequestBuilder =
			adminClient.indices().prepareCreate(_INDEX_NAME);

		createIndexRequestBuilder.get();

		_clusterAdminClient = _elasticsearchConnectionManager.getClusterAdminClient();

		_configureEngine();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testExecuteHealthClusterRequest() throws JSONException {
		HealthClusterRequest healthClusterRequest =
			new HealthClusterRequest(_INDEX_NAME);

		HealthClusterResponse healthClusterResponse =
			(HealthClusterResponse)_searchEngine.execute(healthClusterRequest);

		String health = healthClusterResponse.getHealth();
		int status = healthClusterResponse.getStatus();

		JSONFactory jsonFactory = new JSONFactoryImpl();
		JSONObject jsonObject = jsonFactory.createJSONObject(health);

		Assert.assertEquals(RestStatus.OK.getStatus(), status);
		Assert.assertEquals("LiferayElasticsearchCluster", jsonObject.getString("cluster_name"));
		Assert.assertEquals("5", jsonObject.getString("active_shards"));
	}

	@Test
	public void testExecuteStateClusterRequest() throws JSONException {
		StateClusterRequest stateClusterRequest =
			new StateClusterRequest(_INDEX_NAME);

		StateClusterResponse stateClusterResponse =
			(StateClusterResponse)_searchEngine.execute(stateClusterRequest);

		String state = stateClusterResponse.getState();

		System.out.println("STATE >>>");
		System.out.println(state);

/*		JSONFactory jsonFactory = new JSONFactoryImpl();
		JSONObject jsonObject = jsonFactory.createJSONObject(state);

		System.out.println("JSON >>>");
		System.out.println(jsonObject);*/
	}

	@Test
	public void testExecuteStatsClusterRequest() throws JSONException {
		StatsClusterRequest statsClusterRequest =
			new StatsClusterRequest(_INDEX_NAME);

		StatsClusterResponse statsClusterResponse =
			(StatsClusterResponse)_searchEngine.execute(statsClusterRequest);

		String stats = statsClusterResponse.getStats();

		JSONFactory jsonFactory = new JSONFactoryImpl();
		JSONObject jsonObject = jsonFactory.createJSONObject(stats);

		Assert.assertEquals("1", jsonObject.getJSONObject("indices").getString("count"));
	}

	private void _configureEngine() {
		_searchEngine = new ElasticsearchSearchEngineImpl();

		ClusterRequestExecutorFixture clusterRequestExecutorFixture =
			new ClusterRequestExecutorFixture(_elasticsearchConnectionManager);

		((ElasticsearchSearchEngineImpl)_searchEngine).clusterRequestExecutor =
			clusterRequestExecutorFixture.createExecutor();
	}

	private static final String _INDEX_NAME = "test_request_index";

	private ClusterAdminClient _clusterAdminClient;

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;
	private SearchEngine _searchEngine;

}
