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

import java.util.Locale;

/**
 * Represent one resource in calendar, which could be booked. Resource could be
 * anything from room, equipment to a person. Also, one physical thing / person
 * can have multiple calendar resources, like user can have work and leisure
 * schedule. Those two schedules will be mapped as two instances
 * of <code>CalendarResource</code>.
 *
 * @author Josef Sustacek
 */
public interface CalendarResource {

	/**
	 * Returns unique ID of this resource. Could be used to book this resource
	 * using <code>BookingService</code> instance.
	 * @return
	 */
	public String getCalendarResourceId();

	/**
	 * Returns name for this resource. This is the name in default locale as
	 * specified during creation of instance of class implementing
	 * this interface.
	 *
	 * @return description of the in default locale
	 */
	public String getName();

	/**
	 * Returns name for this resource in given <code>locale</code>, or
	 * default name, if locale-specific version for given
	 * <code>locale</code> was not specified.
	 *
	 * @param locale requested locale
	 * @return name of the resource in given locale, of default name
	 */
	public String getName(Locale locale);

	/**
	 * Returns description for this resource. This is the description in
	 * default locale as specified during creation of instance of class
	 * implementing this interface.
	 *
	 * @return description of the in default locale
	 */
	public String getDescription();

	/**
	 * Returns description for this resource in given <code>locale</code>, or
	 * default description, if locale-specific version for given
	 * <code>locale</code> was not specified.
	 *
	 * @param locale requested locale
	 * @return description of the resource in given locale, of default
	 *      description
	 */
	public String getDescription(Locale locale);

}
