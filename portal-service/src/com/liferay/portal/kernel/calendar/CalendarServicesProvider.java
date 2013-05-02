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

package com.liferay.portal.kernel.calendar;

import com.liferay.portal.kernel.calendar.auth.ServiceAuth;
import com.liferay.portal.kernel.calendar.service.BookingService;
import com.liferay.portal.kernel.calendar.service.ResourceService;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Josef Sustacek
 */
public interface CalendarServicesProvider {

	/**
	 * Returns unique key identifying the external system, from which this
	 * provider is fetching the resources, e.g. <code>webex</code> or
	 * <code>google-calendar</code>.
	 *
	 * @return
	 */
	public String getCalendarServiceProviderId();

	/**
	 * Returns human-friendly name of this provider, e.g. <code>WebEx</code>.
	 * @return
	 */
	public String getName();

	/**
	 * Returns human-friendly name of this provider, in given
	 * <code>locale</code>, e.g. <code>WebEx</code>.
	 *
	 * @param locale
	 * @return
	 */
	public String getName(Locale locale);

	/**
	 * Returns human-friendly description of this provider, e.g.
	 * <i>Allows users to book WebEx meetings, which were configured through
	 * WebEx plugin</i>.
	 *
	 * @return
	 */
	public String getDescription();

	/**
	 * Returns human-friendly description of this provider, in given
	 * <code>locale</code>, e.g. <i>Allows users to book WebEx meetings, which
	 * were configured through WebEx plugin</i>.
	 *
	 * @param locale
	 * @return
	 */
	public String getDescription(Locale locale);

	/**
	 * Returns list of service locations, that could be accessed through the
	 * service, e.g. "WebEx server1 with ID server1", "WebEx server2 with ID
	 * server2"...
	 *
	 * @return list of available service targets for this services provider
	 */
	public List<ServiceTarget> getAvailableServiceTargets();

	/**
	 * Returns service, through which the calendar bookings could be retrieved
	 * for any resource know to this provider.
	 *
	 * @param serviceTargetId and id of required target service, as retrieved
	 *                           from <code>getAvailableServiceTargets()</code>
	 * @param serviceAuth the authentication to be used to access given
	 *                    <code>serviceTargetId</code>
	 *
	 * @return
	 * @throws AuthTypeNotSupportedException when given type of authentication
	 *      is not supported
	 * @throws ServiceTargetNotKnownException when given
	 *      <code>serviceTargetId</code> was not recognized by the provider
	 */
	public BookingService getBookingService(
		String serviceTargetId, ServiceAuth serviceAuth)
		throws AuthTypeNotSupportedException, ServiceTargetNotKnownException;

	/**
	 * Returns service, through which the calendar resources could be fetched.
	 *
	 * @param serviceTargetId and id of required target service, as retrieved
	 *                           from <code>getAvailableServiceTargets()</code>
	 * @param serviceAuth the authentication to be used to access given
	 *                    <code>serviceTargetId</code>
	 *
	 * @throws AuthTypeNotSupportedException when given type of authentication
	 *      is not supported
	 * @throws ServiceTargetNotKnownException when given
	 *      <code>serviceTargetId</code> was not recognized by the provider
	 */
	public ResourceService getResourceService(
		String serviceTargetId, ServiceAuth serviceAuth)
		throws AuthTypeNotSupportedException, ServiceTargetNotKnownException;

	/**
	 * Return all <code>AutType</code>s which the services of this provider
	 * accept.
	 *
	 * @return
	 */
	public Set<ServiceAuth.AuthType> getSupportedAuthTypes();
}
