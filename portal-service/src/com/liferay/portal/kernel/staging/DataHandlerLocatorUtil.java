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

package com.liferay.portal.kernel.staging;

import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.staging.DataHandlerLocator;

/**
 * @author Mate Thurzo
 */
public class DataHandlerLocatorUtil {

	public static DataHandlerLocator getLocator() {
		return _locator;
	}

	public static BaseDataHandler locate(String key) {
		return getLocator().locate(key);
	}

	public void setLocator(DataHandlerLocator locator) {
		_locator = locator;
	}

	private static DataHandlerLocator _locator;

}