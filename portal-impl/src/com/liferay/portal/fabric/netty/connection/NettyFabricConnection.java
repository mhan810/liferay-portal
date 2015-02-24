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

package com.liferay.portal.fabric.netty.connection;

import com.liferay.portal.fabric.connection.FabricConnection;
import com.liferay.portal.fabric.netty.agent.NettyFabricAgentConfig;
import com.liferay.portal.fabric.netty.client.NettyFabricClientConfig;
import com.liferay.portal.fabric.netty.connection.state.Context;
import com.liferay.portal.fabric.netty.connection.state.InitialState;
import com.liferay.portal.fabric.netty.connection.state.State;
import com.liferay.portal.fabric.netty.handlers.NettyChannelAttributes;
import com.liferay.portal.fabric.repository.Repository;
import com.liferay.portal.fabric.worker.FabricWorker;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;

import java.net.SocketAddress;

import java.nio.file.Path;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricConnection implements Context, FabricConnection {

	public NettyFabricConnection(
		Bootstrap bootstrap, SocketAddress socketAddress,
		NettyFabricClientConfig nettyFabricClientConfig,
		Repository<Channel> repository,
		ConcurrentMap<SocketAddress, NettyFabricConnection>
			registrationMap,
		AtomicInteger activeConnectionCounter) {

		this.bootstrap = bootstrap;
		this.socketAddress = socketAddress;
		this.nettyFabricClientConfig = nettyFabricClientConfig;
		this.repository = repository;
		this.registrationMap = registrationMap;
		this.activeConnectionCounter = activeConnectionCounter;

		reconnectCounter.set(nettyFabricClientConfig.getReconnectCount());

		proceed();
	}

	@Override
	public ChannelFuture connect() {
		ChannelFuture channelFuture = bootstrap.connect(socketAddress);

		Channel channel = channelFuture.channel();

		NettyChannelAttributes.setFabricConnection(
			channel, NettyFabricConnection.this);

		return channelFuture;
	}

	@Override
	public void connected(Channel channel, Future<?> future) {
		if (future.isSuccess()) {
			if (_log.isInfoEnabled()) {
				_log.info("Connected to " + socketAddress);
			}

			reconnectCounter.set(nettyFabricClientConfig.getReconnectCount());
		}
		else if (future.isCancelled()) {
			_log.error("Cancelled connecting to " + socketAddress);
		}
		else {
			_log.error("Unable to connect to " + socketAddress, future.cause());
		}
	}

	@Override
	public synchronized Future<?> disconnect() {
		registrationMap.remove(socketAddress, this);

		reconnectCounter.set(0);

		return terminate();
	}

	@Override
	public final void proceed() {
		State state = stateReference.get();

		state.proceed();
	}

	@Override
	public Future<?> register(Channel channel) {
		Path repositoryPath = repository.getRepositoryPath();

		return channel.writeAndFlush(
			new NettyFabricAgentConfig(repositoryPath.toFile()));
	}

	@Override
	public void registered(Channel channel, Future<?> future) {
		if (future.isSuccess()) {
			if (_log.isInfoEnabled()) {
				_log.info("Registered Netty fabric agent on " + channel);
			}

			activeConnectionCounter.incrementAndGet();
		}
		else if (future.isCancelled()) {
			_log.error(
				"Cancelled registering Netty fabric agent on " + channel);
		}
		else {
			_log.error(
				"Unable to register Netty fabric agent on " + channel,
				future.cause());
		}
	}

	@Override
	public Future<?> schedule() {
		EventLoopGroup eventLoopGroup = bootstrap.group();

		Future<?> future = eventLoopGroup.schedule(
			emptyRunnable, nettyFabricClientConfig.getReconnectInterval(),
			TimeUnit.MILLISECONDS);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Try to reconnect " +
					nettyFabricClientConfig.getReconnectInterval() +
						" ms later");
		}

		return future;
	}

	@Override
	public void scheduled(Future<?> future) {
		if (future.isCancelled()) {
			_log.error("Cancelled scheduled reconnect");
		}
	}

	@Override
	public Future<?> terminate() {
		State state = stateReference.get();

		return state.terminate();
	}

	@Override
	public boolean transit(State fromState, State toState) {
		boolean transited = stateReference.compareAndSet(fromState, toState);

		if (transited) {
			if (_log.isDebugEnabled()) {
				_log.debug("Transited from " + fromState + " to " + toState);
			}
		}
		else if (_log.isDebugEnabled()) {
			_log.debug(
				"Unable to transite from obsoleted " + fromState + " to " +
					toState);
		}

		return transited;
	}

	@Override
	public Future<?> unregister(Channel channel) {
		return channel.closeFuture();
	}

	@Override
	public void unregistered(Channel channel, Future<?> future) {
		if (_log.isInfoEnabled()) {
			_log.info("Disconnected from " + socketAddress);
		}

		Map<Long, FabricWorker<?>> fabricWorkers =
			NettyChannelAttributes.getFabricWorkers(channel);

		for (Map.Entry<Long, FabricWorker<?>> entry :
				fabricWorkers.entrySet()) {

			FabricWorker<?> fabricWorker = entry.getValue();

			NoticeableFuture<?> noticeableFuture =
				fabricWorker.getProcessNoticeableFuture();

			try {
				noticeableFuture.get(
					nettyFabricClientConfig.getExecutionTimeout(),
					TimeUnit.MILLISECONDS);
			}
			catch (Throwable t) {
				noticeableFuture.cancel(true);

				_log.error(
					"Unable to terminate fabric worker " + entry.getKey(), t);
			}
		}

		if (activeConnectionCounter.decrementAndGet() <= 0) {
			repository.dispose(false);

			if (_log.isInfoEnabled()) {
				_log.info(
					"All connections are disconnected, disposed " +
						"repository " + repository.getRepositoryPath());
			}
		}
	}

	protected static final Runnable emptyRunnable = new Runnable() {

		@Override
		public void run() {
		}

	};

	protected final AtomicInteger activeConnectionCounter;
	protected final Bootstrap bootstrap;
	protected final NettyFabricClientConfig nettyFabricClientConfig;
	protected final AtomicInteger reconnectCounter = new AtomicInteger();
	protected final ConcurrentMap<SocketAddress, NettyFabricConnection>
		registrationMap;
	protected final Repository<Channel> repository;
	protected final SocketAddress socketAddress;
	protected final AtomicReference<State> stateReference =
		new AtomicReference<State>(new InitialState(this));

	private static final Log _log = LogFactoryUtil.getLog(
		NettyFabricConnection.class);

}