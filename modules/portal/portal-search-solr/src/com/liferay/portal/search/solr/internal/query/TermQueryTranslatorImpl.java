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

import com.liferay.portal.kernel.search.QueryPreProcessConfiguration;
import com.liferay.portal.kernel.search.QueryTerm;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.solr.query.TermQueryTranslator;

import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;

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
	public org.apache.lucene.search.Query translate(TermQuery termQuery) {
		org.apache.lucene.search.Query luceneQuery =
			toCaseInsensitiveSubstringQuery(termQuery);

		if (luceneQuery == null) {
			luceneQuery = toRegularQuery(termQuery);
		}

		applyBoost(termQuery, luceneQuery);

		return luceneQuery;
	}

	protected void addBooleanClause(
		org.apache.lucene.search.BooleanQuery newBooleanQuery,
		org.apache.lucene.search.Query query, BooleanClause booleanClause) {

		newBooleanQuery.add(query, booleanClause.getOccur());
	}

	protected void applyBoost(
		TermQuery termQuery, org.apache.lucene.search.Query luceneQuery) {

		if (!termQuery.isDefaultBoost()) {
			luceneQuery.setBoost(termQuery.getBoost());
		}
	}

	protected String getWildcardText(String value) {
		value = StringUtil.replace(value, StringPool.PERCENT, StringPool.BLANK);
		value = StringUtil.toLowerCase(value);
		value = StringPool.STAR + value + StringPool.STAR;

		return value;
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

	protected org.apache.lucene.search.Query toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.BooleanQuery booleanQuery) {

		org.apache.lucene.search.BooleanQuery newBooleanQuery =
			new org.apache.lucene.search.BooleanQuery();

		List<BooleanClause> clauses = booleanQuery.clauses();

		for (BooleanClause booleanClause : clauses) {
			org.apache.lucene.search.TermQuery termQuery =
				(org.apache.lucene.search.TermQuery)booleanClause.getQuery();

			org.apache.lucene.search.Query query =
				toCaseInsensitiveSubstringQuery(termQuery);

			addBooleanClause(newBooleanQuery, query, booleanClause);
		}

		return newBooleanQuery;
	}

	protected org.apache.lucene.search.Query toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.PhraseQuery phraseQuery) {

		Term[] terms = phraseQuery.getTerms();

		Term term = terms[0];

		String value = term.text();

		value = getWildcardText(value);

		return new org.apache.lucene.search.WildcardQuery(
			new Term(term.field(), value));
	}

	protected org.apache.lucene.search.Query toCaseInsensitiveSubstringQuery(
		org.apache.lucene.search.TermQuery termQuery) {

		Term term = termQuery.getTerm();

		String value = term.text();

		value = getWildcardText(value);

		return new org.apache.lucene.search.WildcardQuery(
			new Term(term.field(), value));
	}

	protected org.apache.lucene.search.Query toCaseInsensitiveSubstringQuery(
		TermQuery termQuery) {

		QueryTerm queryTerm = termQuery.getQueryTerm();

		String field = queryTerm.getField();

		if ((_queryPreProcessConfiguration == null) ||
			!_queryPreProcessConfiguration.isSubstringSearchAlways(field)) {

			return null;
		}

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

	protected org.apache.lucene.search.Query toRegularQuery(
		TermQuery termQuery) {

		QueryTerm queryTerm = termQuery.getQueryTerm();

		return new org.apache.lucene.search.TermQuery(
			new Term(queryTerm.getField(), queryTerm.getValue()));
	}

	protected void unsetQueryPreProcessConfiguration(
		QueryPreProcessConfiguration queryPreProcessConfiguration) {

		_queryPreProcessConfiguration = null;
	}

	private QueryPreProcessConfiguration _queryPreProcessConfiguration;

}