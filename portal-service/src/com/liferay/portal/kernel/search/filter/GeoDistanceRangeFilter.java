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
public class GeoDistanceRangeFilter extends BaseFilter {

	public GeoDistanceRangeFilter(
		String endDistance, GeoLocationPoint pinLocation,
		String startDistance) {

		_endDistance = endDistance;
		_pinLocation = pinLocation;
		_startDistance = startDistance;
	}

	@Override
	public <T> T accept(FilterVisitor<T> filterVisitor) {
		return filterVisitor.visit(this);
	}

	public String getEndDistance() {
		return _endDistance;
	}

	public GeoLocationPoint getPinLocation() {
		return _pinLocation;
	}

	public String getStartDistance() {
		return _startDistance;
	}

	private final String _endDistance;
	private final GeoLocationPoint _pinLocation;
	private final String _startDistance;

}