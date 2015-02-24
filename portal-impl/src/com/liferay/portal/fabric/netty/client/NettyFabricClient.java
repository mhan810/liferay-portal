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

package com.liferay.portal.fabric.netty.client;

import com.liferay.portal.fabric.agent.FabricAgent;
import com.liferay.portal.fabric.client.FabricClient;
import com.liferay.portal.fabric.connection.FabricConnection;
import com.liferay.portal.fabric.local.agent.LocalFabricAgent;
import com.liferay.portal.fabric.netty.codec.serialization.AnnotatedObjectDecoder;
import com.liferay.portal.fabric.netty.codec.serialization.AnnotatedObjectEncoder;
import com.liferay.portal.fabric.netty.connection.NettyFabricConnection;
import com.liferay.portal.fabric.netty.fileserver.handlers.FileRequestChannelHandler;
import com.liferay.portal.fabric.netty.fileserver.handlers.FileResponseChannelHandler;
import com.liferay.portal.fabric.netty.handlers.NettyChannelAttributes;
import com.liferay.portal.fabric.netty.handlers.NettyFabricWorkerExecutionChannelHandler;
import com.liferay.portal.fabric.netty.repository.NettyRepository;
import com.liferay.portal.fabric.netty.rpc.handlers.NettyRPCChannelHandler;
import com.liferay.portal.fabric.netty.util.NettyUtil;
import com.liferay.portal.fabric.repository.Repository;
import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.concurrent.FutureListener;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.process.ProcessExecutor;
import com.liferay.portal.kernel.util.NamedThreadFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.IOException;

import java.lang.Thread.State;

import java.net.SocketAddress;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricClient implements FabricClient {

	public NettyFabricClient(
			ProcessExecutor processExecutor,
			NettyFabricClientConfig nettyFabricClientConfig,
			Runnable shutdownCallback)
		throws IOException {

		this.nettyFabricClientConfig = nettyFabricClientConfig;
		this.shutdownCallback = shutdownCallback;

		fabricAgent = new LocalFabricAgent(processExecutor);

		bootstrap = new Bootstrap();

		bootstrap.channel(NioSocketChannel.class);

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(
			nettyFabricClientConfig.getEventLoopGroupThreadCount(),
			new NamedThreadFactory(
				"Netty Fabric Client/NIO Event Loop Group",
				Thread.NORM_PRIORITY, null));

		bootstrap.group(eventLoopGroup);

		executionEventExecutorGroup = createEventExecutorGroup(
			nettyFabricClientConfig.getExecutionGroupThreadCount(),
			"Netty Fabric Client/Execution Event Executor Group");
		fileServerEventExecutorGroup = createEventExecutorGroup(
			nettyFabricClientConfig.getFileServerGroupThreadCount(),
			"Netty Fabric Client/File Server Event Executor Group");
		rpcEventExecutorGroup = createEventExecutorGroup(
			nettyFabricClientConfig.getRPCGroupThreadCount(),
			"Netty Fabric Client/RPC Event Executor Group");

		bootstrap.handler(new NettyFabricClientChannelInitializer());

		Path repositoryPath = nettyFabricClientConfig.getRepositoryPath();

		Files.createDirectories(repositoryPath);

		repository = new NettyRepository(
			repositoryPath,
			nettyFabricClientConfig.getRepositoryGetFileTimeout());

		Runtime runtime = Runtime.getRuntime();

		runtime.addShutdownHook(shutdownThread);
	}

	@Override
	public NettyFabricConnection connect(SocketAddress socketAddress) {
		NettyFabricConnection nettyFabricConnection =
			new NettyFabricConnection(
				bootstrap, socketAddress, nettyFabricClientConfig, repository,
				nettyFabricConnections, activeConnectionCounter);

		NettyFabricConnection previousNettyFabricConnection =
			nettyFabricConnections.putIfAbsent(
				socketAddress, nettyFabricConnection);

		if (previousNettyFabricConnection != null) {
			nettyFabricConnection.disconnect();

			nettyFabricConnection = previousNettyFabricConnection;
		}

		return nettyFabricConnection;
	}

	@Override
	public Map<SocketAddress, FabricConnection> getFabricConnections() {
		return Collections.<SocketAddress, FabricConnection>unmodifiableMap(
			nettyFabricConnections);
	}

	@Override
	public NoticeableFuture<?> shutdown() {
		DefaultNoticeableFuture<Object> defaultNoticeableFuture =
			new DefaultNoticeableFuture<>();

		defaultNoticeableFuture.addFutureListener(new ShutdownFutureListener());

		try {
			for (Map.Entry<SocketAddress, NettyFabricConnection> entry :
					nettyFabricConnections.entrySet()) {

				SocketAddress socketAddress = entry.getKey();

				NettyFabricConnection nettyFabricConnection = entry.getValue();

				try {
					NettyUtil.syncFully(nettyFabricConnection.disconnect());
				}
				catch (Exception e) {
					_log.error(
						"Error on disconnecting Netty fabric connection with " +
							"address " + socketAddress,
						e);
				}
			}

			repository.dispose(true);
		}
		catch (Throwable throwable) {
			defaultNoticeableFuture.setException(throwable);
		}
		finally {
			EventLoopGroup eventLoopGroup = bootstrap.group();

			NettyUtil.syncFutures(
				eventLoopGroup.shutdownGracefully(
					nettyFabricClientConfig.getShutdownQuietPeriod(),
					nettyFabricClientConfig.getShutdownTimeout(),
					TimeUnit.MILLISECONDS),
				defaultNoticeableFuture);
		}

		return defaultNoticeableFuture;
	}

	protected final EventExecutorGroup createEventExecutorGroup(
		int threadCount, String threadPoolName) {

		EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(
			threadCount,
			new NamedThreadFactory(threadPoolName, Thread.NORM_PRIORITY, null));

		NettyUtil.bindShutdown(
			bootstrap.group(), eventExecutorGroup,
			nettyFabricClientConfig.getShutdownQuietPeriod(),
			nettyFabricClientConfig.getShutdownTimeout());

		return eventExecutorGroup;
	}

	protected final AtomicInteger activeConnectionCounter = new AtomicInteger();
	protected final Bootstrap bootstrap;
	protected final EventExecutorGroup executionEventExecutorGroup;
	protected final FabricAgent fabricAgent;
	protected final EventExecutorGroup fileServerEventExecutorGroup;
	protected final NettyFabricClientConfig nettyFabricClientConfig;
	protected final ConcurrentMap<SocketAddress, NettyFabricConnection>
		nettyFabricConnections = new ConcurrentHashMap<>();
	protected final Repository<Channel> repository;
	protected final EventExecutorGroup rpcEventExecutorGroup;
	protected final Runnable shutdownCallback;

	protected final Thread shutdownThread = new Thread() {

		@Override
		public void run() {
			shutdown();
		}

	};

	protected class NettyFabricClientChannelInitializer
		extends ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel channel) {
			NettyChannelAttributes.setNettyFabricClientConfig(
				channel, nettyFabricClientConfig);

			ChannelPipeline channelPipeline = channel.pipeline();

			channelPipeline.addLast(
				AnnotatedObjectEncoder.NAME, AnnotatedObjectEncoder.INSTANCE);
			channelPipeline.addLast(
				AnnotatedObjectDecoder.NAME, new AnnotatedObjectDecoder());
			channelPipeline.addLast(
				fileServerEventExecutorGroup, FileRequestChannelHandler.NAME,
				new FileRequestChannelHandler(
					nettyFabricClientConfig.
						getFileServerFolderCompressionLevel()));
			channelPipeline.addLast(
				new FileResponseChannelHandler(
					repository.getAsyncBroker(), fileServerEventExecutorGroup));
			channelPipeline.addLast(
				rpcEventExecutorGroup, NettyRPCChannelHandler.NAME,
				NettyRPCChannelHandler.INSTANCE);
			channelPipeline.addLast(
				executionEventExecutorGroup,
				new NettyFabricWorkerExecutionChannelHandler(
					repository, fabricAgent,
					nettyFabricClientConfig.getExecutionTimeout()));
		}

	}

	protected class ShutdownFutureListener implements FutureListener<Object> {

		@Override
		public void complete(java.util.concurrent.Future<Object> future) {
			shutdownCallback.run();

			if (shutdownThread.getState() == State.NEW) {
				Runtime runtime = Runtime.getRuntime();

				runtime.removeShutdownHook(shutdownThread);
			}
		}

	}

	private static final Log _log = LogFactoryUtil.getLog(
		NettyFabricClient.class);

}