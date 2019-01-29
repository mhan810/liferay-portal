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

package com.liferay.portal.search.elasticsearch6.internal.aggregation;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.bucket.BaseBucketAggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.ChildrenAggregation;
import com.liferay.portal.search.aggregation.bucket.ChildrenAggregationResult;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregationResult;
import com.liferay.portal.search.aggregation.bucket.DiversifiedSamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.DiversifiedSamplerAggregationResult;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregationResult;
import com.liferay.portal.search.aggregation.bucket.FiltersAggregation;
import com.liferay.portal.search.aggregation.bucket.FiltersAggregationResult;
import com.liferay.portal.search.aggregation.bucket.GeoDistanceAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoDistanceAggregationResult;
import com.liferay.portal.search.aggregation.bucket.GeoHashGridAggregation;
import com.liferay.portal.search.aggregation.bucket.GeoHashGridAggregationResult;
import com.liferay.portal.search.aggregation.bucket.GlobalAggregation;
import com.liferay.portal.search.aggregation.bucket.GlobalAggregationResult;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.HistogramAggregationResult;
import com.liferay.portal.search.aggregation.bucket.MissingAggregation;
import com.liferay.portal.search.aggregation.bucket.MissingAggregationResult;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregationResult;
import com.liferay.portal.search.aggregation.bucket.RangeAggregation;
import com.liferay.portal.search.aggregation.bucket.RangeAggregationResult;
import com.liferay.portal.search.aggregation.bucket.ReverseNestedAggregation;
import com.liferay.portal.search.aggregation.bucket.ReverseNestedAggregationResult;
import com.liferay.portal.search.aggregation.bucket.SamplerAggregation;
import com.liferay.portal.search.aggregation.bucket.SamplerAggregationResult;
import com.liferay.portal.search.aggregation.bucket.SignificantTermsAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTermsAggregationResult;
import com.liferay.portal.search.aggregation.bucket.SignificantTextAggregation;
import com.liferay.portal.search.aggregation.bucket.SignificantTextsAggregationResult;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.AvgAggregation;
import com.liferay.portal.search.aggregation.metrics.AvgAggregationResult;
import com.liferay.portal.search.aggregation.metrics.CardinalityAggregation;
import com.liferay.portal.search.aggregation.metrics.CardinalityAggregationResult;
import com.liferay.portal.search.aggregation.metrics.ExtendedStatsAggregation;
import com.liferay.portal.search.aggregation.metrics.ExtendedStatsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoBoundsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.GeoCentroidAggregation;
import com.liferay.portal.search.aggregation.metrics.GeoCentroidAggregationResult;
import com.liferay.portal.search.aggregation.metrics.MaxAggregation;
import com.liferay.portal.search.aggregation.metrics.MaxAggregationResult;
import com.liferay.portal.search.aggregation.metrics.MinAggregation;
import com.liferay.portal.search.aggregation.metrics.MinAggregationResult;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregation;
import com.liferay.portal.search.aggregation.metrics.PercentileRanksAggregationResult;
import com.liferay.portal.search.aggregation.metrics.PercentilesAggregation;
import com.liferay.portal.search.aggregation.metrics.ScriptedMetricAggregation;
import com.liferay.portal.search.aggregation.metrics.ScriptedMetricAggregationResult;
import com.liferay.portal.search.aggregation.metrics.StatsAggregation;
import com.liferay.portal.search.aggregation.metrics.StatsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregationResult;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregation;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.ValueCountAggregation;
import com.liferay.portal.search.aggregation.metrics.ValueCountAggregationResult;
import com.liferay.portal.search.aggregation.metrics.WeightedAvgAggregation;
import com.liferay.portal.search.aggregation.metrics.WeightedAvgAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.PercentilesBucketPipelineAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.ElasticsearchPipelineAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.hits.SearchHitsTranslator;
import com.liferay.portal.search.geolocation.GeoLocationPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.join.aggregations.Children;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoHashGrid;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNested;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.sampler.Sampler;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBounds;
import org.elasticsearch.search.aggregations.metrics.geocentroid.GeoCentroid;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.weighted_avg.WeightedAvg;

/**
 * @author Michael C. Han
 */
public class ElasticsearchAggregationResultTranslator
	implements AggregationResultTranslator<AggregationResult, Aggregation> {

	@Override
	public AggregationResult translate(
		AvgAggregation avgAggregation, Aggregation aggregationResult) {

		Avg avg = (Avg)aggregationResult;

		return new AvgAggregationResult(avg.getName(), avg.getValue());
	}

	@Override
	public AggregationResult translate(
		CardinalityAggregation cardinalityAggregation,
		Aggregation aggregationResult) {

		Cardinality cardinality = (Cardinality)aggregationResult;

		return new CardinalityAggregationResult(
			cardinality.getName(), cardinality.getValue());
	}

	@Override
	public AggregationResult translate(
		ChildrenAggregation childrenAggregation,
		Aggregation aggregationResult) {

		Children children = (Children)aggregationResult;

		ChildrenAggregationResult childrenAggregationResult =
			new ChildrenAggregationResult(
				children.getName(), children.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				children.getAggregations(), childrenAggregation);

		childrenAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return childrenAggregationResult;
	}

	@Override
	public AggregationResult translate(
		DateHistogramAggregation dateHistogramAggregation,
		Aggregation aggregationResult) {

		return translateBuckets(
			(Histogram)aggregationResult,
			new DateHistogramAggregationResult(aggregationResult.getName()),
			dateHistogramAggregation);
	}

	@Override
	public AggregationResult translate(
		DiversifiedSamplerAggregation diversifiedSamplerAggregation,
		Aggregation aggregationResult) {

		Sampler sampler = (Sampler)aggregationResult;

		DiversifiedSamplerAggregationResult
			diversifiedSamplerAggregationResult =
				new DiversifiedSamplerAggregationResult(
					sampler.getName(), sampler.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				sampler.getAggregations(), diversifiedSamplerAggregation);

		diversifiedSamplerAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return diversifiedSamplerAggregationResult;
	}

	@Override
	public AggregationResult translate(
		ExtendedStatsAggregation extendedStatsAggregation,
		Aggregation aggregationResult) {

		ExtendedStats extendedStats = (ExtendedStats)aggregationResult;

		return new ExtendedStatsAggregationResult(
			extendedStats.getName(), extendedStats.getAvg(),
			extendedStats.getCount(), extendedStats.getMin(),
			extendedStats.getMax(), extendedStats.getSum(),
			extendedStats.getSumOfSquares(), extendedStats.getVariance(),
			extendedStats.getStdDeviation());
	}

	@Override
	public AggregationResult translate(
		FilterAggregation filterAggregation, Aggregation aggregationResult) {

		Filter filter = (Filter)aggregationResult;

		FilterAggregationResult filterAggregationResult =
			new FilterAggregationResult(filter.getName(), filter.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				filter.getAggregations(), filterAggregation);

		filterAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return filterAggregationResult;
	}

	@Override
	public AggregationResult translate(
		FiltersAggregation filtersAggregation, Aggregation aggregationResult) {

		Filters filters = (Filters)aggregationResult;

		FiltersAggregationResult filtersAggregationResult =
			new FiltersAggregationResult(filters.getName());

		return translateBuckets(
			filters, filtersAggregationResult, filtersAggregation);
	}

	@Override
	public AggregationResult translate(
		GeoBoundsAggregation geoBoundsAggregation,
		Aggregation aggregationResult) {

		GeoBounds geoBounds = (GeoBounds)aggregationResult;

		GeoPoint bottomRight = geoBounds.bottomRight();
		GeoPoint topLeft = geoBounds.topLeft();

		if ((bottomRight == null) || (topLeft == null)) {
			return null;
		}

		return new GeoBoundsAggregationResult(
			geoBounds.getName(),
			new GeoLocationPoint(topLeft.getLat(), topLeft.getLon()),
			new GeoLocationPoint(bottomRight.getLat(), bottomRight.getLon()));
	}

	@Override
	public AggregationResult translate(
		GeoCentroidAggregation geoCentroidAggregation,
		Aggregation aggregationResult) {

		GeoCentroid geoCentroid = (GeoCentroid)aggregationResult;

		GeoPoint geoPoint = geoCentroid.centroid();

		if (geoPoint == null) {
			return null;
		}

		return new GeoCentroidAggregationResult(
			geoCentroid.getName(),
			new GeoLocationPoint(geoPoint.getLat(), geoPoint.getLon()),
			geoCentroid.count());
	}

	@Override
	public AggregationResult translate(
		GeoDistanceAggregation geoDistanceAggregation,
		Aggregation aggregationResult) {

		return translateBuckets(
			(Range)aggregationResult,
			new GeoDistanceAggregationResult(aggregationResult.getName()),
			geoDistanceAggregation);
	}

	@Override
	public AggregationResult translate(
		GeoHashGridAggregation geoHashGridAggregation,
		Aggregation aggregationResult) {

		GeoHashGrid geoHashGrid = (GeoHashGrid)aggregationResult;

		return translateBuckets(
			geoHashGrid,
			new GeoHashGridAggregationResult(geoHashGrid.getName()),
			geoHashGridAggregation);
	}

	@Override
	public AggregationResult translate(
		GlobalAggregation globalAggregation, Aggregation aggregationResult) {

		Global global = (Global)aggregationResult;

		GlobalAggregationResult globalAggregationResult =
			new GlobalAggregationResult(global.getName(), global.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				global.getAggregations(), globalAggregation);

		globalAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return globalAggregationResult;
	}

	@Override
	public AggregationResult translate(
		HistogramAggregation histogramAggregation,
		Aggregation aggregationResult) {

		return translateBuckets(
			(Histogram)aggregationResult,
			new HistogramAggregationResult(aggregationResult.getName()),
			histogramAggregation);
	}

	@Override
	public AggregationResult translate(
		MaxAggregation maxAggregation, Aggregation aggregationResult) {

		Max max = (Max)aggregationResult;

		return new MaxAggregationResult(max.getName(), max.getValue());
	}

	@Override
	public AggregationResult translate(
		MinAggregation minAggregation, Aggregation aggregationResult) {

		Min min = (Min)aggregationResult;

		return new MinAggregationResult(min.getName(), min.getValue());
	}

	@Override
	public AggregationResult translate(
		MissingAggregation missingAggregation, Aggregation aggregationResult) {

		Missing missing = (Missing)aggregationResult;

		MissingAggregationResult missingAggregationResult =
			new MissingAggregationResult(
				missing.getName(), missing.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				missing.getAggregations(), missingAggregation);

		missingAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return missingAggregationResult;
	}

	@Override
	public AggregationResult translate(
		NestedAggregation nestedAggregation, Aggregation aggregationResult) {

		Nested nested = (Nested)aggregationResult;

		NestedAggregationResult nestedAggregationResult =
			new NestedAggregationResult(nested.getName(), nested.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				nested.getAggregations(), nestedAggregation);

		nestedAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return nestedAggregationResult;
	}

	@Override
	public AggregationResult translate(
		PercentileRanksAggregation percentileRanksAggregation,
		Aggregation aggregationResult) {

		PercentileRanks percentileRanks = (PercentileRanks)aggregationResult;

		PercentileRanksAggregationResult percentileRanksAggregationResult =
			new PercentileRanksAggregationResult(percentileRanks.getName());

		percentileRanks.forEach(
			percentileRank ->
				percentileRanksAggregationResult.addPercentile(
					percentileRank.getValue(), percentileRank.getPercent()));

		return percentileRanksAggregationResult;
	}

	@Override
	public AggregationResult translate(
		PercentilesAggregation percentilesAggregation,
		Aggregation aggregationResult) {

		Percentiles percentiles = (Percentiles)aggregationResult;

		PercentilesBucketPipelineAggregationResult
			percentilesBucketPipelineAggregationResult =
				new PercentilesBucketPipelineAggregationResult(
					percentiles.getName());

		percentiles.forEach(
			percentile ->
				percentilesBucketPipelineAggregationResult.addPercentile(
					percentile.getPercent(), percentile.getValue()));

		return percentilesBucketPipelineAggregationResult;
	}

	@Override
	public AggregationResult translate(
		RangeAggregation rangeAggregation, Aggregation aggregationResult) {

		return translateBuckets(
			(Range)aggregationResult,
			new RangeAggregationResult(aggregationResult.getName()),
			rangeAggregation);
	}

	@Override
	public AggregationResult translate(
		ReverseNestedAggregation reverseNestedAggregation,
		Aggregation aggregationResult) {

		ReverseNested reverseNested = (ReverseNested)aggregationResult;

		ReverseNestedAggregationResult reverseNestedAggregationResult =
			new ReverseNestedAggregationResult(
				reverseNested.getName(), reverseNested.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				reverseNested.getAggregations(), reverseNestedAggregation);

		reverseNestedAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return reverseNestedAggregationResult;
	}

	@Override
	public AggregationResult translate(
		SamplerAggregation samplerAggregation, Aggregation aggregationResult) {

		Sampler sampler = (Sampler)aggregationResult;

		SamplerAggregationResult samplerAggregationResult =
			new SamplerAggregationResult(
				sampler.getName(), sampler.getDocCount());

		List<AggregationResult> subaggregationResults =
			translateSubaggAndPipelineAggregation(
				sampler.getAggregations(), samplerAggregation);

		samplerAggregationResult.addChildAggregationResults(
			subaggregationResults);

		return samplerAggregationResult;
	}

	@Override
	public AggregationResult translate(
		ScriptedMetricAggregation scriptedMetricAggregation,
		Aggregation aggregationResult) {

		ScriptedMetric scriptedMetric = (ScriptedMetric)aggregationResult;

		return new ScriptedMetricAggregationResult(
			scriptedMetric.getName(), scriptedMetric.aggregation());
	}

	@Override
	public AggregationResult translate(
		SignificantTermsAggregation significantTermsAggregation,
		Aggregation aggregationResult) {

		Terms terms = (Terms)aggregationResult;

		SignificantTermsAggregationResult significantTermsAggregationResult =
			new SignificantTermsAggregationResult(
				terms.getName(), terms.getDocCountError(),
				terms.getSumOfOtherDocCounts());

		return translateBuckets(
			terms, significantTermsAggregationResult,
			significantTermsAggregation);
	}

	@Override
	public AggregationResult translate(
		SignificantTextAggregation significantTextAggregation,
		Aggregation aggregationResult) {

		Terms terms = (Terms)aggregationResult;

		SignificantTextsAggregationResult significantTextsAggregationResult =
			new SignificantTextsAggregationResult(
				terms.getName(), terms.getDocCountError(),
				terms.getSumOfOtherDocCounts());

		return translateBuckets(
			terms, significantTextsAggregationResult,
			significantTextAggregation);
	}

	@Override
	public AggregationResult translate(
		StatsAggregation statsAggregation, Aggregation aggregationResult) {

		Stats stats = (Stats)aggregationResult;

		return new StatsAggregationResult(
			stats.getName(), stats.getAvg(), stats.getCount(), stats.getMin(),
			stats.getMax(), stats.getSum());
	}

	@Override
	public AggregationResult translate(
		SumAggregation sumAggregation, Aggregation aggregationResult) {

		Sum sum = (Sum)aggregationResult;

		return new SumAggregationResult(sum.getName(), sum.getValue());
	}

	@Override
	public AggregationResult translate(
		TermsAggregation termsAggregation, Aggregation aggregationResult) {

		Terms terms = (Terms)aggregationResult;

		TermsAggregationResult termsAggregationResult =
			new TermsAggregationResult(
				terms.getName(), terms.getDocCountError(),
				terms.getSumOfOtherDocCounts());

		return translateBuckets(
			terms, termsAggregationResult, termsAggregation);
	}

	@Override
	public AggregationResult translate(
		TopHitsAggregation topHitsAggregation, Aggregation aggregationResult) {

		TopHits topHits = (TopHits)aggregationResult;

		SearchHits searchHits = topHits.getHits();

		return new TopHitsAggregationResult(
			topHits.getName(), _searchHitsTranslator.translate(searchHits));
	}

	@Override
	public AggregationResult translate(
		ValueCountAggregation valueCountAggregation,
		Aggregation aggregationResult) {

		ValueCount valueCount = (ValueCount)aggregationResult;

		return new ValueCountAggregationResult(
			valueCount.getName(), valueCount.getValue());
	}

	@Override
	public AggregationResult translate(
		WeightedAvgAggregation weightedAvgAggregation,
		Aggregation aggregationResult) {

		WeightedAvg weightedAvg = (WeightedAvg)aggregationResult;

		return new WeightedAvgAggregationResult(
			weightedAvg.getName(), weightedAvg.getValue());
	}

	protected AggregationResult translateBuckets(
		MultiBucketsAggregation multiBucketsAggregation,
		BaseBucketAggregationResult baseBucketAggregationResult,
		com.liferay.portal.search.aggregation.Aggregation aggregation) {

		List<? extends MultiBucketsAggregation.Bucket>
			multiBucketAggregationBuckets =
				multiBucketsAggregation.getBuckets();

		multiBucketAggregationBuckets.forEach(
			multiBucketAggregationBucket -> {
				Bucket bucket = new Bucket(
					multiBucketAggregationBucket.getKeyAsString(),
					multiBucketAggregationBucket.getDocCount());

				baseBucketAggregationResult.addBucket(bucket);

				List<AggregationResult> childAggregationResults =
					translateSubaggAndPipelineAggregation(
						multiBucketAggregationBucket.getAggregations(),
						aggregation);

				bucket.addChildAggregationResults(childAggregationResults);
			});

		return baseBucketAggregationResult;
	}

	protected List<AggregationResult> translateSubaggAndPipelineAggregation(
		Aggregations subaggregationsResults,
		com.liferay.portal.search.aggregation.Aggregation aggregation) {

		List<AggregationResult> aggregationResults = new ArrayList<>();

		Collection<com.liferay.portal.search.aggregation.Aggregation>
			childrenAggregationRequests = aggregation.getChildAggregations();

		childrenAggregationRequests.forEach(
			childAggregation -> {
				Aggregation elasticsearchAggregation =
					subaggregationsResults.get(childAggregation.getName());

				if (elasticsearchAggregation != null) {
					AggregationResult aggregationResult =
						childAggregation.accept(this, elasticsearchAggregation);

					aggregationResults.add(aggregationResult);
				}
			});

		Collection<PipelineAggregation> pipelineAggregationRequests =
			aggregation.getPipelineAggregations();

		pipelineAggregationRequests.forEach(
			pipelineAggregationRequest -> {
				Aggregation elasticsearchAggregation =
					subaggregationsResults.get(
						pipelineAggregationRequest.getName());

				if (elasticsearchAggregation != null) {
					AggregationResult aggregationResult =
						pipelineAggregationRequest.accept(
							_elasticsearchPipelineAggregationResultTranslator,
							elasticsearchAggregation);

					aggregationResults.add(aggregationResult);
				}
			});

		return aggregationResults;
	}

	private final ElasticsearchPipelineAggregationResultTranslator
		_elasticsearchPipelineAggregationResultTranslator =
			new ElasticsearchPipelineAggregationResultTranslator();
	private final SearchHitsTranslator _searchHitsTranslator =
		new SearchHitsTranslator();

}