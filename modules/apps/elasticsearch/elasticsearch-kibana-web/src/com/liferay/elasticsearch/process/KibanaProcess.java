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
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.OS;

import com.liferay.elasticsearch.configuration.KibanaConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.elasticsearch.configuration.ElasticsearchConfiguration;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class KibanaProcess {

	public KibanaProcess(
		String kibanaHome,
		ElasticsearchConfiguration elasticsearchConfiguration,
		KibanaConfiguration kibanaConfiguration) {

		this.elasticsearchConfiguration = elasticsearchConfiguration;
		this.kibanaConfiguration = kibanaConfiguration;
		this.kibanaHome = kibanaHome;

		updateConfigurationFile();
	}

	public void start() {

		try {
			runExecutable();
		}
		catch (IOException e) {
			_log.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void stop() {

		_kibanaExecutor.getWatchdog().destroyProcess();
	}

	protected String getExecutablePath() {

		String executableName = "kibana";

		if (OS.isFamilyWindows()) {
			executableName += ".bat";
		}

		return kibanaHome + File.separator + "bin" + File.separator +
			executableName;
	}

	protected void updateConfigurationFile() {

		String host = elasticsearchConfiguration.networkHost();
		int port = elasticsearchConfiguration.httpPort();

		kibanaConfigurationWriter = new KibanaConfigurationWriter(
			host, port, kibanaHome, kibanaConfiguration);
		kibanaConfigurationWriter.persistNewPropertyValues();
	}

	private void runExecutable()
		throws ExecuteException, IOException {

		CommandLine cmdLine = new CommandLine(getExecutablePath());

		DefaultExecuteResultHandler resultHandler =
			new DefaultExecuteResultHandler();
		ExecuteWatchdog watchdog =
			new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		_kibanaExecutor = new DefaultExecutor();
		_kibanaExecutor.setExitValue(1);
		_kibanaExecutor.setWatchdog(watchdog);
		_kibanaExecutor.execute(cmdLine, resultHandler);
	}

	private static final Log _log = LogFactoryUtil.getLog(KibanaProcess.class);
	protected volatile ElasticsearchConfiguration elasticsearchConfiguration;
	protected volatile KibanaConfiguration kibanaConfiguration;
	protected KibanaConfigurationWriter kibanaConfigurationWriter;
	protected String kibanaHome;
	private DefaultExecutor _kibanaExecutor;
}
