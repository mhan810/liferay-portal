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

package com.liferay.portal.search.internal.analysis;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.analysis.KeywordTokenizer;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = KeywordTokenizer.class)
public class SimpleKeywordTokenizer implements KeywordTokenizer {

	@Override
	public boolean requiresTokenization(String keyword) {
		int start = keyword.indexOf(StringPool.QUOTE);
		int end = keyword.indexOf(StringPool.QUOTE, start + 1);

		if (!(keyword.startsWith(StringPool.QUOTE) &&
			keyword.endsWith(StringPool.QUOTE))) {

			if ((start > -1) && (end > start)) {
				return true;
			}
		}

		return false;
	}

	public List<String> tokenize(String keyword) {
		List<String> tokens = new ArrayList<>();

		int start = keyword.indexOf(StringPool.QUOTE);
		int end = keyword.indexOf(StringPool.QUOTE, start + 1);

		tokenize(keyword, tokens, start, end);

		return tokens;
	}

	protected void tokenize(
		String keyword, List<String> tokens, int start, int end) {

		if ((start == -1) || (end == -1)) {
			keyword = keyword.trim();

			if (!keyword.isEmpty()) {
				tokens.add(keyword);
			}

			return;
		}

		String token = keyword.substring(0, start).trim();

		if (!token.isEmpty()) {
			tokens.add(token);
		}

		token = keyword.substring(start, end + 1).trim();

		if (!token.isEmpty()) {
			tokens.add(token);
		}

		if ((end + 1) > keyword.length()) {
			return;
		}

		keyword = keyword.substring(end + 1, keyword.length()).trim();

		if (keyword.isEmpty()) {
			return;
		}

		start = keyword.indexOf(StringPool.QUOTE, end + 1);
		end = keyword.indexOf(StringPool.QUOTE, start + 1);

		tokenize(keyword, tokens, start, end);
	}

}