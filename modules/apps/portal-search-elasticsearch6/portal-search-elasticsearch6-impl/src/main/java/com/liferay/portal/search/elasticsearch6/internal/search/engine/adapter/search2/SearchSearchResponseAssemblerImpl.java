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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.ElasticsearchAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.ElasticsearchPipelineAggregationResultTranslator;
import com.liferay.portal.search.elasticsearch6.internal.hits.SearchHitsTranslator;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search2.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHits;

import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = SearchSearchResponseAssembler.class)
public class SearchSearchResponseAssemblerImpl
	implements SearchSearchResponseAssembler {

	@Override
	public void assemble(
		SearchRequestBuilder searchRequestBuilder,
		SearchResponse searchResponse, SearchSearchRequest searchSearchRequest,
		SearchSearchResponse searchSearchResponse) {

		_commonSearchResponseAssembler.assemble(
			searchRequestBuilder, searchResponse, searchSearchRequest,
			searchSearchResponse);

		Aggregations elasticsearchAggregations =
			searchResponse.getAggregations();

		Map<String, Aggregation> aggregationRequests =
			searchSearchRequest.getAggregations();

		aggregationRequests.forEach(
			(aggregationName, aggregationRequest) -> {
				org.elasticsearch.search.aggregations.Aggregation
					elasticsearchAggregation = elasticsearchAggregations.get(
						aggregationName);

				if (elasticsearchAggregation != null) {
					AggregationResult aggregationResult =
						aggregationRequest.accept(
							_aggregationResultTranslator,
							elasticsearchAggregation);

					if (aggregationResult != null) {
						searchSearchResponse.addAggregationResult(
							aggregationResult);
					}
				}
			});

		Map<String, PipelineAggregation> pipelineAggregationRequests =
			searchSearchRequest.getPipelineAggregations();

		pipelineAggregationRequests.forEach(
			(aggregationName, pipelineAggregationRequest) -> {
				org.elasticsearch.search.aggregations.Aggregation
					elasticsearchAggregation = elasticsearchAggregations.get(
						aggregationName);

				if (elasticsearchAggregation != null) {
					AggregationResult aggregationResult =
						pipelineAggregationRequest.accept(
							_pipelineAggregationResultTranslator,
							elasticsearchAggregation);

					searchSearchResponse.addAggregationResult(
						aggregationResult);
				}
			});

		org.elasticsearch.search.SearchHits elasticsearchSearchHits =
			searchResponse.getHits();

		SearchHits searchHits = _searchHitsTranslator.translate(
			elasticsearchSearchHits,
			searchSearchRequest.getAlternateUidFieldName());

		searchHits.setTotalHits(elasticsearchSearchHits.totalHits);

		searchSearchResponse.setSearchHits(searchHits);

		if (Validator.isNotNull(searchResponse.getScrollId())) {
			searchSearchResponse.setScrollId(searchResponse.getScrollId());
		}
	}

	private final AggregationResultTranslator
		<? extends AggregationResult,
		 org.elasticsearch.search.aggregations.Aggregation>
			_aggregationResultTranslator =
				new ElasticsearchAggregationResultTranslator();
	private final CommonSearchResponseAssembler _commonSearchResponseAssembler =
		new CommonSearchResponseAssemblerImpl();
	private final PipelineAggregationResultTranslator
		<? extends AggregationResult,
		 org.elasticsearch.search.aggregations.Aggregation>
			_pipelineAggregationResultTranslator =
				new ElasticsearchPipelineAggregationResultTranslator();
	private final SearchHitsTranslator _searchHitsTranslator =
		new SearchHitsTranslator();

}