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

package com.liferay.portal.fabric.netty.connection.state;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.SucceededFuture;

/**
 * @author Shuyang Zhou
 */
public class InitialState extends AsyncState {

	public InitialState(Context context) {
		super(context);
	}

	@Override
	public synchronized Future<?> asyncProceed() {
		ChannelFuture channelFuture = context.connect();

		channel = channelFuture.channel();

		return channelFuture;
	}

	@Override
	public synchronized Future<?> doTerminate() {
		if (channel == null) {
			return new SucceededFuture<>(ImmediateEventExecutor.INSTANCE, null);
		}

		return channel.close();
	}

	@Override
	public synchronized State onComplete(Future<?> future) {
		context.connected(channel, future);

		if (future.isSuccess()) {
			return new ConnectedState(context, channel);
		}

		return new ScheduledState(context);
	}

	protected Channel channel;

}