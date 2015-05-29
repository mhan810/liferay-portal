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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public abstract class AggregateFilter<T extends Filter> extends BaseFilter {

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	public void addFilters(Filter... filters) {
		this.filters.addAll(Arrays.asList(filters));
	}

	public Collection<Filter> getFilters() {
		return Collections.unmodifiableCollection(filters);
	}

	public boolean isEmpty() {
		return filters.isEmpty();
	}

	protected Set<Filter> filters = new HashSet<>();

}