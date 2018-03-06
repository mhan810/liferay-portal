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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.indexer.BaseModelRetriever;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = BaseModelRetriever.class)
public class BaseModelRetrieverImpl implements BaseModelRetriever {

	@Override
	public Optional<BaseModel> fetchBaseModel(String className, long classPK) {
		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				className);

		if (persistedModelLocalService == null) {
			throw new SystemException(
				"No PersistedModelLocalService found for : " + className);
		}

		try {
			PersistedModel persistModel =
				persistedModelLocalService.getPersistedModel(classPK);

			if (!(persistModel instanceof BaseModel)) {
				if (_log.isWarnEnabled()) {
					_log.warn(persistModel + " is not a BaseModel");
				}

				return Optional.empty();
			}

			return Optional.ofNullable((BaseModel)persistModel);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"No ", className, " found for -",
						String.valueOf(classPK)));
			}

			return Optional.empty();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseModelRetrieverImpl.class);

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

}