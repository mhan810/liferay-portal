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

package com.liferay.portal.search.internal.aggregation.pipeline;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributor;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationResultTranslatorContributorRegistry;

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
@Component(
	service = CustomPipelineAggregationResultTranslatorContributorRegistry.class
)
public class CustomPipelineAggregationResultTranslatorContributorRegistryImpl
	implements CustomPipelineAggregationResultTranslatorContributorRegistry,
			   ServiceTrackerCustomizer
				   <CustomPipelineAggregationResultTranslatorContributor,
				   CustomPipelineAggregationResultTranslatorContributor> {

	@Override
	public CustomPipelineAggregationResultTranslatorContributor addingService(
		ServiceReference<CustomPipelineAggregationResultTranslatorContributor>
			serviceReference) {

		CustomPipelineAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>
				customAggregationTranslatorContributor =
					_bundleContext.getService(serviceReference);

		_customAggregationResultTranslatorContributor.put(
			customAggregationTranslatorContributor.getAggregationClassName(),
			customAggregationTranslatorContributor);

		return customAggregationTranslatorContributor;
	}

	@Override
	public CustomPipelineAggregationResultTranslatorContributor
		<? extends AggregationResult, ?>
			getCustomPipelineAggregationResultTranslatorContributor(
				String aggregationClassName) {

		return _customAggregationResultTranslatorContributor.get(
			aggregationClassName);
	}

	@Override
	public void modifiedService(
		ServiceReference<CustomPipelineAggregationResultTranslatorContributor>
			serviceReference,
		CustomPipelineAggregationResultTranslatorContributor
			customAggregationTranslatorContributor) {

		CustomPipelineAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>
				newCustomPipelineAggregationResultTranslatorContributor =
					_bundleContext.getService(serviceReference);

		_customAggregationResultTranslatorContributor.put(
			customAggregationTranslatorContributor.getAggregationClassName(),
			newCustomPipelineAggregationResultTranslatorContributor);
	}

	@Override
	public void removedService(
		ServiceReference<CustomPipelineAggregationResultTranslatorContributor>
			serviceReference,
		CustomPipelineAggregationResultTranslatorContributor
			customAggregationTranslatorContributor) {

		_customAggregationResultTranslatorContributor.remove(
			customAggregationTranslatorContributor.getAggregationClassName());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.create(
			bundleContext,
			CustomPipelineAggregationResultTranslatorContributor.class, this);

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
	private final Map
		<String,
		 CustomPipelineAggregationResultTranslatorContributor
			<? extends AggregationResult, ?>>
				_customAggregationResultTranslatorContributor = new HashMap<>();
	private ServiceTracker
		<CustomPipelineAggregationResultTranslatorContributor,
		 CustomPipelineAggregationResultTranslatorContributor> _serviceTracker;

}