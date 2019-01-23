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

import com.liferay.portal.search.aggregation.AggregationVisitor;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DateRangeAggregation extends RangeAggregation {

	public DateRangeAggregation(String name, String field) {
		super(name, field);
	}

	@Override
	public <T> T accept(AggregationVisitor<T> aggregationVisitor) {
		return aggregationVisitor.visit(this);
	}

	public void addRange(
		long fromTimeInMilliseconds, long toTimeInMilliseconds) {

		addRange(
			new Range(
				(double)fromTimeInMilliseconds, (double)toTimeInMilliseconds));
	}

	public void addRange(
		String key, long fromTimeInMilliseconds, long toTimeInMilliseconds) {

		addRange(
			new Range(
				key, (double)fromTimeInMilliseconds,
				(double)toTimeInMilliseconds));
	}

	public void addUnboundedFrom(long fromTimeInMilliseconds) {
		addRange(new Range((double)fromTimeInMilliseconds, null));
	}

	public void addUnboundedFrom(String key, long fromTimeInMilliseconds) {
		addRange(new Range(key, (double)fromTimeInMilliseconds, null));
	}

	public void addUnboundedTo(String key, long toTimeInMilliseconds) {
		addRange(new Range(key, null, (double)toTimeInMilliseconds));
	}

	public void unboundedTo(long toTimeInMilliseconds) {
		addRange(new Range(null, (double)toTimeInMilliseconds));
	}

}