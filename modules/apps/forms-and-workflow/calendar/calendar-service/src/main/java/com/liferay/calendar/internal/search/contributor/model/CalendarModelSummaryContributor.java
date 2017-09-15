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

package com.liferay.calendar.internal.search.contributor.model;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.contributor.model.ModelSummaryContributor;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"indexer.class.name=com.liferay.calendar.model.Calendar"},
	service = ModelSummaryContributor.class
)
public class CalendarModelSummaryContributor
	implements ModelSummaryContributor {

	@Override
	public Summary getSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String prefix = Field.SNIPPET + StringPool.UNDERLINE;

		String title = document.get(prefix + Field.NAME, Field.NAME);
		String content = document.get(
			prefix + Field.DESCRIPTION, Field.DESCRIPTION);

		Summary summary = new Summary(title, content);

		summary.setMaxContentLength(200);

		return summary;
	}

}