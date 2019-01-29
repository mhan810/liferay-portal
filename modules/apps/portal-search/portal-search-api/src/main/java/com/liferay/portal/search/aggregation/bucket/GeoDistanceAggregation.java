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

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseFieldAggregation;
import com.liferay.portal.search.geolocation.DistanceUnit;
import com.liferay.portal.search.geolocation.GeoDistance;
import com.liferay.portal.search.geolocation.GeoDistanceType;
import com.liferay.portal.search.geolocation.GeoLocationPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class GeoDistanceAggregation extends BaseFieldAggregation {

	public GeoDistanceAggregation(
		String name, String field, GeoLocationPoint geoLocationPoint) {

		super(name, field);

		_geoLocationPoint = geoLocationPoint;
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		AggregationResultTranslator<S, T> aggregationResultTranslator,
		T aggregationResult) {

		return aggregationResultTranslator.translate(this, aggregationResult);
	}

	@Override
	public <T> T accept(AggregationVisitor<T> aggregationVisitor) {
		return aggregationVisitor.visit(this);
	}

	public void addRange(Range range) {
		_ranges.add(range);
	}

	public void addRanges(Range... ranges) {
		Collections.addAll(_ranges, ranges);
	}

	public void addUnboundedFrom(Double from) {
		addRange(new Range(from, null));
	}

	public void addUnboundedFrom(String key, Double from) {
		addRange(new Range(key, from, null));
	}

	public void addUnboundedTo(String key, Double to) {
		addRange(new Range(key, null, to));
	}

	public DistanceUnit getDistanceUnit() {
		return _distanceUnit;
	}

	public GeoDistance getGeoDistance() {
		return _geoDistance;
	}

	public GeoDistanceType getGeoDistanceType() {
		return _geoDistanceType;
	}

	public GeoLocationPoint getGeoLocationPoint() {
		return _geoLocationPoint;
	}

	public Boolean getKeyed() {
		return _keyed;
	}

	public List<Range> getRanges() {
		return Collections.unmodifiableList(_ranges);
	}

	public void setDistanceUnit(DistanceUnit distanceUnit) {
		_distanceUnit = distanceUnit;
	}

	public void setGeoDistance(GeoDistance geoDistance) {
		_geoDistance = geoDistance;
	}

	public void setGeoDistanceType(GeoDistanceType geoDistanceType) {
		_geoDistanceType = geoDistanceType;
	}

	public void setKeyed(Boolean keyed) {
		_keyed = keyed;
	}

	private DistanceUnit _distanceUnit;
	private GeoDistance _geoDistance;
	private GeoDistanceType _geoDistanceType;
	private final GeoLocationPoint _geoLocationPoint;
	private Boolean _keyed;
	private List<Range> _ranges = new ArrayList<>();

}