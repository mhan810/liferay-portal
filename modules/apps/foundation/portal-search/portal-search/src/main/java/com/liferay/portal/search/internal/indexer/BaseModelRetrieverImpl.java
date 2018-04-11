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

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.model.ResourcedModel;
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
	public Optional<BaseModel<?>> fetchBaseModel(
		String className, long classPK) {

		Optional<BaseModel<?>> baseModel = null;
		
		try {
			Class clazz = Class.forName(className);

			if (ResourcedModel.class.isAssignableFrom(clazz)) {
				baseModel = _getAssetBaseModel(
					className, classPK);
			}
			else if (BaseModel.class.isAssignableFrom(clazz)) {
				baseModel = _getPersistedModel(className, classPK);
			}
		}
		catch (ClassNotFoundException e) {
			throw new SystemException(e);
		}

		return baseModel;
	}

	private Optional<BaseModel<?>> _getAssetBaseModel(
		String className, long classPK) {

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		if (assetRendererFactory == null) {
			return Optional.empty();
		}

		try {
			AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(
				classPK);

			if (assetRenderer != null) {
				Object assetObject = assetRenderer.getAssetObject();

				if (assetObject instanceof BaseModel<?>) {
					return Optional.ofNullable((BaseModel<?>)assetObject);
				}
			}
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Unable to get asset renderer for ", className,
						" with class PK ", String.valueOf(classPK)),
					pe);
			}
		}

		return Optional.empty();
	}

	private Optional<BaseModel<?>> _getPersistedModel(
		String className, long classPK) {

		PersistedModelLocalService persistedModelLocalService =
			_getPersistedModelLocalService(className);

		try {
			PersistedModel persistedModel =
				persistedModelLocalService.getPersistedModel(classPK);

			return Optional.ofNullable((BaseModel<?>)persistedModel);
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"No ", className, " found for class PK ",
						String.valueOf(classPK)),
					pe);
			}

			return Optional.empty();
		}
	}

	private PersistedModelLocalService _getPersistedModelLocalService(
		String className) {

		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				className);

		if (persistedModelLocalService == null) {
			throw new SystemException(
				"No persisted model local service found for class " +
					className);
		}

		return persistedModelLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseModelRetrieverImpl.class);

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

}