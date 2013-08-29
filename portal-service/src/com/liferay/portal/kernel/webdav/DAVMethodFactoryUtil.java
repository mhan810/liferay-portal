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

package com.liferay.portal.kernel.webdav;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import java.util.Map;

/**
 * @author Fabio Pezzutto
 */
public class DAVMethodFactoryUtil {

	public static DAVMethodFactory getDAVMethodFactory(String type) {
		return _davMethodFactoryMap.get(type);
	}

	public static void setDAVMethodFactory(
			String type, DAVMethodFactory davMethodFactory) {
		_davMethodFactoryMap.put(type, davMethodFactory);
	}

	public static void removeDAVMethodFactory(String type) {
		_davMethodFactoryMap.remove(type);
	}

	public void setDAVMethodFactoryMap(
			Map<String, DAVMethodFactory> davMethodFactoryMap) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_davMethodFactoryMap = davMethodFactoryMap;
	}

	private static Map<String, DAVMethodFactory> _davMethodFactoryMap;

}
