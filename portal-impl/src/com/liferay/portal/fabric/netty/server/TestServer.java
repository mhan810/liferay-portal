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

package com.liferay.portal.fabric.netty.server;

import com.liferay.portal.fabric.FabricProcessExecutor;
import com.liferay.portal.fabric.InputResource;
import com.liferay.portal.fabric.OutputResource;
import com.liferay.portal.fabric.agent.FabricAgent;
import com.liferay.portal.fabric.agent.FabricAgentRegistry;
import com.liferay.portal.fabric.agent.selectors.FabricAgentSelector;
import com.liferay.portal.fabric.local.agent.LocalFabricAgent;
import com.liferay.portal.fabric.netty.fileserver.FileHelperUtil;
import com.liferay.portal.fabric.server.FabricServerUtil;
import com.liferay.portal.fabric.worker.FabricWorker;
import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.ProcessConfig;
import com.liferay.portal.kernel.process.ProcessException;
import com.liferay.portal.kernel.process.ProcessExecutor;
import com.liferay.portal.kernel.process.ProcessExecutorUtil;
import com.liferay.portal.kernel.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import io.netty.util.ResourceLeakDetector;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shuyang Zhou
 */
public class TestServer {

	public static void main(String[] args) throws Exception {
		ClassLoaderPool.register("", TestServer.class.getClassLoader());

		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

		PortalClassLoaderUtil.setClassLoader(TestServer.class.getClassLoader());

		ProcessExecutorUtil processExecutorUtil = new ProcessExecutorUtil();

		ProcessExecutor processExecutor = new LocalProcessExecutor();

		processExecutorUtil.setProcessExecutor(processExecutor);

		Logger logger = Logger.getLogger(FileHelperUtil.class.getName());

		logger.setLevel(Level.FINEST);

		for (Handler handler : Logger.getLogger("").getHandlers()) {
			handler.setLevel(Level.FINEST);
		}

		//--------------------------------------------------------------------//

		Files.write(
			Paths.get(System.getProperty("user.home") + "/test-input"),
			"This is test input!".getBytes());
		Files.write(
			Paths.get(System.getProperty("user.home") + "/test-input-array1"),
			"This is test input array1!".getBytes());
		Files.write(
			Paths.get(System.getProperty("user.home") + "/test-input-array2"),
			"This is test input array2!".getBytes());

		FabricAgentRegistry fabricAgentRegistry = new FabricAgentRegistry(
			new LocalFabricAgent(processExecutor));

		FabricAgentSelector fabricAgentSelector = new FabricAgentSelector() {

			@Override
			public Collection<FabricAgent> select(
				Collection<FabricAgent> fabricAgents,
				ProcessCallable<?> processCallable) {

				List<FabricAgent> fabricAgentsList = new ArrayList<>();

				for (FabricAgent fabricAgent : fabricAgents) {
					if (!(fabricAgent instanceof LocalFabricAgent)) {
						fabricAgentsList.add(fabricAgent);
					}
				}

				if (fabricAgentsList.isEmpty()) {
					return fabricAgents;
				}

				return fabricAgentsList;
			}

		};

		FabricProcessExecutor fabricProcessExecutor = new FabricProcessExecutor(
			fabricAgentRegistry, fabricAgentSelector);

		NettyFabricServerConfig nettyFabricServerConfig =
			new NettyFabricServerConfig();

		NettyFabricServer nettyFabricServer = new NettyFabricServer(
			fabricAgentRegistry, nettyFabricServerConfig);

		FabricServerUtil fabricServerUtil = new FabricServerUtil();

		fabricServerUtil.setFabricServer(nettyFabricServer);

		//--------------------------------------------------------------------//

		FabricServerUtil.start();

		ProcessConfig processConfig = new ProcessConfig.Builder().build();
		ProcessCallable<String> processCallable =
			new HelloWorldProcessCallable();

		FabricWorker<String> fabricWorker = fabricProcessExecutor.execute(
			processConfig, processCallable);

		System.out.println(
			fabricWorker.getFabricStatus().getAdvancedOperatingSystemMXBean().
				getSystemCpuLoad());

		Future<String> pingFuture = fabricWorker.write(
			new PingProcessCallable());

		System.out.println("Ping result : " + pingFuture.get());

		Future<String> future = fabricWorker.getProcessNoticeableFuture();

		System.out.println("Location 1 : " + future.get());

		Thread.sleep(10 * 1000);

		fabricWorker = fabricProcessExecutor.execute(
			processConfig, processCallable);

		System.out.println(
			fabricWorker.getFabricStatus().getAdvancedOperatingSystemMXBean().
				getSystemCpuLoad());

		pingFuture = fabricWorker.write(new PingProcessCallable());

		System.out.println("Ping result : " + pingFuture.get());

		future = fabricWorker.getProcessNoticeableFuture();

		System.out.println("Location 2 : " + future.get());

		FabricServerUtil.stop();
	}

	private static final class HelloWorldProcessCallable
		implements ProcessCallable<String> {

		@Override
		public String call() throws ProcessException {
			System.out.println("Hello World!!!!!!");

			try {
				System.out.println(
					"test-input : " +
						new String(Files.readAllBytes(_testInput.toPath())));

				System.out.println(
					"test-input-array1 : " +
						new String(
							Files.readAllBytes(_testInputArray[0].toPath())));
				System.out.println(
					"test-input-array2 : " +
						new String(
							Files.readAllBytes(_testInputArray[1].toPath())));

				Files.write(_testOutput.toPath(), "test-ouput".getBytes());
				Files.write(
					_testOutputArray[0].toPath(),
					"test-ouput-array1".getBytes());
				Files.write(
					_testOutputArray[1].toPath(),
					"test-ouput-array2".getBytes());

				Thread.sleep(5000);
			}
			catch (Exception e) {
				throw new ProcessException(e);
			}

			return "I said Hello world!!!!!!";
		}

		private static final long serialVersionUID = 1L;

		@InputResource
		private final File _testInput = new File(
			System.getProperty("user.home") + "/test-input");

		@InputResource
		private final File[] _testInputArray = new File[] {
			new File(System.getProperty("user.home") + "/test-input-array1"),
			new File(System.getProperty("user.home") + "/test-input-array2")};

		@OutputResource
		private final File _testOutput = new File(
			System.getProperty("user.home") + "/test-output");

		@OutputResource
		private final File[] _testOutputArray = new File[] {
			new File(System.getProperty("user.home") + "/test-output-array1"),
			new File(System.getProperty("user.home") + "/test-output-array2")};

	}

	private static class PingProcessCallable
		implements ProcessCallable<String> {

		@Override
		public String call() throws ProcessException {
			String pingMessage = "This is a ping message!";

			System.out.println(pingMessage);

			return pingMessage;
		}

	}

}