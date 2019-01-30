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

import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.DateHistogramAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.DateRangeAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.FilterAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.FiltersAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.GeoDistanceAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.HistogramAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.RangeAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.SignificantTermsAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.SignificantTextAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.bucket.TermsAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.ScriptedMetricAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.TopHitsAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.metrics.WeightedAvgAggregationTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.ElasticsearchPipelineAggregationVisitorFixture;
import com.liferay.portal.search.elasticsearch6.internal.query2.ElasticsearchQueryTranslatorFixture;
import com.liferay.portal.search.elasticsearch6.internal.sort.ElasticsearchSortFieldTranslatorFixture;

/**
 * @author Michael C. Han
 */
public class ElasticsearchAggregationVisitorFixture {

	public ElasticsearchAggregationVisitorFixture() {
		ElasticsearchPipelineAggregationVisitorFixture
			pipelineAggregationVisitorFixture =
				new ElasticsearchPipelineAggregationVisitorFixture();

		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		ElasticsearchSortFieldTranslatorFixture
			elasticsearchSortFieldTranslatorFixture =
				new ElasticsearchSortFieldTranslatorFixture(
					elasticsearchQueryTranslatorFixture.
						getElasticsearchQueryTranslator());

		_elasticsearchAggregationVisitor =
			new ElasticsearchAggregationVisitor() {
				{
					setDateHistogramAggregationTranslator(
						new DateHistogramAggregationTranslatorImpl());
					setDateRangeAggregationTranslator(
						new DateRangeAggregationTranslatorImpl());
					setFilterAggregationTranslator(
						new FilterAggregationTranslatorImpl() {
							{
								setQueryTranslator(
									elasticsearchQueryTranslatorFixture.
										getElasticsearchQueryTranslator());
							}
						});
					setFiltersAggregationTranslator(
						new FiltersAggregationTranslatorImpl() {
							{
								setQueryTranslator(
									elasticsearchQueryTranslatorFixture.
										getElasticsearchQueryTranslator());
							}
						});
					setGeoDistanceAggregationTranslator(
						new GeoDistanceAggregationTranslatorImpl());
					setHistogramAggregationTranslator(
						new HistogramAggregationTranslatorImpl());
					setPipelineAggregationTranslator(
						pipelineAggregationVisitorFixture.
							getElasticsearchPipelineAggregationVisitor());
					setRangeAggregationTranslator(
						new RangeAggregationTranslatorImpl());
					setScriptedMetricAggregationTranslator(
						new ScriptedMetricAggregationTranslatorImpl());
					setSignificantTermsAggregationTranslator(
						new SignificantTermsAggregationTranslatorImpl() {
							{
								setQueryTranslator(
									elasticsearchQueryTranslatorFixture.
										getElasticsearchQueryTranslator());
							}
						});
					setSignificantTextAggregationTranslator(
						new SignificantTextAggregationTranslatorImpl() {
							{
								setQueryTranslator(
									elasticsearchQueryTranslatorFixture.
										getElasticsearchQueryTranslator());
							}
						});
					setTermsAggregationTranslator(
						new TermsAggregationTranslatorImpl());
					setTopHitsAggregationTranslator(
						new TopHitsAggregationTranslatorImpl() {
							{
								setQueryTranslator(
									elasticsearchQueryTranslatorFixture.
										getElasticsearchQueryTranslator());
								setSortFieldTranslator(
									elasticsearchSortFieldTranslatorFixture.
										getElasticsearchQueryTranslator());
							}
						});
					setWeightedAvgAggregationTranslator(
						new WeightedAvgAggregationTranslatorImpl());
				}
			};
	}

	public ElasticsearchAggregationVisitor
		getElasticsearchAggregationVisitor() {

		return _elasticsearchAggregationVisitor;
	}

	private final ElasticsearchAggregationVisitor
		_elasticsearchAggregationVisitor;

}