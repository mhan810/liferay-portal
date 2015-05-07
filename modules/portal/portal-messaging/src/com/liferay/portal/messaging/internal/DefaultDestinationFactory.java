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

package com.liferay.portal.messaging.internal;

import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.messaging.DestinationPrototype;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = DestinationFactory.class)
public class DefaultDestinationFactory implements DestinationFactory {

	@Override
	public Destination createDestination(
		DestinationConfiguration destinationConfiguration) {

		String type = destinationConfiguration.getDestinationType();

		DestinationPrototype destinationPrototype = _destinationPrototypes.get(
			type);

		if (destinationPrototype == null) {
			throw new IllegalArgumentException(
				"No destination prototype configured for " + type);
		}

		return destinationPrototype.createDestination(destinationConfiguration);
	}

	@Override
	public Collection<String> getDestinationTypes() {
		return Collections.unmodifiableCollection(
			_destinationPrototypes.keySet());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		addDestinationPrototype(
			DestinationConfiguration.DESTINATION_TYPE_PARALLEL,
			new ParallelDestinationPrototype());

		addDestinationPrototype(
			DestinationConfiguration.DESTINATION_TYPE_SERIAL,
			new SerialDestinationPrototype());

		addDestinationPrototype(
			DestinationConfiguration.DESTINATION_TYPE_SYNCHRONOUS,
			new SynchronousDestinationPrototype());
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected synchronized void addDestinationConfiguration(
		DestinationConfiguration destinationConfiguration,
		Map<String, Object> properties) {

		_destinationConfigurations.put(
			destinationConfiguration.getDestinationName(),
			destinationConfiguration);

		Destination destination = createDestination(
			destinationConfiguration);

		Dictionary<String, Object> dictionary =
			new HashMapDictionary<>();

		dictionary.put("destination.name", destination.getName());

		ServiceRegistration<Destination> serviceRegistration =
			_bundleContext.registerService(
				Destination.class, destination, dictionary);

		_serviceRegistrations.put(
			destination.getName(), serviceRegistration);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addDestinationPrototype(
		DestinationPrototype destinationPrototype,
		Map<String, Object> properties) {

		String destinationType = MapUtil.getString(
			properties, "destination.type");

		_destinationPrototypes.put(destinationType, destinationPrototype);
	}

	protected void addDestinationPrototype(
		String destinationType, DestinationPrototype destinationPrototype) {

		Map<String, Object> properties = new HashMap<>();

		properties.put("destination.type", destinationType);

		addDestinationPrototype(destinationPrototype, properties);
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<Destination>
				destinationServiceRegistration :
					_serviceRegistrations.values()) {

			destinationServiceRegistration.unregister();
		}

		_serviceRegistrations.clear();

		_destinationConfigurations.clear();

		_bundleContext = null;
	}

	protected void removeDestinationPrototype(
		DestinationPrototype destinationPrototype,
		Map<String, Object> properties) {

		String destinationType = MapUtil.getString(
			properties, "destination.type");

		_destinationPrototypes.remove(destinationType);
	}


	protected synchronized void removeDestinationConfiguration(
		DestinationConfiguration destinationConfiguration,
		Map<String, Object> properties) {

		_destinationConfigurations.remove(
			destinationConfiguration.getDestinationName());

		if (_serviceRegistrations.containsKey(
				destinationConfiguration.getDestinationName())) {

			ServiceRegistration<Destination> serviceRegistration =
				_serviceRegistrations.get(
					destinationConfiguration.getDestinationName());

			serviceRegistration.unregister();
		}
	}

	@Reference(unbind = "-")
	protected void setPortalExecutorManager(
		PortalExecutorManager portalExecutorManager) {
	}

	private BundleContext _bundleContext;
	private final Map<String, DestinationPrototype> _destinationPrototypes =
		new ConcurrentHashMap<>();
	private final Map<String, ServiceRegistration<Destination>>
		_serviceRegistrations = new HashMap<>();
	private Map<String, DestinationConfiguration> _destinationConfigurations =
		new HashMap<>();

}