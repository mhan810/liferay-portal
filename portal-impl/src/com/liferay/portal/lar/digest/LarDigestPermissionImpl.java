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

package com.liferay.portal.lar.digest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Kocsis
 */
public class LarDigestPermissionImpl implements LarDigestPermission {

	public void addActionId(String actionId) {
		_actionIds.add(actionId);
	}

	public List<String> getActionIds() {
		return _actionIds;
	}

	public String getRoleName() {
		return _roleName;
	}

	public void setRoleName(String roleName) {
		_roleName = roleName;
	}

	public void setActionIds(List<String> actionIds) {
		_actionIds = actionIds;
	}

	private String _roleName;

	private List<String> _actionIds = new ArrayList<String>();

}
