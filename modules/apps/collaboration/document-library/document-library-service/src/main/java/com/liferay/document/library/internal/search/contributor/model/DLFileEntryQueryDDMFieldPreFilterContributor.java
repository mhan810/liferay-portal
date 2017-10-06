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

package com.liferay.document.library.internal.search.contributor.model;

import com.liferay.dynamic.data.mapping.kernel.DDMStructure;
import com.liferay.dynamic.data.mapping.kernel.DDMStructureManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.contributor.query.QueryPreFilterContributor;

import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry"
	},
	service = QueryPreFilterContributor.class
)
public class DLFileEntryQueryDDMFieldPreFilterContributor
	implements QueryPreFilterContributor {

	@Override
	public void contribute(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		try {
			String ddmStructureFieldName = (String)searchContext.getAttribute(
				"ddmStructureFieldName");
			Serializable ddmStructureFieldValue = searchContext.getAttribute(
				"ddmStructureFieldValue");

			if (Validator.isNotNull(ddmStructureFieldName) &&
				Validator.isNotNull(ddmStructureFieldValue)) {

				String[] ddmStructureFieldNameParts = StringUtil.split(
					ddmStructureFieldName,
					DDMStructureManager.STRUCTURE_INDEXER_FIELD_SEPARATOR);

				DDMStructure ddmStructure = ddmStructureManager.getStructure(
					GetterUtil.getLong(ddmStructureFieldNameParts[2]));

				String fieldName = StringUtil.replaceLast(
					ddmStructureFieldNameParts[3],
					StringPool.UNDERLINE.concat(
						LocaleUtil.toLanguageId(searchContext.getLocale())),
					StringPool.BLANK);

				try {
					ddmStructureFieldValue =
						ddmStructureManager.getIndexedFieldValue(
							ddmStructureFieldValue,
							ddmStructure.getFieldType(fieldName));
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(e, e);
					}
				}

				BooleanQuery booleanQuery = new BooleanQueryImpl();

				booleanQuery.addRequiredTerm(
					ddmStructureFieldName,
					StringPool.QUOTE + ddmStructureFieldValue +
						StringPool.QUOTE);

				booleanFilter.add(
					new QueryFilter(booleanQuery), BooleanClauseOccur.MUST);
			}
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	@Reference
	protected DDMStructureManager ddmStructureManager;

	private static final Log _log = LogFactoryUtil.getLog(
		DLFileEntryQueryDDMFieldPreFilterContributor.class);

}