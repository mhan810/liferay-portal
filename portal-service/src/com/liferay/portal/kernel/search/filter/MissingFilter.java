/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.search.filter;

/**
 * @author Michael C. Han
 */
public class MissingFilter extends BaseFilter {

	public MissingFilter(String fieldName) {
		_fieldName = fieldName;
	}

	@Override
	public <T> T accept(FilterVisitor<T> filterVisitor) {
		return filterVisitor.visit(this);
	}

	public String getFieldName() {
		return _fieldName;
	}

	public boolean isExist() {
		return _exist;
	}

	public boolean isNull() {
		return _null;
	}

	public void setExist(boolean exist) {
		_exist = exist;
	}

	public void setNull(boolean aNull) {
		_null = aNull;
	}

	private boolean _exist = true;
	private final String _fieldName;
	private boolean _null;

}