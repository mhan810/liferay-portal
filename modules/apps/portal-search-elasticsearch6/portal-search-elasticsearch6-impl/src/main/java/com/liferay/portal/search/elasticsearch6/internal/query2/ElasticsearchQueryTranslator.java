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

package com.liferay.portal.search.elasticsearch6.internal.query2;

import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.BoostingQuery;
import com.liferay.portal.search.query.CommonTermsQuery;
import com.liferay.portal.search.query.ConstantScoreQuery;
import com.liferay.portal.search.query.DateRangeTermQuery;
import com.liferay.portal.search.query.DisMaxQuery;
import com.liferay.portal.search.query.ExistsQuery;
import com.liferay.portal.search.query.FuzzyQuery;
import com.liferay.portal.search.query.GeoBoundingBoxQuery;
import com.liferay.portal.search.query.GeoDistanceQuery;
import com.liferay.portal.search.query.GeoDistanceRangeQuery;
import com.liferay.portal.search.query.GeoPolygonQuery;
import com.liferay.portal.search.query.GeoShapeQuery;
import com.liferay.portal.search.query.IdsQuery;
import com.liferay.portal.search.query.MatchAllQuery;
import com.liferay.portal.search.query.MatchPhrasePrefixQuery;
import com.liferay.portal.search.query.MatchPhraseQuery;
import com.liferay.portal.search.query.MatchQuery;
import com.liferay.portal.search.query.MoreLikeThisQuery;
import com.liferay.portal.search.query.MultiMatchQuery;
import com.liferay.portal.search.query.NestedQuery;
import com.liferay.portal.search.query.PrefixQuery;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.QueryTranslator;
import com.liferay.portal.search.query.QueryVisitor;
import com.liferay.portal.search.query.RangeTermQuery;
import com.liferay.portal.search.query.ScriptQuery;
import com.liferay.portal.search.query.StringQuery;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.query.TermsQuery;
import com.liferay.portal.search.query.TermsSetQuery;
import com.liferay.portal.search.query.WildcardQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true, property = "search.engine.impl=Elasticsearch",
	service = QueryTranslator.class
)
public class ElasticsearchQueryTranslator
	implements QueryTranslator<QueryBuilder>, QueryVisitor<QueryBuilder> {

	@Override
	public QueryBuilder translate(Query query) {
		QueryBuilder queryBuilder = query.accept(this);

		if (queryBuilder == null) {
			queryBuilder = QueryBuilders.queryStringQuery(query.toString());
		}

		if (!query.isDefaultBoost()) {
			queryBuilder.boost(query.getBoost());
		}

		return queryBuilder;
	}

	@Override
	public QueryBuilder visit(BooleanQuery booleanQuery) {
		return booleanQueryTranslator.translate(booleanQuery, this);
	}

	@Override
	public QueryBuilder visit(BoostingQuery boostingQuery) {
		return boostingQueryTranslator.translate(boostingQuery, this);
	}

	@Override
	public QueryBuilder visit(CommonTermsQuery commonTermsQuery) {
		return commonTermsQueryTranslator.translate(commonTermsQuery);
	}

	@Override
	public QueryBuilder visit(ConstantScoreQuery constantScoreQuery) {
		return constantScoreQueryTranslator.translate(constantScoreQuery, this);
	}

	@Override
	public QueryBuilder visit(DateRangeTermQuery dateRangeTermQuery) {
		return dateRangeTermQueryTranslator.translate(dateRangeTermQuery);
	}

	@Override
	public QueryBuilder visit(DisMaxQuery disMaxQuery) {
		return disMaxQueryTranslator.translate(disMaxQuery, this);
	}

	@Override
	public QueryBuilder visit(ExistsQuery existsQuery) {
		return existsQueryTranslator.translate(existsQuery);
	}

	@Override
	public QueryBuilder visit(FuzzyQuery fuzzyQuery) {
		return fuzzyQueryTranslator.translate(fuzzyQuery);
	}

	@Override
	public QueryBuilder visit(GeoBoundingBoxQuery geoBoundingBoxQuery) {
		return geoBoundingBoxQueryTranslator.translate(geoBoundingBoxQuery);
	}

	@Override
	public QueryBuilder visit(GeoDistanceQuery geoDistanceQuery) {
		return geoDistanceQueryTranslator.translate(geoDistanceQuery);
	}

	@Override
	public QueryBuilder visit(GeoDistanceRangeQuery geoDistanceRangeQuery) {
		return geoDistanceRangeQueryTranslator.translate(geoDistanceRangeQuery);
	}

	@Override
	public QueryBuilder visit(GeoPolygonQuery geoPolygonQuery) {
		return geoPolygonQueryTranslator.translate(geoPolygonQuery);
	}

	@Override
	public QueryBuilder visit(GeoShapeQuery geoShapeQuery) {
		return geoShapeQueryTranslator.translate(geoShapeQuery);
	}

	@Override
	public QueryBuilder visit(IdsQuery idsQuery) {
		return idsQueryTranslator.translate(idsQuery);
	}

	@Override
	public QueryBuilder visit(MatchAllQuery matchAllQuery) {
		return matchAllQueryTranslator.translate(matchAllQuery);
	}

	@Override
	public QueryBuilder visit(MatchPhrasePrefixQuery matchPhrasePrefixQuery) {
		return matchPhrasePrefixQueryTranslator.translate(
			matchPhrasePrefixQuery);
	}

	@Override
	public QueryBuilder visit(MatchPhraseQuery matchPhraseQuery) {
		return matchPhraseQueryTranslator.translate(matchPhraseQuery);
	}

	@Override
	public QueryBuilder visit(MatchQuery matchQuery) {
		return matchQueryTranslator.translate(matchQuery);
	}

	@Override
	public QueryBuilder visit(MoreLikeThisQuery moreLikeThisQuery) {
		return moreLikeThisQueryTranslator.translate(moreLikeThisQuery);
	}

	@Override
	public QueryBuilder visit(MultiMatchQuery multiMatchQuery) {
		return multiMatchQueryTranslator.translate(multiMatchQuery);
	}

	@Override
	public QueryBuilder visit(NestedQuery nestedQuery) {
		return nestedQueryTranslator.translate(nestedQuery, this);
	}

	@Override
	public QueryBuilder visit(PrefixQuery prefixQuery) {
		return prefixQueryTranslator.translate(prefixQuery);
	}

	@Override
	public QueryBuilder visit(RangeTermQuery rangeTermQuery) {
		return rangeTermQueryTranslator.translate(rangeTermQuery);
	}

	@Override
	public QueryBuilder visit(ScriptQuery scriptQuery) {
		return scriptQueryTranslator.translate(scriptQuery);
	}

	@Override
	public QueryBuilder visit(StringQuery stringQuery) {
		return stringQueryTranslator.translate(stringQuery);
	}

	@Override
	public QueryBuilder visit(TermQuery termQuery) {
		return termQueryTranslator.translate(termQuery);
	}

	@Override
	public QueryBuilder visit(TermsQuery termsQuery) {
		return termsQueryTranslator.translate(termsQuery);
	}

	@Override
	public QueryBuilder visit(TermsSetQuery termsSetQuery) {
		return termsSetQueryTranslator.translate(termsSetQuery);
	}

	@Override
	public QueryBuilder visit(WildcardQuery wildcardQuery) {
		return wildcardQueryTranslator.translate(wildcardQuery);
	}

	@Reference
	protected BooleanQueryTranslator booleanQueryTranslator;

	@Reference
	protected BoostingQueryTranslator boostingQueryTranslator;

	@Reference
	protected CommonTermsQueryTranslator commonTermsQueryTranslator;

	@Reference
	protected ConstantScoreQueryTranslator constantScoreQueryTranslator;

	@Reference
	protected DateRangeTermQueryTranslator dateRangeTermQueryTranslator;

	@Reference
	protected DisMaxQueryTranslator disMaxQueryTranslator;

	@Reference
	protected ExistsQueryTranslator existsQueryTranslator;

	@Reference
	protected FuzzyQueryTranslator fuzzyQueryTranslator;

	@Reference
	protected GeoBoundingBoxQueryTranslator geoBoundingBoxQueryTranslator;

	@Reference
	protected GeoDistanceQueryTranslator geoDistanceQueryTranslator;

	@Reference
	protected GeoDistanceRangeQueryTranslator geoDistanceRangeQueryTranslator;

	@Reference
	protected GeoPolygonQueryTranslator geoPolygonQueryTranslator;

	@Reference
	protected GeoShapeQueryTranslator geoShapeQueryTranslator;

	@Reference
	protected IdsQueryTranslator idsQueryTranslator;

	@Reference
	protected MatchAllQueryTranslator matchAllQueryTranslator;

	@Reference
	protected MatchPhrasePrefixQueryTranslator matchPhrasePrefixQueryTranslator;

	@Reference
	protected MatchPhraseQueryTranslator matchPhraseQueryTranslator;

	@Reference
	protected MatchQueryTranslator matchQueryTranslator;

	@Reference
	protected MoreLikeThisQueryTranslator moreLikeThisQueryTranslator;

	@Reference
	protected MultiMatchQueryTranslator multiMatchQueryTranslator;

	@Reference
	protected NestedQueryTranslator nestedQueryTranslator;

	@Reference
	protected PrefixQueryTranslator prefixQueryTranslator;

	@Reference
	protected RangeTermQueryTranslator rangeTermQueryTranslator;

	@Reference
	protected ScriptQueryTranslator scriptQueryTranslator;

	@Reference
	protected StringQueryTranslator stringQueryTranslator;

	@Reference
	protected TermQueryTranslator termQueryTranslator;

	@Reference
	protected TermsQueryTranslator termsQueryTranslator;

	@Reference
	protected TermsSetQueryTranslator termsSetQueryTranslator;

	@Reference
	protected WildcardQueryTranslator wildcardQueryTranslator;

}