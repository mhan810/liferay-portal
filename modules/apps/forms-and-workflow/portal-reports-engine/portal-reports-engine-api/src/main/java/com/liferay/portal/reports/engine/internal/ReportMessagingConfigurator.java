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

package com.liferay.portal.reports.engine.internal;

import com.liferay.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.concurrent.CallerRunsPolicy;
import com.liferay.portal.kernel.concurrent.RejectedExecutionHandler;
import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.reports.engine.configuration.ReportConfiguration;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Brian Greenwald
 * @author Dylan Rebelak
 * @author Prathima Shreenath
 */
@Component(
	configurationPid = "com.liferay.portal.reports.engine.configuration.ReportConfiguration",
	immediate = true, service = ReportMessagingConfigurator.class
)
public class ReportMessagingConfigurator {

	@Activate
	protected void activate(ComponentContext componentContext) {
		_bundleContext = componentContext.getBundleContext();

		Dictionary<String, Object> properties =
			componentContext.getProperties();

		_reportConfiguration = ConfigurableUtil.createConfigurable(
			ReportConfiguration.class, properties);
	}

	protected void configureDestination(String destinationName) {
		DestinationConfiguration destinationConfiguration =
			new DestinationConfiguration(
				DestinationConfiguration.DESTINATION_TYPE_PARALLEL,
				destinationName);

		destinationConfiguration.setMaximumQueueSize(
			_reportConfiguration.auditMessageMaxQueueSize());

		RejectedExecutionHandler rejectedExecutionHandler =
			new CallerRunsPolicy() {

				@Override
				public void rejectedExecution(
					Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

					if (_log.isWarnEnabled()) {
						_log.warn(
							"The current thread will handle the request " +
								"because the report engine's task queue is " +
									"at its maximum capacity");
					}

					super.rejectedExecution(runnable, threadPoolExecutor);
				}

			};

		destinationConfiguration.setRejectedExecutionHandler(
			rejectedExecutionHandler);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		Dictionary<String, Object> destinationProperties =
			new HashMapDictionary<>();

		destinationProperties.put("destination.name", destination.getName());

		_destinationServiceRegistration = _bundleContext.registerService(
			Destination.class, destination, destinationProperties);

		MessageListener messageListener = _messageListeners.get(
			destinationName);

		destination.register(messageListener);
	}

	@Deactivate
	protected void deactivate() {
		if (_destinationServiceRegistration != null) {
			Destination destination = _bundleContext.getService(
				_destinationServiceRegistration.getReference());

			_destinationServiceRegistration.unregister();

			destination.destroy();
		}

		_bundleContext = null;
	}

	@Modified
	protected void modified(ComponentContext componentContext) {
		deactivate();

		activate(componentContext);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(destination=report)", unbind = "unsetReportMessageListener"
	)
	protected void setReportMessageListener(
		MessageListener messageListener, Map<String, Object> properties) {

		String destinationName = GetterUtil.getString(
			properties.get("destination.name"));

		_messageListeners.put(destinationName, messageListener);

		configureDestination(destinationName);
	}

	protected void unsetReportMessageListener(
		MessageListener messageListener,
		Map<String, Object> properties) {

		String destinationName = GetterUtil.getString(
			properties.get("destination.name"));

		if (Validator.isNull(destinationName)) {
			if (_log.isWarnEnabled()) {
				_log.warn("No destination specified for: " + messageListener);
			}

			return;
		}

		_messageListeners.remove(destinationName);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReportMessagingConfigurator.class);

	private volatile BundleContext _bundleContext;

	@Reference
	private DestinationFactory _destinationFactory;

	private volatile ServiceRegistration<Destination>
		_destinationServiceRegistration;
	private final Map<String, MessageListener> _messageListeners =
		new ConcurrentHashMap<>();
	private ReportConfiguration _reportConfiguration;

}