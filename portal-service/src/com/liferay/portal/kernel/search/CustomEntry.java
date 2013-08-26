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

package com.liferay.portal.kernel.search;

import java.util.Locale;

/**
 * @author David Mendez Gonzalez
 */
public class CustomEntry {

	public long getCompanyId() {
		return _companyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public Locale getLocale() {
		return _locale;
	}

	public String getPath() {
		return _path;
	}

	public String getType() {
		return _type;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setType(String type) {
		_type = type;
	}

	private long _companyId;
	private long _groupId;
	private Locale _locale;
	private String _path;
	private String _type;

}