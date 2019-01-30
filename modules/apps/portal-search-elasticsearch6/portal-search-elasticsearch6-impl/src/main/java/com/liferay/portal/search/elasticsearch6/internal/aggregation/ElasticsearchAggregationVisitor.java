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

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.bucket.ChildrenAggregation;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.bucket.DateRangeAggregation;
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
import com.liferay.portal.search.aggregation.metrics.PercentilesMethod;
import com.liferay.portal.search.aggregation.metrics.ScriptedMetricAggregation;
import com.liferay.portal.search.aggregation.metrics.StatsAggregation;
import com.liferay.portal.search.aggregation.metrics.SumAggregation;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregation;
import com.liferay.portal.search.aggregation.metrics.ValueCountAggregation;
import com.liferay.portal.search.aggregation.metrics.WeightedAvgAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.DateHistogramAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.DateRangeAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.FilterAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.FiltersAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.GeoDistanceAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.HistogramAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.RangeAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.SignificantTermsAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.SignificantTextAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.TermsAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.ScriptedMetricAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.TopHitsAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.WeightedAvgAggregationTranslator;

import org.elasticsearch.join.aggregations.ChildrenAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGridAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.sampler.DiversifiedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.sampler.SamplerAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBoundsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	property = "search.engine.impl=Elasticsearch",
	service = {AggregationTranslator.class, AggregationVisitor.class}
)
public class ElasticsearchAggregationVisitor
	implements AggregationTranslator<AggregationBuilder>,
			   AggregationVisitor<AggregationBuilder> {

	@Override
	public AggregationBuilder translate(Aggregation aggregation) {
		return aggregation.accept(this);
	}

	@Override
	public AggregationBuilder visit(AvgAggregation avgAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.avg(baseMetricsAggregation.getName()),
			avgAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		CardinalityAggregation cardinalityAggregation) {

		CardinalityAggregationBuilder cardinalityAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.cardinality(
						baseMetricsAggregation.getName()),
				cardinalityAggregation, this, pipelineAggregationTranslator);

		if (cardinalityAggregation.getPrecisionThreshold() != null) {
			cardinalityAggregationBuilder.precisionThreshold(
				cardinalityAggregation.getPrecisionThreshold());
		}

		return cardinalityAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(ChildrenAggregation childrenAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				new ChildrenAggregationBuilder(
					baseMetricsAggregation.getName(),
					childrenAggregation.getChildType()),
			childrenAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		DateHistogramAggregation dateHistogramAggregation) {

		return dateHistogramAggregationTranslator.translate(
			dateHistogramAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(DateRangeAggregation dateRangeAggregation) {
		return dateRangeAggregationTranslator.translate(
			dateRangeAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		DiversifiedSamplerAggregation diversifiedSamplerAggregation) {

		DiversifiedAggregationBuilder diversifiedAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.diversifiedSampler(
						diversifiedSamplerAggregation.getName()),
				diversifiedSamplerAggregation, this,
				pipelineAggregationTranslator);

		if (diversifiedSamplerAggregation.getExecutionHint() != null) {
			diversifiedAggregationBuilder.executionHint(
				diversifiedSamplerAggregation.getExecutionHint());
		}

		if (diversifiedSamplerAggregation.getMaxDocsPerValue() != null) {
			diversifiedAggregationBuilder.maxDocsPerValue(
				diversifiedSamplerAggregation.getMaxDocsPerValue());
		}

		if (diversifiedSamplerAggregation.getShardSize() != null) {
			diversifiedAggregationBuilder.shardSize(
				diversifiedSamplerAggregation.getShardSize());
		}

		return diversifiedAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(
		ExtendedStatsAggregation extendedStatsAggregation) {

		ExtendedStatsAggregationBuilder extendedStatsAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.extendedStats(
						baseMetricsAggregation.getName()),
				extendedStatsAggregation, this, pipelineAggregationTranslator);

		if (extendedStatsAggregation.getSigma() != null) {
			extendedStatsAggregationBuilder.sigma(
				extendedStatsAggregation.getSigma());
		}

		return extendedStatsAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(FilterAggregation filterAggregation) {
		return filterAggregationTranslator.translate(
			filterAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(FiltersAggregation filtersAggregation) {
		return filtersAggregationTranslator.translate(
			filtersAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(GeoBoundsAggregation geoBoundsAggregation) {
		GeoBoundsAggregationBuilder geoBoundsAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.geoBounds(
						geoBoundsAggregation.getName()),
				geoBoundsAggregation, this, pipelineAggregationTranslator);

		if (geoBoundsAggregation.getWrapLongitude() != null) {
			geoBoundsAggregationBuilder.wrapLongitude(
				geoBoundsAggregation.getWrapLongitude());
		}

		return geoBoundsAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(
		GeoCentroidAggregation geoCentroidAggregation) {

		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.geoCentroid(
					geoCentroidAggregation.getName()),
			geoCentroidAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		GeoDistanceAggregation geoDistanceAggregation) {

		return geoDistanceAggregationTranslator.translate(
			geoDistanceAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		GeoHashGridAggregation geoHashGridAggregation) {

		GeoGridAggregationBuilder geoGridAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.geohashGrid(
						geoHashGridAggregation.getName()),
				geoHashGridAggregation, this, pipelineAggregationTranslator);

		if (geoHashGridAggregation.getPrecision() != null) {
			geoGridAggregationBuilder.precision(
				geoHashGridAggregation.getPrecision());
		}

		if (geoHashGridAggregation.getShardSize() != null) {
			geoGridAggregationBuilder.shardSize(
				geoHashGridAggregation.getShardSize());
		}

		if (geoHashGridAggregation.getSize() != null) {
			geoGridAggregationBuilder.size(geoHashGridAggregation.getSize());
		}

		return geoGridAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(GlobalAggregation globalAggregation) {
		GlobalAggregationBuilder globalAggregationBuilder =
			AggregationBuilders.global(globalAggregation.getName());

		_baseAggregationTranslator.translate(
			globalAggregationBuilder, globalAggregation, this,
			pipelineAggregationTranslator);

		return globalAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(HistogramAggregation histogramAggregation) {
		return histogramAggregationTranslator.translate(
			histogramAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(MaxAggregation maxAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.max(baseMetricsAggregation.getName()),
			maxAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(MinAggregation minAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.min(baseMetricsAggregation.getName()),
			minAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(MissingAggregation missingAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.missing(baseMetricsAggregation.getName()),
			missingAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(NestedAggregation nestedAggregation) {
		NestedAggregationBuilder nestedAggregationBuilder =
			AggregationBuilders.nested(
				nestedAggregation.getName(), nestedAggregation.getPath());

		_baseAggregationTranslator.translate(
			nestedAggregationBuilder, nestedAggregation, this,
			pipelineAggregationTranslator);

		return nestedAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(
		final PercentileRanksAggregation percentileRanksAggregation) {

		PercentileRanksAggregationBuilder percentileRanksAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.percentileRanks(
						baseMetricsAggregation.getName(),
						percentileRanksAggregation.getValues()),
				percentileRanksAggregation, this,
				pipelineAggregationTranslator);

		if (percentileRanksAggregation.getCompression() != null) {
			percentileRanksAggregationBuilder.compression(
				percentileRanksAggregation.getCompression());
		}

		if (percentileRanksAggregation.getHdrSignificantValueDigits() != null) {
			percentileRanksAggregationBuilder.numberOfSignificantValueDigits(
				percentileRanksAggregation.getHdrSignificantValueDigits());
		}

		if (percentileRanksAggregation.getKeyed() != null) {
			percentileRanksAggregationBuilder.keyed(
				percentileRanksAggregation.getKeyed());
		}

		if (percentileRanksAggregation.getPercentilesMethod() != null) {
			PercentilesMethod percentilesMethod =
				percentileRanksAggregation.getPercentilesMethod();

			percentileRanksAggregationBuilder.method(
				org.elasticsearch.search.aggregations.metrics.percentiles.
					PercentilesMethod.valueOf(percentilesMethod.name()));
		}

		return percentileRanksAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(
		PercentilesAggregation percentilesAggregation) {

		PercentilesAggregationBuilder percentilesAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.percentiles(
						baseMetricsAggregation.getName()),
				percentilesAggregation, this, pipelineAggregationTranslator);

		if (percentilesAggregation.getCompression() != null) {
			percentilesAggregationBuilder.compression(
				percentilesAggregation.getCompression());
		}

		if (percentilesAggregation.getHdrSignificantValueDigits() != null) {
			percentilesAggregationBuilder.numberOfSignificantValueDigits(
				percentilesAggregation.getHdrSignificantValueDigits());
		}

		if (percentilesAggregation.getKeyed() != null) {
			percentilesAggregationBuilder.keyed(
				percentilesAggregation.getKeyed());
		}

		double[] percents = percentilesAggregation.getPercents();

		if (percents != null) {
			percentilesAggregationBuilder.percentiles(percents);
		}

		if (percentilesAggregation.getPercentilesMethod() != null) {
			PercentilesMethod percentilesMethod =
				percentilesAggregation.getPercentilesMethod();

			percentilesAggregationBuilder.method(
				org.elasticsearch.search.aggregations.metrics.percentiles.
					PercentilesMethod.valueOf(percentilesMethod.name()));
		}

		return percentilesAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(RangeAggregation rangeAggregation) {
		return rangeAggregationTranslator.translate(
			rangeAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		ReverseNestedAggregation reverseNestedAggregation) {

		ReverseNestedAggregationBuilder reverseNestedAggregationBuilder =
			AggregationBuilders.reverseNested(
				reverseNestedAggregation.getName());

		if (reverseNestedAggregation.getPath() != null) {
			reverseNestedAggregationBuilder.path(
				reverseNestedAggregation.getPath());
		}

		_baseAggregationTranslator.translate(
			reverseNestedAggregationBuilder, reverseNestedAggregation, this,
			pipelineAggregationTranslator);

		return reverseNestedAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(SamplerAggregation samplerAggregation) {
		SamplerAggregationBuilder samplerAggregationBuilder =
			AggregationBuilders.sampler(samplerAggregation.getName());

		if (samplerAggregation.getShardSize() != null) {
			samplerAggregationBuilder.shardSize(
				samplerAggregation.getShardSize());
		}

		_baseAggregationTranslator.translate(
			samplerAggregationBuilder, samplerAggregation, this,
			pipelineAggregationTranslator);

		return samplerAggregationBuilder;
	}

	@Override
	public AggregationBuilder visit(
		ScriptedMetricAggregation scriptedMetricAggregation) {

		return scriptedMetricAggregationTranslator.translate(
			scriptedMetricAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		SignificantTermsAggregation significantTermsAggregation) {

		return significantTermsAggregationTranslator.translate(
			significantTermsAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		SignificantTextAggregation significantTextAggregation) {

		return significantTextAggregationTranslator.translate(
			significantTextAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(StatsAggregation statsAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.stats(baseMetricsAggregation.getName()),
			statsAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(SumAggregation sumAggregation) {
		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.sum(baseMetricsAggregation.getName()),
			sumAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(TermsAggregation termsAggregation) {
		return termsAggregationTranslator.translate(
			termsAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(TopHitsAggregation topHitsAggregation) {
		return topHitsAggregationTranslator.translate(
			topHitsAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		ValueCountAggregation valueCountAggregation) {

		return _baseFieldAggregationTranslator.translate(
			baseMetricsAggregation ->
				AggregationBuilders.count(baseMetricsAggregation.getName()),
			valueCountAggregation, this, pipelineAggregationTranslator);
	}

	@Override
	public AggregationBuilder visit(
		WeightedAvgAggregation weightedAvgAggregation) {

		return weightedAvgAggregationTranslator.translate(
			weightedAvgAggregation, this, pipelineAggregationTranslator);
	}

	@Reference
	protected DateHistogramAggregationTranslator
		dateHistogramAggregationTranslator;

	@Reference
	protected DateRangeAggregationTranslator dateRangeAggregationTranslator;

	@Reference
	protected FilterAggregationTranslator filterAggregationTranslator;

	@Reference
	protected FiltersAggregationTranslator filtersAggregationTranslator;

	@Reference
	protected GeoDistanceAggregationTranslator geoDistanceAggregationTranslator;

	@Reference
	protected HistogramAggregationTranslator histogramAggregationTranslator;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected PipelineAggregationTranslator<PipelineAggregationBuilder>
		pipelineAggregationTranslator;

	@Reference
	protected RangeAggregationTranslator rangeAggregationTranslator;

	@Reference
	protected ScriptedMetricAggregationTranslator
		scriptedMetricAggregationTranslator;

	@Reference
	protected SignificantTermsAggregationTranslator
		significantTermsAggregationTranslator;

	@Reference
	protected SignificantTextAggregationTranslator
		significantTextAggregationTranslator;

	@Reference
	protected TermsAggregationTranslator termsAggregationTranslator;

	@Reference
	protected TopHitsAggregationTranslator topHitsAggregationTranslator;

	@Reference
	protected WeightedAvgAggregationTranslator weightedAvgAggregationTranslator;

	private final BaseAggregationTranslator _baseAggregationTranslator =
		new BaseAggregationTranslator();
	private final BaseFieldAggregationTranslator
		_baseFieldAggregationTranslator = new BaseFieldAggregationTranslator();

}