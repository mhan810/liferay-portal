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

import com.liferay.portal.service.persistence.BaseDataHandler;

/**
 * @author Mate Thurzo
 */
public class DataHandlersUtil {

	public static void addDataHandlerMapping(
		String key, String dataHandlerClass) {

		getDataHandlers().addDataHandlerMapping(key, dataHandlerClass);
	}

	public static String getDataHandlerClass(String key) {
		return getDataHandlers().getDataHandlerClass(key);
	}

	public static BaseDataHandler getDataHandlerInstance(String key) {
		return getDataHandlers().getDataHandlerInstance(key);
	}

	public static DataHandlers getDataHandlers() {
		return _dataHandlers;
	}

	public static void read(ClassLoader classLoader, String source)
		throws Exception {

		getDataHandlers().read(classLoader, source);
	}

	public void setDataHandlers(DataHandlers dataHandlers) {
		_dataHandlers = dataHandlers;
	}

	private static DataHandlers _dataHandlers;

}