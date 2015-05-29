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

/**
 * @author Michael C. Han
 */
public class GeoDistanceFilter extends BaseFilter {

	public GeoDistanceFilter(GeoLocationPoint pinLocation, String distance) {
		_pinLocation = pinLocation;
		_distance = distance;
	}

	@Override
	public <T> T accept(FilterVisitor<T> filterVisitor) {
		return filterVisitor.visit(this);
	}

	public String getDistance() {
		return _distance;
	}

	public GeoLocationPoint getPinLocation() {
		return _pinLocation;
	}

	private final String _distance;
	private final GeoLocationPoint _pinLocation;

}