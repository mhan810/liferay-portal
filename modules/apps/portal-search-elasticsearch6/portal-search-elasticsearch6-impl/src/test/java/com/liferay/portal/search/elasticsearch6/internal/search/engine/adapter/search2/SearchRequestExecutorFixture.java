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

import com.liferay.portal.search.elasticsearch6.internal.aggregation.ElasticsearchAggregationVisitorFixture;
import com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline.ElasticsearchPipelineAggregationVisitorFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch6.internal.query2.ElasticsearchQueryTranslatorFixture;
import com.liferay.portal.search.elasticsearch6.internal.sort.ElasticsearchSortFieldTranslatorFixture;
import com.liferay.portal.search.engine.adapter.search2.SearchRequestExecutor;

/**
 * @author Michael C. Han
 */
public class SearchRequestExecutorFixture {

	public SearchRequestExecutorFixture(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		_elasticsearchClientResolver = elasticsearchClientResolver;
	}

	public SearchRequestExecutor createExecutor() {
		return new ElasticsearchSearchRequestExecutor() {
			{
				countSearchRequestExecutor = createCountSearchRequestExecutor();
				multisearchSearchRequestExecutor =
					createMultisearchSearchRequestExecutor();
				searchSearchRequestExecutor =
					createSearchSearchRequestExecutor();
			}
		};
	}

	protected CommonSearchRequestBuilderAssembler
		createCommonSearchRequestBuilderAssembler() {

		ElasticsearchPipelineAggregationVisitorFixture
			elasticsearchPipelineAggregationVisitorFixture =
				new ElasticsearchPipelineAggregationVisitorFixture();

		ElasticsearchAggregationVisitorFixture
			elasticsearchAggregationVisitorFixture =
				new ElasticsearchAggregationVisitorFixture();

		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		return new CommonSearchRequestBuilderAssemblerImpl() {
			{
				aggregationTranslator =
					elasticsearchAggregationVisitorFixture.
						getElasticsearchAggregationVisitor();

				pipelineAggregationTranslator =
					elasticsearchPipelineAggregationVisitorFixture.
						getElasticsearchPipelineAggregationVisitor();

				queryTranslator =
					elasticsearchQueryTranslatorFixture.
						getElasticsearchQueryTranslator();
			}
		};
	}

	protected CountSearchRequestExecutor createCountSearchRequestExecutor() {
		return new CountSearchRequestExecutorImpl() {
			{
				commonSearchRequestBuilderAssembler =
					createCommonSearchRequestBuilderAssembler();
				commonSearchResponseAssembler =
					new CommonSearchResponseAssemblerImpl();
				elasticsearchClientResolver = _elasticsearchClientResolver;
			}
		};
	}

	protected MultisearchSearchRequestExecutor
		createMultisearchSearchRequestExecutor() {

		return new MultisearchSearchRequestExecutorImpl() {
			{
				elasticsearchClientResolver = _elasticsearchClientResolver;
				searchSearchRequestAssembler =
					createSearchSearchRequestAssembler();
				searchSearchResponseAssembler =
					createSearchSearchResponseAssembler();
			}
		};
	}

	protected SearchSearchRequestAssembler
		createSearchSearchRequestAssembler() {

		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		ElasticsearchSortFieldTranslatorFixture
			elasticsearchSortFieldTranslatorFixture =
				new ElasticsearchSortFieldTranslatorFixture(
					elasticsearchQueryTranslatorFixture.
						getElasticsearchQueryTranslator());

		return new SearchSearchRequestAssemblerImpl() {
			{
				commonSearchRequestBuilderAssembler =
					createCommonSearchRequestBuilderAssembler();
				queryTranslator =
					elasticsearchQueryTranslatorFixture.
						getElasticsearchQueryTranslator();

				sortFieldTranslator =
					elasticsearchSortFieldTranslatorFixture.
						getElasticsearchQueryTranslator();
			}
		};
	}

	protected SearchSearchRequestExecutor createSearchSearchRequestExecutor() {
		return new SearchSearchRequestExecutorImpl() {
			{
				elasticsearchClientResolver = _elasticsearchClientResolver;
				searchSearchRequestAssembler =
					createSearchSearchRequestAssembler();
				searchSearchResponseAssembler =
					createSearchSearchResponseAssembler();
			}
		};
	}

	protected SearchSearchResponseAssembler
		createSearchSearchResponseAssembler() {

		return new SearchSearchResponseAssemblerImpl() {
			{
				commonSearchResponseAssembler =
					new CommonSearchResponseAssemblerImpl();
			}
		};
	}

	private final ElasticsearchClientResolver _elasticsearchClientResolver;

}