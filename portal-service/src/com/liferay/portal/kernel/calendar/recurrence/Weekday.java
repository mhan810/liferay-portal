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

package com.liferay.portal.kernel.calendar.recurrence;

/**
 * TODO this was copied from new calendar-portlet. Decide where to keep this.
 *
 * @author Marcellus Tavares
 * @author Josef Sustacek
 */
public enum Weekday {

	SUNDAY("SU"), MONDAY("MO"), TUESDAY("TU"), WEDNESDAY("WE"), THURSDAY("TH"),
	FRIDAY("FR"), SATURDAY("SA");

	public static Weekday parse(String value) {
		for(Weekday weekday: values()) {
			if (weekday.getValue().equals(value)) {
				return weekday;
			}
		}

		throw new IllegalArgumentException("Invalid value " + value);
	}

	public int getPosition() {
		return _position;
	}

	public String getValue() {
		return _value;
	}

	public void setPosition(int position) {
		if ((position < -53) || (position > 53)) {
			throw new IllegalArgumentException();
		}

		_position = position;
	}

	@Override
	public String toString() {
		return _value;
	}

	private Weekday(String value) {
		_value = value;
	}

	private int _position;
	private String _value;

}