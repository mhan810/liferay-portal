/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BasePersistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of the staged model data handler registry framework.
 *
 * @author Mate Thurzo
 * @see    com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistryUtil
 * @since  6.2
 */
@DoPrivileged
public class StagedModelDataHandlerRegistryImpl
	implements StagedModelDataHandlerRegistry {

	@Override
	public StagedModelDataHandler<?> getStagedModelDataHandler(
		String className) {

		return _stagedModelDataHandlers.get(className);
	}

	@Override
	public List<StagedModelDataHandler<?>> getStagedModelDataHandlers() {
		return ListUtil.fromMapValues(_stagedModelDataHandlers);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void register(StagedModelDataHandler<?> stagedModelDataHandler) {
		for (String className : stagedModelDataHandler.getClassNames()) {
			if (_stagedModelDataHandlers.containsKey(className)) {
				if (_log.isWarnEnabled()) {
					_log.warn("Duplicate class " + className);
				}

				continue;
			}

			BasePersistence persistence = getPersistence(className);

			if (persistence != null) {
				persistence.registerListener(
					(ModelListener<?>)stagedModelDataHandler);
			}

			_stagedModelDataHandlers.put(className, stagedModelDataHandler);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void unregister(StagedModelDataHandler<?> stagedModelDataHandler) {
		for (String className : stagedModelDataHandler.getClassNames()) {
			BasePersistence persistence = getPersistence(className);

			if (persistence != null) {
				persistence.unregisterListener(
					(ModelListener<?>)stagedModelDataHandler);
			}

			_stagedModelDataHandlers.remove(className);
		}
	}

	protected BasePersistence<?> getPersistence(String modelName) {
		int pos = modelName.lastIndexOf(CharPool.PERIOD);

		String entityName = modelName.substring(pos + 1);

		pos = modelName.lastIndexOf(".model.");

		String packagePath = modelName.substring(0, pos);

		try {
			return (BasePersistence<?>)PortalBeanLocatorUtil.locate(
				packagePath + ".service.persistence." + entityName +
					"Persistence");
		}
		catch (Exception e) {
			return null;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		StagedModelDataHandlerRegistryImpl.class);

	private Map<String, StagedModelDataHandler<?>> _stagedModelDataHandlers =
		new HashMap<String, StagedModelDataHandler<?>>();

}