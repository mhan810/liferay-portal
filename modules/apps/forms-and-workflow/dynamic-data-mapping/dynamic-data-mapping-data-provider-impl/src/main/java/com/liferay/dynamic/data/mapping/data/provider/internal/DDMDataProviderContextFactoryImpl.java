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

package com.liferay.dynamic.data.mapping.data.provider.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContextFactory;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true)
public class DDMDataProviderContextFactoryImpl
	implements DDMDataProviderContextFactory {

	@Override
	public DDMDataProviderContext create(String ddmDataProviderInstanceId)
		throws DDMDataProviderException {

		DDMDataProvider ddmDataProvider =
			ddmDataProviderTracker.getDDMDataProviderByInstanceId(
				ddmDataProviderInstanceId);

		if (ddmDataProvider != null) {
			return new DDMDataProviderContext(
				null, ddmDataProviderInstanceId, null);
		}

		try {
			DDMDataProviderInstance ddmDataProviderInstance =
				ddmDataProviderInstanceService.getDataProviderInstance(
					Long.valueOf(ddmDataProviderInstanceId));

			ddmDataProvider = ddmDataProviderTracker.getDDMDataProvider(
				ddmDataProviderInstance.getType());

			DDMForm ddmForm = DDMFormFactory.create(
				ddmDataProvider.getSettings());

			DDMFormValues ddmFormValues =
				ddmFormValuesJSONDeserializer.deserialize(
					ddmForm, ddmDataProviderInstance.getDefinition());

			return new DDMDataProviderContext(
				ddmDataProviderInstance.getType(), ddmDataProviderInstanceId,
				ddmFormValues);
		}
		catch (PortalException pe) {
			_log.error(
				"Unable to create DDMDataProviderContext with instance ID " +
					ddmDataProviderInstanceId,
				pe);

			throw new DDMDataProviderException(pe);
		}
	}

	@Reference
	protected DDMDataProviderInstanceService ddmDataProviderInstanceService;

	@Reference
	protected DDMDataProviderTracker ddmDataProviderTracker;

	@Reference
	protected DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer;

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderContextFactoryImpl.class);

}