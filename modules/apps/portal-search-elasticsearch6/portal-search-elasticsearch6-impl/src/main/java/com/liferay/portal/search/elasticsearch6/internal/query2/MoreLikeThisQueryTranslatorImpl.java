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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.elasticsearch6.internal.index.IndexNameBuilder;
import com.liferay.portal.search.elasticsearch6.internal.util.DocumentTypes;
import com.liferay.portal.search.query.MoreLikeThisQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = MoreLikeThisQueryTranslator.class)
public class MoreLikeThisQueryTranslatorImpl
	implements MoreLikeThisQueryTranslator {

	@Override
	public QueryBuilder translate(MoreLikeThisQuery moreLikeThisQuery) {
		List<String> fields = moreLikeThisQuery.getFields();

		List<String> likeTexts = new ArrayList<>();

		likeTexts.addAll(fields);

		List<MoreLikeThisQueryBuilder.Item> likeItems = new ArrayList<>();

		if (moreLikeThisQuery.getDocumentUIDs() != null) {
			String type = moreLikeThisQuery.getType();

			if (Validator.isNotNull(type)) {
				type = DocumentTypes.LIFERAY;
			}

			for (String documentUID : moreLikeThisQuery.getDocumentUIDs()) {
				MoreLikeThisQueryBuilder.Item moreLikeThisQueryBuilderItem =
					new MoreLikeThisQueryBuilder.Item(
						_indexNameBuilder.getIndexName(
							moreLikeThisQuery.getCompanyId()),
						type, documentUID);

				likeItems.add(moreLikeThisQueryBuilderItem);
			}
		}

		if (Validator.isNotNull(moreLikeThisQuery.getLikeText())) {
			likeTexts.add(moreLikeThisQuery.getLikeText());
		}

		MoreLikeThisQueryBuilder moreLikeThisQueryBuilder =
			QueryBuilders.moreLikeThisQuery(
				likeTexts.toArray(new String[likeTexts.size()]),
				likeItems.toArray(
					new MoreLikeThisQueryBuilder.Item[likeItems.size()]));

		if (Validator.isNotNull(moreLikeThisQuery.getAnalyzer())) {
			moreLikeThisQueryBuilder.analyzer(moreLikeThisQuery.getAnalyzer());
		}

		if (moreLikeThisQuery.getMaxDocFrequency() != null) {
			moreLikeThisQueryBuilder.maxDocFreq(
				moreLikeThisQuery.getMaxDocFrequency());
		}

		if (moreLikeThisQuery.getMaxQueryTerms() != null) {
			moreLikeThisQueryBuilder.maxQueryTerms(
				moreLikeThisQuery.getMaxQueryTerms());
		}

		if (moreLikeThisQuery.getMaxWordLength() != null) {
			moreLikeThisQueryBuilder.maxWordLength(
				moreLikeThisQuery.getMaxWordLength());
		}

		if (moreLikeThisQuery.getMinDocFrequency() != null) {
			moreLikeThisQueryBuilder.minDocFreq(
				moreLikeThisQuery.getMinDocFrequency());
		}

		if (Validator.isNotNull(moreLikeThisQuery.getMinShouldMatch())) {
			moreLikeThisQueryBuilder.minimumShouldMatch(
				moreLikeThisQuery.getMinShouldMatch());
		}

		if (moreLikeThisQuery.getMinTermFrequency() != null) {
			moreLikeThisQueryBuilder.minTermFreq(
				moreLikeThisQuery.getMinTermFrequency());
		}

		if (moreLikeThisQuery.getMinWordLength() != null) {
			moreLikeThisQueryBuilder.minWordLength(
				moreLikeThisQuery.getMinWordLength());
		}

		Collection<String> stopWords = moreLikeThisQuery.getStopWords();

		if (!stopWords.isEmpty()) {
			moreLikeThisQueryBuilder.stopWords(
				stopWords.toArray(new String[stopWords.size()]));
		}

		if (moreLikeThisQuery.getTermBoost() != null) {
			moreLikeThisQueryBuilder.boostTerms(
				moreLikeThisQuery.getTermBoost());
		}

		if (moreLikeThisQuery.isIncludeInput() != null) {
			moreLikeThisQueryBuilder.include(
				moreLikeThisQuery.isIncludeInput());
		}

		return moreLikeThisQueryBuilder;
	}

	@Reference(unbind = "-")
	protected void setIndexNameBuilder(IndexNameBuilder indexNameBuilder) {
		_indexNameBuilder = indexNameBuilder;
	}

	private IndexNameBuilder _indexNameBuilder;

}