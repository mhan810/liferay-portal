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

package com.liferay.portal.search.test.util.aggregation.metrics.geobounds;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregationResult;
import com.liferay.portal.search.geolocation.GeoLocationPoint;
import com.liferay.portal.search.test.util.aggregation.BaseAggregationTestCase;
import com.liferay.portal.search.test.util.indexing.DocumentCreationHelpers;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rafael Praxedes
 */
public abstract class BaseGeoBoundsAggregationTestCase
	extends BaseAggregationTestCase {

	@Test
	public void testGeoBoundsAggregation() throws Exception {
		addDocument(
			DocumentCreationHelpers.singleGeoLocation(
				Field.GEO_LOCATION, 48.861111, 2.336389));
		addDocument(
			DocumentCreationHelpers.singleGeoLocation(
				Field.GEO_LOCATION, 48.860000, 2.327000));

		GeoBoundsAggregation geoBoundsAggregation = new GeoBoundsAggregation(
			"geoBounds", Field.GEO_LOCATION);

		geoBoundsAggregation.setWrapLongitude(true);

		GeoBoundsAggregationResult geoBoundsAggregationResult = search(
			geoBoundsAggregation);

		GeoLocationPoint bottomRight =
			geoBoundsAggregationResult.getBottomRight();

		Assert.assertEquals(48.85999997612089, bottomRight.getLatitude(), 0);
		Assert.assertEquals(2.3363889567553997, bottomRight.getLongitude(), 0);

		GeoLocationPoint topLeft = geoBoundsAggregationResult.getTopLeft();

		Assert.assertEquals(48.86111099738628, topLeft.getLatitude(), 0);
		Assert.assertEquals(2.3269999679178, topLeft.getLongitude(), 0);
	}

}