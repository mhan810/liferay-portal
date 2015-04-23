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

package com.liferay.portal.dao.orm.hibernate.region;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.Properties;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.TimestampsRegion;
import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.impl.NoCachingRegionFactory;
import org.hibernate.cfg.Settings;

/**
 * @author Edward Han
 * @author Shuyang Zhou
 */
public class ResettableRegionFactory implements RegionFactory {

	public ResettableRegionFactory() {
		init();
	}

	@Override
	public CollectionRegion buildCollectionRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		return getRegionFactory().buildCollectionRegion(
			regionName, properties, cacheDataDescription);
	}

	@Override
	public EntityRegion buildEntityRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		return getRegionFactory().buildEntityRegion(
			regionName, properties, cacheDataDescription);
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(
			String regionName, Properties properties)
		throws CacheException {

		return getRegionFactory().buildQueryResultsRegion(
			regionName, properties);
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(
			String regionName, Properties properties)
		throws CacheException {

		return getRegionFactory().buildTimestampsRegion(regionName, properties);
	}

	@Override
	public AccessType getDefaultAccessType() {
		return getRegionFactory().getDefaultAccessType();
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return getRegionFactory().isMinimalPutsEnabledByDefault();
	}

	@Override
	public long nextTimestamp() {
		return getRegionFactory().nextTimestamp();
	}

	@Override
	public synchronized void start(Settings settings, Properties properties) {
		if (_regionFactory == null) {
			_settings = settings;

			_properties = properties;
		}
		else {
			if (_instanceCounter++ == 0) {
				_regionFactory.start(settings, properties);
			}
		}
	}

	@Override
	public synchronized void stop() {
		if (_regionFactory == null) {
			return;
		}

		if (--_instanceCounter == 0) {
			_regionFactory.stop();
		}
	}

	protected RegionFactory getRegionFactory() {
		if (_regionFactory == null) {
			if (_log.isInfoEnabled()) {
				_log.info("Using NoCachingRegionFactory");
			}

			return _noCachingRegionFactory;
		}

		return _regionFactory;
	}

	protected synchronized void init() {
		if (_serviceTracker != null) {
			return;
		}

		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
				RegionFactory.class,
				new RegionFactoryServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ResettableRegionFactory.class);

	private static int _instanceCounter;
	private static Properties _properties;
	private static ServiceTracker<RegionFactory, RegionFactory> _serviceTracker;
	private static Settings _settings;

	private final RegionFactory _noCachingRegionFactory =
		new NoCachingRegionFactory(null);
	private volatile RegionFactory _regionFactory;

	private class RegionFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<RegionFactory, RegionFactory> {

		@Override
		public RegionFactory addingService(
			ServiceReference<RegionFactory> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			_regionFactory = registry.getService(serviceReference);

			_regionFactory.start(_settings, _properties);

			return _regionFactory;
		}

		@Override
		public void modifiedService(
			ServiceReference<RegionFactory> serviceReference,
			RegionFactory service) {
		}

		@Override
		public void removedService(
			ServiceReference<RegionFactory> serviceReference,
			RegionFactory service) {

			_regionFactory = null;
		}

	}

}