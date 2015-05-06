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
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationEventListener;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusEventListener;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.nio.intraband.RegistrationReference;
import com.liferay.portal.kernel.nio.intraband.messaging.DestinationConfigurationProcessCallable;
import com.liferay.portal.kernel.nio.intraband.rpc.IntrabandRPCUtil;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.security.pacl.permission.PortalMessageBusPermission;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceFinalizer;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistrar;
import com.liferay.registry.dependency.ServiceDependencyListener;
import com.liferay.registry.dependency.ServiceDependencyManager;

import java.lang.reflect.Method;

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

		serviceDependencyManager.addServiceDependencyListener(

			new ServiceDependencyListener() {
				@Override
				public void dependenciesFulfilled() {
					Registry registry = RegistryUtil.getRegistry();

					_messageBus = registry.getService(MessageBus.class);

					initialize();
				}

				@Override
				public void destroy() {
				}
			}
		);

		serviceDependencyManager.registerDependencies(MessageBus.class);
	}

	@Override
	public void connect() {
		if (SPIUtil.isSPI() && _portalMessagingConfigurator) {
			return;
		}

		Registry registry = RegistryUtil.getRegistry();

		_messageListenerServiceRegistrar = registry.getServiceRegistrar(
			MessageListener.class);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		try {
			ClassLoader operatingClassLoader = getOperatingClassloader();

			currentThread.setContextClassLoader(operatingClassLoader);

			for (Map.Entry<String, List<MessageListener>> messageListeners :
					_messageListeners.entrySet()) {

				String destinationName = messageListeners.getKey();

				ServiceDependencyManager serviceDependencyManager =
					new ServiceDependencyManager();

				serviceDependencyManager.addServiceDependencyListener(
					new DestinationServiceDependencyListener(
						destinationName, messageListeners.getValue()));

				Filter destinationFilter = registry.getFilter(
					"(&(destination.name=" + destinationName +
						")(objectClass=" + Destination.class.getName() + "))");

				serviceDependencyManager.registerDependencies(
					destinationFilter);
			}
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	@Override
	public void destroy() {
		disconnect();

		_messageListeners.clear();

		_messageListenerServiceRegistrar.destroy();

		_messageBusEventListenerServiceRegistrar.destroy();

		_destinationConfigServiceRegistrar.destroy();

		_destinationServiceRegistrar.destroy(

			new ServiceFinalizer<Destination>() {

				@Override
				public void finalize(
					ServiceReference<Destination> serviceReference,
					Destination destination) {

					destination.close();

					destination.removeDestinationEventListeners();

					destination.unregisterMessageListeners();
			}

		});

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
	public void setDestinationConfigurations(
		Set<DestinationConfiguration> destinationConfigurations) {

		Registry registry = RegistryUtil.getRegistry();

		_destinationConfigServiceRegistrar = registry.getServiceRegistrar(
			DestinationConfiguration.class);

		for (DestinationConfiguration destinationConfiguration :
				destinationConfigurations) {

			try {
				PortalMessageBusPermission.checkListen(
					destinationConfiguration.getDestinationName());
			}
			catch (SecurityException se) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Rejecting destination " +
							destinationConfiguration.getDestinationName());
				}

				continue;
			}

			_destinationConfigServiceRegistrar.registerService(
				DestinationConfiguration.class, destinationConfiguration);
		}
	}

	@Override
	public void setDestinationEventListeners(
		Map<String, List<DestinationEventListener>> destinationEventListeners) {

		_destinationEventListeners = destinationEventListeners;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by
	 *             {@link #setDestinationConfigurations(Set)}
	 *
	 * @param destinations
	 */
	@Deprecated
	@Override
	public void setDestinations(List<Destination> destinations) {
		registerDestinations(destinations);
	}

	@Override
	public void setMessageBusEventListeners(
		List<MessageBusEventListener> messageBusEventListeners) {

		Registry registry = RegistryUtil.getRegistry();

		_messageBusEventListenerServiceRegistrar = registry.getServiceRegistrar(
			MessageBusEventListener.class);

		for (MessageBusEventListener messageBusEventListener :
				messageBusEventListeners) {

			_messageBusEventListenerServiceRegistrar.registerService(
				MessageBusEventListener.class, messageBusEventListener);
		}
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
				}

				try {
					Method setMessageBusMethod =
						messageListenerClass.getDeclaredMethod(
							"setMessageBus", MessageBus.class);

					setMessageBusMethod.setAccessible(true);

					setMessageBusMethod.invoke(messageListener, _messageBus);
				}
				catch (Exception e) {
				}
			}
		}
	}

	/**
	 * @deprecated As of 7.0.0, replaced by
	 *             {@link #setDestinationConfigurations(Set)}
	 *
	 * @param replacementDestinations
	 */
	@Deprecated
	@Override
	public void setReplacementDestinations(
		List<Destination> replacementDestinations) {

		registerDestinations(replacementDestinations);
	}

	protected abstract ClassLoader getOperatingClassloader();

	protected void initialize() {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		ClassLoader operatingClassLoader = getOperatingClassloader();

		if (contextClassLoader == operatingClassLoader) {
			_portalMessagingConfigurator = true;
		}

		connect();

		String servletContextName = ClassLoaderPool.getContextName(
			operatingClassLoader);

		MessagingConfiguratorRegistry.registerMessagingConfigurator(
			servletContextName, this);
	}

	protected void registerDestinations(List<Destination> destinations) {
		Registry registry = RegistryUtil.getRegistry();

		if (_destinationServiceRegistrar == null) {
			_destinationServiceRegistrar = registry.getServiceRegistrar(
				Destination.class);
		}

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

			Map<String, Object> properties = new HashMap<>();

			properties.put("destination.name", destination.getName());

			_destinationServiceRegistrar.registerService(
				Destination.class, destination, properties);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AbstractMessagingConfigurator.class);

	private ServiceRegistrar<DestinationConfiguration>
		_destinationConfigServiceRegistrar;
	private Map<String, List<DestinationEventListener>>
		_destinationEventListeners = new HashMap<>();
	private ServiceRegistrar<Destination> _destinationServiceRegistrar;
	private volatile MessageBus _messageBus;
	private ServiceRegistrar<MessageBusEventListener>
		_messageBusEventListenerServiceRegistrar;
	private Map<String, List<MessageListener>> _messageListeners =
		new HashMap<>();
	private ServiceRegistrar<MessageListener> _messageListenerServiceRegistrar;
	private boolean _portalMessagingConfigurator;

	private class DestinationServiceDependencyListener
		implements ServiceDependencyListener {

		public DestinationServiceDependencyListener(
			String destinationName, List<MessageListener> messageListeners) {

			_destinationName = destinationName;
			_messageListeners = messageListeners;
		}

		@Override
		public void dependenciesFulfilled() {
			ClassLoader operatingClassLoader = getOperatingClassloader();

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

			Map<String, Object> properties = new HashMap<>();

			properties.put("destination.name", _destinationName);
			properties.put("operatingClassLoader", operatingClassLoader);

			for (MessageListener messageListener : _messageListeners) {
				_messageListenerServiceRegistrar.registerService(
					MessageListener.class, messageListener, properties);
			}
		}

		@Override
		public void destroy() {
		}

		private final String _destinationName;
		private final List<MessageListener> _messageListeners;

	}

}