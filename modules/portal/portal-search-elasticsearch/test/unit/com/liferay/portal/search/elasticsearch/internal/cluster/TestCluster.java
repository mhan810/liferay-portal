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

package com.liferay.portal.search.elasticsearch.internal.cluster;

import com.liferay.portal.search.elasticsearch.internal.connection.ElasticsearchFixture;

/**
 * @author André de Oliveira
 */
public class TestCluster implements UnicastContext {

	public TestCluster(int size, Object object) {
		_elasticsearchFixtures = new ElasticsearchFixture[size];

		Class<?> clazz = object.getClass();

		_prefix = clazz.getSimpleName();
	}

	public ElasticsearchFixture createNode(int i) throws Exception {
		ElasticsearchFixture elasticsearchFixture = new ElasticsearchFixture(
			_prefix + "-" + i);

		elasticsearchFixture.setUnicastContext(this);

		elasticsearchFixture.createNode();

		_elasticsearchFixtures[i] = elasticsearchFixture;

		return elasticsearchFixture;
	}

	public void createNodes() throws Exception {
		for (int i = 0; i < _elasticsearchFixtures.length; i++) {
			createNode(i);
		}
	}

	public void destroyNode(int i) throws Exception {
		if (_elasticsearchFixtures[i] != null) {
			_elasticsearchFixtures[i].destroyNode();
			_elasticsearchFixtures[i] = null;
		}
	}

	public void destroyNodes() throws Exception {
		for (int i = 0; i < _elasticsearchFixtures.length; i++) {
			destroyNode(i);
		}
	}

	@Override
	public String[] getHosts() {
		_hostCount++;

		String[] hosts = new String[_hostCount];

		for (int i = 0; i < hosts.length; i++) {
			int port = 9300 + i;
			hosts[i] = "127.0.0.1:" + port;
		}

		return hosts;
	}

	public ElasticsearchFixture getNode(int i) {
		return _elasticsearchFixtures[i];
	}

	@Override
	public boolean isUnicastWanted() {
		return true;
	}

	public void setUp() throws Exception {
		createNodes();
	}

	public void tearDown() throws Exception {
		destroyNodes();
	}

	private final ElasticsearchFixture[] _elasticsearchFixtures;
	private int _hostCount;
	private final String _prefix;

}