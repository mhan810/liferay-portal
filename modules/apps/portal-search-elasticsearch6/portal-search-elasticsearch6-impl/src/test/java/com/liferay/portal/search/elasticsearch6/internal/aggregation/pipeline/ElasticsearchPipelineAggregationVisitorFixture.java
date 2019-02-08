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

package com.liferay.portal.search.elasticsearch6.internal.aggregation.pipeline;

import com.liferay.portal.search.elasticsearch6.internal.query2.ElasticsearchQueryTranslatorFixture;
import com.liferay.portal.search.elasticsearch6.internal.sort.ElasticsearchSortFieldTranslatorFixture;
import com.liferay.portal.search.spi.aggregation.pipeline.CustomPipelineAggregationTranslatorContributorRegistry;
import com.liferay.portal.search.test.util.aggregation.pipeline.TestCustomPipelineAggregationTranslatorContributorRegistry;

/**
 * @author Michael C. Han
 */
public class ElasticsearchPipelineAggregationVisitorFixture {

	public ElasticsearchPipelineAggregationVisitorFixture() {
		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		ElasticsearchSortFieldTranslatorFixture
			elasticsearchSortFieldTranslatorFixture =
				new ElasticsearchSortFieldTranslatorFixture(
					elasticsearchQueryTranslatorFixture.
						getElasticsearchQueryTranslator());

		CustomPipelineAggregationTranslatorContributorRegistry
			customPipelineAggregationTranslatorContributorRegistry =
				TestCustomPipelineAggregationTranslatorContributorRegistry.
					getInstance();

		_elasticsearchPipelineAggregationVisitor =
			new ElasticsearchPipelineAggregationVisitor() {
				{
					CustomPipelineAggregationTranslator
						customPipelineAggregationTranslator =
							new CustomPipelineAggregationTranslatorImpl() {
								{
									setCustomPipelineAggregationTranslatorContributorRegistry(
										customPipelineAggregationTranslatorContributorRegistry);
								}
							};

					setCustomPipelineAggregationTranslator(
						customPipelineAggregationTranslator);

					setSortFieldTranslator(
						elasticsearchSortFieldTranslatorFixture.
							getElasticsearchQueryTranslator());
				}
			};
	}

	public ElasticsearchPipelineAggregationVisitor
		getElasticsearchPipelineAggregationVisitor() {

		return _elasticsearchPipelineAggregationVisitor;
	}

	private final ElasticsearchPipelineAggregationVisitor
		_elasticsearchPipelineAggregationVisitor;

}