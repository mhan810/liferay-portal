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

package com.liferay.portal.fabric.netty.cluster;

import com.liferay.portal.fabric.agent.FabricAgent;
import com.liferay.portal.fabric.agent.FabricAgentListener;
import com.liferay.portal.fabric.agent.FabricAgentRegistry;
import com.liferay.portal.fabric.client.FabricClientUtil;
import com.liferay.portal.fabric.connection.FabricConnection;
import com.liferay.portal.fabric.netty.agent.NettyFabricAgentStub;
import com.liferay.portal.fabric.netty.client.NettyFabricClientConfig;
import com.liferay.portal.fabric.netty.handlers.NettyChannelAttributes;
import com.liferay.portal.fabric.netty.handlers.NettyFabricAgentRegistrationChannelHandler;
import com.liferay.portal.fabric.netty.rpc.ChannelThreadLocal;
import com.liferay.portal.fabric.netty.rpc.RPCCallable;
import com.liferay.portal.fabric.netty.rpc.RPCUtil;
import com.liferay.portal.fabric.netty.rpc.SyncProcessRPCCallable;
import com.liferay.portal.fabric.server.FabricServerUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterNodeResponses;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.concurrent.BaseFutureListener;
import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.concurrent.FutureListener;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.ProcessException;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.util.PropsValues;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;

import java.io.Serializable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricClusterUtil {

	public static final ClusterEventListener CLUSTER_EVENT_LISTENER =
		new ClusterEventListener() {

			@Override
			public void processClusterEvent(ClusterEvent clusterEvent) {
				updateClusterLayout();

				FabricAgentRegistry fabricAgentRegistry =
					(FabricAgentRegistry)PortalBeanLocatorUtil.locate(
						FabricAgentRegistry.class.getName());

				fabricAgentRegistry.registerFabricAgentListener(
					FABRIC_AGENT_LISTENER);
			}

		};

	public static void updateClusterLayout() {
		FutureClusterResponses futureClusterResponses =
			ClusterExecutorUtil.execute(
				ClusterRequest.createMulticastRequest(
					GET_FABRIC_SERVER_INET_SOCKET_ADDRESS_METHOD_HANDLER,
					true));

		futureClusterResponses.addFutureListener(
			UPDATE_CLUSTER_LAYOUT_FUTURE_LISTENER);
	}

	protected static InetSocketAddress getFabricServerInetSocketAddress() {
		InetSocketAddress inetSocketAddress =
			FabricServerUtil.getInetSocketAddress();

		InetAddress inetAddress = inetSocketAddress.getAddress();

		if (inetAddress.isAnyLocalAddress()) {
			ClusterNode clusterNode = ClusterExecutorUtil.getLocalClusterNode();

			inetSocketAddress = new InetSocketAddress(
				clusterNode.getBindInetAddress(),
				PropsValues.PORTAL_FABRIC_SERVER_PORT);
		}

		return inetSocketAddress;
	}

	protected static void notifyFabricAgents(
		Set<InetSocketAddress> inetSocketAddresses,
		InetSocketAddress originInetSocketAddress) {

		FabricAgentRegistry fabricAgentRegistry =
			(FabricAgentRegistry)PortalBeanLocatorUtil.locate(
				FabricAgentRegistry.class.getName());

		for (FabricAgent fabricAgent : fabricAgentRegistry.getFabricAgents()) {
			if (!(fabricAgent instanceof NettyFabricAgentStub)) {
				continue;
			}

			NettyFabricAgentStub nettyFabricAgentStub =
				(NettyFabricAgentStub)fabricAgent;

			RPCUtil.execute(
				nettyFabricAgentStub.getChannel(),
				new UpdateClusterLayoutRPCCallable(
					inetSocketAddresses, originInetSocketAddress));
		}
	}

	protected static final FabricAgentListener FABRIC_AGENT_LISTENER =
		new FabricAgentListener() {

		@Override
		public void registered(FabricAgent fabricAgent) {
			updateClusterLayout();
		}

		@Override
		public void unregistered(FabricAgent fabricAgent) {
		}

	};

	protected static final MethodHandler
		GET_FABRIC_SERVER_INET_SOCKET_ADDRESS_METHOD_HANDLER =
			new MethodHandler(
				new MethodKey(
					NettyFabricClusterUtil.class,
					"getFabricServerInetSocketAddress"));

	protected static final FutureListener<ClusterNodeResponses>
		UPDATE_CLUSTER_LAYOUT_FUTURE_LISTENER =
			new BaseFutureListener<ClusterNodeResponses>() {

				@Override
				public void completeWithResult(
					Future<ClusterNodeResponses> future,
					ClusterNodeResponses clusterNodeResponses) {

					Set<InetSocketAddress> inetSocketAddresses =
						new HashSet<>();

					for (ClusterNodeResponse clusterNodeResponse :
							clusterNodeResponses.getClusterResponses()) {

						try {
							inetSocketAddresses.add(
								(InetSocketAddress)
									clusterNodeResponse.getResult());
						}
						catch (Exception e) {
							_log.error(
								"Unable to get fabric server inet socket " +
									"address from " +
										clusterNodeResponse.getClusterNode() +
											", excluding it from cluster", e);
						}
					}

					InetSocketAddress originInetSocketAddress =
						getFabricServerInetSocketAddress();

					inetSocketAddresses.add(originInetSocketAddress);

					notifyFabricAgents(
						inetSocketAddresses, originInetSocketAddress);
				}

				@Override
				public void completeWithException(
					Future<ClusterNodeResponses> future, Throwable throwable) {

					_log.error("Unable to update cluster layout", throwable);
				}

				@Override
				public void completeWithCancel(
					Future<ClusterNodeResponses> future) {

					_log.error("Cancelled cluster layout update");
				}

			};

	protected static class UnregisterFabricAgentProcessCallable
		implements ProcessCallable<Serializable> {

		@Override
		public Serializable call() throws ProcessException {
			Channel channel = ChannelThreadLocal.getChannel();

			ChannelPipeline channelPipeline = channel.pipeline();

			channelPipeline.remove(
				NettyFabricAgentRegistrationChannelHandler.class);

			FabricAgentRegistry fabricAgentRegistry =
				NettyChannelAttributes.getFabricAgentRegistry(channel);

			fabricAgentRegistry.unregisterFabricAgent(
				NettyChannelAttributes.getNettyFabricAgentStub(channel), null);

			if (_log.isInfoEnabled()) {
				_log.info("Shallowly unregistered fabric agent on " + channel);
			}

			return null;
		}

		private static final Log _log = LogFactoryUtil.getLog(
			UnregisterFabricAgentProcessCallable.class);

		private static final long serialVersionUID = 1L;

	}

	protected static class UpdateClusterLayoutRPCCallable
		implements RPCCallable<Serializable> {

		@Override
		public NoticeableFuture<Serializable> call() {
			Map<SocketAddress, FabricConnection> fabricConnections =
				new HashMap<>(FabricClientUtil.getFabricConnections());

			Iterator<InetSocketAddress> iterator =
				inetSocketAddresses.iterator();

			while (iterator.hasNext()) {
				InetSocketAddress inetSocketAddress = iterator.next();

				if (fabricConnections.remove(inetSocketAddress) != null) {
					iterator.remove();

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Retained exist connection to cluster member at " +
								inetSocketAddress);
					}
				}
			}

			Channel channel = ChannelThreadLocal.getChannel();

			NettyFabricClientConfig nettyFabricClientConfig =
				NettyChannelAttributes.getNettyFabricClientConfig(channel);

			FabricConnection currentFabricConnection =
				NettyChannelAttributes.getFabricConnection(channel);

			for (Map.Entry<SocketAddress, FabricConnection> entry :
					fabricConnections.entrySet()) {

				List<InetSocketAddress> inetSocketAddresses =
					nettyFabricClientConfig.
						getFabricServerInetSocketAddresses();

				final SocketAddress socketAddress = entry.getKey();

				final FabricConnection fabricConnection = entry.getValue();

				if (currentFabricConnection == fabricConnection) {
					long timeout =
						nettyFabricClientConfig.getExecutionTimeout();

					if (_log.isInfoEnabled()) {
						_log.info(
							"Self re-register from fabric server address : " +
								socketAddress + " to : " +
									originInetSocketAddresses +
										". Shallowly unregister from " +
											socketAddress +
												", full shutdown will start " +
													timeout + " ms later.");
					}

					// Shallow unregister to prevent any further incoming jobs.

					inetSocketAddresses.remove(socketAddress);
					inetSocketAddresses.add(originInetSocketAddresses);

					fabricConnection.disconnect();

					RPCUtil.execute(
						channel,
						new SyncProcessRPCCallable<>(
							new UnregisterFabricAgentProcessCallable()));

					EventLoop eventLoop = channel.eventLoop();

					eventLoop.schedule(
						new Runnable() {

							@Override
							public void run() {
								if (_log.isInfoEnabled()) {
									_log.info(
										"Scheduled self re-register full " +
											"shutdown at " + socketAddress);
								}

								fabricConnection.disconnect();
							}
						},
						timeout, TimeUnit.MILLISECONDS);
				}
				else if (!inetSocketAddresses.contains(socketAddress)) {
					if (_log.isInfoEnabled()) {
						_log.info(
							"Disconnecting from departed cluster member at " +
								entry.getKey());
					}

					fabricConnection.disconnect();
				}
			}

			for (InetSocketAddress inetSocketAddress : inetSocketAddresses) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Connecting to newly joined cluster member at " +
							inetSocketAddress);
				}

				FabricClientUtil.connect(inetSocketAddress);
			}

			return new DefaultNoticeableFuture<>();
		}

		protected UpdateClusterLayoutRPCCallable(
			Set<InetSocketAddress> inetSocketAddresses,
			InetSocketAddress originInetSocketAddresses) {

			this.inetSocketAddresses = inetSocketAddresses;
			this.originInetSocketAddresses = originInetSocketAddresses;
		}

		protected final Set<InetSocketAddress> inetSocketAddresses;
		protected final InetSocketAddress originInetSocketAddresses;

		private static final Log _log = LogFactoryUtil.getLog(
			UpdateClusterLayoutRPCCallable.class);

		private static final long serialVersionUID = 1L;

	}

	private static final Log _log = LogFactoryUtil.getLog(
		NettyFabricClusterUtil.class);

}