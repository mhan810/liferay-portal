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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.liferay.elasticsearch.configuration.KibanaConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class KibanaConfigurationWriter {

	public KibanaConfigurationWriter(
		String host, int port, String kibanaHome,
		KibanaConfiguration kibanaConfiguration) {
		this.kibanaConfiguration = kibanaConfiguration;
		this.kibanaHome = kibanaHome;

		updateConfigProperties(host, port);
	}

	public void persistNewPropertyValues() {

		backupConfiguration();
		
		String confilePath = getConfigFilePath();
		File configFile = new File(confilePath);
		try {
			FileWriter fileWriter = new FileWriter(configFile);
			configFileContent.dump(yamlProperties, fileWriter);
		}
		catch (IOException e) {
			_log.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected void backupConfiguration() {

		String confilePath = getConfigFilePath();
		File destination = new File(confilePath + ".source");

		if (!destination.exists()) {
			try {
				File origin = new File(confilePath);
				Files.copy(origin.toPath(), destination.toPath());
			}
			catch (IOException e) {
				_log.error(e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	protected String getConfigFilePath() {

		String configFileName = "kibana.yml";

		return kibanaHome + File.separator + "config" + File.separator +
			configFileName;
	}

	protected void loadFileContent() {

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setPrettyFlow(true);
		options.setIndent(4);
		options.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());

		configFileContent = new Yaml(options);

		FileReader fileReader;
		try {
			fileReader = new FileReader(new File(getConfigFilePath()));
			yamlProperties =
				(Map<String, Object>) configFileContent.load(fileReader);
		}
		catch (FileNotFoundException e) {
			_log.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	protected void updateConfigProperties(String host, int port) {

		loadFileContent();
		
		yamlProperties.put(
			"default_app_id", kibanaConfiguration.defaultAppId());
		yamlProperties.put(
			"elasticsearch_preserve_host",
			kibanaConfiguration.elasticsearchPreserveHost());
		yamlProperties.put("host", kibanaConfiguration.host());
		yamlProperties.put("elasticsearch_url", "http://" + host + ":" + port);
		yamlProperties.put(
			"kibana_home_folder", kibanaConfiguration.kibanaHomeFolder());
		yamlProperties.put("kibana_index", kibanaConfiguration.kibanaIndex());
		yamlProperties.put("port", kibanaConfiguration.port());
		yamlProperties.put(
			"request_timeout", kibanaConfiguration.requestTimeout());
		yamlProperties.put("shard_timeout", kibanaConfiguration.shardTimeout());
		yamlProperties.put("verify_ssl", kibanaConfiguration.verifySsl());
	}

	private static final Log _log =
		LogFactoryUtil.getLog(KibanaConfigurationWriter.class);
	protected Yaml configFileContent;
	protected KibanaConfiguration kibanaConfiguration;
	protected String kibanaHome;
	protected Map<String, Object> yamlProperties;
}
