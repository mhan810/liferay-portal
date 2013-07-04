/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.backgroundtask;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Michael C. Han
 */
public abstract class BaseBackgroundTaskExecutor
	implements BackgroundTaskExecutor {

	@Override
	public boolean isSerial() {
		return _serial;
	}

	protected BackgroundTaskResult processError(
		Map<String, Serializable> taskContextMap, String message) {

		BackgroundTaskResult failureBackgroundTaskResult =
			new BackgroundTaskResult(
				BackgroundTaskConstants.STATUS_FAILED, message);

		return failureBackgroundTaskResult;
	}

	protected void setSerial(boolean serial) {
		_serial = serial;
	}

	private boolean _serial;

}