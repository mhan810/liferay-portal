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

package com.liferay.portal.kernel.search.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class FilterCacheSettingsImpl implements FilterCacheSettings {

	@Override
	public boolean isCached(String field) {
		return _cachedFields.contains(field);
	}

	public void setCachedFields(Set<String> fields) {
		_cachedFields.addAll(fields);
	}

	private Set<String> _cachedFields = new HashSet<>();
}
