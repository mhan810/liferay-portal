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
public class Range {

	public static Range unboundedFrom(Double from) {
		return new Range(from, null);
	}

	public static Range unboundedFrom(String key, Double from) {
		return new Range(key, from, null);
	}

	public static Range unboundedTo(Double to) {
		return new Range(null, to);
	}

	public static Range unboundedTo(String key, Double to) {
		return new Range(key, null, to);
	}

	public Range(Double from, Double to) {
		_from = from;
		_to = to;
	}

	public Range(String key, Double from, Double to) {
		_key = key;
		_from = from;
		_to = to;
	}

	public Double getFrom() {
		return _from;
	}

	public String getKey() {
		return _key;
	}

	public Double getTo() {
		return _to;
	}

	private final Double _from;
	private String _key;
	private final Double _to;

}