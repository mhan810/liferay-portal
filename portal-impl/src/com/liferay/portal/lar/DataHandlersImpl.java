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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.lar.StagedDataHandler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class DataHandlersImpl implements DataHandlers {

	public void addDataHandlerMapping(String key, String dataHandlerClass) {
		_dataHandlerMapping.put(key, dataHandlerClass);
	}

	public void afterPropertiesSet() {
		_dataHandlerMapping = new HashMap<String, String>();
	}

	public String getDataHandlerClass(String key) {
		if (Validator.isNull(key) || !_dataHandlerMapping.containsKey(key)) {
			return null;
		}

		return _dataHandlerMapping.get(key);
	}

	public StagedDataHandler getDataHandlerInstance(String key) {
		if (Validator.isNull(key) || !_dataHandlerMapping.containsKey(key)) {
			return null;
		}

		String dataHandlerClass = _dataHandlerMapping.get(key);

		return (StagedDataHandler)PortalBeanLocatorUtil.locate(
			dataHandlerClass);
	}

	public Map<String, String> getDataHandlerMapping() {
		return _dataHandlerMapping;
	}

	private Log _log = LogFactoryUtil.getLog(DataHandlersImpl.class);
	private Map<String, String> _dataHandlerMapping;

}