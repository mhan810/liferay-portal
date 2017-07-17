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

import com.liferay.portal.kernel.search.highlight.HighlightUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 * @author Tibor Lipusz
 */
public class Summary {

	public Summary(
		boolean mutable, Locale locale, String title, String content) {

		_mutable = mutable;
		_locale = locale;
		_title = title;
		_content = content;
	}

	public Summary(boolean mutable, String title, String content) {
		this(
			mutable, LocaleThreadLocal.getThemeDisplayLocale(), title, content);
	}

	public Summary(Locale locale, String title, String content) {
		this(true, locale, title, content);
	}

	public Summary(String title, String content) {
		this(LocaleThreadLocal.getThemeDisplayLocale(), title, content);
	}

	public String getContent() {
		if (!_mutable) {
			return _content;
		}

		if (Validator.isNull(_content)) {
			return StringPool.BLANK;
		}

		if ((_maxContentLength <= 0) ||
			(_content.length() <= _maxContentLength)) {

			return _content;
		}

		if (!ArrayUtil.isEmpty(_queryTerms)) {
			int index = StringUtil.indexOfAny(_content, _queryTerms);

			if (index > _maxContentLength) {
				_content = _content.substring(index);
			}
		}

		_content = StringUtil.shorten(_content, _maxContentLength);

		return _content;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public String getHighlightedContent() {
		if (!_mutable) {
			return _content;
		}

		return _escapeAndHighlight(_content);
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public String getHighlightedTitle() {
		if (!_mutable) {
			return _title;
		}

		return _escapeAndHighlight(_title);
	}

	public Locale getLocale() {
		return _locale;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public int getMaxContentLength() {
		return _maxContentLength;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public String[] getQueryTerms() {
		return _queryTerms;
	}

	public String getTitle() {
		if (!_mutable) {
			return _title;
		}

		if (Validator.isNull(_title)) {
			return StringPool.BLANK;
		}

		return _title;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public boolean isEscape() {
		return _escape;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public boolean isHighlight() {
		return _highlight;
	}

	public void setContent(String content) {
		_content = content;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public void setEscape(boolean escape) {
		_escape = escape;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public void setHighlight(boolean highlight) {
		_highlight = highlight;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public void setMaxContentLength(int maxContentLength) {
		_maxContentLength = maxContentLength;
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public void setQueryTerms(String[] queryTerms) {
		if (ArrayUtil.isEmpty(queryTerms)) {
			return;
		}

		_queryTerms = queryTerms;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _escapeAndHighlight(String text) {
		if (!_highlight || Validator.isNull(text) ||
			ArrayUtil.isEmpty(_queryTerms)) {

			if (_escape) {
				return HtmlUtil.escape(text);
			}

			return text;
		}

		text = HighlightUtil.highlight(
			text, _queryTerms, _ESCAPE_SAFE_HIGHLIGHTS[0],
			_ESCAPE_SAFE_HIGHLIGHTS[1]);

		if (_escape) {
			text = HtmlUtil.escape(text);
		}

		return StringUtil.replace(
			text, _ESCAPE_SAFE_HIGHLIGHTS, HighlightUtil.HIGHLIGHTS);
	}

	private static final String[] _ESCAPE_SAFE_HIGHLIGHTS =
		{"[@HIGHLIGHT1@]", "[@HIGHLIGHT2@]"};

	private String _content;
	private boolean _escape = true;
	private boolean _highlight;
	private Locale _locale;
	private int _maxContentLength;
	private boolean _mutable;
	private String[] _queryTerms;
	private String _title;

}