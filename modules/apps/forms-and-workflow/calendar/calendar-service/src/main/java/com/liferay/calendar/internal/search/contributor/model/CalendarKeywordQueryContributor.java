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

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.contributor.query.KeywordQueryContributor;
import com.liferay.portal.search.query.QueryUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"indexer.class.name=com.liferay.calendar.model.Calendar"},
	service = KeywordQueryContributor.class
)
public class CalendarKeywordQueryContributor
	implements KeywordQueryContributor {

	@Override
	public void contribute(
		String entryClassName, BooleanQuery keywordBooleanQuery,
		String keywords, SearchContext searchContext) {

		QueryUtil.addSearchLocalizedTerm(
			keywordBooleanQuery, searchContext, Field.NAME, true);
		QueryUtil.addSearchLocalizedTerm(
			keywordBooleanQuery, searchContext, CalendarField.RESOURCE_NAME,
			true);
	}

}