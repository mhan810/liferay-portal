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

import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author Shuyang Zhou
 */
public abstract class BaseState implements State {

	public BaseState(Context context) {
		if (context == null) {
			throw new NullPointerException("Context is null");
		}

		this.context = context;
	}

	public abstract Future<?> doTerminate();

	@Override
	public final Future<?> terminate() {
		if (context.transit(this, TerminatedState.INSTANCE)) {
			return doTerminate();
		}

		return new FailedFuture<>(
			ImmediateEventExecutor.INSTANCE, new IllegalStateException());
	}

	@Override
	public String toString() {
		Class<? extends BaseState> clazz = getClass();

		return clazz.getSimpleName();
	}

	protected final Context context;

}