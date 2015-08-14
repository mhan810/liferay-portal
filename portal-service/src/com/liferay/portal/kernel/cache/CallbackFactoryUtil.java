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

package com.liferay.portal.kernel.cache;

import com.liferay.portal.kernel.cache.cluster.ClusterLinkCallbackFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.util.Properties;

/**
 * @author Tina Tian
 */
public class CallbackFactoryUtil {

	public static PortalCacheBootstrapLoader createPortalCacheBootstrapLoader(
		Properties properties) {

		return _getCallbackFactory().createPortalCacheBootstrapLoader(
			properties);
	}

	public static <K extends Serializable, V> PortalCacheListener<K, V>
		createPortalCacheListener(Properties properties) {

		return _getCallbackFactory().createPortalCacheListener(properties);
	}

	public static PortalCacheManagerListener
		createPortalCacheManagerListener(
			PortalCacheManager<?, ?> portalCacheManager,
			Properties properties) {

		return _getCallbackFactory().createPortalCacheManagerListener(
			portalCacheManager, properties);
	}

	private static CallbackFactory<PortalCacheManager<?, ?>>
		_getCallbackFactory() {

		return _instance._serviceTracker.getService();
	}

	private CallbackFactoryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			(Class<CallbackFactory<PortalCacheManager<?, ?>>>)(Class<?>)
				CallbackFactory.class,
			new CallbackFactoryServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static final CallbackFactoryUtil _instance =
		new CallbackFactoryUtil();

	private final ServiceTracker<CallbackFactory<PortalCacheManager<?, ?>>,
		CallbackFactory<PortalCacheManager<?, ?>>> _serviceTracker;

	private class CallbackFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<CallbackFactory<PortalCacheManager<?, ?>>,
				CallbackFactory<PortalCacheManager<?, ?>>> {

		@Override
		public CallbackFactory<PortalCacheManager<?, ?>> addingService(
			ServiceReference<CallbackFactory<PortalCacheManager<?, ?>>>
				serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			CallbackFactory<PortalCacheManager<?, ?>> callbackFactory =
				registry.getService(serviceReference);

			boolean clusterLinkReplicator = GetterUtil.getBoolean(
				PropsUtil.get(
					PropsKeys.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED));

			if (clusterLinkReplicator) {
				return new ClusterLinkCallbackFactory(callbackFactory);
			}

			return callbackFactory;
		}

		@Override
		public void modifiedService(
			ServiceReference<CallbackFactory<PortalCacheManager<?, ?>>>
				serviceReference,
			CallbackFactory<PortalCacheManager<?, ?>> service) {
		}

		@Override
		public void removedService(
			ServiceReference<CallbackFactory<PortalCacheManager<?, ?>>>
				serviceReference,
			CallbackFactory<PortalCacheManager<?, ?>> service) {
		}

	}

}