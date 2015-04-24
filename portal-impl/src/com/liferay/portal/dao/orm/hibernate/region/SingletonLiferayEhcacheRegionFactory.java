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

	public SingletonLiferayEhcacheRegionFactory() {
		synchronized (this) {
			_init();
		}
	}

	@Override
	public CollectionRegion buildCollectionRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		return _liferayEhcacheRegionFactory.buildCollectionRegion(
			regionName, properties, cacheDataDescription);
	}

	@Override
	public EntityRegion buildEntityRegion(
			String regionName, Properties properties,
			CacheDataDescription cacheDataDescription)
		throws CacheException {

		return _liferayEhcacheRegionFactory.buildEntityRegion(
			regionName, properties, cacheDataDescription);
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(
			String regionName, Properties properties)
		throws CacheException {

		return _liferayEhcacheRegionFactory.buildQueryResultsRegion(
			regionName, properties);
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(
			String regionName, Properties properties)
		throws CacheException {

		return _liferayEhcacheRegionFactory.buildTimestampsRegion(
			regionName, properties);
	}

	@Override
	public AccessType getDefaultAccessType() {
		return _liferayEhcacheRegionFactory.getDefaultAccessType();
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return _liferayEhcacheRegionFactory.isMinimalPutsEnabledByDefault();
	}

	@Override
	public long nextTimestamp() {
		return _liferayEhcacheRegionFactory.nextTimestamp();
	}

	@Override
	public synchronized void start(Settings settings, Properties properties) {
		if (_instanceCounter++ == 0) {
			_liferayEhcacheRegionFactory.start(settings, properties);
		}
	}

	@Override
	public synchronized void stop() {
		if (--_instanceCounter == 0) {
			_liferayEhcacheRegionFactory.stop();
		}
	}

	private static void _init() {
		if (_liferayEhcacheRegionFactory != null) {
			return;
		}

		Registry registry = RegistryUtil.getRegistry();

		ServiceTracker<RegionFactory, RegionFactory> serviceTracker =
			registry.trackServices(RegionFactory.class);

		serviceTracker.open();

		try {
			_liferayEhcacheRegionFactory = serviceTracker.waitForService(0);
		}
		catch (InterruptedException ie) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to get RegionFactory instance ", ie);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SingletonLiferayEhcacheRegionFactory.class);

	private static int _instanceCounter;
	private static RegionFactory _liferayEhcacheRegionFactory;

}