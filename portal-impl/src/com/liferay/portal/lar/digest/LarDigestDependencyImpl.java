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

/**
 * @author Daniel Kocsis
 */
public class LarDigestDependencyImpl implements LarDigestDependency {

	public LarDigestDependencyImpl(String className, String uuid) {
		_className = className;
		_uuid = uuid;
	}

	public String getClassName() {
		return _className;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setClassName(String className) {
		_className = _className;
	}

	public void setUuid(String uuid) {
		_uuid = _uuid;
	}

	private String _className;

	private String _uuid;

}
