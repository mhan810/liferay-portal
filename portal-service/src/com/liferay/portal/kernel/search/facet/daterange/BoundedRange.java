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

package com.liferay.portal.kernel.search.facet.daterange;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class BoundedRange implements Range {

	public BoundedRange(Object from, Object to) {
		_from = from;
		_to = to;
	}

	@Override
	public void addRangeToBuilder(DateRangeBuilderWrapper dateRangeBuilder) {
		dateRangeBuilder.addInterval(_from, _to);
	}

	private final Object _from;
	private final Object _to;

}