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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.model.Layout;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public interface PermissionDigester {
	public static final String ROLE_TEAM_PREFIX = "ROLE_TEAM_,*";

	public Map<String, List<String>> digestLayoutPermissions(
		DataHandlerContext context, Layout layout) throws Exception;

	public Map<String, List<String>> digestEntityPermissions(
			String resourceName, long resourcePK, DataHandlerContext context)
		throws Exception;

	public Map<String, List<String>> digestPortletPermissions(
			DataHandlerContext context, String portletId, Layout layout)
		throws Exception;

}