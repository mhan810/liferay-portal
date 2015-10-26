/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tomas Polesovsky
 * @author Brian Wing Shun Chan
 */
public class AggregateResourceBundle extends ResourceBundle {

	public AggregateResourceBundle(ResourceBundle... resourceBundles) {
		for (int i = resourceBundles.length - 1; i >= 0; i--) {
			add(resourceBundles[i]);
		}
		_languageId = null;
	}

	public AggregateResourceBundle(String languageId) {
		_languageId = languageId;
	}

	public void add(ResourceBundle resourceBundle) {
		_resourceBundles.add(resourceBundle);
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(handleKeySet());
	}

	public List<ResourceBundle> getResourceBundles() {
		return Collections.unmodifiableList(_resourceBundles);
	}

	public void remove(ResourceBundle resourceBundle) {
		_resourceBundles.remove(resourceBundle);
	}

	@Override
	public String toString() {
		return "AggregateResourceBundle{" +
			"_resourceBundles=" + _resourceBundles.size() +
			", _languageId='" + _languageId + '\'' +
			'}';
	}

	@Override
	protected Object handleGetObject(String key) {
		for (int i = _resourceBundles.size() - 1; i >= 0; i--) {
			ResourceBundle resourceBundle = _resourceBundles.get(i);

			if (resourceBundle.containsKey(key)) {
				return resourceBundle.getObject(key);
			}
		}

		return null;
	}

	@Override
	protected Set<String> handleKeySet() {
		Set<String> keySet = new HashSet<>();

		for (int i = _resourceBundles.size() - 1; i >= 0; i--) {
			ResourceBundle resourceBundle = _resourceBundles.get(i);

			keySet.addAll(resourceBundle.keySet());
		}

		return Collections.unmodifiableSet(keySet);
	}

	private final String _languageId;
	private final List<ResourceBundle> _resourceBundles =
		new CopyOnWriteArrayList<>();

}