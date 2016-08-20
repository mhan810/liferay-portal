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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.InactiveRequestHandler;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.util.ContentUtil;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
public class InactiveRequestHandlerImpl implements InactiveRequestHandler {

	@Override
	public void processInactiveRequest(
			HttpServletRequest request, HttpServletResponse response,
			String messageKey)
		throws IOException {

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		PrintWriter printWriter = response.getWriter();

		if (!PropsValues.VIRTUAL_INSTANCES_SHOW_INACTIVE_MESSAGE) {
			printWriter.print(StringPool.BLANK);

			return;
		}

		response.setContentType(ContentTypes.TEXT_HTML_UTF8);

		Locale locale = PortalUtil.getLocale(request);

		String message = null;

		if (LanguageUtil.isValidLanguageKey(locale, messageKey)) {
			message = LanguageUtil.get(locale, messageKey);
		}
		else {
			message = HtmlUtil.escape(messageKey);
		}

		String html = ContentUtil.get(
			"com/liferay/portal/dependencies/inactive.html");

		html = StringUtil.replace(html, "[$MESSAGE$]", message);

		printWriter.print(html);
	};

}