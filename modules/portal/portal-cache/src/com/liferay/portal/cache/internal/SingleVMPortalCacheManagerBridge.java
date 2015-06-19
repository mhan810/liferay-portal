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

package com.liferay.portal.cache.internal;

import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(immediate = true, service = SingleVMPortalCacheManagerBridge.class)
public class SingleVMPortalCacheManagerBridge {

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		Registry registry = RegistryUtil.getRegistry();

		StringBundler sb = new StringBundler(11);

		sb.append("(&(objectClass=");
		sb.append(PortalCacheManager.class.getName());
		sb.append(")(");
		sb.append(PortalCacheManager.PORTAL_CACHE_MANAGER_NAME);
		sb.append("=");
		sb.append(PortalCacheManagerNames.SINGLE_VM);
		sb.append(")(");
		sb.append(PortalCacheManager.PORTAL_CACHE_MANAGER_TYPE);
		sb.append("=");
		sb.append(_props.get(PropsKeys.PORTAL_CACHE_MANAGER_TYPE_SINGLE_VM));
		sb.append("))");

		Filter filter = registry.getFilter(sb.toString());

		_serviceTracker = registry.trackServices(
			filter, new SingleVMPortalCacheManagerServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		_props = props;
	}

	private Props _props;
	private volatile ServiceTracker
		<PortalCacheManager<? extends Serializable, ?>,
		PortalCacheManager<? extends Serializable, ?>> _serviceTracker;

	private class SingleVMPortalCacheManagerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<PortalCacheManager<? extends Serializable, ?>,
			PortalCacheManager<? extends Serializable, ?>> {

		@Override
		public PortalCacheManager<? extends Serializable, ?> addingService(
			ServiceReference
				<PortalCacheManager<? extends Serializable, ?>>
					serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			PortalCacheManager<? extends Serializable, ?>
				portalCacheManager = registry.getService(serviceReference);

			Map<String, Object> properties = new HashMap<>();

			properties.put(
				"portal.cache.manager.bridge",
				SingleVMPortalCacheManagerBridge.class.getName());

			_serviceRegistration = registry.registerService(
				(Class<PortalCacheManager<? extends Serializable, ?>>)
					(Class<?>)PortalCacheManager.class,
				portalCacheManager, properties);

			return portalCacheManager;
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalCacheManager<? extends Serializable, ?>>
				serviceReference,
			PortalCacheManager<? extends Serializable, ?> service) {
		}

		@Override
		public void removedService(
			ServiceReference<PortalCacheManager<? extends Serializable, ?>>
				serviceReference,
			PortalCacheManager<? extends Serializable, ?> service) {

			if (_serviceRegistration != null) {
				_serviceRegistration.unregister();
			}
		}

		private ServiceRegistration
			<PortalCacheManager<? extends Serializable, ?>>
				_serviceRegistration;

	}

}