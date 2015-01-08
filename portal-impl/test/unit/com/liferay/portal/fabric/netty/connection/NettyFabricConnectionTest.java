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

import com.liferay.portal.fabric.agent.FabricAgent;
import com.liferay.portal.fabric.local.agent.EmbeddedProcessExecutor;
import com.liferay.portal.fabric.local.agent.LocalFabricAgent;
import com.liferay.portal.fabric.local.worker.EmbeddedProcessChannel;
import com.liferay.portal.fabric.local.worker.LocalFabricWorker;
import com.liferay.portal.fabric.netty.NettyTestUtil;
import com.liferay.portal.fabric.netty.handlers.NettyChannelAttributes;
import com.liferay.portal.fabric.netty.util.NettyUtil;
import com.liferay.portal.fabric.worker.FabricWorker;
import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.process.local.ReturnProcessCallable;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;

import io.netty.channel.Channel;

import java.io.Serializable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricConnectionTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testAssertCreation() {
		Assert.assertSame(_channel, _nettyFabricConnection.channel);
		Assert.assertEquals(
			_EXECUTION_TIMEOUT, _nettyFabricConnection.executionTimeout);
		Assert.assertSame(
			_channel.localAddress(),
			_nettyFabricConnection.getLocalSocketAddress());
		Assert.assertSame(
			_channel.remoteAddress(),
			_nettyFabricConnection.getRemoteSocketAddress());
		Assert.assertEquals(
			_channel.toString(), _nettyFabricConnection.toString());
	}

	@Test
	public void testDisconnectWithFabricWorker() throws Exception {
		FabricWorker<Serializable> fabricWorker1 = _fabricAgent.execute(
			null, new ReturnProcessCallable<>(null));

		NettyChannelAttributes.putFabricWorker(_channel, 0, fabricWorker1);

		DefaultNoticeableFuture<Serializable> defaultNoticeableFuture1 =
			(DefaultNoticeableFuture<Serializable>)
				fabricWorker1.getProcessNoticeableFuture();

		defaultNoticeableFuture1.run();

		FabricWorker<Serializable> fabricWorker2 = _fabricAgent.execute(
			null, new ReturnProcessCallable<>(null));

		NettyChannelAttributes.putFabricWorker(_channel, 1, fabricWorker2);

		final DefaultNoticeableFuture<Serializable> defaultNoticeableFuture3 =
			new DefaultNoticeableFuture<Serializable>() {

				@Override
				public Serializable get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException,
						TimeoutException {

					run();

					return super.get(timeout, unit);
				}

			};

		FabricWorker<Serializable> fabricWorker3 =
			new LocalFabricWorker<Serializable>(
				new EmbeddedProcessChannel<>(null)) {

				@Override
				public NoticeableFuture<Serializable>
					getProcessNoticeableFuture() {

					return defaultNoticeableFuture3;
				}

			};

		NettyChannelAttributes.putFabricWorker(_channel, 2, fabricWorker3);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.SEVERE)) {

			NettyUtil.syncFully(_nettyFabricConnection.disconnect());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to terminate fabric worker 1", logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(TimeoutException.class, throwable.getClass());

			NoticeableFuture<Serializable> noticeableFuture =
				fabricWorker2.getProcessNoticeableFuture();

			Assert.assertTrue(noticeableFuture.isCancelled());
		}
	}

	@Test
	public void testDisconnectWithoutFabricWorker() throws Exception {
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					NettyFabricConnection.class.getName(), Level.INFO)) {

			NettyUtil.syncFully(_nettyFabricConnection.disconnect());

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Disconnect Netty fabric connection on " + _channel,
				logRecord.getMessage());
		}
	}

	private static final long _EXECUTION_TIMEOUT = 1;

	private final Channel _channel = NettyTestUtil.createEmptyEmbeddedChannel();
	private final FabricAgent _fabricAgent = new LocalFabricAgent(
		new EmbeddedProcessExecutor());
	private final NettyFabricConnection _nettyFabricConnection =
		new NettyFabricConnection(_channel, _EXECUTION_TIMEOUT);

}