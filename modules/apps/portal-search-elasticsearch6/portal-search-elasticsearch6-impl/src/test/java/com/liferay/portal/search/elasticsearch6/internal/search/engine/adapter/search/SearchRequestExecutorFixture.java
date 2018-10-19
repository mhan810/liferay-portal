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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search;

import com.liferay.portal.search.elasticsearch6.internal.SearchHitDocumentTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.facet.DefaultFacetProcessor;
import com.liferay.portal.search.elasticsearch6.internal.facet.DefaultFacetTranslator;
import com.liferay.portal.search.elasticsearch6.internal.facet.FacetProcessor;
import com.liferay.portal.search.elasticsearch6.internal.filter.BooleanFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.DateRangeFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.DateRangeTermFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.ElasticsearchFilterTranslator;
import com.liferay.portal.search.elasticsearch6.internal.filter.ExistsFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.GeoBoundingBoxFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.GeoDistanceFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.GeoDistanceRangeFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.GeoPolygonFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.MissingFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.PrefixFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.QueryFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.RangeTermFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.TermFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.TermsFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.filter.TermsSetFilterTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.groupby.DefaultGroupByTranslator;
import com.liferay.portal.search.elasticsearch6.internal.highlight.DefaultHighlighterTranslator;
import com.liferay.portal.search.elasticsearch6.internal.query.BooleanQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.DisMaxQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.ElasticsearchQueryTranslator;
import com.liferay.portal.search.elasticsearch6.internal.query.FuzzyQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.MatchAllQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.MatchQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.MoreLikeThisQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.MultiMatchQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.NestedQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.StringQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.TermQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.TermRangeQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.query.WildcardQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch6.internal.search.response.DefaultSearchResponseTranslator;
import com.liferay.portal.search.elasticsearch6.internal.sort.DefaultSortTranslator;
import com.liferay.portal.search.elasticsearch6.internal.stats.DefaultStatsTranslator;
import com.liferay.portal.search.engine.adapter.search.SearchRequestExecutor;

import org.elasticsearch.action.search.SearchRequestBuilder;

/**
 * @author Michael C. Han
 */
public class SearchRequestExecutorFixture {

	public SearchRequestExecutorFixture(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
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

	public void setFacetProcessor(
		FacetProcessor<SearchRequestBuilder> facetProcessor) {

		_facetProcessor = facetProcessor;
	}

	protected CommonSearchRequestBuilderAssembler
		createCommonSearchRequestBuilderAssembler() {

		return new CommonSearchRequestBuilderAssemblerImpl() {
			{
				facetTranslator = createDefaultFacetTranslator();
				filterTranslator = createElasticsearchFilterTranslator();
				queryTranslator = createElasticsearchQueryTranslator();
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
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected DefaultFacetTranslator createDefaultFacetTranslator() {
		return new DefaultFacetTranslator() {
			{
				facetProcessor = _facetProcessor;
				filterTranslator = createElasticsearchFilterTranslator();
			}
		};
	}

	protected ElasticsearchFilterTranslator
		createElasticsearchFilterTranslator() {

		return new ElasticsearchFilterTranslator() {
			{
				booleanFilterTranslator = new BooleanFilterTranslatorImpl();
				dateRangeFilterTranslator = new DateRangeFilterTranslatorImpl();
				dateRangeTermFilterTranslator =
					new DateRangeTermFilterTranslatorImpl();
				existsFilterTranslator = new ExistsFilterTranslatorImpl();
				geoBoundingBoxFilterTranslator =
					new GeoBoundingBoxFilterTranslatorImpl();
				geoDistanceFilterTranslator =
					new GeoDistanceFilterTranslatorImpl();
				geoDistanceRangeFilterTranslator =
					new GeoDistanceRangeFilterTranslatorImpl();
				geoPolygonFilterTranslator =
					new GeoPolygonFilterTranslatorImpl();
				missingFilterTranslator = new MissingFilterTranslatorImpl();
				prefixFilterTranslator = new PrefixFilterTranslatorImpl();
				queryFilterTranslator = new QueryFilterTranslatorImpl();
				rangeTermFilterTranslator = new RangeTermFilterTranslatorImpl();
				termFilterTranslator = new TermFilterTranslatorImpl();
				termsFilterTranslator = new TermsFilterTranslatorImpl();
				termsSetFilterTranslator = new TermsSetFilterTranslatorImpl();
			}
		};
	}

	protected ElasticsearchQueryTranslator
		createElasticsearchQueryTranslator() {

		return new ElasticsearchQueryTranslator() {
			{
				booleanQueryTranslator = new BooleanQueryTranslatorImpl();
				disMaxQueryTranslator = new DisMaxQueryTranslatorImpl();
				fuzzyQueryTranslator = new FuzzyQueryTranslatorImpl();
				matchAllQueryTranslator = new MatchAllQueryTranslatorImpl();
				matchQueryTranslator = new MatchQueryTranslatorImpl();
				moreLikeThisQueryTranslator =
					new MoreLikeThisQueryTranslatorImpl();
				multiMatchQueryTranslator = new MultiMatchQueryTranslatorImpl();
				nestedQueryTranslator = new NestedQueryTranslatorImpl();
				stringQueryTranslator = new StringQueryTranslatorImpl();
				termQueryTranslator = new TermQueryTranslatorImpl();
				termRangeQueryTranslator = new TermRangeQueryTranslatorImpl();
				wildcardQueryTranslator = new WildcardQueryTranslatorImpl();
			}
		};
	}

	protected MultisearchSearchRequestExecutor
		createMultisearchSearchRequestExecutor() {

		return new MultisearchSearchRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
				searchSearchRequestAssembler =
					createSearchSearchRequestAssembler();
				searchSearchResponseAssembler =
					createSearchSearchResponseAssembler();
			}
		};
	}

	protected SearchSearchRequestAssembler
		createSearchSearchRequestAssembler() {

		return new SearchSearchRequestAssemblerImpl() {
			{
				commonSearchRequestBuilderAssembler =
					createCommonSearchRequestBuilderAssembler();
				groupByTranslator = new DefaultGroupByTranslator();
				highlighterTranslator = new DefaultHighlighterTranslator();
				sortTranslator = new DefaultSortTranslator();
				statsTranslator = new DefaultStatsTranslator();
			}
		};
	}

	protected SearchSearchRequestExecutor createSearchSearchRequestExecutor() {
		return new SearchSearchRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
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
				searchResponseTranslator =
					new DefaultSearchResponseTranslator() {
						{
							searchHitDocumentTranslator =
								new SearchHitDocumentTranslatorImpl();
							statsTranslator = new DefaultStatsTranslator();
						}
					};
			}
		};
	}

	private final ElasticsearchConnectionManager
		_elasticsearchConnectionManager;
	private FacetProcessor<SearchRequestBuilder> _facetProcessor =
		new DefaultFacetProcessor();

}