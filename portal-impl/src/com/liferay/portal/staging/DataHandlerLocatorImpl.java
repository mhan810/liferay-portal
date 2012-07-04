/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.staging;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.BaseDataHandler;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class DataHandlerLocatorImpl implements DataHandlerLocator {

	public Map<String, String> getLarPersistenceMapping() {
		return _dataHandlerMapping;
	}

	public BaseDataHandler locate(String key) {
		if (Validator.isNull(key)) {
			return null;
		}

		if (!_dataHandlerMapping.containsKey(key)) {
			return null;
		}

		String beanId = _dataHandlerMapping.get(key);

		Object obj = PortalBeanLocatorUtil.locate(beanId);

		if (obj instanceof BaseDataHandler) {
			return (BaseDataHandler)obj;
		}

		return null;
	}

	public void setLarPersistenceMapping(
		Map<String, String> dataHandlerMapping) {

		_dataHandlerMapping = dataHandlerMapping;
	}

	private Map<String, String> _dataHandlerMapping;

}