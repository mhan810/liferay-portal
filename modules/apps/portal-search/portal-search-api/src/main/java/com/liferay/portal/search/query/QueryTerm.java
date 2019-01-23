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

package com.liferay.portal.search.query;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringBundler;

import java.io.Serializable;

/**
 * @author Michael C. Han
 */
@ProviderType
public class QueryTerm implements Serializable {

	public QueryTerm(String field, Object value) {
		_field = field;
		_value = value;
	}

	public String getField() {
		return _field;
	}

	public Object getValue() {
		return _value;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{field=");
		sb.append(_field);
		sb.append(", value=");
		sb.append(String.valueOf(_value));
		sb.append("}");

		return sb.toString();
	}

	private final String _field;
	private final Object _value;

}