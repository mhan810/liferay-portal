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

import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.io.Serializable;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 */
@OSGiBeanProperties(service = SingleVMPoolUtil.class)
public class SingleVMPoolUtil {

	public static void clear() {
		_singleVMPool.clear();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCache(String)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName) {

		return getPortalCache(portalCacheName);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCache(String,
	 *             boolean)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName, boolean blocking) {

		return getPortalCache(portalCacheName, blocking);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCacheManager()}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getCacheManager() {

		return getPortalCacheManager();
	}

	public static <K extends Serializable, V> PortalCache<K, V>
		getDynamicPortalCache(String portalCacheName) {

		return new DynamicPortalCache<>(portalCacheName);
	}

	public static <K extends Serializable, V> PortalCache<K, V>
		getDynamicPortalCache(
			String portalCacheName, boolean blocking) {

		return new DynamicPortalCache<>(portalCacheName, blocking);
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName) {

		return (PortalCache<K, V>)_singleVMPool.getPortalCache(portalCacheName);
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName, boolean blocking) {

		return (PortalCache<K, V>)_singleVMPool.getPortalCache(
			portalCacheName, blocking);
	}

	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getPortalCacheManager() {

		return (PortalCacheManager<K, V>)_singleVMPool.getPortalCacheManager();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #removePortalCache(String)}
	 */
	@Deprecated
	public static void removeCache(String portalCacheName) {
		removePortalCache(portalCacheName);
	}

	public static void removePortalCache(String portalCacheName) {
		_singleVMPool.removePortalCache(portalCacheName);
	}

	private static final SingleVMPool _singleVMPool =
		ProxyFactory.newServiceTrackedInstance(SingleVMPool.class);

	private static class DynamicPortalCache<K extends Serializable, V>
		implements PortalCache<K, V> {

		@Override
		public V get(K key) {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache.get(key);
		}

		@Override
		public List<K> getKeys() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache.getKeys();
		}

		/**
		 * @deprecated As of 7.0.0
		 */
		@Deprecated
		@Override
		public String getName() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache.getName();
		}

		@Override
		public PortalCacheManager<K, V> getPortalCacheManager() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache.getPortalCacheManager();
		}

		@Override
		public String getPortalCacheName() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache.getPortalCacheName();
		}

		public PortalCache<K, V> getWrappedPortalCache() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return null;
			}

			return portalCache;
		}

		@Override
		public void put(K key, V value) {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.put(key, value);
		}

		@Override
		public void put(K key, V value, int timeToLive) {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.put(key, value, timeToLive);
		}

		@Override
		public void registerPortalCacheListener(
			PortalCacheListener<K, V> portalCacheListener) {

			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.registerPortalCacheListener(portalCacheListener);
		}

		@Override
		public void registerPortalCacheListener(
			PortalCacheListener<K, V> portalCacheListener,
			PortalCacheListenerScope portalCacheListenerScope) {

			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.registerPortalCacheListener(
				portalCacheListener, portalCacheListenerScope);
		}

		@Override
		public void remove(K key) {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.remove(key);
		}

		@Override
		public void removeAll() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.removeAll();
		}

		@Override
		public void unregisterPortalCacheListener(
			PortalCacheListener<K, V> portalCacheListener) {

			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.unregisterPortalCacheListener(portalCacheListener);
		}

		@Override
		public void unregisterPortalCacheListeners() {
			PortalCache<K, V> portalCache =
				(PortalCache<K, V>)_singleVMPool.getPortalCache(
					_portalCacheName, _blocking);

			if (portalCache == null) {
				return;
			}

			portalCache.unregisterPortalCacheListeners();
		}

		private DynamicPortalCache(String portalCacheName) {
			this(portalCacheName, false);
		}

		private DynamicPortalCache(String portalCacheName, boolean blocking) {
			_portalCacheName = portalCacheName;
			_blocking = blocking;
		}

		private final boolean _blocking;
		private final String _portalCacheName;

	}

}