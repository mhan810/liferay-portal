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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Josef Sustacek
 */
public class PluginCalendarServicesProviderConfigurator {

	public void afterPropertiesSet() {

		for (CalendarServicesProvider provider: _calendarServiceProviders) {
			initCalendarServicesProvider(provider);
		}

		_calendarServiceProviders.clear();
	}

	public void destroy() {
		for (CalendarServicesProviderRegistration registration :
			_calendarServiceProviderRegistrations) {

			destroyCalendarServicesProvider(registration);
		}

		_calendarServiceProviderRegistrations.clear();
	}

	protected void destroyCalendarServicesProvider(
		CalendarServicesProviderRegistration
			calendarServicesProviderRegistration) {

		CalendarServicesProviderRegistryUtil.unregister(
			calendarServicesProviderRegistration
				.getCalendarServicesProviderId());

		if (!calendarServicesProviderRegistration.isOverride()) {
			return;
		}

		CalendarServicesProvider originalCalendarServicesProvider =
			calendarServicesProviderRegistration
				.getOriginalCalendarServicesProvider();

		CalendarServicesProviderRegistryUtil.register(
			originalCalendarServicesProvider);
	}

	protected void initCalendarServicesProvider(
		CalendarServicesProvider calendarServicesProvider) {

		String calendarServicesProviderId =
			calendarServicesProvider.getCalendarServiceProviderId();

		CalendarServicesProviderRegistration registration =
			new CalendarServicesProviderRegistration(
				calendarServicesProviderId);

		_calendarServiceProviderRegistrations.add(registration);

		if(CalendarServicesProviderRegistryUtil.isRegistered(
			calendarServicesProviderId)) {

			CalendarServicesProvider originalProvider =
				CalendarServicesProviderRegistryUtil.getProvider(
					calendarServicesProviderId);

			registration.setOverride(true);

			registration.setOriginalCalendarServicesProvider(
				originalProvider);
		}

		// TODO some wrappers necessary?

		CalendarServicesProviderRegistryUtil.register(calendarServicesProvider);
	}

	public void setCalendarServiceProviders(
		List<CalendarServicesProvider> calendarServiceProviders) {

		_calendarServiceProviders = calendarServiceProviders;
	}

	private List<CalendarServicesProvider> _calendarServiceProviders;
	private List<CalendarServicesProviderRegistration>
		_calendarServiceProviderRegistrations =
			new ArrayList<CalendarServicesProviderRegistration>();

	private class CalendarServicesProviderRegistration {

		private CalendarServicesProviderRegistration(
			String calendarServicesProviderId) {

			_calendarServicesProviderId = calendarServicesProviderId;
		}

		public CalendarServicesProvider getOriginalCalendarServicesProvider() {
			return _originalCalendarServicesProvider;
		}

		public String getCalendarServicesProviderId() {
			return _calendarServicesProviderId;
		}

		public boolean isOverride() {
			return _override;
		}

		public void setOriginalCalendarServicesProvider(
			CalendarServicesProvider calendarServicesProvider) {

			_originalCalendarServicesProvider = calendarServicesProvider;
		}

		public void setOverride(boolean override) {
			_override = override;
		}

		private CalendarServicesProvider _originalCalendarServicesProvider;
		private boolean _override;
		private String _calendarServicesProviderId;

	}

}
