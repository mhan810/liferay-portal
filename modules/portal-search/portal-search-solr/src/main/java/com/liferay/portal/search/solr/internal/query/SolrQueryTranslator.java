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

package com.liferay.portal.search.solr.internal.query;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.WildcardQuery;
import com.liferay.portal.kernel.search.generic.DisMaxQuery;
import com.liferay.portal.kernel.search.generic.FuzzyQuery;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.search.generic.MatchQuery;
import com.liferay.portal.kernel.search.generic.MoreLikeThisQuery;
import com.liferay.portal.kernel.search.generic.MultiMatchQuery;
import com.liferay.portal.kernel.search.generic.NestedQuery;
import com.liferay.portal.kernel.search.generic.StringQuery;
import com.liferay.portal.kernel.search.query.QueryTranslator;
import com.liferay.portal.kernel.search.query.QueryVisitor;
import com.liferay.portal.search.solr.query.BooleanQueryTranslator;
import com.liferay.portal.search.solr.query.DisMaxQueryTranslator;
import com.liferay.portal.search.solr.query.FuzzyQueryTranslator;
import com.liferay.portal.search.solr.query.LuceneQueryConverter;
import com.liferay.portal.search.solr.query.MatchAllQueryTranslator;
import com.liferay.portal.search.solr.query.MatchQueryTranslator;
import com.liferay.portal.search.solr.query.MoreLikeThisQueryTranslator;
import com.liferay.portal.search.solr.query.MultiMatchQueryTranslator;
import com.liferay.portal.search.solr.query.NestedQueryTranslator;
import com.liferay.portal.search.solr.query.StringQueryTranslator;
import com.liferay.portal.search.solr.query.TermQueryTranslator;
import com.liferay.portal.search.solr.query.TermRangeQueryTranslator;
import com.liferay.portal.search.solr.query.WildcardQueryTranslator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(
	immediate = true, property = {"search.engine.impl=Solr"},
	service = {LuceneQueryConverter.class, QueryTranslator.class}
)
public class SolrQueryTranslator
	implements LuceneQueryConverter, QueryTranslator<String>,
			   QueryVisitor<org.apache.lucene.search.Query> {

	@Override
	public org.apache.lucene.search.Query convert(Query query) {
		return query.accept(this);
	}

	@Override
	public String translate(Query query, SearchContext searchContext) {
		org.apache.lucene.search.Query luceneQuery = query.accept(this);

		String queryString = null;

		if (luceneQuery != null) {
			queryString = luceneQuery.toString();
		}
		else {
			queryString = _postProcess(query.toString(), searchContext);
		}

		return queryString;
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		BooleanQuery booleanQuery) {

		return booleanQueryTranslator.translate(booleanQuery, this);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(DisMaxQuery disMaxQuery) {
		return disMaxQueryTranslator.translate(disMaxQuery, this);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(FuzzyQuery fuzzyQuery) {
		return fuzzyQueryTranslator.translate(fuzzyQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		MatchAllQuery matchAllQuery) {

		return matchAllQueryTranslator.translate(matchAllQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(MatchQuery matchQuery) {
		return matchQueryTranslator.translate(matchQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		MoreLikeThisQuery moreLikeThisQuery) {

		return moreLikeThisQueryTranslator.translate(moreLikeThisQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		MultiMatchQuery multiMatchQuery) {

		return multiMatchQueryTranslator.translate(multiMatchQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(NestedQuery nestedQuery) {
		return nestedQueryTranslator.translate(nestedQuery, this);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(StringQuery stringQuery) {
		return stringQueryTranslator.translate(stringQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(TermQuery termQuery) {
		return termQueryTranslator.translate(termQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		TermRangeQuery termRangeQuery) {

		return termRangeQueryTranslator.translate(termRangeQuery);
	}

	@Override
	public org.apache.lucene.search.Query visitQuery(
		WildcardQuery wildcardQuery) {

		return wildcardQueryTranslator.translate(wildcardQuery);
	}

	private String _postProcess(
		String queryString, SearchContext searchContext) {

		SolrPostProcesor solrPostProcesor = new SolrPostProcesor(
			queryString, searchContext.getKeywords());

		return solrPostProcesor.postProcess();
	}

	@Reference
	protected BooleanQueryTranslator booleanQueryTranslator;

	@Reference
	protected DisMaxQueryTranslator disMaxQueryTranslator;

	@Reference
	protected FuzzyQueryTranslator fuzzyQueryTranslator;

	@Reference
	protected MatchAllQueryTranslator matchAllQueryTranslator;

	@Reference
	protected MatchQueryTranslator matchQueryTranslator;

	@Reference
	protected MoreLikeThisQueryTranslator moreLikeThisQueryTranslator;

	@Reference
	protected MultiMatchQueryTranslator multiMatchQueryTranslator;

	@Reference
	protected NestedQueryTranslator nestedQueryTranslator;

	@Reference
	protected StringQueryTranslator stringQueryTranslator;

	@Reference
	protected TermQueryTranslator termQueryTranslator;

	@Reference
	protected TermRangeQueryTranslator termRangeQueryTranslator;

	@Reference
	protected WildcardQueryTranslator wildcardQueryTranslator;

}