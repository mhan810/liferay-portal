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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.CustomEntryIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QuerySuggestionIndexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PortletKeys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author David Mendez Gonzalez
 * @author Josef Sustacek
 * @author Daniela Zapata Riesco
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
	public void indexQuerySuggestion(SearchContext searchContext)
		throws SearchException {

		indexQuerySuggestion(
			searchContext.getCompanyId(), searchContext.getGroupIds(),
			searchContext.getLocale(), searchContext.getKeywords());
	}

	@Override
	public void indexQuerySuggestions(
			long companyId, long[] groupIds, Locale locale,
			InputStream inputStream)
		throws SearchException {

		if (_log.isDebugEnabled()) {
			_log.debug("Loading query suggestions for locale '" + locale + "'");
		}

		Set<Document> documents = new HashSet<Document>();

		BufferedReader bufferedReader = null;

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, StringPool.UTF8);

			bufferedReader = new BufferedReader(inputStreamReader);

			String line = bufferedReader.readLine();

			if (line == null) {
				return;
			}

			if (line.charAt(0) == CustomEntryIndexer.UNICODE_BYTE_ORDER_MARK) {
				line = line.substring(1);
			}

			int lineCounter = 0;

			do {
				lineCounter++;

				line = StringUtil.trim(line);

				if (line != null & line.equals(StringPool.BLANK)) {
					line = bufferedReader.readLine();
					continue;
				}

				documents.add(
					getSuggestionDocument(companyId, groupIds, locale, line));

				line = bufferedReader.readLine();

				if (lineCounter == _batchSize || line == null) {
					SearchEngineUtil.addDocuments(
						SearchEngineUtil.getDefaultSearchEngineId(),
						companyId, documents);

					documents.clear();

					lineCounter = 0;
				}
			}
			while (line != null);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to index suggestions", e);
			}

			throw new SearchException(e.getMessage(), e);
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ioe) {
					if (_log.isDebugEnabled()) {
						_log.debug("Unable to close suggestions file", ioe);
					}
				}
			}
		}
	}

	public void setBatchSize(int batchSize) {
		_batchSize = batchSize;
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

	protected void addNGrams(Document document, String keywords) {
		String lowerCaseKeywords = keywords.toLowerCase();

		int maxNGrams = Math.min(_maxNGrams, lowerCaseKeywords.length());

		StringBundler nGram = new StringBundler(maxNGrams);
		String prefix = "start";

		for (int i = 0; i < maxNGrams; i++) {
			nGram.append(keywords.charAt(i));
			String field = prefix + (i + 1);
			document.addKeyword(field, nGram.toString());
		}
	}

	protected Document getSuggestionDocument(
		long companyId, long[] groupIds, Locale locale, String keywords) {

		Document document = (Document)_document.clone();

		document.addKeyword(
			Field.UID, getUID(companyId, locale, groupIds, keywords));

		document.addKeyword(Field.COMPANY_ID, companyId);
		document.addKeyword(Field.GROUP_ID, groupIds);
		document.addKeyword(Field.KEYWORD_SEARCH, keywords);
		document.addKeyword(Field.LANGUAGE_ID, locale.toString());
		document.addKeyword(Field.PORTLET_ID, PortletKeys.SEARCH);
		document.addKeyword(Field.TYPE, FILTER_TYPE_QUERY_SUGGESTION);

		addNGrams(document, keywords);

		return document;
	}

	protected void indexQuerySuggestions(
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
			SearchEngineUtil.getDefaultSearchEngineId(), companyId,
			suggestionsDocuments);
	}

	protected void indexQuerySuggestion(
			long companyId, long[] groupIds, Locale locale,
			String querySuggestion)
		throws SearchException {

		indexQuerySuggestions(
			companyId, groupIds, locale,
			Collections.singletonList(querySuggestion));
	}

	private static final int _DEFAULT_BATCH_SIZE = 1000;
	private static final int _DEFAULT_MAX_N_GRAMS = 50;

	private static CacheKeyGenerator _cacheKeyGenerator;

	private int _batchSize = _DEFAULT_BATCH_SIZE;
	private Document _document;
	private int _maxNGrams =_DEFAULT_MAX_N_GRAMS;

	private static Log _log = LogFactoryUtil.getLog(
		(QuerySuggestionIndexerImpl.class));

}