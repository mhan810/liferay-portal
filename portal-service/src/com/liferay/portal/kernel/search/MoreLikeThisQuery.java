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

package com.liferay.portal.kernel.search;

import java.util.List;

public interface MoreLikeThisQuery {

	Object getWrappedQuery();

	/**
	 * @return the analyzer
	 */
	String getAnalyzer();

	/**
	 * @return the boost
	 */
	float getBoost();

	/**
	 * @return the boostTerms
	 */
	float getBoostTerms();

	/**
	 * @return the docs
	 */
	List<Document> getDocs();

	/**
	 * @return the failOnUnsupportedField
	 */
	Boolean getFailOnUnsupportedField();

	/**
	 * @return the fields
	 */
	String[] getFields();

	/**
	 * @return the ignoreDocs
	 */
	List<Document> getIgnoreDocs();

	/**
	 * @return the include
	 */
	Boolean getInclude();

	/**
	 * @return the likeText
	 */
	String getLikeText();

	/**
	 * @return the maxDocFreq
	 */
	int getMaxDocFreq();

	/**
	 * @return the maxQueryTerms
	 */
	int getMaxQueryTerms();

	/**
	 * @return the maxWordLength
	 */
	int getMaxWordLength();

	/**
	 * @return the minDocFreq
	 */
	int getMinDocFreq();

	/**
	 * @return the minimumShouldMatch
	 */
	String getMinimumShouldMatch();

	/**
	 * @return the minTermFreq
	 */
	int getMinTermFreq();

	/**
	 * @return the minWordLength
	 */
	int getMinWordLength();

	/**
	 * @return the queryName
	 */
	String getQueryName();

	/**
	 * @return the stopWords
	 */
	String[] getStopWords();

	/**
	 * @param analyzer the analyzer to set
	 */
	void setAnalyzer(String analyzer);

	/**
	 * @param boost the boost to set
	 */
	void setBoost(float boost);

	/**
	 * @param boostTerms the boostTerms to set
	 */
	void setBoostTerms(float boostTerms);

	/**
	 * @param docs the docs to set
	 */
	void setDocs(List<Document> docs);

	/**
	 * @param failOnUnsupportedField the failOnUnsupportedField to set
	 */
	void setFailOnUnsupportedField(Boolean failOnUnsupportedField);

	/**
	 * @param fields the fields to set
	 */
	void setFields(String[] fields);

	/**
	 * @param ignoreDocs the ignoreDocs to set
	 */
	void setIgnoreDocs(List<Document> ignoreDocs);

	/**
	 * @param include the include to set
	 */
	void setInclude(Boolean include);

	/**
	 * @param likeText the likeText to set
	 */
	void setLikeText(String likeText);

	/**
	 * @param maxDocFreq the maxDocFreq to set
	 */
	void setMaxDocFreq(int maxDocFreq);

	/**
	 * @param maxQueryTerms the maxQueryTerms to set
	 */
	void setMaxQueryTerms(int maxQueryTerms);

	/**
	 * @param maxWordLength the maxWordLength to set
	 */
	void setMaxWordLength(int maxWordLength);

	/**
	 * @param minDocFreq the minDocFreq to set
	 */
	void setMinDocFreq(int minDocFreq);

	/**
	 * @param minimumShouldMatch the minimumShouldMatch to set
	 */
	void setMinimumShouldMatch(String minimumShouldMatch);

	/**
	 * @param minTermFreq the minTermFreq to set
	 */
	void setMinTermFreq(int minTermFreq);

	/**
	 * @param minWordLength the minWordLength to set
	 */
	void setMinWordLength(int minWordLength);

	/**
	 * @param queryName the queryName to set
	 */
	void setQueryName(String queryName);

	/**
	 * @param stopWords the stopWords to set
	 */
	void setStopWords(String[] stopWords);

}
