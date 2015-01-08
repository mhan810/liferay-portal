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

package com.liferay.portal.fabric.netty;

import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Time;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.concurrent.TimeUnit;

/**
 * @author Shuyang Zhou
 */
public class NettyTestUtil {

	public static final long SYNC_WAIT_TIME = 10 * Time.MINUTE;

	public static EmbeddedChannel createEmptyEmbeddedChannel() {
		return new EmbeddedChannel(
			new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) {
				}

			});
	}

	public static EventLoopGroup directScheduleEventLoopGroup(
		final EventLoopGroup eventLoopGroup) {

		return (EventLoopGroup)ProxyUtil.newProxyInstance(
				EventLoopGroup.class.getClassLoader(),
				new Class<?>[] {EventLoopGroup.class},
				new InvocationHandler() {

					@Override
					public Object invoke(
							Object proxy, Method method, Object[] args)
						throws Exception {

						String name = method.getName();
						Class<?>[] parameterTypes = method.getParameterTypes();

						if (name.equals("schedule") &&
							(parameterTypes.length == 3) &&
							(parameterTypes[0] == Runnable.class) &&
							(parameterTypes[1] == long.class) &&
							(parameterTypes[2] == TimeUnit.class)) {

							Runnable runnable = (Runnable)args[0];

							runnable.run();

							return null;
						}

						return method.invoke(eventLoopGroup, args);
					}

				});
	}

}