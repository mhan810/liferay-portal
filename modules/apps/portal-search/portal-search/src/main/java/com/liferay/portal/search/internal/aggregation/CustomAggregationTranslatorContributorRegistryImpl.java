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

package com.liferay.portal.search.internal.aggregation;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.search.spi.aggregation.CustomAggregationTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.CustomAggregationTranslatorContributorRegistry;

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
@Component(service = CustomAggregationTranslatorContributorRegistry.class)
public class CustomAggregationTranslatorContributorRegistryImpl
	implements CustomAggregationTranslatorContributorRegistry,
			   ServiceTrackerCustomizer
				   <CustomAggregationTranslatorContributor,
				   CustomAggregationTranslatorContributor> {

	@Override
	public CustomAggregationTranslatorContributor addingService(
		ServiceReference<CustomAggregationTranslatorContributor>
			serviceReference) {

		CustomAggregationTranslatorContributor<?>
			customAggregationTranslatorContributor = _bundleContext.getService(
				serviceReference);

		_customAggregationTranslatorContributor.put(
			customAggregationTranslatorContributor.getAggregationClassName(),
			customAggregationTranslatorContributor);

		return customAggregationTranslatorContributor;
	}

	@Override
	public CustomAggregationTranslatorContributor<?>
		getCustomAggregationTranslatorContributor(String aggregationClassName) {

		return _customAggregationTranslatorContributor.get(
			aggregationClassName);
	}

	@Override
	public void modifiedService(
		ServiceReference<CustomAggregationTranslatorContributor>
			serviceReference,
		CustomAggregationTranslatorContributor
			customAggregationTranslatorContributor) {

		CustomAggregationTranslatorContributor<?>
			newCustomAggregationTranslatorContributor =
				_bundleContext.getService(serviceReference);

		_customAggregationTranslatorContributor.put(
			customAggregationTranslatorContributor.getAggregationClassName(),
			newCustomAggregationTranslatorContributor);
	}

	@Override
	public void removedService(
		ServiceReference<CustomAggregationTranslatorContributor>
			serviceReference,
		CustomAggregationTranslatorContributor
			customAggregationTranslatorContributor) {

		_customAggregationTranslatorContributor.remove(
			customAggregationTranslatorContributor.getAggregationClassName());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.create(
			bundleContext, CustomAggregationTranslatorContributor.class, this);

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
	private final Map<String, CustomAggregationTranslatorContributor<?>>
		_customAggregationTranslatorContributor = new HashMap<>();
	private ServiceTracker
		<CustomAggregationTranslatorContributor,
		 CustomAggregationTranslatorContributor> _serviceTracker;

}