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

package com.liferay.portal.kernel.search.generic;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.search.BaseQueryImpl;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.MoreLikeThisQuery;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class MoreLikeThisQueryImpl extends BaseQueryImpl implements
	MoreLikeThisQuery {

	public MoreLikeThisQueryImpl(String likeText,
		int maxQueryTerms, int minTermFreq, String... fields) {

		this.fields = fields;
		this.likeText = likeText;
		this.maxQueryTerms = maxQueryTerms;
		this.minTermFreq = minTermFreq;
	}

	@Override
	public Object getWrappedQuery() {

		return this;
	}

	@Override
	public String getAnalyzer() {

		return analyzer;
	}

	@Override
	public float getBoost() {

		return boost;
	}

	@Override
	public float getBoostTerms() {

		return boostTerms;
	}

	@Override
	public List<Document> getDocs() {

		return docs;
	}

	@Override
	public Boolean getFailOnUnsupportedField() {

		return failOnUnsupportedField;
	}

	@Override
	public String[] getFields() {

		return fields;
	}

	@Override
	public List<Document> getIgnoreDocs() {

		return ignoreDocs;
	}

	@Override
	public Boolean getInclude() {

		return include;
	}

	@Override
	public String getLikeText() {

		return likeText;
	}

	@Override
	public int getMaxDocFreq() {

		return maxDocFreq;
	}

	@Override
	public int getMaxQueryTerms() {

		return maxQueryTerms;
	}

	@Override
	public int getMaxWordLength() {

		return maxWordLength;
	}

	@Override
	public int getMinDocFreq() {

		return minDocFreq;
	}

	@Override
	public String getMinimumShouldMatch() {

		return minimumShouldMatch;
	}

	@Override
	public int getMinTermFreq() {

		return minTermFreq;
	}

	@Override
	public int getMinWordLength() {

		return minWordLength;
	}

	@Override
	public String getQueryName() {

		return queryName;
	}

	@Override
	public String[] getStopWords() {

		return stopWords;
	}

	@Override
	public void setAnalyzer(String analyzer) {

		this.analyzer = analyzer;
	}

	@Override
	public void setBoost(float boost) {

		this.boost = boost;
	}

	@Override
	public void setBoostTerms(float boostTerms) {

		this.boostTerms = boostTerms;
	}

	@Override
	public void setDocs(List<Document> docs) {

		this.docs = docs;
	}

	@Override
	public void setFailOnUnsupportedField(Boolean failOnUnsupportedField) {

		this.failOnUnsupportedField = failOnUnsupportedField;
	}

	@Override
	public void setFields(String[] fields) {

		this.fields = fields;
	}

	@Override
	public void setIgnoreDocs(List<Document> ignoreDocs) {

		this.ignoreDocs = ignoreDocs;
	}

	@Override
	public void setInclude(Boolean include) {

		this.include = include;
	}

	@Override
	public void setLikeText(String likeText) {

		this.likeText = likeText;
	}

	@Override
	public void setMaxDocFreq(int maxDocFreq) {

		this.maxDocFreq = maxDocFreq;
	}

	@Override
	public void setMaxQueryTerms(int maxQueryTerms) {

		this.maxQueryTerms = maxQueryTerms;
	}

	@Override
	public void setMaxWordLength(int maxWordLength) {

		this.maxWordLength = maxWordLength;
	}

	@Override
	public void setMinDocFreq(int minDocFreq) {

		this.minDocFreq = minDocFreq;
	}

	@Override
	public void setMinimumShouldMatch(String minimumShouldMatch) {

		this.minimumShouldMatch = minimumShouldMatch;
	}

	@Override
	public void setMinTermFreq(int minTermFreq) {

		this.minTermFreq = minTermFreq;
	}

	@Override
	public void setMinWordLength(int minWordLength) {

		this.minWordLength = minWordLength;
	}

	@Override
	public void setQueryName(String queryName) {

		this.queryName = queryName;
	}

	@Override
	public void setStopWords(String[] stopWords) {

		this.stopWords = stopWords;
	}

	private String analyzer;
	private float boost = -1;
	private float boostTerms = -1;
	private List<Document> docs = new ArrayList<>();
	private Boolean failOnUnsupportedField;
	private String[] fields;
	private List<Document> ignoreDocs = new ArrayList<>();
	private Boolean include = null;
	private String likeText;
	private int maxDocFreq = -1;
	private int maxQueryTerms;
	private int maxWordLength = -1;
	private int minDocFreq = -1;
	private String minimumShouldMatch = null;
	private int minTermFreq;
	private int minWordLength = -1;
	private String queryName;
	private String[] stopWords = null;

}
