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

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

import java.util.Properties;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.TimestampsRegion;
import org.hibernate.cache.access.AccessType;
import org.hibernate.cfg.Settings;

/**
 * @author Edward Han
 * @author Shuyang Zhou
 */
public class SingletonLiferayEhcacheRegionFactory implements RegionFactory {

	public SingletonLiferayEhcacheRegionFactory(Properties properties) {
		synchronized (this) {
			_init(properties);
		}
	}

	@Override
	public CollectionRegion buildCollectionRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.buildCollectionRegion(
				regionName, properties, cacheDataDescription);
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public EntityRegion buildEntityRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.buildEntityRegion(
				regionName, properties, cacheDataDescription);
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(
			String regionName, Properties properties)
		throws CacheException {

		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.buildQueryResultsRegion(
				regionName, properties);
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(
			String regionName, Properties properties)
		throws CacheException {

		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.buildTimestampsRegion(regionName, properties);
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public AccessType getDefaultAccessType() {
		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.getDefaultAccessType();
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.isMinimalPutsEnabledByDefault();
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public long nextTimestamp() {
		RegionFactory regionFactory;

		try {
			regionFactory = _serviceTracker.waitForService(0);

			return regionFactory.nextTimestamp();
		}
		catch (InterruptedException ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public synchronized void start(Settings settings, Properties properties) {
		if (_enabled && (_instanceCounter++ == 0)) {
			RegionFactory regionFactory;

			try {
				regionFactory = _serviceTracker.waitForService(0);

				regionFactory.start(settings, properties);
			}
			catch (InterruptedException ex) {
			}
		}
	}

	@Override
	public synchronized void stop() {
		if (_enabled && (--_instanceCounter == 0)) {
			RegionFactory regionFactory;

			try {
				regionFactory = _serviceTracker.waitForService(0);

				regionFactory.stop();
			}
			catch (InterruptedException ex) {
			}
		}
	}

	private static void _init(Properties properties) {
		boolean useQueryCache = GetterUtil.getBoolean(
			properties.get(PropsKeys.HIBERNATE_CACHE_USE_QUERY_CACHE));
		boolean useSecondLevelCache = GetterUtil.getBoolean(
			properties.get(PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));

		if (useQueryCache || useSecondLevelCache) {
			_enabled = true;
		}

		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(RegionFactory.class);

		_serviceTracker.open();
	}

	private static boolean _enabled;
	private static int _instanceCounter;
	private static ServiceTracker<RegionFactory, RegionFactory> _serviceTracker;

}