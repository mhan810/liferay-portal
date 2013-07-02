/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.search;

import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.QuerySuggestionIndexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PortletKeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author David Mendez Gonzalez
 * @author Josef Sustacek
 */
public class QuerySuggestionIndexerImpl implements QuerySuggestionIndexer {

	@Override
	public String getUID(
		long companyId, Locale locale, long[] groupIds, String keywords) {

		List<String> keys = new ArrayList<String>();

		keys.add(FILTER_TYPE_QUERY_SUGGESTION);

		if (companyId > 0) {
			keys.add(String.valueOf(companyId));
		}

		keys.add(locale.toString());

		for (long groupId : groupIds) {
			keys.add(String.valueOf(groupId));
		}

		String lowerCaseKeywords = keywords.toLowerCase();
		keys.add(lowerCaseKeywords);

		String[] keyArray = new String[keys.size()];
		keyArray = keys.toArray(keyArray);

		return (String)_cacheKeyGenerator.getCacheKey(keyArray);
	}

	@Override
	public void indexQuerySuggestion(
			long companyId, long[] groupIds, Locale locale,
			String querySuggestion)
		throws SearchException {

		indexQuerySuggestions(
			companyId, groupIds, locale,
			Collections.singletonList(querySuggestion));
	}

	@Override
	public void indexQuerySuggestion(SearchContext searchContext)
		throws SearchException {

		indexQuerySuggestion(
			searchContext.getCompanyId(), searchContext.getGroupIds(),
			searchContext.getLocale(), searchContext.getKeywords());
	}

	@Override
	public void indexQuerySuggestions(
			long companyId, long[] groupIds, Locale locale, File file)
		throws SearchException {

		List<String> querySuggestions = _readQuerySuggestionsFromFile(file);

		indexQuerySuggestions(companyId, groupIds, locale, querySuggestions);
	}

	public void indexQuerySuggestions(
			long companyId, long[] groupIds, Locale locale,
			List<String> querySuggestions)
		throws SearchException {

		List<Document> suggestionsDocuments = new ArrayList<Document>(
			querySuggestions.size());

		for (String querySuggestion : querySuggestions) {
			if (querySuggestion.length() <= _DEFAULT_MAX_N_GRAMS) {
				Document suggestionsDocument = getSuggestionDocument(
					companyId, groupIds, locale, querySuggestion);

				if (suggestionsDocument != null) {
					suggestionsDocuments.add(suggestionsDocument);
				}
			}
		}

		SearchEngineUtil.addDocuments(
			SearchEngineUtil.SYSTEM_ENGINE_ID, companyId, suggestionsDocuments);
	}

	public void setCacheKeyGenerator(CacheKeyGenerator cacheKeyGenerator) {
		_cacheKeyGenerator = cacheKeyGenerator;
	}

	public void setDocument(Document document) {
		_document = document;
	}

	public void setMaxNGrams(int maxNGrams) {
		_maxNGrams = maxNGrams;
	}

	public void setMinNGrams(int minNGrams) {
		_minNGrams = minNGrams;
	}

	protected void addNGrams(Document document, String keywords) {
		String lowerCaseKeywords = keywords.toLowerCase();

		int maxNGrams = Math.min(_maxNGrams, lowerCaseKeywords.length());
		int minNGrams = Math.min(maxNGrams, _minNGrams);

		StringBundler nGram = new StringBundler(maxNGrams - minNGrams + 1);
		nGram.append(lowerCaseKeywords.substring(0, minNGrams));
		String prefix = "start";

		for (int i = minNGrams; i < maxNGrams; i++) {
			nGram.append(keywords.charAt(i));
			String field = prefix + (i + 1);
			document.addKeyword(field, nGram.toString());
		}
	}

	protected Document getSuggestionDocument(
		long companyId, long[] groupIds, Locale locale, String keywords) {

		Document document = (Document)_document.clone();

		document.addKeyword(Field.COMPANY_ID, companyId);
		document.addKeyword(Field.GROUP_ID, groupIds);
		document.addKeyword(Field.KEYWORD_SEARCH, keywords);
		document.addKeyword(QueryConfig.LOCALE, locale.toString());
		document.addKeyword(Field.PORTLET_ID, PortletKeys.SEARCH);
		document.addKeyword(Field.TYPE, FILTER_TYPE_QUERY_SUGGESTION);
		document.addKeyword(
			Field.UID, getUID(companyId, locale, groupIds, keywords));

		addNGrams(document, keywords);

		return document;
	}

	private List<String> _readQuerySuggestionsFromFile(File file) {

		List<String> keywordsQueries = new ArrayList<String>();

		BufferedReader bufferedReader = null;

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(
				fileInputStream, "UTF-8");

			bufferedReader = new BufferedReader(inputStreamReader);

			String line = bufferedReader.readLine();

			if (line == null) {
				return null;
			}

			line = line.substring(1);

			do {
				line = StringUtil.trim(line);
				keywordsQueries.add(line);
				line = bufferedReader.readLine();
			}
			while (line != null);
		}
		catch (Exception e) {
		}

		return keywordsQueries;
	}

	private static final int _DEFAULT_MAX_N_GRAMS = 50;

	private static final int _DEFAULT_MIN_N_GRAMS = 2;

	private static CacheKeyGenerator _cacheKeyGenerator;

	private Document _document;
	private int _maxNGrams =_DEFAULT_MAX_N_GRAMS;
	private int _minNGrams =_DEFAULT_MIN_N_GRAMS;

}