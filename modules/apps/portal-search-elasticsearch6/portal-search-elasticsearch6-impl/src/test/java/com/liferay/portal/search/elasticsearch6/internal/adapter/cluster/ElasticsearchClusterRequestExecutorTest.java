package com.liferay.portal.search.elasticsearch6.internal.adapter.cluster;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.cluster.HealthClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StateClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.StatsClusterRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Dylan Rebelak
 */
public class ElasticsearchClusterRequestExecutorTest {
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		_elasticsearchClusterRequestExecutor=
			new ElasticsearchClusterRequestExecutor();

		_elasticsearchClusterRequestExecutor.elasticsearchConnectionManager =
			_elasticsearchConnectionManager;

		_elasticsearchClusterRequestExecutor.healthClusterRequestTranslator =
			_healthClusterRequestTranslator;
		_elasticsearchClusterRequestExecutor.stateClusterRequestTranslator =
			_stateClusterRequestTranslator;
		_elasticsearchClusterRequestExecutor.statsClusterRequestTranslator =
			_statsClusterRequestTranslator;
	}

	@Test
	public void testExecuteHealthClusterRequest() {
		HealthClusterRequest healthClusterRequest=
			new HealthClusterRequest(null);

		_elasticsearchClusterRequestExecutor.executeClusterRequest(
			healthClusterRequest);

		Mockito.verify(
			_healthClusterRequestTranslator
		).execute(
			healthClusterRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteStateClusterRequest() {
		StateClusterRequest stateClusterRequest =
			new StateClusterRequest(null);

		_elasticsearchClusterRequestExecutor.executeClusterRequest(
			stateClusterRequest);

		Mockito.verify(
			_stateClusterRequestTranslator
		).execute(
			stateClusterRequest, _elasticsearchConnectionManager
		);
	}

	@Test
	public void testExecuteStatsClusterRequest() {
		StatsClusterRequest statsClusterRequest=
			new StatsClusterRequest(null);

		_elasticsearchClusterRequestExecutor.executeClusterRequest(
			statsClusterRequest);

		Mockito.verify(
			_statsClusterRequestTranslator
		).execute(
			statsClusterRequest, _elasticsearchConnectionManager
		);
	}

	@Mock
	private ElasticsearchConnectionManager _elasticsearchConnectionManager;

	private ElasticsearchClusterRequestExecutor
		_elasticsearchClusterRequestExecutor;

	@Mock
	private HealthClusterRequestTranslator _healthClusterRequestTranslator;

	@Mock
	private StateClusterRequestTranslator _stateClusterRequestTranslator;

	@Mock
	private StatsClusterRequestTranslator _statsClusterRequestTranslator;
}
