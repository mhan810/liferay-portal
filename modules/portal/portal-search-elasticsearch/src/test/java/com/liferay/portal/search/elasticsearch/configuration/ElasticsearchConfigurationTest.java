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

package com.liferay.portal.search.elasticsearch.configuration;

import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.search.elasticsearch.internal.connection.ElasticsearchFixture;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class ElasticsearchConfigurationTest {

	@Test
	public void testConfigurationsFromBuildTestXmlAntFile() throws Exception {
		ElasticsearchFixture elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchConfigurationTest.class.getSimpleName(),
			_readConfigurationsFromBuildTestXml());

		try {
			elasticsearchFixture.createNode();
		}
		finally {
			elasticsearchFixture.destroyNode();
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> _read(String name) throws Exception {
		Properties properties = new Properties();

		Class<?> clazz = getClass();

		properties.load(clazz.getResourceAsStream(name));

		Map<String, Object> elasticsearchConfigurationProperties =
			PropertiesUtil.toMap(properties);

		return elasticsearchConfigurationProperties;
	}

	private Map<String, Object> _readConfigurationsFromBuildTestXml()
		throws Exception {

		return _read("ElasticsearchConfigurationTest-build-test-xml.cfg");
	}

}