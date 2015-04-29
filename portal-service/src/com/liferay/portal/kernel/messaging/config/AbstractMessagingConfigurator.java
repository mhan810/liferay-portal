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

package com.liferay.portal.kernel.messaging.config;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfig;
import com.liferay.portal.kernel.messaging.DestinationEventListener;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusEventListener;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.nio.intraband.RegistrationReference;
import com.liferay.portal.kernel.nio.intraband.messaging.DestinationConfigurationProcessCallable;
import com.liferay.portal.kernel.nio.intraband.messaging.IntrabandBridgeDestination;
import com.liferay.portal.kernel.nio.intraband.rpc.IntrabandRPCUtil;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.security.pacl.permission.PortalMessageBusPermission;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.dependency.ServiceDependencyListener;
import com.liferay.registry.dependency.ServiceDependencyManager;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public abstract class AbstractMessagingConfigurator
	implements MessagingConfigurator {

	public void afterPropertiesSet() {
		final ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		serviceDependencyManager.registerDependencies(MessageBus.class);

		serviceDependencyManager.addServiceDependencyListener(

			new ServiceDependencyListener() {
				@Override
				public void dependenciesFulfilled() {
					Registry registry = RegistryUtil.getRegistry();

					_messageBus = registry.getService(MessageBus.class);

					initialize();

					serviceDependencyManager.destroy();
				}

				@Override
				public void destroy() {
				}
			}
		);
	}

	@Override
	public void connect() {
		if (SPIUtil.isSPI() && _portalMessagingConfigurator) {
			return;
		}

		Registry registry = RegistryUtil.getRegistry();

		for (final Map.Entry<String, List<MessageListener>> messageListeners :
				_messageListeners.entrySet()) {

			final String destinationName = messageListeners.getKey();

			final ServiceDependencyManager serviceDependencyManager =
				new ServiceDependencyManager();

			final Filter destinationFilter = registry.getFilter(
				"(&(name=" + destinationName + ")(objectClass=" +
					Destination.class.getName() + "))");

			serviceDependencyManager.registerDependencies(destinationFilter);

			serviceDependencyManager.addServiceDependencyListener(
				new DestinationServiceDependencyListener(
					destinationName, messageListeners.getValue(),
					serviceDependencyManager));
		}
	}

	@Override
	public void destroy() {
		disconnect();

		for (Destination destination : _destinations) {
			_messageBus.removeDestination(destination.getName());

			destination.close();
		}

		for (Map.Entry<String, List<DestinationEventListener>>
			destinationEventListeners :
			_destinationEventListeners.entrySet()) {

			String destinationName = destinationEventListeners.getKey();

			for (DestinationEventListener destinationEventListener :
					destinationEventListeners.getValue()) {

				Destination destination = _messageBus.getDestination(
					destinationName);

				if (destination != null) {
					destination.removeDestinationEventListener(
						destinationEventListener);
				}
			}
		}

		for (MessageBusEventListener messageBusEventListener :
				_messageBusEventListeners) {

			_messageBus.removeMessageBusEventListener(messageBusEventListener);
		}

		ClassLoader operatingClassLoader = getOperatingClassloader();

		String servletContextName = ClassLoaderPool.getContextName(
			operatingClassLoader);

		MessagingConfiguratorRegistry.unregisterMessagingConfigurator(
			servletContextName, this);
	}

	@Override
	public void disconnect() {
		if (SPIUtil.isSPI() && _portalMessagingConfigurator) {
			return;
		}

		for (Map.Entry<String, List<MessageListener>> messageListeners :
				_messageListeners.entrySet()) {

			String destinationName = messageListeners.getKey();

			for (MessageListener messageListener :
					messageListeners.getValue()) {

				_messageBus.unregisterMessageListener(
					destinationName, messageListener);
			}
		}
	}

	@Override
	public void setDestinationConfig(
		Set<DestinationConfig> destinationConfigs) {

		Registry registry = RegistryUtil.getRegistry();

		for (DestinationConfig destinationConfig : destinationConfigs) {
			try {
				PortalMessageBusPermission.checkListen(
					destinationConfig.getDestinationName());
			}
			catch (SecurityException se) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Rejecting destination " +
							destinationConfig.getDestinationName());
				}

				continue;
			}

			registry.registerService(
				DestinationConfig.class, destinationConfig);
		}
	}

	@Override
	public void setDestinationEventListeners(
		Map<String, List<DestinationEventListener>> destinationEventListeners) {

		_destinationEventListeners = destinationEventListeners;
	}

	/**
	 * @param destinations
	 * @deprecated As of 7.0.0, replaced by {@link #setDestinationConfig(Set)}
	 */
	@Deprecated
	@Override
	public void setDestinations(List<Destination> destinations) {
		for (Destination destination : destinations) {
			try {
				PortalMessageBusPermission.checkListen(destination.getName());
			}
			catch (SecurityException se) {
				if (_log.isInfoEnabled()) {
					_log.info("Rejecting destination " + destination.getName());
				}

				continue;
			}

			_destinations.add(destination);
		}
	}

	@Override
	public void setMessageBusEventListeners(
		List<MessageBusEventListener> messageBusEventListeners) {

		_messageBusEventListeners = messageBusEventListeners;
	}

	@Override
	public void setMessageListeners(
		Map<String, List<MessageListener>> messageListeners) {

		_messageListeners = messageListeners;

		for (List<MessageListener> messageListenersList :
				_messageListeners.values()) {

			for (MessageListener messageListener : messageListenersList) {
				Class<?> messageListenerClass = messageListener.getClass();

				try {
					Method setMessageBusMethod = messageListenerClass.getMethod(
						"setMessageBus", MessageBus.class);

					setMessageBusMethod.setAccessible(true);

					setMessageBusMethod.invoke(messageListener, _messageBus);

					continue;
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(e, e);
					}
				}

				try {
					Method setMessageBusMethod =
						messageListenerClass.getDeclaredMethod(
							"setMessageBus", MessageBus.class);

					setMessageBusMethod.setAccessible(true);

					setMessageBusMethod.invoke(messageListener, _messageBus);
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(e, e);
					}
				}
			}
		}
	}

	@Override
	public void setReplacementDestinations(
		List<Destination> replacementDestinations) {

		_replacementDestinations = replacementDestinations;
	}

	protected abstract ClassLoader getOperatingClassloader();

	protected void initialize() {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		ClassLoader operatingClassLoader = getOperatingClassloader();

		if (contextClassLoader == operatingClassLoader) {
			_portalMessagingConfigurator = true;
		}

		for (MessageBusEventListener messageBusEventListener :
				_messageBusEventListeners) {

			_messageBus.addMessageBusEventListener(messageBusEventListener);
		}

		for (Destination destination : _destinations) {
			if (SPIUtil.isSPI()) {
				destination = new IntrabandBridgeDestination(destination);
			}

			_messageBus.addDestination(destination);
		}

		for (Map.Entry<String, List<DestinationEventListener>>
				destinationEventListeners :
					_destinationEventListeners.entrySet()) {

			String destinationName = destinationEventListeners.getKey();

			for (DestinationEventListener destinationEventListener :
					destinationEventListeners.getValue()) {

				Destination destination = _messageBus.getDestination(
					destinationName);

				if (destination != null) {
					destination.addDestinationEventListener(
						destinationEventListener);
				}
			}
		}

		for (Destination destination : _replacementDestinations) {
			_messageBus.replace(destination);
		}

		connect();

		String servletContextName = ClassLoaderPool.getContextName(
			operatingClassLoader);

		MessagingConfiguratorRegistry.registerMessagingConfigurator(
			servletContextName, this);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AbstractMessagingConfigurator.class);

	private Map<String, List<DestinationEventListener>>
		_destinationEventListeners = new HashMap<>();
	private final List<Destination> _destinations = new ArrayList<>();
	private volatile MessageBus _messageBus;
	private List<MessageBusEventListener> _messageBusEventListeners =
		new ArrayList<>();
	private Map<String, List<MessageListener>> _messageListeners =
		new HashMap<>();
	private boolean _portalMessagingConfigurator;
	private List<Destination> _replacementDestinations = new ArrayList<>();

	private class DestinationServiceDependencyListener
		implements ServiceDependencyListener {

		public DestinationServiceDependencyListener(
			String destinationName, List<MessageListener> messageListeners,
			ServiceDependencyManager serviceDependencyManager) {

			_destinationName = destinationName;
			_messageListeners = messageListeners;
			_serviceDependencyManager = serviceDependencyManager;
		}

		@Override
		public void dependenciesFulfilled() {
			Thread currentThread = Thread.currentThread();

			final ClassLoader contextClassLoader =
				currentThread.getContextClassLoader();

			try {
				ClassLoader operatingClassLoader = getOperatingClassloader();

				currentThread.setContextClassLoader(operatingClassLoader);

				if (SPIUtil.isSPI()) {
					SPI spi = SPIUtil.getSPI();

					try {
						RegistrationReference registrationReference =
							spi.getRegistrationReference();

						IntrabandRPCUtil.execute(
							registrationReference,
							new DestinationConfigurationProcessCallable(
								_destinationName));
					}
					catch (Exception e) {
						StringBundler sb = new StringBundler(4);

						sb.append("Unable to install ");
						sb.append(
							DestinationConfigurationProcessCallable.class.
								getName());
						sb.append(" on MPI for ");
						sb.append(_destinationName);

						_log.error(sb.toString(), e);
					}
				}

				for (MessageListener messageListener : _messageListeners) {
					_messageBus.registerMessageListener(
						_destinationName, messageListener);
				}
			}
			finally {
				currentThread.setContextClassLoader(contextClassLoader);
			}

			_serviceDependencyManager.destroy();
		}

		@Override
		public void destroy() {
		}

		private final String _destinationName;
		private final List<MessageListener> _messageListeners;
		private final ServiceDependencyManager _serviceDependencyManager;

	}

}