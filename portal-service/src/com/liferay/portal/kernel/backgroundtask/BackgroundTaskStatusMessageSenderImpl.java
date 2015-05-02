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

package com.liferay.portal.kernel.backgroundtask;

import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSenderFactoryUtil;

/**
 * @author Andrew Betts
 */
public class BackgroundTaskStatusMessageSenderImpl
	implements BackgroundTaskStatusMessageSender {

	public void afterPropertiesSet() {
		if (_singleDestinationMessageSender == null) {
			_singleDestinationMessageSender =
				SingleDestinationMessageSenderFactoryUtil.
					createSingleDestinationMessageSender(_destinationName);
		}
	}

	@Override
	public void sendStatusMessage(
		BackgroundTaskStatusMessage backgroundTaskStatusMessage) {

		if (!BackgroundTaskThreadLocal.hasBackgroundTask()) {
			return;
		}

		_singleDestinationMessageSender.send(backgroundTaskStatusMessage);
	}

	public void setDestinationName(String destinationName) {
		_destinationName = destinationName;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setDestinationName(String)})
	 */
	@Deprecated
	public void setSingleDestinationMessageSender(
		SingleDestinationMessageSender singleDestinationMessageSender) {

		_singleDestinationMessageSender = singleDestinationMessageSender;
	}

	private String _destinationName;
	private SingleDestinationMessageSender _singleDestinationMessageSender;

}