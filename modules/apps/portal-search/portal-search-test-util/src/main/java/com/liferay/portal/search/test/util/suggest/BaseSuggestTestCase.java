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

package com.liferay.portal.search.test.util.suggest;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.suggest.QuerySuggester;
import com.liferay.portal.kernel.search.suggest.SpellCheckIndexWriter;
import com.liferay.portal.kernel.search.suggest.SuggestionConstants;
import com.liferay.portal.search.test.util.IdempotentRetryAssert;
import com.liferay.portal.search.test.util.indexing.BaseIndexingTestCase;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bryan Engler
 * @author André de Oliveira
 */
public abstract class BaseSuggestTestCase extends BaseIndexingTestCase {

	@Test
	public void testBlank() throws Exception {
		indexSuccessfulQuery("creating the keywordSearch mapping");

		// need to wait so that "suggest" will be included in response.
		// otherwise test will always succeed in first pass of retryAssert,
		// even without "suggest" in response, becase returning empty suggestion
		// array is the default when no "suggest" is included in the response

		Thread.sleep(3000);

		assertSuggest("[]", "");
	}

	@Test
	public void testMultipleWords() throws Exception {
		indexSuccessfulQuery("indexed this phrase");

		// suggestion framework needs tuning?
		// using the english analyzer because of _en_US suffix

		assertSuggest(
			"[indexef phrase, index phrasd]", "indexef   this   phrasd", 2);
	}

	@Test
	public void testNothingToSuggest() throws Exception {
		indexSuccessfulQuery("creating the keywordSearch mapping");

		assertSuggest("[]", "nothign");
	}

	//@Test
	public void testNull() throws Exception {
		indexSuccessfulQuery("creating the keywordSearch mapping");

		// test will always fail because null text will throw an exception in
		// elastic. "illegal_argument_exception" : "The required text option is
		// missing". need to add exception handling?
		// test was passing previously because we werent calling
		// indexSuccessfulQuery first, and the exception handling for a missing
		// mapping exception was returning an empty suggestion array by default.
		// that exception handling was removed, AND calling indexSuccessfulQuery
		// causes original exception to not even be thrown in the first place.

		assertSuggest("[]", null);
	}

	protected void assertSuggest(String expectedSuggestions, String keywords)
		throws Exception {

		assertSuggest(expectedSuggestions, keywords, 1);
	}

	protected void assertSuggest(
			String expectedSuggestions, String keywords, int max)
		throws Exception {

		IdempotentRetryAssert.retryAssert(
			3, TimeUnit.SECONDS,
			() -> {
				String actualSuggestions = String.valueOf(
					Arrays.asList(suggestKeywordQueries(keywords, max)));

				Assert.assertEquals(expectedSuggestions, actualSuggestions);

				return null;
			});
	}

	protected SearchContext createSearchContext(String keywords) {
		SearchContext searchContext = createSearchContext();

		searchContext.setKeywords(keywords);

		return searchContext;
	}

	protected void indexSuccessfulQuery(String value) throws Exception {
		SpellCheckIndexWriter spellCheckIndexWriter = getIndexWriter();

		spellCheckIndexWriter.indexKeyword(
			createSearchContext(value), 0,
			SuggestionConstants.TYPE_QUERY_SUGGESTION);
	}

	protected String[] suggestKeywordQueries(String keywords, int max)
		throws Exception {

		QuerySuggester querySuggester = getIndexSearcher();

		return querySuggester.suggestKeywordQueries(
			createSearchContext(keywords), max);
	}

}