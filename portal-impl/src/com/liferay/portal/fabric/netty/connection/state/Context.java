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

/**
 * @author Shuyang Zhou
 */
public interface Context extends State {

	public ChannelFuture connect();

	public void connected(Channel channel, Future<?> future);

	public Future<?> register(Channel channel);

	public void registered(Channel channel, Future<?> future);

	public Future<?> schedule();

	public void scheduled(Future<?> future);

	public boolean transit(State fromState, State toState);

	public Future<?> unregister(Channel channel);

	public void unregistered(Channel channel, Future<?> future);

}