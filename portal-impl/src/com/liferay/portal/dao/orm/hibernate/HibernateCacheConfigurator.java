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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.dao.orm.hibernate.region.LiferayEhcacheRegionFactory;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import org.hibernate.cache.RegionFactory;

/**
 * @author Michael C. Han
 */
public class HibernateCacheConfigurator {

	public void afterPropertiesSet() {
		if (SPIUtil.isSPI()) {
			return;
		}

		boolean useQueryCache = GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.HIBERNATE_CACHE_USE_QUERY_CACHE));

		boolean useSecondLevelCache = GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));

		if (!useQueryCache && !useSecondLevelCache) {
			return;
		}

		Registry registry = RegistryUtil.getRegistry();

		LiferayEhcacheRegionFactory liferayEhcacheRegionFactory =
			new LiferayEhcacheRegionFactory();

		_serviceRegistration = registry.registerService(
			RegionFactory.class, liferayEhcacheRegionFactory,
			new HashMap<String, Object>());
	}

	public void destroy() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private ServiceRegistration<RegionFactory> _serviceRegistration;

}