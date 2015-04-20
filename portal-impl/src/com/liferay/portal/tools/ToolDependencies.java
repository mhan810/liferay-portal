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

package com.liferay.portal.tools;

import com.liferay.portal.cache.MultiVMPoolImpl;
import com.liferay.portal.cache.SingleVMPoolImpl;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.CacheManagerListener;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.microsofttranslator.MicrosoftTranslatorFactoryUtil;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.microsofttranslator.MicrosoftTranslatorFactoryImpl;
import com.liferay.portal.model.DefaultModelHintsImpl;
import com.liferay.portal.model.ModelHintsUtil;
import com.liferay.portal.security.auth.DefaultFullNameGenerator;
import com.liferay.portal.security.auth.FullNameGenerator;
import com.liferay.portal.security.permission.ResourceActionsImpl;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.security.xml.SecureXMLFactoryProviderImpl;
import com.liferay.portal.security.xml.SecureXMLFactoryProviderUtil;
import com.liferay.portal.service.permission.PortletPermissionImpl;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.util.DigesterImpl;
import com.liferay.portal.util.FastDateFormatFactoryImpl;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.FriendlyURLNormalizerImpl;
import com.liferay.portal.util.HtmlImpl;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.xml.SAXReaderImpl;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.Serializable;

import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Raymond Augé
 */
public class ToolDependencies {

	public static void wireBasic() {
		InitUtil.init();

		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		registry.registerService(
			FullNameGenerator.class, new DefaultFullNameGenerator());

		Map<String, Object> properties = new HashMap<>();

		properties.put(
			"portal.cache.manager.name", PortalCacheManagerNames.SINGLE_VM);
		properties.put(
			"portal.cache.manager", PropsValues.PORTAL_CACHE_MANAGER_SINGLE_VM);

		registry.registerService(
			PortalCacheManager.class,
			new DummyPortalCacheManager<>(PortalCacheManagerNames.SINGLE_VM),
			properties);

		properties = new HashMap<>();

		properties.put(
			"portal.cache.manager.name", PortalCacheManagerNames.MULTI_VM);
		properties.put(
			"portal.cache.manager", PropsValues.PORTAL_CACHE_MANAGER_MULTI_VM);

		registry.registerService(
			PortalCacheManager.class,
			new DummyPortalCacheManager(PortalCacheManagerNames.MULTI_VM),
			properties);

		DigesterUtil digesterUtil = new DigesterUtil();

		digesterUtil.setDigester(new DigesterImpl());

		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());

		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		FriendlyURLNormalizerUtil friendlyURLNormalizerUtil =
			new FriendlyURLNormalizerUtil();

		friendlyURLNormalizerUtil.setFriendlyURLNormalizer(
			new FriendlyURLNormalizerImpl());

		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(new HttpImpl());

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());

		MicrosoftTranslatorFactoryUtil microsoftTranslatorFactoryUtil =
			new MicrosoftTranslatorFactoryUtil();

		microsoftTranslatorFactoryUtil.setMicrosoftTranslatorFactory(
			new MicrosoftTranslatorFactoryImpl());

		SingleVMPoolUtil singleVMPoolUtil = new SingleVMPoolUtil();

		PortletPermissionUtil portletPermissionUtil =
			new PortletPermissionUtil();

		portletPermissionUtil.setPortletPermission(new PortletPermissionImpl());

		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		SAXReaderImpl secureSAXReader = new SAXReaderImpl();

		secureSAXReader.setSecure(true);

		saxReaderUtil.setSecureSAXReader(secureSAXReader);

		saxReaderUtil.setUnsecureSAXReader(new SAXReaderImpl());

		SecureXMLFactoryProviderUtil secureXMLFactoryProviderUtil =
			new SecureXMLFactoryProviderUtil();

		secureXMLFactoryProviderUtil.setSecureXMLFactoryProvider(
			new SecureXMLFactoryProviderImpl());

		try {
			singleVMPoolUtil.setSingleVMPool(new SingleVMPoolImpl());
		}
		catch (InterruptedException ex) {
		}

		// DefaultModelHintsImpl requires SecureXMLFactoryProviderUtil

		ModelHintsUtil modelHintsUtil = new ModelHintsUtil();

		DefaultModelHintsImpl defaultModelHintsImpl =
			new DefaultModelHintsImpl();

		defaultModelHintsImpl.afterPropertiesSet();

		modelHintsUtil.setModelHints(defaultModelHintsImpl);
	}

	public static void wireDeployers() {
		wireBasic();

		MultiVMPoolUtil multiVMPoolUtil = new MultiVMPoolUtil();

		try {
			multiVMPoolUtil.setMultiVMPool(new MultiVMPoolImpl());
		}
		catch (Exception ex) {
		}

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());
	}

	public static void wireServiceBuilder() {
		wireDeployers();

		ResourceActionsUtil resourceActionsUtil = new ResourceActionsUtil();

		ResourceActionsImpl resourceActionsImpl = new ResourceActionsImpl();

		resourceActionsImpl.afterPropertiesSet();

		resourceActionsUtil.setResourceActions(resourceActionsImpl);
	}

	private static class DummyPortalCache<K extends Serializable, V>
		implements PortalCache<K, V> {

		@Override
		public V get(K key) {
			return null;
		}

		@Override
		public List<K> getKeys() {
			return Collections.emptyList();
		}

		@Override
		public String getName() {
			return _name;
		}

		@Override
		public PortalCacheManager<K, V> getPortalCacheManager() {
			return _portalCacheManager;
		}

		@Override
		public void put(K key, V value) {
		}

		@Override
		public void put(K key, V value, int timeToLive) {
		}

		@Override
		public void registerCacheListener(CacheListener<K, V> cacheListener) {
		}

		@Override
		public void registerCacheListener(
			CacheListener<K, V> cacheListener,
			CacheListenerScope cacheListenerScope) {
		}

		@Override
		public void remove(K key) {
		}

		@Override
		public void removeAll() {
		}

		@Override
		public void unregisterCacheListener(CacheListener<K, V> cacheListener) {
		}

		@Override
		public void unregisterCacheListeners() {
		}

		private DummyPortalCache(
			String name, PortalCacheManager<K, V> portalCacheManager) {

			_name = name;
			_portalCacheManager = portalCacheManager;
		}

		private final String _name;
		private final PortalCacheManager<K, V> _portalCacheManager;

	}

	private static class DummyPortalCacheManager<K extends Serializable, V>
		implements PortalCacheManager<K, V> {

		public DummyPortalCacheManager(String name) {
			_name = name;
		}

		@Override
		public void clearAll() {
		}

		@Override
		public void destroy() {
		}

		@Override
		public PortalCache<K, V> getCache(String name) {
			return new DummyPortalCache<>(name, this);
		}

		@Override
		public PortalCache<K, V> getCache(String name, boolean blocking) {
			return new DummyPortalCache<>(name, this);
		}

		@Override
		public Set<CacheManagerListener> getCacheManagerListeners() {
			return Collections.emptySet();
		}

		@Override
		public String getName() {
			return _name;
		}

		@Override
		public void initialize() {
		}

		@Override
		public boolean isClusterAware() {
			return false;
		}

		@Override
		public void reconfigureCaches(URL configurationURL) {
		}

		@Override
		public boolean registerCacheManagerListener(
			CacheManagerListener cacheManagerListener) {

			return false;
		}

		@Override
		public void removeCache(String name) {
		}

		@Override
		public boolean unregisterCacheManagerListener(
			CacheManagerListener cacheManagerListener) {

			return false;
		}

		@Override
		public void unregisterCacheManagerListeners() {
		}

		private final String _name;

	}

}