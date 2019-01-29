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

package com.liferay.portal.search.aggregation.bucket;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public class Order {

	public Order(String path) {
		_path = path;
	}

	public String getMetricName() {
		return _metricName;
	}

	public String getPath() {
		return _path;
	}

	public boolean isAscending() {
		return _ascending;
	}

	public void setAscending(boolean ascending) {
		_ascending = ascending;
	}

	public void setMetricName(String metricName) {
		_metricName = metricName;
	}

	private boolean _ascending;
	private String _metricName;
	private final String _path;

}