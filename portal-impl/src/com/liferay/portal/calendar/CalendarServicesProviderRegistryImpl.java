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

package com.liferay.portal.calendar;

import com.liferay.portal.kernel.calendar.CalendarServicesProvider;
import com.liferay.portal.kernel.calendar.CalendarServicesProviderRegistry;
import com.liferay.portal.kernel.calendar.ProviderNotRegisteredException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.aop.ServiceBeanAopCacheManagerUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Josef Sustacek
 */
public class CalendarServicesProviderRegistryImpl
	implements CalendarServicesProviderRegistry {

	@Override
	public boolean isRegistered(String calendarServiceProviderId) {
		return _providers.containsKey(calendarServiceProviderId);
	}

	@Override
	public CalendarServicesProvider getProvider(
		String calendarServiceProviderId)
		throws ProviderNotRegisteredException {

		if(!isRegistered(calendarServiceProviderId)) {
			throw new ProviderNotRegisteredException(calendarServiceProviderId);
		}

		return _providers.get(calendarServiceProviderId);
	}

	@Override
	public List<CalendarServicesProvider> getProviders() {
		return ListUtil.fromMapValues(_providers);
	}

	@Override
	public void register(CalendarServicesProvider calendarServicesProvider) {

		String calendarServicesProviderId =
			calendarServicesProvider.getCalendarServiceProviderId();

		if(Validator.isNull(calendarServicesProviderId)) {

			throw new IllegalArgumentException(
				"provider.getCalendarServiceProviderId() cannot have a blank " +
				"value");
		}

		if(_log.isInfoEnabled()) {
			_log.info("Registering " +
				calendarServicesProvider.getName() +
				" as new Calendar resources provider with id '" +
				calendarServicesProviderId + "'");
		}

		// TODO are some proxy wrappers necessary? Based on Tomcat 7.0.34
		// testing, this seems to be not necessary

		_providers.put(
			calendarServicesProvider.getCalendarServiceProviderId(),
			calendarServicesProvider);

		// TODO based on usage in IndexerRegistryImpl, confirm if this is necessary

		ServiceBeanAopCacheManagerUtil.reset();
	}

	@Override
	public void unregister(String calendarServiceProviderId) {

		if(_log.isInfoEnabled()) {
			_log.info("Unregistering " + calendarServiceProviderId);
		}

		_providers.remove(calendarServiceProviderId);
	}

	private Map<String, CalendarServicesProvider> _providers =
		new ConcurrentHashMap<String, CalendarServicesProvider>();

	private static final Log _log =
		LogFactoryUtil.getLog(CalendarServicesProviderRegistryImpl.class);
}
