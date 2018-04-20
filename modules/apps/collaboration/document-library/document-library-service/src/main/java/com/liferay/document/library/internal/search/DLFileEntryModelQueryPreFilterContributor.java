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

package com.liferay.document.library.internal.search;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.dynamic.data.mapping.kernel.DDMStructure;
import com.liferay.dynamic.data.mapping.kernel.DDMStructureManager;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseRelatedEntryIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.query.ModelQueryPreFilterContributorHelper;
import com.liferay.portal.search.spi.model.query.contributor.QueryPreFilterContributor;

import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bryan Engler
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry",
	service = QueryPreFilterContributor.class
)
public class DLFileEntryModelQueryPreFilterContributor
	implements QueryPreFilterContributor {

	@Override
	public void contribute(
		BooleanFilter fullQueryBooleanFilter, SearchContext searchContext) {

		addAttachmentFilter(fullQueryBooleanFilter, searchContext);
		addDDMFieldFilter(fullQueryBooleanFilter, searchContext);
		addHelperFilters(fullQueryBooleanFilter, searchContext);
		addHiddenFilter(fullQueryBooleanFilter, searchContext);
		addMimeTypesFilter(fullQueryBooleanFilter, searchContext);
	}

	protected void addAttachmentFilter(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		if (!searchContext.isIncludeAttachments()) {
			return;
		}

		try {
			relatedEntryIndexer.addRelatedClassNames(
				booleanFilter, searchContext);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected void addDDMFieldFilter(
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

	protected void addHelperFilters(
		BooleanFilter fullQueryBooleanFilter, SearchContext searchContext) {

		modelQueryPreFilterContributorHelper.addClassTypeIdsFilter(
			fullQueryBooleanFilter, searchContext);
		modelQueryPreFilterContributorHelper.addStagingFilter(
			fullQueryBooleanFilter, searchContext);
		modelQueryPreFilterContributorHelper.addWorkflowStatusesFilter(
			fullQueryBooleanFilter, searchContext);
	}

	protected void addHiddenFilter(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		if (ArrayUtil.isEmpty(searchContext.getFolderIds()) ||
			ArrayUtil.contains(
				searchContext.getFolderIds(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {

			booleanFilter.addRequiredTerm(
				Field.HIDDEN, searchContext.isIncludeAttachments());
		}
	}

	protected void addMimeTypesFilter(
		BooleanFilter booleanFilter, SearchContext searchContext) {

		String[] mimeTypes = (String[])searchContext.getAttribute("mimeTypes");

		if (ArrayUtil.isNotEmpty(mimeTypes)) {
			BooleanFilter mimeTypesBooleanFilter = new BooleanFilter();

			for (String mimeType : mimeTypes) {
				mimeTypesBooleanFilter.addTerm(
					"mimeType",
					StringUtil.replace(
						mimeType, CharPool.FORWARD_SLASH, CharPool.UNDERLINE));
			}

			booleanFilter.add(mimeTypesBooleanFilter, BooleanClauseOccur.MUST);
		}
	}

	@Reference
	protected DDMStructureManager ddmStructureManager;

	@Reference
	protected ModelQueryPreFilterContributorHelper
		modelQueryPreFilterContributorHelper;

	protected RelatedEntryIndexer relatedEntryIndexer =
		new BaseRelatedEntryIndexer();

	private static final Log _log = LogFactoryUtil.getLog(
		DLFileEntryModelQueryPreFilterContributor.class);

}