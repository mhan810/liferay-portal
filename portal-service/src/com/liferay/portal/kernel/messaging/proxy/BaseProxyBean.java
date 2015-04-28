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

package com.liferay.portal.kernel.messaging.proxy;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSenderFactory;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationSynchronousMessageSender;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

/**
 * @author Micha Kiener
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public abstract class BaseProxyBean {

	public void afterPropertiesSet() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			SingleDestinationMessageSenderFactory.class,
			new SingleDestinationMessageSenderFactoryServiceTrackerCustomizer()
		);

		_serviceTracker.open();
	}

	public void destroy() {
		_serviceTracker.close();
	}

	public void send(ProxyRequest proxyRequest) {
		_singleDestinationMessageSender.send(buildMessage(proxyRequest));
	}

	public void setDestinationName(String destinationName) {
		_destinationName = destinationName;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setDestinationName)
	 */
	@Deprecated
	public void setSingleDestinationMessageSender(
		SingleDestinationMessageSender singleDestinationMessageSender) {

		_singleDestinationMessageSender = singleDestinationMessageSender;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by
	 * {@link #setSynchronousMessageSenderMode} and
	 * {@link #setSynchronousDestinationName}
	 */
	@Deprecated
	public void setSingleDestinationSynchronousMessageSender(
		SingleDestinationSynchronousMessageSender
			singleDestinationSynchronousMessageSender) {

		_singleDestinationSynchronousMessageSender =
			singleDestinationSynchronousMessageSender;
	}

	public void setSynchronousDestinationName(
		String synchronousDestinationName) {

		_synchronousDestinationName = synchronousDestinationName;
	}

	public void setSynchronousMessageSenderMode(
		SynchronousMessageSender.Mode synchronousMessageSenderMode) {

		_synchronousMessageSenderMode = synchronousMessageSenderMode;
	}

	public Object synchronousSend(ProxyRequest proxyRequest) throws Exception {
		ProxyResponse proxyResponse =
			(ProxyResponse)_singleDestinationSynchronousMessageSender.send(
				buildMessage(proxyRequest));

		if (proxyResponse == null) {
			return proxyRequest.execute(this);
		}
		else if (proxyResponse.hasError()) {
			throw proxyResponse.getException();
		}
		else {
			return proxyResponse.getResult();
		}
	}

	protected Message buildMessage(ProxyRequest proxyRequest) {
		Message message = new Message();

		message.setPayload(proxyRequest);

		MessageValuesThreadLocal.populateMessageFromThreadLocals(message);

		if (proxyRequest.isLocal()) {
			message.put(MessagingProxy.LOCAL_MESSAGE, Boolean.TRUE);
		}

		return message;
	}

	protected void initializeSingleDestinationSenders(
		SingleDestinationMessageSenderFactory
			singleDestinationMessageSenderFactory) {

		if ((_singleDestinationSynchronousMessageSender == null) &&
			Validator.isNotNull(_synchronousDestinationName)) {

			_singleDestinationSynchronousMessageSender =
				singleDestinationMessageSenderFactory.
					createSingleDestinationSynchronousMessageSender(
						_synchronousDestinationName,
						_synchronousMessageSenderMode);
		}

		if ((_singleDestinationMessageSender == null) &&
			Validator.isNotNull(_destinationName)) {

			_singleDestinationMessageSender =
				singleDestinationMessageSenderFactory.
					createSingleDestinationMessageSender(_destinationName);
		}
	}

	private String _destinationName;
	private ServiceTracker
		<SingleDestinationMessageSenderFactory,
			SingleDestinationMessageSenderFactory> _serviceTracker;
	private SingleDestinationMessageSender _singleDestinationMessageSender;
	private SingleDestinationSynchronousMessageSender
		_singleDestinationSynchronousMessageSender;
	private String _synchronousDestinationName;
	private SynchronousMessageSender.Mode _synchronousMessageSenderMode;

	private class SingleDestinationMessageSenderFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
		<SingleDestinationMessageSenderFactory,
			SingleDestinationMessageSenderFactory> {

		@Override
		public SingleDestinationMessageSenderFactory addingService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory = registry.getService(
					serviceReference);

			initializeSingleDestinationSenders(
				singleDestinationMessageSenderFactory);

			return singleDestinationMessageSenderFactory;
		}

		@Override
		public void modifiedService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference,
			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory) {
		}

		@Override
		public void removedService(
			ServiceReference<SingleDestinationMessageSenderFactory>
				serviceReference,
			SingleDestinationMessageSenderFactory
				singleDestinationMessageSenderFactory) {
		}

	}

}