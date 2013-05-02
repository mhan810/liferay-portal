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

import java.util.List;

/**
 * @author Josef Sustacek
 */
public interface CalendarServicesProviderRegistry {

	/**
	 * Returns true, if <code>CalendarServiceProvider</code> with given
	 * <code>calendarServiceProviderId</code> was registered and can be used to
	 * fetch calendar resources.
	 *
	 * @param calendarServiceProviderId ID of the provider which is looked up
	 * @return true, if provider with given ID was registered, false otherwise
	 */
	public boolean isRegistered(String calendarServiceProviderId);

	/**
	 * Returns provider registered under given ID.
	 *
	 * @param calendarResourceProviderId
	 * @return
	 */
	public CalendarServicesProvider getProvider(
		String calendarResourceProviderId)
		throws ProviderNotRegisteredException;

	public List<CalendarServicesProvider> getProviders();

//	public CalendarServicesProvider nullSafeGetProvider(
//        String calendarResourceProviderId);

	public void register(CalendarServicesProvider calendarServicesProvider);

	public void unregister(String calendarServiceProviderId);

}
