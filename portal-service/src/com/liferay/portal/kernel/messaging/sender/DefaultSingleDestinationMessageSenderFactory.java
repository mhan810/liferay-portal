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

package com.liferay.portal.kernel.messaging.sender;

import com.liferay.portal.kernel.messaging.MessageBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class DefaultSingleDestinationMessageSenderFactory
	implements SingleDestinationMessageSenderFactory {

	@Override
	public SingleDestinationMessageSender createSingleDestinationMessageSender(
		String destinationName) {

		DefaultSingleDestinationMessageSender
			defaultSingleDestinationMessageSender =
				new DefaultSingleDestinationMessageSender();

		defaultSingleDestinationMessageSender.setDestinationName(
			destinationName);

		defaultSingleDestinationMessageSender.setMessageBus(_messageBus);

		return defaultSingleDestinationMessageSender;
	}

	@Override
	public SingleDestinationSynchronousMessageSender
		createSingleDestinationSynchronousMessageSender(
			String destinationName, SynchronousMessageSender.Mode mode) {

		SynchronousMessageSender synchronousMessageSender =
			_synchronousMessageSenders.get(mode);

		if (synchronousMessageSender == null) {
			throw new IllegalStateException(
				"No SynchronousMessageSender configured for: " + mode);
		}

		DefaultSingleDestinationSynchronousMessageSender
			defaultSingleDestinationSynchronousMessageSender =
				new DefaultSingleDestinationSynchronousMessageSender();

		defaultSingleDestinationSynchronousMessageSender.setDestinationName(
			destinationName);

		defaultSingleDestinationSynchronousMessageSender.
			setSynchronousMessageSender(synchronousMessageSender);

		return defaultSingleDestinationSynchronousMessageSender;
	}

	public void setMessageBus(MessageBus messageBus) {
		_messageBus = messageBus;
	}

	public void setSynchronousMessageSenders(
		Map<SynchronousMessageSender.Mode, SynchronousMessageSender>
			synchronousMessageSenders) {

		_synchronousMessageSenders.putAll(synchronousMessageSenders);
	}

	private MessageBus _messageBus;
	private final Map<SynchronousMessageSender.Mode, SynchronousMessageSender>
		_synchronousMessageSenders = new HashMap<>();

}