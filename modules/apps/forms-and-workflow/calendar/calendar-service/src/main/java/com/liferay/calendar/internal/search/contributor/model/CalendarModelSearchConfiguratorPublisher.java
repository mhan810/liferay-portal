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

package com.liferay.calendar.internal.search.contributor.model;

import com.liferay.calendar.model.Calendar;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.search.contributor.model.ModelIndexerWriterContributor;
import com.liferay.portal.search.contributor.model.ModelPermissionPostFilter;
import com.liferay.portal.search.contributor.model.ModelSearchConfigurator;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.contributor.model.ModelSummaryContributor;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true, service = CalendarModelSearchConfiguratorPublisher.class
)
public class CalendarModelSearchConfiguratorPublisher {

	@Activate
	protected void activate(BundleContext bundleContext) {
		ModelSearchSettings modelSearchSettings = new ModelSearchSettings(
			Calendar.class.getName());

		modelSearchSettings.setDefaultSelectedFieldNames(
			Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
			Field.UID);

		modelSearchSettings.setDefaultSelectedLocalizedFieldNames(
			Field.DESCRIPTION, Field.NAME, CalendarField.RESOURCE_NAME);

		modelSearchSettings.setSelectAllLocales(true);

		ModelSearchConfigurator<Calendar> modelSearchConfigurator =
			new ModelSearchConfigurator<>(
				bundleContext, modelIndexWriterContributor,
				modelPermissionPostFilter, modelSearchSettings,
				modelSummaryContributor);

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put(
			"indexer.class.name", modelSearchSettings.getClassName());

		_serviceRegistration = bundleContext.registerService(
			ModelSearchConfigurator.class, modelSearchConfigurator, properties);
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Reference(
		target = "(indexer.class.name=com.liferay.calendar.model.Calendar)"
	)
	protected ModelIndexerWriterContributor<Calendar>
		modelIndexWriterContributor;

	@Reference(
		target = "(indexer.class.name=com.liferay.calendar.model.Calendar)"
	)
	protected ModelPermissionPostFilter modelPermissionPostFilter;

	@Reference(
		target = "(indexer.class.name=com.liferay.calendar.model.Calendar)"
	)
	protected ModelSummaryContributor modelSummaryContributor;

	private ServiceRegistration<ModelSearchConfigurator> _serviceRegistration;

}