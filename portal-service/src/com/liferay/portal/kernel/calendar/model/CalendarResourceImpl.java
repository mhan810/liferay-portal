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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of <code>CalendarResource</code>. Constructors provide ways
 * how to create both localized and plain resource instances.
 *
 * @author Josef Sustacek
 */
public class CalendarResourceImpl implements CalendarResource {

	/**
	 * Creates new CalendarResourceImpl with given attributes.
	 *
	 * @param calendarResourceId unique ID of this resource
	 * @param name the name of this resource; will be returned for any
	 *                requested locale
	 * @param description the description of this resource; will be returned
	 *                       for any requested locale
	 */
	public CalendarResourceImpl(
		String calendarResourceId, String name, String description) {

		this(
			calendarResourceId, Collections.EMPTY_MAP, name,
			Collections.EMPTY_MAP, description);

//		this(
//			calendarResourceId,
//			Collections.singletonMap(LocaleUtil.getMostRelevantLocale(), name),
//			Collections.singletonMap(
//				LocaleUtil.getMostRelevantLocale(), description),
//			LocaleUtil.getMostRelevantLocale());
	}

	/**
	 * Creates new CalendarResourceImpl with given attributes, including
	 * possibility to specify localized names and descriptions.
	 *
	 * @param calendarResourceId unique ID of this resource
	 * @param nameMap mapping for resource's name in multiple locales, has
	 *                   to contains entry with key equal to given
	 *                   <code>defaultLocale</code>
	 * @param defaultName name to be returned, when no locale is specified or
	 *                       requested locale-specic value was not provided
	 * @param descriptionMap mapping for resource's description in multiple
	 *                          locales, has to contains entry with key equal
	 *                          to given <code>defaultLocale</code>
	 * @param defaultDescription description to be returned, when no locale is
	 *                              specified or requested locale-specic value
	 *                              was not provided
	 */
	public CalendarResourceImpl(
		String calendarResourceId, Map<Locale,String> nameMap,
		String defaultName, Map<Locale,String> descriptionMap,
		String defaultDescription) {

//		if(!nameMap.containsKey(defaultLocale)) {
//			throw new IllegalArgumentException(
//				"Name for specified default locale " + defaultLocale +
//					" is mandatory and it is not present in the nameMap");
//		}
//
//		if(!descriptionMap.containsKey(defaultLocale)) {
//			throw new IllegalArgumentException(
//				"Description for specified default locale " + defaultLocale +
//					" is mandatory and it is not present in the " +
//					"descriptionMap");
//		}

		_calendarResourceId = calendarResourceId;

		_defaultName = defaultName;
		_defaultDescription = defaultDescription;

//		_defaultName = nameMap.get(defaultLocale);
//		_defaultDescription = descriptionMap.get(defaultLocale);

		_nameMap = new HashMap<Locale, String>(nameMap);
		_descriptionMap = new HashMap<Locale, String>(descriptionMap);
	}

	public String getCalendarResourceId() {
		return _calendarResourceId;
	}

	public String getDescription() {
		return _defaultDescription;
	}

	public String getDescription(Locale locale) {

		if(_descriptionMap.containsKey(locale)) {
			return _descriptionMap.get(locale);
		}
		else {
			return _defaultDescription;
		}
	}

	public String getName() {
		return _defaultName;
	}

	public String getName(Locale locale) {

		if(_nameMap.containsKey(locale)) {
			return _nameMap.get(locale);
		}
		else {
			return _defaultName;
		}
	}

	private String _calendarResourceId;
	private String _defaultName;
	private Map<Locale,String> _nameMap;
	private String _defaultDescription;
	private Map<Locale,String> _descriptionMap;

}
