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

package com.liferay.portal.search.aggregation;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.bucket.ChildrenAggregation;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.DiversifiedSamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FiltersAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoDistanceAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoHashGridAggregation;
import com.liferay.portal.search.aggregation.bucket.GlobalAggregation;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.MissingAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.aggregation.bucket.RangeAggregation;
import com.liferay.portal.search.aggregation.bucket.ReverseNestedAggregation;
import com.liferay.portal.search.aggregation.bucket.SamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTermsAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTextAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.metrics.AvgAggregation;
import com.liferay.portal.search.aggregation.metrics.CardinalityAggregation;
import com.liferay.portal.search.aggregation.metrics.ExtendedStatsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoCentroidAggregation;
import com.liferay.portal.search.aggregation.metrics.MaxAggregation;
import com.liferay.portal.search.aggregation.metrics.MinAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentilesAggregation;
import com.liferay.portal.search.aggregation.metrics.ScriptedMetricAggregation;
import com.liferay.portal.search.aggregation.metrics.StatsAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregation;
import com.liferay.portal.search.aggregation.metrics.ValueCountAggregation;
import com.liferay.portal.search.aggregation.metrics.WeightedAvgAggregation;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface AggregationResultTranslator<S extends AggregationResult, T> {

	public S translate(AvgAggregation avgAggregation, T aggregationResult);

	public S translate(
		CardinalityAggregation cardinalityAggregation, T aggregationResult);

	public S translate(
		ChildrenAggregation childrenAggregation, T aggregationResult);

	public S translate(
		CustomAggregation customAggregation, T aggregationResult);

	public S translate(
		DateHistogramAggregation dateHistogramAggregation, T aggregationResult);

	public S translate(
		DiversifiedSamplerAggregation diversifiedSamplerAggregation,
		T aggregationResult);

	public S translate(
		ExtendedStatsAggregation extendedStatsAggregation, T aggregationResult);

	public S translate(
		FilterAggregation filterAggregation, T aggregationResult);

	public S translate(
		FiltersAggregation filtersAggregation, T aggregationResult);

	public S translate(
		GeoBoundsAggregation geoBoundsAggregation, T aggregationResult);

	public S translate(
		GeoCentroidAggregation geoCentroidAggregation, T aggregationResult);

	public S translate(
		GeoDistanceAggregation geoDistanceAggregation, T aggregationResult);

	public S translate(
		GeoHashGridAggregation geoHashGridAggregation, T aggregationResult);

	public S translate(
		GlobalAggregation globalAggregation, T aggregationResult);

	public S translate(
		HistogramAggregation histogramAggregation, T aggregationResult);

	public S translate(MaxAggregation maxAggregation, T aggregationResult);

	public S translate(MinAggregation minAggregation, T aggregationResult);

	public S translate(
		MissingAggregation missingAggregation, T aggregationResult);

	public S translate(
		NestedAggregation nestedAggregation, T aggregationResult);

	public S translate(
		PercentileRanksAggregation percentileRanksAggregation,
		T aggregationResult);

	public S translate(
		PercentilesAggregation percentilesAggregation, T aggregationResult);

	public S translate(RangeAggregation rangeAggregation, T aggregationResult);

	public S translate(
		ReverseNestedAggregation reverseNestedAggregation, T aggregationResult);

	public S translate(
		SamplerAggregation samplerAggregation, T aggregationResult);

	public S translate(
		ScriptedMetricAggregation scriptedMetricAggregation,
		T aggregationResult);

	public S translate(
		SignificantTermsAggregation significantTermsAggregation,
		T aggregationResult);

	public S translate(
		SignificantTextAggregation significantTextAggregation,
		T aggregationResult);

	public S translate(StatsAggregation statsAggregation, T aggregationResult);

	public S translate(SumAggregation sumAggregation, T aggregationResult);

	public S translate(TermsAggregation termsAggregation, T aggregationResult);

	public S translate(
		TopHitsAggregation topHitsAggregation, T aggregationResult);

	public S translate(
		ValueCountAggregation valueCountAggregation, T aggregationResult);

	public S translate(
		WeightedAvgAggregation weightedAvgAggregation, T aggregationResult);

}