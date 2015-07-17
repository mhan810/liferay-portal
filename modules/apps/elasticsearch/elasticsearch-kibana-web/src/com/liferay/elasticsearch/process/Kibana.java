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

package com.liferay.elasticsearch.process;

import java.io.File;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.liferay.elasticsearch.configuration.KibanaConfiguration;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.search.elasticsearch.configuration.ElasticsearchConfiguration;
import com.liferay.portal.util.PropsUtil;

import aQute.bnd.annotation.metatype.Configurable;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(immediate = true, configurationPid = "com.liferay.elasticsearch.configuration.KibanaConfiguration", service = Kibana.class)
public class Kibana {

	public Kibana() {
	}

	public String getUrl() {

		return "http://" + kibanaConfiguration.host() + ":" +
			kibanaConfiguration.port();
	}

	@Activate
	protected void activate(Map<String, Object> properties) {

		System.out.println("Kibana.activate(1)");
		kibanaConfiguration = Configurable.createConfigurable(
			KibanaConfiguration.class, properties);
		elasticsearchConfiguration = Configurable.createConfigurable(
			ElasticsearchConfiguration.class, properties);

		kibanaProcess = new KibanaProcess(
			getKibanaHome(), elasticsearchConfiguration, kibanaConfiguration);

		kibanaProcess.start();

	}

	protected String getKibanaHome() {

		String kibanaHomeFolder = kibanaConfiguration.kibanaHomeFolder();

		if (kibanaHomeFolder == null || kibanaHomeFolder.equals("")) {
			kibanaHomeFolder = PropsUtil.get(PropsKeys.LIFERAY_HOME) +
				File.separator + "kibana";
		}

		return kibanaHomeFolder;
	}

	@Deactivate
	protected void shutdown() {

		System.out.println("Kibana.shutdown()");
		kibanaProcess.stop();
	}

	protected volatile ElasticsearchConfiguration elasticsearchConfiguration;
	protected volatile KibanaConfiguration kibanaConfiguration;
	protected KibanaProcess kibanaProcess;
}
