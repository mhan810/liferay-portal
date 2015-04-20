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

package com.liferay.portal.cache.memory;

import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;

import java.io.Serializable;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Tina Tian
 */
@Component(
	immediate = true,
	property = {
		"portal.cache.manager=" +
			"com.liferay.portal.cache.memory.MemoryPortalCacheManager",
		"portal.cache.manager.name=" + PortalCacheManagerNames.MULTI_VM
	},
	service = PortalCacheManager.class
)
public class MultiVMMemoryPortalCacheManager
	<K extends Serializable, V extends Serializable>
		extends MemoryPortalCacheManager<K, V> {

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		setClusterAware(true);
		setMpiOnly(true);
		setName(PortalCacheManagerNames.MULTI_VM);

		initialize();
	}

	@Deactivate
	protected void deactivate() {
		destroy();
	}

}