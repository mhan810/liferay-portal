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

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.SucceededFuture;

/**
 * @author Shuyang Zhou
 */
public class ScheduledState extends AsyncState {

	public ScheduledState(Context context) {
		super(context);
	}

	@Override
	public synchronized Future<?> asyncProceed() {
		future = context.schedule();

		return future;
	}

	@Override
	public synchronized Future<?> doTerminate() {
		if (future != null) {
			future.cancel(true);
		}

		return new SucceededFuture<>(ImmediateEventExecutor.INSTANCE, null);
	}

	@Override
	public State onComplete(Future<?> future) {
		if (future.isSuccess()) {
			return new InitialState(context);
		}

		context.scheduled(future);

		return TerminatedState.INSTANCE;
	}

	protected Future<?> future;

}