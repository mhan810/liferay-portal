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

package com.liferay.portal.search.internal.contributor.query;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.util.ExpandoBridgeFactory;
import com.liferay.expando.kernel.util.ExpandoBridgeIndexer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.search.contributor.query.KeywordQueryContributor;

import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = KeywordQueryContributor.class)
public class ExpandoKeywordQueryContributor implements KeywordQueryContributor {

	@Override
	public void contribute(
		String entryClassName, BooleanQuery keywordBooleanQuery,
		String keywords, SearchContext searchContext) {

		ExpandoBridge expandoBridge = expandoBridgeFactory.getExpandoBridge(
			searchContext.getCompanyId(), entryClassName);

		Set<String> attributeNames = SetUtil.fromEnumeration(
			expandoBridge.getAttributeNames());

		if (SetUtil.isEmpty(attributeNames)) {
			return;
		}

		for (String attributeName : attributeNames) {
			UnicodeProperties properties = expandoBridge.getAttributeProperties(
				attributeName);

			int indexType = GetterUtil.getInteger(
				properties.getProperty(ExpandoColumnConstants.INDEX_TYPE));

			if (indexType == ExpandoColumnConstants.INDEX_TYPE_NONE) {
				continue;
			}

			String fieldName = getExpandoFieldName(
				searchContext, expandoBridge, attributeName);

			boolean like = false;

			if (indexType == ExpandoColumnConstants.INDEX_TYPE_TEXT) {
				like = true;
			}

			if (searchContext.isAndSearch()) {
				keywordBooleanQuery.addRequiredTerm(fieldName, keywords, like);
			}
			else {
				try {
					keywordBooleanQuery.addTerm(fieldName, keywords, like);
				}
				catch (ParseException pe) {
					if (_log.isWarnEnabled()) {
						_log.warn("Error parsing field: " + fieldName, pe);
					}
				}
			}
		}
	}

	protected String getExpandoFieldName(
		SearchContext searchContext, ExpandoBridge expandoBridge,
		String attributeName) {

		ExpandoColumn expandoColumn =
			expandoColumnLocalService.getDefaultTableColumn(
				expandoBridge.getCompanyId(), expandoBridge.getClassName(),
				attributeName);

		UnicodeProperties unicodeProperties =
			expandoColumn.getTypeSettingsProperties();

		int indexType = GetterUtil.getInteger(
			unicodeProperties.getProperty(ExpandoColumnConstants.INDEX_TYPE));

		String fieldName = expandoBridgeIndexer.encodeFieldName(
			attributeName, indexType);

		if (expandoColumn.getType() ==
				ExpandoColumnConstants.STRING_LOCALIZED) {

			fieldName = Field.getLocalizedName(
				searchContext.getLocale(), fieldName);
		}

		return fieldName;
	}

	@Reference
	protected ExpandoBridgeFactory expandoBridgeFactory;

	@Reference
	protected ExpandoBridgeIndexer expandoBridgeIndexer;

	@Reference
	protected ExpandoColumnLocalService expandoColumnLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		ExpandoKeywordQueryContributor.class);

}