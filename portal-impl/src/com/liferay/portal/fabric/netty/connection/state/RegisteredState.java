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
public class RegisteredState extends AsyncState {

	public RegisteredState(Context context, Channel channel) {
		super(context);

		this.channel = channel;
	}

	@Override
	public synchronized Future<?> asyncProceed() {
		return context.unregister(channel);
	}

	@Override
	public synchronized Future<?> doTerminate() {
		return channel.close();
	}

	@Override
	public State onComplete(Future<?> future) {
		context.unregistered(channel, future);

		return new UnregisteredState(context, channel);
	}

	protected final Channel channel;

}