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

package com.liferay.portal.search.elasticsearch.util;

import java.util.concurrent.Callable;

/**
 * @author André de Oliveira
 */
public class RetryAssertExecutor {

	public RetryAssertExecutor(long timeout) {
		_timeout = timeout;
	}

	public <T> T execute(Callable<T> callable) throws Exception {
		long deadline = System.currentTimeMillis() + _timeout;

		while (true) {
			try {
				return callable.call();
			}
			catch (AssertionError ae) {
				if (System.currentTimeMillis() > deadline) {
					throw ae;
				}
			}
		}
	}

	private final long _timeout;

}