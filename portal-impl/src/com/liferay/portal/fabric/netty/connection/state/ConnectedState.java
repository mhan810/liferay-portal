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
import io.netty.util.concurrent.Future;

/**
 * @author Shuyang Zhou
 */
public class ConnectedState extends AsyncState {

	public ConnectedState(Context context, Channel channel) {
		super(context);

		this.channel = channel;
	}

	@Override
	public Future<?> asyncProceed() {
		return context.register(channel);
	}

	@Override
	public Future<?> doTerminate() {
		return channel.close();
	}

	@Override
	public State onComplete(Future<?> future) {
		context.registered(channel, future);

		if (future.isSuccess()) {
			return new RegisteredState(context, channel);
		}

		return new ScheduledState(context);
	}

	protected final Channel channel;

}