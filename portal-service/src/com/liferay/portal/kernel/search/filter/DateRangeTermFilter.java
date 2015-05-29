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

import com.liferay.portal.kernel.util.StringBundler;

import java.util.TimeZone;

/**
 * @author Michael C. Han
 */
public class DateRangeTermFilter extends RangeTermFilter {

	public DateRangeTermFilter(
		String fieldName, String startDate, String endDate,
		boolean includesLower, boolean includesUpper) {

		super(fieldName, startDate, endDate, includesLower, includesUpper);
	}

	public String getDateFormat() {
		return _dateFormat;
	}

	public TimeZone getTimeZone() {
		return _timeZone;
	}

	public void setDateFormat(String dateFormat) {
		_dateFormat = dateFormat;
	}

	public void setTimeZone(TimeZone timeZone) {
		_timeZone = timeZone;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(8);

		sb.append("{(");
		sb.append(super.toString());
		sb.append("), ");
		sb.append(_dateFormat);
		sb.append(", ");
		sb.append(_timeZone);
		sb.append(")}");

		return sb.toString();
	}

	private String _dateFormat;
	private TimeZone _timeZone;

}