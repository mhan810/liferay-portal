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

package com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket;

import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.bucket.SignificantTermsAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.BaseFieldAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.significance.SignificanceHeuristicTranslator;
import com.liferay.portal.search.query.QueryTranslator;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTermsAggregationBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = SignificantTermsAggregationTranslator.class)
public class SignificantTermsAggregationTranslatorImpl
	implements SignificantTermsAggregationTranslator {

	public SignificantTermsAggregationBuilder translate(
		SignificantTermsAggregation significantTermsAggregation,
		AggregationTranslator<AggregationBuilder> aggregationTranslator,
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		SignificantTermsAggregationBuilder significantTermsAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.significantTerms(
						baseMetricsAggregation.getName()),
				significantTermsAggregation, aggregationTranslator,
				pipelineAggregationTranslator);

		significantTermsAggregationBuilder.field(
			significantTermsAggregation.getField());

		if (significantTermsAggregation.getBucketCountThresholds() != null) {
			significantTermsAggregationBuilder.bucketCountThresholds(
				_bucketCountThresholdsTranslator.translate(
					significantTermsAggregation.getBucketCountThresholds()));
		}

		if (significantTermsAggregation.getBackgroundFilterQuery() != null) {
			significantTermsAggregationBuilder.backgroundFilter(
				queryTranslator.translate(
					significantTermsAggregation.getBackgroundFilterQuery()));
		}

		if (significantTermsAggregation.getExecutionHint() != null) {
			significantTermsAggregationBuilder.executionHint(
				significantTermsAggregation.getExecutionHint());
		}

		if (significantTermsAggregation.getIncludeExcludeClause() != null) {
			significantTermsAggregationBuilder.includeExclude(
				_includeExcludeTranslator.translate(
					significantTermsAggregation.getIncludeExcludeClause()));
		}

		if (significantTermsAggregation.getMinDocCount() != null) {
			significantTermsAggregationBuilder.minDocCount(
				significantTermsAggregation.getMinDocCount());
		}

		if (significantTermsAggregation.getShardMinDocCount() != null) {
			significantTermsAggregationBuilder.shardMinDocCount(
				significantTermsAggregation.getShardMinDocCount());
		}

		if (significantTermsAggregation.getShardSize() != null) {
			significantTermsAggregationBuilder.shardSize(
				significantTermsAggregation.getShardSize());
		}

		if (significantTermsAggregation.getSignificanceHeuristic() != null) {
			significantTermsAggregationBuilder.significanceHeuristic(
				_significanceHeuristicTranslator.translate(
					significantTermsAggregation.getSignificanceHeuristic()));
		}

		return significantTermsAggregationBuilder;
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected QueryTranslator<QueryBuilder> queryTranslator;

	private final BaseFieldAggregationTranslator
		_baseFieldAggregationTranslator = new BaseFieldAggregationTranslator();
	private final BucketCountThresholdsTranslator
		_bucketCountThresholdsTranslator =
			new BucketCountThresholdsTranslator();
	private final IncludeExcludeTranslator _includeExcludeTranslator =
		new IncludeExcludeTranslator();
	private final SignificanceHeuristicTranslator
		_significanceHeuristicTranslator =
			new SignificanceHeuristicTranslator();

}