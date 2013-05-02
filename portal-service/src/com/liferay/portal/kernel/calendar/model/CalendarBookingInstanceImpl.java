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

import com.liferay.portal.kernel.calendar.CalendarBookingInstance;

import java.util.Date;

/**
 * @author Josef Sustacek
 */
public class CalendarBookingInstanceImpl implements CalendarBookingInstance {

	public CalendarBookingInstanceImpl(
		String calendarBookingId, String calendarResourceId) {

		_calendarBookingId = calendarBookingId;
		_calendarResourceId = calendarResourceId;
	}

	public String getCalendarBookingInstanceId() {
		return _calendarBookingId;
	}

	public String getCalendarResourceId() {
		return _calendarResourceId;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public String getCreateUserId() {
		return _createUserId;
	}

	public void setCreateUserId(String createUserId) {
		_createUserId = createUserId;
	}

	public String getCreateUserName() {
		return _createUserName;
	}

	public void setCreateUserName(String createUserName) {
		_createUserName = createUserName;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getModifiedUserId() {
		return _modifiedUserId;
	}

	public void setModifiedUserId(String modifiedUserId) {
		_modifiedUserId = modifiedUserId;
	}

	public String getModifiedUserName() {
		return _modifiedUserName;
	}

	public void setModifiedUserName(String modifiedUserName) {
		_modifiedUserName = modifiedUserName;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public Date getStartTime() {
		return _startTime;
	}

	public void setStartTime(Date startTime) {
		_startTime = startTime;
	}

	public Date getEndTime() {
		return _endTime;
	}

	public void setEndTime(Date endTime) {
		_endTime = endTime;
	}

	public boolean isAllDay() {
		return _allDay;
	}

	public void setAllDay(boolean allDay) {
		_allDay = allDay;
	}

	public boolean isRecurring() {
        return _recurring;
	}

	private String _calendarBookingId;
	private String _calendarResourceId;

	private Date _createDate;
	private String _createUserId;
	private String _createUserName;

	private Date _modifiedDate;
	private String _modifiedUserId;
	private String _modifiedUserName;

	private String _description;
	private Date _startTime;
	private Date _endTime;
	private boolean _allDay;
	private boolean _recurring;

}
