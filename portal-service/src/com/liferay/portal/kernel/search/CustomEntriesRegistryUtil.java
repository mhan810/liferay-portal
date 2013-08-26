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

package com.liferay.portal.kernel.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author David Mendez Gonzalez
 */
public class CustomEntriesRegistryUtil {

	public static void addCustomEntry(
		ClassLoader classLoader, CustomEntry customEntry) {

		List<CustomEntry> customEntriesList = _customEntries.get(classLoader);

		if ( customEntriesList == null) {
			customEntriesList = new ArrayList<CustomEntry>();
		}

		customEntriesList.add(customEntry);

		_customEntries.put(classLoader, customEntriesList);

	}

	public void addCustomEntries(
		Map<ClassLoader, List<CustomEntry>> customEntries) {

		_customEntries.putAll(customEntries);
	}

	public static List<CustomEntry> getCustomEntries(ClassLoader classLoader) {
		return _customEntries.get(classLoader);
	}

	public static Map<ClassLoader, List<CustomEntry>> getCustomEntries() {
		return _customEntries;
	}

	private static Map<ClassLoader, List<CustomEntry>> _customEntries =
		new HashMap<ClassLoader, List<CustomEntry>>();

}