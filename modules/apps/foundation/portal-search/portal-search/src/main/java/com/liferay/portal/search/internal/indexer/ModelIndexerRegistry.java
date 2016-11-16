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

package com.liferay.portal.search.internal.indexer;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.search.indexer.IndexerFactory;
import com.liferay.portal.search.indexer.ModelIndexer;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = ModelIndexerRegistry.class)
public class ModelIndexerRegistry {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, "(objectClass=)" + ModelIndexer.class.getName(),
			new ModelIndexerServiceTrackerCustomizer());
	}

	@Activate
	protected void deactivate() {
		_serviceTracker.close();
	}

	@Reference
	protected IndexerFactory indexerFactory;

	private BundleContext _bundleContext;
	private ServiceTracker<ModelIndexer<?>, ModelIndexer<?>> _serviceTracker;

	private class ModelIndexerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<ModelIndexer<?>, ModelIndexer<?>> {

		@Override
		public ModelIndexer<?> addingService(
			ServiceReference<ModelIndexer<?>> serviceReference) {

			ModelIndexer<?> modelIndexer = _bundleContext.getService(
				serviceReference);

			String searchEngineId = (String)serviceReference.getProperty(
				"search.engine.id");

			Indexer<?> indexer = indexerFactory.create(
				searchEngineId, modelIndexer);

			ServiceRegistration<Indexer> serviceRegistration = null;

			synchronized (_serviceRegistrations) {
				serviceRegistration = _serviceRegistrations.get(
					modelIndexer.getClassName());

				if (serviceRegistration != null) {
					serviceRegistration.unregister();
				}

				serviceRegistration = _bundleContext.registerService(
					Indexer.class, indexer, new HashMapDictionary<>());

				_serviceRegistrations.put(
					modelIndexer.getClassName(), serviceRegistration);
			}

			return modelIndexer;
		}

		@Override
		public void modifiedService(
			ServiceReference<ModelIndexer<?>> serviceReference,
			ModelIndexer<?> modelIndexer) {
		}

		@Override
		public void removedService(
			ServiceReference<ModelIndexer<?>> serviceReference,
			ModelIndexer<?> modelIndexer) {

			synchronized (_serviceRegistrations) {
				ServiceRegistration<?> serviceRegistration =
					_serviceRegistrations.get(modelIndexer.getClassName());

				if (serviceRegistration != null) {
					serviceRegistration.unregister();
				}
			}
		}

		private final Map<String, ServiceRegistration<Indexer>>
			_serviceRegistrations = new HashMap<>();

	}

}