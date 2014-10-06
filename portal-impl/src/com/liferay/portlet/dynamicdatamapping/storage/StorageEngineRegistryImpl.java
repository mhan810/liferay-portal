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

package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcellus Tavares
 */
public class StorageEngineRegistryImpl implements StorageEngineRegistry {

	public StorageEngineRegistryImpl() {
		Registry registry = RegistryUtil.getRegistry();

		Class<?> clazz = getClass();

		Filter filter = registry.getFilter(
			"(&(objectClass=" + StorageEngine.class.getName() +
				")(!(objectClass=" + clazz.getName() + ")))");

		_serviceTracker = registry.trackServices(
			filter, new StorageEngineServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Override
	public StorageEngine getStorageEngine(String storageEngineId) {
		return _storageEnginesMap.get(storageEngineId);
	}

	@Override
	public Set<String> getStorageEngineIds() {
		return _storageEnginesMap.keySet();
	}

	public void setDefaultStorageEngine(StorageEngine storageEngine) {
		Registry registry = RegistryUtil.getRegistry();

		registry.registerService(StorageEngine.class, storageEngine);
	}

	private ServiceTracker<StorageEngine, StorageEngine> _serviceTracker;
	private Map<String, StorageEngine> _storageEnginesMap =
		new ConcurrentHashMap<String, StorageEngine>();

	private class StorageEngineServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<StorageEngine, StorageEngine> {

		@Override
		public StorageEngine addingService(
			ServiceReference<StorageEngine> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			StorageEngine storageEngine = registry.getService(serviceReference);

			_storageEnginesMap.put(
				storageEngine.getStorageEngineId(), storageEngine);

			return storageEngine;
		}

		@Override
		public void modifiedService(
			ServiceReference<StorageEngine> serviceReference,
			StorageEngine storageEngine) {
		}

		@Override
		public void removedService(
			ServiceReference<StorageEngine> serviceReference,
			StorageEngine storageEngine) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

}