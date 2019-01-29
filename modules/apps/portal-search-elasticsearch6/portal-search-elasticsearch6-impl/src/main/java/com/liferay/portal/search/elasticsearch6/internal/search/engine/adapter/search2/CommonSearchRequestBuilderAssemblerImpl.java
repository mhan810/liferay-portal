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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search2;

import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.engine.adapter.search2.BaseSearchRequest;
import com.liferay.portal.search.query.QueryTranslator;

import java.util.Collection;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CommonSearchRequestBuilderAssembler.class)
public class CommonSearchRequestBuilderAssemblerImpl
	implements CommonSearchRequestBuilderAssembler {

	@Override
	public void assemble(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		Map<String, Aggregation> aggregationsMap =
			baseSearchRequest.getAggregations();

		if (MapUtil.isNotEmpty(aggregationsMap)) {
			Collection<Aggregation> aggregations = aggregationsMap.values();

			aggregations.forEach(
				aggregation -> {
					AggregationBuilder aggregationBuilder =
						_aggregationTranslator.translate(aggregation);

					searchRequestBuilder.addAggregation(aggregationBuilder);
				});
		}

		if (baseSearchRequest.getExplain() != null) {
			searchRequestBuilder.setExplain(baseSearchRequest.getExplain());
		}

		searchRequestBuilder.setIndices(baseSearchRequest.getIndexNames());

		Map<String, Float> indexBoosts = baseSearchRequest.getIndexBoosts();

		if (MapUtil.isNotEmpty(indexBoosts)) {
			indexBoosts.forEach(searchRequestBuilder::addIndexBoost);
		}

		if (baseSearchRequest.getMinimumScore() != null) {
			searchRequestBuilder.setMinScore(
				baseSearchRequest.getMinimumScore());
		}

		Map<String, PipelineAggregation> pipelineAggregationsMap =
			baseSearchRequest.getPipelineAggregations();

		if (MapUtil.isNotEmpty(pipelineAggregationsMap)) {
			Collection<PipelineAggregation> pipelineAggregations =
				pipelineAggregationsMap.values();

			pipelineAggregations.forEach(
				pipelineAggregation -> {
					PipelineAggregationBuilder pipelineAggregationBuilder =
						_pipelineAggregationTranslator.translate(
							pipelineAggregation);

					searchRequestBuilder.addAggregation(
						pipelineAggregationBuilder);
				});
		}

		if (baseSearchRequest.getPostFilterQuery() != null) {
			QueryBuilder postFilterQueryBuilder = _queryTranslator.translate(
				baseSearchRequest.getPostFilterQuery());

			searchRequestBuilder.setPostFilter(postFilterQueryBuilder);
		}

		QueryBuilder queryBuilder = _queryTranslator.translate(
			baseSearchRequest.getQuery());

		searchRequestBuilder.setQuery(queryBuilder);

		if (baseSearchRequest.getRequestCache() != null) {
			searchRequestBuilder.setRequestCache(
				baseSearchRequest.getRequestCache());
		}

		if (baseSearchRequest.getRescoreQuery() != null) {
			searchRequestBuilder.setRescorer(
				new QueryRescorerBuilder(
					_queryTranslator.translate(
						baseSearchRequest.getRescoreQuery())));
		}

		if (Validator.isNotNull(baseSearchRequest.getTimeoutInMilliseconds())) {
			searchRequestBuilder.setTimeout(
				TimeValue.timeValueMillis(
					baseSearchRequest.getTimeoutInMilliseconds()));
		}

		if (baseSearchRequest.getTrackTotalHits() != null) {
			searchRequestBuilder.setTrackTotalHits(
				baseSearchRequest.getTrackTotalHits());
		}

		if (baseSearchRequest.getTypes() != null) {
			searchRequestBuilder.setTypes(baseSearchRequest.getTypes());
		}
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)", unbind = "-")
	protected void setAggregationTranslator(
		AggregationTranslator<AggregationBuilder> aggregationTranslator) {

		_aggregationTranslator = aggregationTranslator;
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)", unbind = "-")
	protected void setPipelineAggregationTranslator(
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		_pipelineAggregationTranslator = pipelineAggregationTranslator;
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)", unbind = "-")
	protected void setQueryTranslator(
		QueryTranslator<QueryBuilder> queryTranslator) {

		_queryTranslator = queryTranslator;
	}

	private AggregationTranslator<AggregationBuilder> _aggregationTranslator;
	private PipelineAggregationTranslator<PipelineAggregationBuilder>
		_pipelineAggregationTranslator;
	private QueryTranslator<QueryBuilder> _queryTranslator;

}