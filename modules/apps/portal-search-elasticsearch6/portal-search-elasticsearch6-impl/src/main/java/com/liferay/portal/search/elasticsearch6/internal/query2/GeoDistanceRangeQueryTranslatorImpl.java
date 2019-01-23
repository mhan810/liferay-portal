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

package com.liferay.portal.search.elasticsearch6.internal.query2;

import com.liferay.portal.kernel.search.geolocation.GeoDistance;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.search.query.GeoDistanceRangeQuery;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = GeoDistanceRangeQueryTranslator.class)
public class GeoDistanceRangeQueryTranslatorImpl
	implements GeoDistanceRangeQueryTranslator {

	@Override
	public QueryBuilder translate(GeoDistanceRangeQuery geoDistanceRangeQuery) {
		GeoDistanceQueryBuilder geoDistanceQueryBuilder =
			new GeoDistanceQueryBuilder(geoDistanceRangeQuery.getField());

		GeoDistance geoDistance =
			geoDistanceRangeQuery.getUpperBoundGeoDistance();

		geoDistanceQueryBuilder.distance(
			String.valueOf(geoDistance.getDistance()),
			DistanceUnit.fromString(
				String.valueOf(geoDistance.getDistanceUnit())));

		GeoLocationPoint geoLocationPoint =
			geoDistanceRangeQuery.getPinGeoLocationPoint();

		geoDistanceQueryBuilder.point(
			new GeoPoint(
				geoLocationPoint.getLatitude(),
				geoLocationPoint.getLongitude()));

		return geoDistanceQueryBuilder;
	}

}