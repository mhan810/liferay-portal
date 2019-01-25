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

/**
 * @author Michael C. Han
 */
public class ElasticsearchQueryTranslatorFixture {

	public ElasticsearchQueryTranslatorFixture() {
		_elasticsearchQueryTranslator = new ElasticsearchQueryTranslator() {
			{

				booleanQueryTranslator = new BooleanQueryTranslatorImpl();
				boostingQueryTranslator = new BoostingQueryTranslatorImpl();
				commonTermsQueryTranslator =
					new CommonTermsQueryTranslatorImpl();
				constantScoreQueryTranslator =
					new ConstantScoreQueryTranslatorImpl();
				dateRangeTermQueryTranslator =
					new DateRangeTermQueryTranslatorImpl();
				disMaxQueryTranslator = new DisMaxQueryTranslatorImpl();
				existsQueryTranslator = new ExistsQueryTranslatorImpl();
				fuzzyQueryTranslator = new FuzzyQueryTranslatorImpl();
				geoBoundingBoxQueryTranslator =
					new GeoBoundingBoxQueryTranslatorImpl();
				geoDistanceQueryTranslator =
					new GeoDistanceQueryTranslatorImpl();
				geoDistanceRangeQueryTranslator =
					new GeoDistanceRangeQueryTranslatorImpl();
				geoPolygonQueryTranslator = new GeoPolygonQueryTranslatorImpl();
				geoShapeQueryTranslator = new GeoShapeQueryTranslatorImpl();
				idsQueryTranslator = new IdsQueryTranslatorImpl();
				matchAllQueryTranslator = new MatchAllQueryTranslatorImpl();
				matchPhraseQueryTranslator =
					new MatchPhraseQueryTranslatorImpl();
				matchPhrasePrefixQueryTranslator =
					new MatchPhrasePrefixQueryTranslatorImpl();
				matchQueryTranslator = new MatchQueryTranslatorImpl();

				moreLikeThisQueryTranslator =
					new MoreLikeThisQueryTranslatorImpl() {
						{
							indexNameBuilder = companyId -> "Test_" + companyId;
						}
					};

				multiMatchQueryTranslator = new MultiMatchQueryTranslatorImpl();
				nestedQueryTranslator = new NestedQueryTranslatorImpl();
				prefixQueryTranslator = new PrefixQueryTranslatorImpl();
				rangeTermQueryTranslator = new RangeTermQueryTranslatorImpl();
				scriptQueryTranslator = new ScriptQueryTranslatorImpl();
				stringQueryTranslator = new StringQueryTranslatorImpl();
				termQueryTranslator = new TermQueryTranslatorImpl();
				termsQueryTranslator = new TermsQueryTranslatorImpl();
				termsSetQueryTranslator = new TermsSetQueryTranslatorImpl();
				wildcardQueryTranslator = new WildcardQueryTranslatorImpl();
			}
		};
	}

	public ElasticsearchQueryTranslator getElasticsearchQueryTranslator() {
		return _elasticsearchQueryTranslator;
	}

	private final ElasticsearchQueryTranslator _elasticsearchQueryTranslator;

}