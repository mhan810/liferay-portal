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

package com.liferay.portal.settings.web.internal;

import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.settings.portlet.action.PortalSettingsConfigurationScreenContributor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Drew Brokke
 */
@Component(service = {})
public class PortalSettingsConfigurationScreenContributorRegistry {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, PortalSettingsConfigurationScreenContributor.class,
			new PortalSettingsCSCServiceTrackerCustomizer());
	}

	@Deactivate
	public void deactivate() {
		_serviceTracker.close();
	}

	private BundleContext _bundleContext;

	@Reference
	private JSPRenderer _jspRenderer;

	private final Map<String, ServiceRegistration<ConfigurationScreen>>
		_serviceRegistrationMap = new ConcurrentHashMap<>();
	private ServiceTracker
		<PortalSettingsConfigurationScreenContributor, ConfigurationScreen>
			_serviceTracker;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.portal.settings.web)"
	)
	private ServletContext _servletContext;

	private class PortalSettingsCSCServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<PortalSettingsConfigurationScreenContributor,
			 ConfigurationScreen> {

		@Override
		public ConfigurationScreen addingService(
			ServiceReference<PortalSettingsConfigurationScreenContributor>
				serviceReference) {

			return _registerConfigurationScreen(
				_bundleContext.getService(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalSettingsConfigurationScreenContributor>
				serviceReference,
			ConfigurationScreen service) {
		}

		@Override
		public void removedService(
			ServiceReference<PortalSettingsConfigurationScreenContributor>
				serviceReference,
			ConfigurationScreen service) {

			_unregisterConfigurationScreen(
				_bundleContext.getService(serviceReference));
		}

		private ConfigurationScreen _registerConfigurationScreen(
			PortalSettingsConfigurationScreenContributor
				portalSettingsConfigurationScreenContributor) {

			_jspRenderer.setServletContext(_servletContext);

			PortalSettingsConfigurationScreen configurationScreen =
				new PortalSettingsConfigurationScreen(
					portalSettingsConfigurationScreenContributor, _jspRenderer);

			_serviceRegistrationMap.put(
				portalSettingsConfigurationScreenContributor.getKey(),
				_bundleContext.registerService(
					ConfigurationScreen.class, configurationScreen,
					new HashMapDictionary<>()));

			return configurationScreen;
		}

		private void _unregisterConfigurationScreen(
			PortalSettingsConfigurationScreenContributor
				portalSettingsConfigurationScreenContributor) {

			ServiceRegistration<ConfigurationScreen> serviceRegistration =
				_serviceRegistrationMap.remove(
					portalSettingsConfigurationScreenContributor.getKey());

			if (serviceRegistration != null) {
				serviceRegistration.unregister();
			}
		}

	}

}