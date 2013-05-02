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
import com.liferay.portal.kernel.calendar.recurrence.Recurrence;

import java.util.List;

/**
 * @author Josef Sustacek
 */
public interface BookingService {

	/**
	 * Book given resource with given params.
	 *
	 * @param calendarResourceId the resource that shoudl be booked
	 * @param startTime the time when the event starts
	 * @param endTime the time when the event ends
	 * @param description a human-readable description of this booking
	 * @param allDay true, when the event is a whole-day event; false otherwise
	 * @param recurrence a definition how the event will be recurring; could be
	 *                      null, meaning no recurring for this event (it
	 *                      happens only once)
	 *
	 * @return the resulting booking instance resulting from the creation; in
	 *      case of recurring booking, only the first instance is returned
	 *
	 * @throws BookingChangesNotSupportedException when this implementation
	 *      does not support create / updated / delete of calendar resource
	 *      bookings
	 */
	public CalendarBookingInstance addBooking(
		String calendarResourceId, long startTime, long endTime,
		String description, boolean allDay, Recurrence recurrence)
		throws BookingChangesNotSupportedException;

	/**
	 * Retrieve calendar booking instance of given
	 * <code>calendarBookingInstanceId</code>.
	 *
	 * @param calendarBookingInstanceId
	 * @return calendar booking of given <code>calendarBookingInstanceId</code>
	 *      or <code>null</code>, if not found
	 */
	public CalendarBookingInstance getCalendarBookingInstance(
		String calendarBookingInstanceId);

	/**
	 * Retrieve all calendar booking instances for given resource, between
	 * <code>startTime</code> and <code>endTime</code>. When some event is
	 * recurring, multiple instances of the same event may be returned, if this
	 * event happens more that once in given time period.
	 *
	 * @param calendarResourceId the ID of a resource, for which to fetch the
	 *                              booking instances
	 * @param startTime the earliest time, for which we're interested
	 * @param endTime the latest time, for which we're interested
	 * @return the list of booking instances, which have the time of happening
	 *      between given dates
	 */
	public List<CalendarBookingInstance> getCalendarBookingInstances(
		String calendarResourceId, long startTime, long endTime);


	/**
	 * Retrieve first <code>max</code> calendar booking instances for given
	 * resource, between <code>startTime</code> and <code>endTime</code>.
	 * When some event is recurring, it may be listed multiple times, as
	 * distinct instances.
	 *
	 * @param calendarResourceId the ID of a resource, for which to fetch the
	 *                              booking instances
	 * @param startTime the earliest time, for which we're interested
	 * @param endTime the latest time, for which we're interested
	 * @param max the maximal count of instances, that will be returned in the
	 *               resulting list
	 * @return
	 */
	public List<CalendarBookingInstance> getCalendarBookingInstances(
		String calendarResourceId, long startTime, long endTime, int max);

	/**
	 * Update given booking instance of some resource.
	 *
	 * @param calendarBookingInstanceId
	 * @param startTime
	 * @param endTime
	 * @param description
	 * @param allDay
	 * @param recurrence definition hot the even will be recurring; could be
	 *                      null, meaning no recurring
	 * @return the resulting booking instance resulting from the creation; in
	 *      case of recurring booking, only the first instance (the one having
	 *      <code>calendarBookingInstanceId</code>) is returned
	 * @throws BookingChangesNotSupportedException when this implementation
	 *      does not support create / updated / delete of calendar resource
	 *      bookings
	 */
	public CalendarBookingInstance updateBookingInstance(
		String calendarBookingInstanceId, long startTime, long endTime,
		String description, boolean allDay, Recurrence recurrence)
		throws BookingChangesNotSupportedException;

	/**
	 * Removes given booking instance. When the instance was part of a
	 * recurring event, the other instances are left untouched.
	 *
	 * @param calendarBookingInstanceId
	 * @throws BookingChangesNotSupportedException when this implementation
	 *      does not support create / updated / delete of calendar resource
	 *      bookings
	 */
	public void deleteBookingInstance(String calendarBookingInstanceId)
		throws BookingChangesNotSupportedException;

	/**
	 * Removes booking instances of given <code>calendarBookingInstanceId</code>
	 * and when <code>deleteAllFollowing == true</code>, also all recurring
	 * instances related to this instance (if present).
	 *
	 * @param firstCalendarBookingInstanceId
	 * @param deleteAllFollowing use true, when all recurring appearances of
	 *                              this booking should be deleted as well; use
	 *                              false, when only this particular booking
	 *                              should be deleted
	 *
	 * @throws BookingChangesNotSupportedException when this implementation
	 *      does not support create / updated / delete of calendar resource
	 *      bookings
	 */
	public void deleteBookingInstances(
		String firstCalendarBookingInstanceId, boolean deleteAllFollowing)
		throws BookingChangesNotSupportedException;

	/**
	 * Returns true, if this implementation supports updating of bookings
	 * through its <code>BookingService</code> implementation. State-changing
	 * methods then can throw <code>BookingChangesNotSupportedException</code>
	 * in case they are invoked.
	 *
	 * @return true, when resource bookings can be changed through this
	 *      provider's <code>BookingService</code>, false otherwise
	 */
	public boolean supportsBookingUpdates();
}
