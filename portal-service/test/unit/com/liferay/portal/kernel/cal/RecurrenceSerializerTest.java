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

package com.liferay.portal.kernel.cal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Steven Smith
 */
public class RecurrenceSerializerTest extends RecurrenceSerializer {

	public Recurrence getRecurrence(Calendar calendar, int recurrenceType) {
		Recurrence recurrence = new Recurrence(
			calendar, new Duration(1, 0, 0, 0), recurrenceType);

		return recurrence;
	}

	@Test
	public void testDailyRecurrence() throws Exception {
		Calendar recurrenceCalendar = new GregorianCalendar(
			2010, 0, 2, 3, 4, 5);

		Recurrence recurrence = getRecurrence(
			recurrenceCalendar, Recurrence.DAILY);

		recurrence.setWeekStart(Calendar.SUNDAY);

		recurrence.setInterval(1);

		Assert.assertEquals("0 4 3 1/1 * ? *", toCronText(recurrence));
	}

	@Test
	public void testMonthlyRecurrence() throws Exception {
		Calendar recurrenceCalendar = new GregorianCalendar(
			2010, 0, 2, 3, 4, 5);

		Recurrence recurrence = getRecurrence(
			recurrenceCalendar, Recurrence.MONTHLY);

		recurrence.setWeekStart(Calendar.SUNDAY);

		recurrence.setInterval(1);

		recurrence.setByMonthDay(new int[] {15});

		Assert.assertEquals("0 4 3 15 1/1 ? *", toCronText(recurrence));
		
		recurrence.setInterval(3);

		Assert.assertEquals("0 4 3 15 1/3 ? *", toCronText(recurrence));
	}

	@Test
	public void testNoRecurrence() throws Exception {
		Calendar recurrenceCalendar = new GregorianCalendar(
			2010, 0, 2, 3, 4, 5);

		Recurrence recurrence = getRecurrence(
			recurrenceCalendar, Recurrence.NO_RECURRENCE);

		Assert.assertEquals("0 4 3 2 1 ? 2010", toCronText(recurrence));
	}

	@Test
	public void testWeeklyRecurrence() throws Exception {
		Calendar recurrenceCalendar = new GregorianCalendar(
			2010, 0, 2, 3, 4, 5);

		Recurrence recurrence = getRecurrence(
			recurrenceCalendar, Recurrence.WEEKLY);

		List<DayAndPosition> dayPos = new ArrayList<>();

		dayPos.add(new DayAndPosition(Calendar.MONDAY, 0));

		recurrence.setByDay(dayPos.toArray(new DayAndPosition[0]));

		Assert.assertEquals("0 4 3 ? * 2/1 *", toCronText(recurrence));
	}

	@Test
	public void testYearlyRecurrence() throws Exception {
		Calendar recurrenceCalendar = new GregorianCalendar(
			2010, 0, 2, 3, 4, 5);

		Recurrence recurrence = getRecurrence(
			recurrenceCalendar, Recurrence.YEARLY);

		recurrence.setInterval(1);

		Assert.assertEquals("0 4 3 ? 1 ? 2010/1", toCronText(recurrence));
	}

}