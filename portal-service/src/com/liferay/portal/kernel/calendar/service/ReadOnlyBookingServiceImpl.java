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

package com.liferay.portal.kernel.calendar.service;

import com.liferay.portal.kernel.calendar.BookingChangesNotSupportedException;
import com.liferay.portal.kernel.calendar.CalendarBookingInstance;
import com.liferay.portal.kernel.calendar.auth.ServiceAuth;
import com.liferay.portal.kernel.calendar.recurrence.Recurrence;

import java.util.List;

/**
 * Booking service implementation, where all state-changing methods are throwing
 * declared <code>BookingChangesNotSupportedException</code>. This class could
 * be used as a base for read-only booking services.
 *
 * @author Josef Sustacek
 */
public abstract class ReadOnlyBookingServiceImpl extends AbstractServiceImpl
	implements BookingService {

	public ReadOnlyBookingServiceImpl(
		String serviceTargetId, ServiceAuth serviceAuth) {

		super(serviceTargetId, serviceAuth);
	}

	public CalendarBookingInstance addBooking(
		String calendarResourceId, long startTime, long endTime,
		String description, boolean allDay, Recurrence recurrence)
		throws BookingChangesNotSupportedException {

		throw new BookingChangesNotSupportedException();
	}

	public abstract CalendarBookingInstance getCalendarBookingInstance(
		String calendarBookingInstanceId);

	public abstract List<CalendarBookingInstance> getCalendarBookingInstances(
		String calendarResourceId, long startTime, long endTime);

	public abstract List<CalendarBookingInstance> getCalendarBookingInstances(
		String calendarResourceId, long startTime, long endTime, int max);

	public CalendarBookingInstance updateBookingInstance(
		String calendarBookingInstanceId, long startTime, long endTime,
		String description, boolean allDay, Recurrence recurrence)
		throws BookingChangesNotSupportedException {

		throw new BookingChangesNotSupportedException();
	}

	public void deleteBookingInstance(String calendarBookingInstanceId)
		throws BookingChangesNotSupportedException {

		throw new BookingChangesNotSupportedException();
	}

	public void deleteBookingInstances(
		String firstCalendarBookingInstanceId, boolean deleteAllFollowing)
		throws BookingChangesNotSupportedException {

		throw new BookingChangesNotSupportedException();
	}

	public boolean supportsBookingUpdates() {
		return false;
	}

}
