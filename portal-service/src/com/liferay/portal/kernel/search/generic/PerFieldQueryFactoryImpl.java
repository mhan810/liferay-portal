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

package com.liferay.portal.kernel.search.generic;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.query.PerFieldQueryFactory;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael C. Han
 */
public class PerFieldQueryFactoryImpl implements PerFieldQueryFactory {

	@Override
	public Query getQuery(String field, String value) {
		if (isSubstringSearchRequired(field)) {
			value = getCaseInsensitiveSubstringValue(value);

			return new WildcardQueryImpl(field, value);
		}
		else {
			return new TermQueryImpl(field, value);
		}
	}

	public void setFieldNames(Set<String> fieldNames) {
		for (String fieldName : fieldNames) {
			_fieldNamePatterns.put(fieldName, Pattern.compile(fieldName));
		}
	}

	protected String getCaseInsensitiveSubstringValue(String value) {
		value = StringUtil.replace(value, StringPool.PERCENT, StringPool.BLANK);
		value = StringUtil.toLowerCase(value);
		value = StringPool.STAR + value + StringPool.STAR;

		return value;
	}

	protected boolean isSubstringSearchRequired(String field) {
		if (_fieldNamePatterns.containsKey(field)) {
			return true;
		}

		for (Pattern pattern : _fieldNamePatterns.values()) {
			Matcher matcher = pattern.matcher(field);

			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	private final Map<String, Pattern> _fieldNamePatterns =
		new LinkedHashMap<>();

}