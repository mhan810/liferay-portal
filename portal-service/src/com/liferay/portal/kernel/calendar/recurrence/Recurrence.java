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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * TODO this was copied from new calendar-portlet. Decide where to keep this.
 *
 * @author Marcellus Tavares
 * @author Josef Sustacek
 */
public class Recurrence {

	public void addExceptionDate(Calendar calendar) {
		_exceptionJCalendars.add(calendar);
	}

	public int getMaxInstancesCount() {
		return _maxInstancesCount;
	}

	public List<Calendar> getExceptionJCalendars() {
		return _exceptionJCalendars;
	}

	public Frequency getFrequency() {
		return _frequency;
	}

	public int getRepeatInterval() {
		return _repeatInterval;
	}

	public Calendar getUntilJCalendar() {
		return _untilJCalendar;
	}

	public List<Weekday> getWeekdays() {
		return _weekdays;
	}

	public void setMaxInstancesCount(int maxInstancesCount) {
		_maxInstancesCount = maxInstancesCount;
	}

	public void setExceptionJCalendars(List<Calendar> exceptionJCalendars) {
		_exceptionJCalendars = exceptionJCalendars;
	}

	public void setFrequency(Frequency frequency) {
		_frequency = frequency;
	}

	public void setRepeatInterval(int repeatInterval) {
		_repeatInterval = repeatInterval;
	}

	public void setUntilJCalendar(Calendar untilJCalendar) {
		_untilJCalendar = untilJCalendar;
	}

	public void setWeekdays(List<Weekday> weekdays) {
		_weekdays = weekdays;
	}

	// max count of repeats
	private int _maxInstancesCount;
	private List<Calendar> _exceptionJCalendars = new ArrayList<Calendar>();
	private Frequency _frequency;

	// repeat every n-th day, week, month...
	// (1 is the default = repeat every 1st)
	private int _repeatInterval;
	private Calendar _untilJCalendar;
	private List<Weekday> _weekdays;

}