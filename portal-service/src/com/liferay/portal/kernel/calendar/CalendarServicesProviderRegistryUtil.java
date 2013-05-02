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

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import java.util.List;

/**
 * @author Josef Sustacek
 */
public class CalendarServicesProviderRegistryUtil {

	public static CalendarServicesProvider getProvider(
		String calendarServiceProviderId)
		throws ProviderNotRegisteredException{

		return getCalendarServicesProviderRegistry().getProvider(
			calendarServiceProviderId);
	}

	public static List<CalendarServicesProvider> getProviders() {
		return getCalendarServicesProviderRegistry().getProviders();
	}

	public static boolean isRegistered(String calendarServiceProviderId) {
		return getCalendarServicesProviderRegistry().isRegistered(
			calendarServiceProviderId);
	}

	public static void register(
		CalendarServicesProvider calendarServicesProvider) {

		getCalendarServicesProviderRegistry().register(
			calendarServicesProvider);
	}

	public static void unregister(String calendarServiceProviderId) {
		getCalendarServicesProviderRegistry().unregister(
			calendarServiceProviderId);
	}

	public static CalendarServicesProviderRegistry
		getCalendarServicesProviderRegistry() {

		PortalRuntimePermission.checkGetBeanProperty(
			CalendarServicesProviderRegistryUtil.class);

		return _calendarServicesProviderRegistry;
	}

	public void setCalendarServicesProviderRegistry(
		CalendarServicesProviderRegistry calendarServiceProviderRegistry) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_calendarServicesProviderRegistry = calendarServiceProviderRegistry;
	}

	private static CalendarServicesProviderRegistry
		_calendarServicesProviderRegistry;
}
