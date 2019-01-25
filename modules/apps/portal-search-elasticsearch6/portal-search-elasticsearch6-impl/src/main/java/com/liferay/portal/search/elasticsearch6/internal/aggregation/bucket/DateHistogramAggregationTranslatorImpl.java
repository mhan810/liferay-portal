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

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.bucket.DateHistogramAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.BaseFieldAggregationTranslator;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = DateHistogramAggregationTranslator.class)
public class DateHistogramAggregationTranslatorImpl
	implements DateHistogramAggregationTranslator {

	@Override
	public DateHistogramAggregationBuilder translate(
		DateHistogramAggregation dateHistogramAggregation,
		AggregationTranslator<AggregationBuilder> aggregationTranslator,
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		DateHistogramAggregationBuilder dateHistogramAggregationBuilder =
			_baseFieldAggregationTranslator.translate(
				baseMetricsAggregation ->
					AggregationBuilders.dateHistogram(
						baseMetricsAggregation.getName()),
				dateHistogramAggregation, aggregationTranslator,
				pipelineAggregationTranslator);

		if (!ListUtil.isEmpty(dateHistogramAggregation.getOrders())) {
			BucketOrder bucketOrder = _orderTranslator.translate(
				dateHistogramAggregation.getOrders());

			dateHistogramAggregationBuilder.order(bucketOrder);
		}

		if ((dateHistogramAggregation.getMaxBound() != null) &&
			(dateHistogramAggregation.getMinBound() != null)) {

			ExtendedBounds extendedBounds = new ExtendedBounds(
				dateHistogramAggregation.getMinBound(),
				dateHistogramAggregation.getMaxBound());

			dateHistogramAggregationBuilder.extendedBounds(extendedBounds);
		}

		if (dateHistogramAggregation.getMinDocCount() != null) {
			dateHistogramAggregationBuilder.minDocCount(
				dateHistogramAggregation.getMinDocCount());
		}

		if (dateHistogramAggregation.getInterval() != null) {
			dateHistogramAggregationBuilder.interval(
				dateHistogramAggregation.getInterval());
		}

		if (dateHistogramAggregation.getOffset() != null) {
			dateHistogramAggregationBuilder.offset(
				dateHistogramAggregation.getOffset());
		}

		return dateHistogramAggregationBuilder;
	}

	private final BaseFieldAggregationTranslator
		_baseFieldAggregationTranslator = new BaseFieldAggregationTranslator();
	private final OrderTranslator _orderTranslator = new OrderTranslator();

}