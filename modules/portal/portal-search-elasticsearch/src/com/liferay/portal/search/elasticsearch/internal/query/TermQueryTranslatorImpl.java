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

package com.liferay.portal.search.elasticsearch.internal.query;

import com.liferay.portal.kernel.search.QueryPreProcessConfiguration;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.elasticsearch.query.TermQueryTranslator;

import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author André de Oliveira
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(immediate = true, service = TermQueryTranslator.class)
public class TermQueryTranslatorImpl implements TermQueryTranslator {

	@Override
	public QueryBuilder translate(TermQuery termQuery) {
		QueryTerm queryTerm = termQuery.getQueryTerm();

		String field = queryTerm.getField();
		String value = queryTerm.getValue();

		if ((_queryPreProcessConfiguration != null) &&
			_queryPreProcessConfiguration.isSubstringSearchAlways(field)) {

			return toCaseInsensitiveSubstringQuery(termQuery);
		}

		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
			field, value);

		if (!termQuery.isDefaultBoost()) {
			matchQueryBuilder.boost(termQuery.getBoost());
		}

		if (Validator.isNotNull(termQuery.getAnalyzer())) {
			matchQueryBuilder.analyzer(termQuery.getAnalyzer());
		}

		return matchQueryBuilder;
	}

	protected org.apache.lucene.search.Query parseLuceneQuery(
		String field, String value) {

		QueryParser queryParser = new QueryParser(field, new KeywordAnalyzer());

		try {
			return queryParser.parse(value);
		}
		catch (ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setQueryPreProcessConfiguration(
		QueryPreProcessConfiguration queryPreProcessConfiguration) {

		_queryPreProcessConfiguration = queryPreProcessConfiguration;
	}

	protected QueryBuilder toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.BooleanQuery booleanQuery) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		List<BooleanClause> clauses = booleanQuery.clauses();

		for (BooleanClause booleanClause : clauses) {
			org.apache.lucene.search.TermQuery termQuery =
				(org.apache.lucene.search.TermQuery)booleanClause.getQuery();

			QueryBuilder queryBuilder = toCaseInsensitiveSubstringQuery(
				termQuery);

			BooleanClause.Occur booleanClauseOccur = booleanClause.getOccur();

			if (booleanClauseOccur.equals(BooleanClause.Occur.MUST)) {
				boolQueryBuilder.must(queryBuilder);
			}
			else if (booleanClauseOccur.equals(BooleanClause.Occur.MUST_NOT)) {
				boolQueryBuilder.mustNot(queryBuilder);
			}
			else if (booleanClauseOccur.equals(BooleanClause.Occur.SHOULD)) {
				boolQueryBuilder.should(queryBuilder);
			}
		}

		return boolQueryBuilder;
	}

	protected QueryBuilder toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.PhraseQuery phraseQuery) {

		Term[] terms = phraseQuery.getTerms();

		Term term = terms[0];

		String value = term.text();

		value = getWildcardText(value);

		return QueryBuilders.wildcardQuery(term.field(), value);
	}

	protected QueryBuilder toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.TermQuery termQuery) {

		Term term = termQuery.getTerm();

		String value = term.text();

		value = getWildcardText(value);

		return QueryBuilders.termQuery(term.field(), value);
	}

	protected QueryBuilder toCaseInsensitiveSubstringQuery(
		TermQuery termQuery) {

		QueryTerm queryTerm = termQuery.getQueryTerm();

		String field = queryTerm.getField();
		String value = queryTerm.getValue();

		org.apache.lucene.search.Query query = parseLuceneQuery(field, value);

		if (query instanceof org.apache.lucene.search.BooleanQuery) {
			return toCaseInsensitiveSubstringQuery(
				(org.apache.lucene.search.BooleanQuery)query);
		}

		if (query instanceof org.apache.lucene.search.TermQuery) {
			return toCaseInsensitiveSubstringQuery(
				(org.apache.lucene.search.TermQuery)query);
		}

		if (query instanceof org.apache.lucene.search.PhraseQuery) {
			return toCaseInsensitiveSubstringQuery(
				(org.apache.lucene.search.PhraseQuery)query);
		}

		throw new IllegalArgumentException(
			"Invalid parsed query: " + query.toString());
	}

	protected void unsetQueryPreProcessConfiguration(
		QueryPreProcessConfiguration queryPreProcessConfiguration) {

		_queryPreProcessConfiguration = null;
	}

	private String getWildcardText(String value) {
		value = StringUtil.replace(value, StringPool.PERCENT, StringPool.BLANK);
		value = StringUtil.toLowerCase(value);
		value = StringPool.STAR + value + StringPool.STAR;

		return value;
	}

	private QueryPreProcessConfiguration _queryPreProcessConfiguration;

}