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

package com.liferay.portal.kernel.calendar.model;

import java.util.Date;

/**
 * Instances of this interface represent one event in resource's calendar.
 * Repeated event result in multiple CalendarBookingInstance.
 *
 * @author Josef Sustacek
 */
public interface CalendarBookingInstance {

	/**
	 * Returns unique ID of this booking.
	 * @return
	 */
	public String getCalendarBookingInstanceId();

	public String getCalendarResourceId();

	public Date getCreateDate();

	public String getCreateUserId();

	public String getCreateUserName();

	public Date getModifiedDate();

	public String getModifiedUserId();

	public String getModifiedUserName();

	public String getDescription();

	public Date getStartTime();

	public Date getEndTime();

	public boolean isAllDay();

	public boolean isRecurring();

}
