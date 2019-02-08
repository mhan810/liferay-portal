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

package com.liferay.portal.search.internal.query2;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributor;
import com.liferay.portal.search.spi.query.CustomQueryTranslatorContributorRegistry;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Michael C. Han
 */
@Component(service = CustomQueryTranslatorContributorRegistry.class)
public class CustomQueryTranslatorContributorRegistryImpl
	implements CustomQueryTranslatorContributorRegistry,
			   ServiceTrackerCustomizer
				   <CustomQueryTranslatorContributor,
				   CustomQueryTranslatorContributor> {

	@Override
	public CustomQueryTranslatorContributor addingService(
		ServiceReference<CustomQueryTranslatorContributor> serviceReference) {

		CustomQueryTranslatorContributor<?> customQueryTranslatorContributor =
			_bundleContext.getService(serviceReference);

		_customQueryTranslatorContributors.put(
			customQueryTranslatorContributor.getQueryClassName(),
			customQueryTranslatorContributor);

		return customQueryTranslatorContributor;
	}

	@Override
	public CustomQueryTranslatorContributor<?>
		getCustomQueryTranslatorContributor(String queryClassName) {

		return _customQueryTranslatorContributors.get(queryClassName);
	}

	@Override
	public void modifiedService(
		ServiceReference<CustomQueryTranslatorContributor> serviceReference,
		CustomQueryTranslatorContributor customQueryTranslatorContributor) {

		CustomQueryTranslatorContributor<?>
			newCustomQueryTranslatorContributor = _bundleContext.getService(
				serviceReference);

		_customQueryTranslatorContributors.put(
			customQueryTranslatorContributor.getQueryClassName(),
			newCustomQueryTranslatorContributor);
	}

	@Override
	public void removedService(
		ServiceReference<CustomQueryTranslatorContributor> serviceReference,
		CustomQueryTranslatorContributor customQueryTranslatorContributor) {

		_customQueryTranslatorContributors.remove(
			customQueryTranslatorContributor.getQueryClassName());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.create(
			bundleContext, CustomQueryTranslatorContributor.class, this);

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}

		_bundleContext = null;
	}

	private BundleContext _bundleContext;
	private final Map<String, CustomQueryTranslatorContributor<?>>
		_customQueryTranslatorContributors = new HashMap<>();
	private ServiceTracker
		<CustomQueryTranslatorContributor, CustomQueryTranslatorContributor>
			_serviceTracker;

}