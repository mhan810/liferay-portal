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

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.search.indexer.IndexerFactory;
import com.liferay.portal.search.indexer.ModelIndexer;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = ModelIndexerRegistry.class)
public class ModelIndexerRegistry {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Activate
	protected void deactivate() {
		synchronized (_serviceRegistrations) {
			if (MapUtil.isNotEmpty(_serviceRegistrations)) {
				for (ServiceRegistration<?> serviceRegistration :
						_serviceRegistrations.values()) {

					serviceRegistration.unregister();
				}
			}
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "unsetModelIndexer"
	)
	protected void setModelIndexer(
		ModelIndexer<?> modelIndexer, Map<String, Object> properties) {

		String searchEngineId = (String)properties.get("search.engine.id");

		Indexer<?> indexer = indexerFactory.create(
			searchEngineId, modelIndexer, properties);

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
	}

	protected void unsetModelIndexer(
		ModelIndexer<?> modelIndexer, Map<String, Object> properties) {

		synchronized (_serviceRegistrations) {
			ServiceRegistration<?> serviceRegistration =
				_serviceRegistrations.get(modelIndexer.getClassName());

			if (serviceRegistration != null) {
				serviceRegistration.unregister();
			}
		}
	}

	@Reference
	protected IndexerFactory indexerFactory;

	private BundleContext _bundleContext;
	private final Map<String, ServiceRegistration<Indexer>>
		_serviceRegistrations = new HashMap<>();

}