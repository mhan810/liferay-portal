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

package com.liferay.portal.search.lucene;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Analyzer;
import com.liferay.portal.kernel.search.SearchException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Fieldable;

/**
 * @author Raymond Augé
 * @author Mate Thurzo
 */
public class PerFieldAnalyzer
	extends org.apache.lucene.analysis.Analyzer implements Analyzer {

	public PerFieldAnalyzer(
		org.apache.lucene.analysis.Analyzer defaultAnalyzer,
		Map<String, org.apache.lucene.analysis.Analyzer> analyzerMap) {

		_analyzer = defaultAnalyzer;
		_analyzers = analyzerMap;
	}

	public void addAnalyzer(
		String fieldName, org.apache.lucene.analysis.Analyzer analyzer) {
		_analyzers.put(fieldName, analyzer);
	}

	public org.apache.lucene.analysis.Analyzer getAnalyzer(String fieldName) {
		org.apache.lucene.analysis.Analyzer analyzer =
			_analyzers.get(fieldName);

		if (analyzer != null) {
			return analyzer;
		}

		for (String key : _analyzers.keySet()) {
			if (Pattern.matches(key, fieldName)) {
				return _analyzers.get(key);
			}
		}

		return _analyzer;
	}

	@Override
	public int getOffsetGap(Fieldable field) {
		org.apache.lucene.analysis.Analyzer analyzer =
			getAnalyzer(field.name());

		return analyzer.getOffsetGap(field);
	}

	@Override
	public int getPositionIncrementGap(String fieldName) {
		org.apache.lucene.analysis.Analyzer analyzer = getAnalyzer(fieldName);

		return analyzer.getPositionIncrementGap(fieldName);
	}

	@Override
	public TokenStream reusableTokenStream(String fieldName, Reader reader)
		throws IOException {

		org.apache.lucene.analysis.Analyzer analyzer = getAnalyzer(fieldName);

		return analyzer.reusableTokenStream(fieldName, reader);
	}

	@Override
	public List<String> tokenize(String fieldName, String input, Locale locale)
		throws SearchException {

		List<String> result = new ArrayList<String>();

		TokenStream tokenStream = null;

		try {
			tokenStream = getAnalyzer(fieldName).tokenStream(
				locale.toString(), new StringReader(input));

			CharTermAttribute charTermAttribute = tokenStream.addAttribute(
				CharTermAttribute.class);

			tokenStream.reset();

			while (tokenStream.incrementToken()) {
				result.add(charTermAttribute.toString());
			}

			tokenStream.end();
		}
		catch (IOException e) {
			throw new SearchException(e);
		}
		finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				}
				catch (IOException e) {
					if (_log.isWarnEnabled()) {
						_log.warn("Unable to close token stream", e);
					}
				}
			}

		}

		return result;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		org.apache.lucene.analysis.Analyzer analyzer = getAnalyzer(fieldName);

		return analyzer.tokenStream(fieldName, reader);
	}

	private static Log _log =
		LogFactoryUtil.getLog(PerFieldAnalyzer.class);

	private org.apache.lucene.analysis.Analyzer _analyzer;
	private Map<String, org.apache.lucene.analysis.Analyzer> _analyzers =
		new HashMap<String, org.apache.lucene.analysis.Analyzer>();

}