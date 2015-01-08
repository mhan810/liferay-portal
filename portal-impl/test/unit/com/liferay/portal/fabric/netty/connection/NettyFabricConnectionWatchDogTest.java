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

import com.liferay.portal.fabric.netty.NettyTestUtil;
import com.liferay.portal.fabric.netty.agent.NettyFabricAgentConfig;
import com.liferay.portal.fabric.netty.client.NettyFabricClientConfig;
import com.liferay.portal.fabric.netty.connection.NettyFabricConnection.PostConnectChannelFutureListener;
import com.liferay.portal.fabric.netty.connection.NettyFabricConnection.PostDisconnectReconnectChannelFutureListener;
import com.liferay.portal.fabric.netty.connection.NettyFabricConnection.PostDisconnectUnregisterChannelFutureListener;
import com.liferay.portal.fabric.netty.connection.NettyFabricConnection.PostRegisterChannelFutureListener;
import com.liferay.portal.fabric.repository.MockRepository;
import com.liferay.portal.fabric.repository.Repository;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.PropsKeys;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;

import java.net.SocketAddress;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricConnectionWatchDogTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() throws Exception {
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(_embeddedChannel.eventLoop());
		bootstrap.channelFactory(
			new ChannelFactory<Channel>() {

			@Override
			public Channel newChannel() {
				_embeddedChannel.deregister();

				return _embeddedChannel;
			}

		});
		bootstrap.handler(
			new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) {
				}

			});

		Properties properties = new Properties();

		properties.setProperty(
			PropsKeys.PORTAL_FABRIC_CLIENT_RECONNECT_COUNT, String.valueOf(0));
		properties.setProperty(
			PropsKeys.PORTAL_FABRIC_CLIENT_RECONNECT_INTERVAL,
			String.valueOf(Long.MAX_VALUE));

		ConcurrentMap<SocketAddress, NettyFabricConnection>
			registrationMap = new ConcurrentHashMap<>();

		_nettyFabricConnectionWatchDog = new NettyFabricConnection(
			bootstrap, _embeddedChannel.remoteAddress(),
			new NettyFabricClientConfig("test", properties), _repository,
			registrationMap, _activeConnectionCounter);

		registrationMap.put(
			_embeddedChannel.remoteAddress(), _nettyFabricConnectionWatchDog);

		Assert.assertNull(_nettyFabricConnectionWatchDog.getFabricConnection());
		Assert.assertSame(
			_embeddedChannel.remoteAddress(),
			_nettyFabricConnectionWatchDog.getSocketAddress());

		_nettyFabricConnectionWatchDog.disconnect();

		Assert.assertTrue(registrationMap.isEmpty());

		AtomicInteger reconnectCounter =
			_nettyFabricConnectionWatchDog.reconnectCounter;

		Assert.assertEquals(0, reconnectCounter.get());
	}

	@Test
	public void testPostConnectChannelFutureListener() {

		// Cancelled

		PostConnectChannelFutureListener postConnectChannelFutureListener =
			_nettyFabricConnectionWatchDog.
				new PostConnectChannelFutureListener();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			ChannelFuture channelFuture = _embeddedChannel.newPromise();

			channelFuture.cancel(true);

			postConnectChannelFutureListener.operationComplete(channelFuture);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Cancelled connecting to " + _embeddedChannel.remoteAddress(),
				logRecord.getMessage());
		}

		// Failed

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			Throwable throwable = new Throwable();

			postConnectChannelFutureListener.operationComplete(
				_embeddedChannel.newFailedFuture(throwable));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to connect to " + _embeddedChannel.remoteAddress(),
				logRecord.getMessage());
			Assert.assertSame(throwable, logRecord.getThrown());
		}

		// Connected with log

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			postConnectChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			Object object = _embeddedChannel.readOutbound();

			Assert.assertSame(NettyFabricAgentConfig.class, object.getClass());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Connected to " + _embeddedChannel.remoteAddress(),
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Registered Netty fabric agent on " + _embeddedChannel,
				logRecord.getMessage());
		}

		// Connected without log

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.OFF)) {

			postConnectChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			Object object = _embeddedChannel.readOutbound();

			Assert.assertSame(NettyFabricAgentConfig.class, object.getClass());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
		}
	}

	@Test
	public void testPostDisconnectReconnectChannelFutureListener()
		throws Exception {

		// No reconnect

		PostDisconnectReconnectChannelFutureListener
			postDisconnectReconnectChannelFutureListener =
				_nettyFabricConnectionWatchDog.
					new PostDisconnectReconnectChannelFutureListener();

		AtomicInteger reconnectCounter =
			_nettyFabricConnectionWatchDog.reconnectCounter;

		reconnectCounter.set(0);

		postDisconnectReconnectChannelFutureListener.operationComplete(
			_embeddedChannel.newSucceededFuture());

		// Reconnect, with log

		reconnectCounter.set(1);

		ReflectionTestUtil.setFieldValue(
			_nettyFabricConnectionWatchDog.bootstrap, "group",
			NettyTestUtil.directScheduleEventLoopGroup(
				ReflectionTestUtil.<EventLoopGroup>getFieldValue(
					_nettyFabricConnectionWatchDog.bootstrap, "group")));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			postDisconnectReconnectChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Try to reconnect " + Long.MAX_VALUE + " ms later",
				logRecord.getMessage());
		}

		// Reconnect, without log

		reconnectCounter.set(1);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			postDisconnectReconnectChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
		}
	}

	@Test
	public void testPostDisconnectUnregisterChannelFutureListener() {

		// Dispose, with log

		PostDisconnectUnregisterChannelFutureListener
			postDisconnectUnregisterChannelFutureListener =
				_nettyFabricConnectionWatchDog.
					new PostDisconnectUnregisterChannelFutureListener();

		_activeConnectionCounter.set(1);

		ReflectionTestUtil.setFieldValue(
			_nettyFabricConnectionWatchDog, "nettyFabricConnection",
				new NettyFabricConnection(_embeddedChannel, 1));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			postDisconnectUnregisterChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			Assert.assertNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Disconnected from " + _embeddedChannel.remoteAddress(),
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"All connections are disconnected, disposed " +
					"repository " + _repository.getRepositoryPath(),
				logRecord.getMessage());
		}

		// Dispose, without log

		_activeConnectionCounter.set(1);

		ReflectionTestUtil.setFieldValue(
			_nettyFabricConnectionWatchDog, "nettyFabricConnection",
				new NettyFabricConnection(_embeddedChannel, 1));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.OFF)) {

			postDisconnectUnregisterChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			Assert.assertNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
		}

		// No dispose, with log

		_activeConnectionCounter.set(2);

		ReflectionTestUtil.setFieldValue(
			_nettyFabricConnectionWatchDog, "nettyFabricConnection",
				new NettyFabricConnection(_embeddedChannel, 1));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			postDisconnectUnregisterChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			Assert.assertNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Disconnected from " + _embeddedChannel.remoteAddress(),
				logRecord.getMessage());
		}
	}

	@Test
	public void testPostRegisterChannelFutureListener() {

		// Cancelled

		PostRegisterChannelFutureListener postRegisterChannelFutureListener =
			_nettyFabricConnectionWatchDog.
				new PostRegisterChannelFutureListener();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			int connectionCount = _activeConnectionCounter.get();

			ChannelFuture channelFuture = _embeddedChannel.newPromise();

			channelFuture.cancel(true);

			postRegisterChannelFutureListener.operationComplete(channelFuture);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Cancelled registering Netty fabric agent on " +
					_embeddedChannel,
				logRecord.getMessage());
			Assert.assertNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());
			Assert.assertEquals(
				connectionCount, _activeConnectionCounter.get());
		}

		// Failed

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			int connectionCount = _activeConnectionCounter.get();

			Throwable throwable = new Throwable();

			postRegisterChannelFutureListener.operationComplete(
				_embeddedChannel.newFailedFuture(throwable));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to register Netty fabric agent on " +
					_embeddedChannel,
				logRecord.getMessage());
			Assert.assertSame(throwable, logRecord.getThrown());
			Assert.assertNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());
			Assert.assertEquals(
				connectionCount, _activeConnectionCounter.get());
		}

		// Successed, with log

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			int connectionCount = _activeConnectionCounter.get();

			postRegisterChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Registered Netty fabric agent on " + _embeddedChannel,
				logRecord.getMessage());
			Assert.assertNotNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());
			Assert.assertEquals(
				connectionCount + 1, _activeConnectionCounter.get());
		}

		// Successed, without log

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.OFF)) {

			int connectionCount = _activeConnectionCounter.get();

			postRegisterChannelFutureListener.operationComplete(
				_embeddedChannel.newSucceededFuture());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
			Assert.assertNotNull(
				_nettyFabricConnectionWatchDog.getFabricConnection());
			Assert.assertEquals(
				connectionCount + 1, _activeConnectionCounter.get());
		}
	}

	private final AtomicInteger _activeConnectionCounter = new AtomicInteger();
	private final EmbeddedChannel _embeddedChannel =
		NettyTestUtil.createEmptyEmbeddedChannel();
	private NettyFabricConnection _nettyFabricConnectionWatchDog;
	private final Repository<Channel> _repository = new MockRepository<>(
		"repository");

}