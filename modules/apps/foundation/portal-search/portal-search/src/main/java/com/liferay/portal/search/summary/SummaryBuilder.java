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

package com.liferay.portal.search.summary;

import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.highlight.HighlightUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

/**
 * @author André de Oliveira
 * @author Bryan Engler
 * @author Ryan Park
 * @author Tibor Lipusz
 */
public class SummaryBuilder {

	public Summary build() {
		return new Summary(_locale, buildTitle(), buildContent(), false);
	}

	public void setContent(String content) {
		_content = content;
	}

	public void setEscape(boolean escape) {
		_escape = escape;
	}

	public void setHighlight(boolean highlight) {
		_highlight = highlight;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setMaxContentLength(int maxContentLength) {
		_maxContentLength = maxContentLength;
	}

	public void setTitle(String title) {
		_title = title;
	}

	protected String buildContent() {
		if (Validator.isNull(_content)) {
			return StringPool.BLANK;
		}

		if (_highlight) {
			return buildContentHighlighted();
		}

		return buildContentPlain();
	}

	protected String buildContentHighlighted() {
		return _escapeAndHighlight(_content);
	}

	protected String buildContentPlain() {
		if ((_maxContentLength <= 0) ||
			(_content.length() <= _maxContentLength)) {

			return _content;
		}

		return StringUtil.shorten(_content, _maxContentLength);
	}

	protected String buildTitle() {
		if (Validator.isNull(_title)) {
			return StringPool.BLANK;
		}

		if (_highlight) {
			return buildTitleHighlighted();
		}

		return buildTitlePlain();
	}

	protected String buildTitleHighlighted() {
		return _escapeAndHighlight(_title);
	}

	protected String buildTitlePlain() {
		return _title;
	}

	private String _escapeAndHighlight(String text) {
		text = StringUtil.replace(
			text, _HIGHLIGHT_TAGS, _ESCAPE_SAFE_HIGHLIGHTS);

		if (_escape) {
			text = HtmlUtil.escape(text);
		}

		text = StringUtil.replace(
			text, _ESCAPE_SAFE_HIGHLIGHTS, HighlightUtil.HIGHLIGHTS);

		return text;
	}

	private static final String[] _ESCAPE_SAFE_HIGHLIGHTS =
		{"[@HIGHLIGHT1@]", "[@HIGHLIGHT2@]"};

	private static final String[] _HIGHLIGHT_TAGS =
		{HighlightUtil.HIGHLIGHT_TAG_OPEN, HighlightUtil.HIGHLIGHT_TAG_CLOSE};

	private String _content;
	private boolean _escape = true;
	private boolean _highlight;
	private Locale _locale;
	private int _maxContentLength;
	private String _title;

}