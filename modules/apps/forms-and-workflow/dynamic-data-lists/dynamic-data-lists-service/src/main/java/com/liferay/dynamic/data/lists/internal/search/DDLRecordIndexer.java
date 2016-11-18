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

package com.liferay.dynamic.data.lists.internal.search;

import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalService;
import com.liferay.dynamic.data.lists.service.DDLRecordVersionLocalService;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageEngine;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.Locale;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.search.document.DocumentHelper;
import com.liferay.portal.search.indexer.IndexerHelper;
import com.liferay.portal.search.indexer.IndexerPropertyKeys;
import com.liferay.portal.search.indexer.ModelIndexer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		IndexerPropertyKeys.DEFAULT_SELECTED_FIELD_NAMES + "=" +
			Field.COMPANY_ID + "|" + Field.ENTRY_CLASS_NAME + "|" +
			Field.ENTRY_CLASS_PK + "|" + Field.UID,
		IndexerPropertyKeys.DEFAULT_SELECTED_LOCALIZED_FIELD_NAMES + "=" +
			Field.DESCRIPTION + "|" + Field.TITLE,
		IndexerPropertyKeys.PERMISSION_AWARE + "=true"
	},
	service = ModelIndexer.class
)
public class DDLRecordIndexer implements ModelIndexer<DDLRecord> {

	public static final String CLASS_NAME = DDLRecord.class.getName();

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public BooleanFilter getFacetBooleanFilter(
			String className, SearchContext searchContext)
		throws Exception {

		BooleanFilter facetBooleanFilter = new BooleanFilter();

		facetBooleanFilter.addTerm(
			Field.ENTRY_CLASS_NAME, DDLRecord.class.getName());

		if (searchContext.getUserId() > 0) {
			facetBooleanFilter =
				searchPermissionChecker.getPermissionBooleanFilter(
					searchContext.getCompanyId(), searchContext.getGroupIds(),
					searchContext.getUserId(), DDLRecordSet.class.getName(),
					facetBooleanFilter, searchContext);
		}

		return facetBooleanFilter;
	}

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		int status = GetterUtil.getInteger(
			searchContext.getAttribute(Field.STATUS),
			WorkflowConstants.STATUS_APPROVED);

		if (status != WorkflowConstants.STATUS_ANY) {
			contextBooleanFilter.addRequiredTerm(Field.STATUS, status);
		}

		long recordSetId = GetterUtil.getLong(
			searchContext.getAttribute("recordSetId"));

		if (recordSetId > 0) {
			contextBooleanFilter.addRequiredTerm("recordSetId", recordSetId);
		}

		long recordSetScope = GetterUtil.getLong(
			searchContext.getAttribute("recordSetScope"),
			DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS);

		contextBooleanFilter.addRequiredTerm("recordSetScope", recordSetScope);

		indexerHelper.addSearchClassTypeIds(
			contextBooleanFilter, searchContext);

		String ddmStructureFieldName = (String)searchContext.getAttribute(
			"ddmStructureFieldName");
		Serializable ddmStructureFieldValue = searchContext.getAttribute(
			"ddmStructureFieldValue");

		if (Validator.isNotNull(ddmStructureFieldName) &&
			Validator.isNotNull(ddmStructureFieldValue)) {

			QueryFilter queryFilter = ddmIndexer.createFieldValueQueryFilter(
				ddmStructureFieldName, ddmStructureFieldValue,
				searchContext.getLocale());

			contextBooleanFilter.add(queryFilter, BooleanClauseOccur.MUST);
		}
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		indexerHelper.addSearchTerm(
			searchQuery, searchContext, Field.USER_NAME, false);

		addDDMContentSearchTerm(searchQuery, searchContext);
	}

	protected void addDDMContent(
			DDLRecordVersion recordVersion, DDMFormValues ddmFormValues,
			Document document)
		throws Exception {

		Set<Locale> locales = ddmFormValues.getAvailableLocales();

		for (Locale locale : locales) {
			StringBundler sb = new StringBundler(3);

			sb.append("ddmContent");
			sb.append(StringPool.UNDERLINE);
			sb.append(LocaleUtil.toLanguageId(locale));

			document.addText(
				sb.toString(), extractDDMContent(recordVersion, locale));
		}
	}

	protected void addDDMContentSearchTerm(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {

		Locale locale = searchContext.getLocale();

		StringBundler sb = new StringBundler(3);

		sb.append("ddmContent");
		sb.append(StringPool.UNDERLINE);
		sb.append(LocaleUtil.toLanguageId(locale));

		addSearchTerm(searchQuery, searchContext, sb.toString(), false);
		indexerHelper.addSearchTerm(
			searchQuery, searchContext, "ddmContent", false);
	}

	@Override
	public void delete(DDLRecord ddlRecord) throws Exception {
		indexerHelper.deleteDocument(
			ddlRecord.getCompanyId(), ddlRecord.getRecordId(), this);
	}

	@Override
	public Document getDocument(DDLRecord ddlRecord) throws PortalException {
		Document document = documentHelper.getBaseModelDocument(
			CLASS_NAME, ddlRecord);

		DDLRecordVersion recordVersion = ddlRecord.getRecordVersion();

		DDLRecordSet recordSet = recordVersion.getRecordSet();

		document.addKeyword(
			Field.CLASS_NAME_ID,
			classNameLocalService.getClassNameId(DDLRecordSet.class));
		document.addKeyword(Field.CLASS_PK, recordSet.getRecordSetId());
		document.addKeyword(
			Field.CLASS_TYPE_ID, recordVersion.getRecordSetId());
		document.addKeyword(Field.RELATED_ENTRY, true);
		document.addKeyword(Field.STATUS, recordVersion.getStatus());
		document.addKeyword(Field.VERSION, recordVersion.getVersion());

		document.addKeyword("recordSetId", recordSet.getRecordSetId());
		document.addKeyword("recordSetScope", recordSet.getScope());

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		DDMFormValues ddmFormValues = storageEngine.getDDMFormValues(
			recordVersion.getDDMStorageId());

		addDDMContent(recordVersion, ddmFormValues, document);

		ddmIndexer.addAttributes(document, ddmStructure, ddmFormValues);

		return document;
	}

	@Override
	public Summary getSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		long recordSetId = GetterUtil.getLong(document.get("recordSetId"));

		String title = getTitle(recordSetId, locale);

		Summary summary = documentHelper.createSummary(
			document, Field.TITLE, Field.DESCRIPTION);

		summary.setMaxContentLength(200);
		summary.setTitle(title);

		return summary;
	}

	@Override
	public void reindex(DDLRecord ddlRecord) throws Exception {
		Document document = getDocument(ddlRecord);

		indexWriterHelper.updateDocument(
			getSearchEngineId(), ddlRecord.getCompanyId(), document,
			isCommitImmediately());
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		DDLRecord record = ddlRecordLocalService.getRecord(classPK);

		reindex(record);
	}

	@Override
	public void reindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		reindexRecords(companyId);
	}

	protected String extractDDMContent(
			DDLRecordVersion recordVersion, Locale locale)
		throws PortalException {

		DDMFormValues ddmFormValues = storageEngine.getDDMFormValues(
			recordVersion.getDDMStorageId());

		if (ddmFormValues == null) {
			return StringPool.BLANK;
		}

		DDLRecordSet recordSet = recordVersion.getRecordSet();

		return ddmIndexer.extractIndexableAttributes(
			recordSet.getDDMStructure(), ddmFormValues, locale);
	}

	protected String getTitle(long recordSetId, Locale locale) {
		try {
			DDLRecordSet recordSet = ddlRecordSetLocalService.getRecordSet(
				recordSetId);

			DDMStructure ddmStructure = recordSet.getDDMStructure();

			String ddmStructureName = ddmStructure.getName(locale);

			String recordSetName = recordSet.getName(locale);

			return LanguageUtil.format(
				locale, "new-x-for-list-x",
				new Object[] {ddmStructureName, recordSetName}, false);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return StringPool.BLANK;
	}

	protected void reindexRecords(long companyId) throws Exception {
		final IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			ddlRecordLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					Property recordIdProperty = PropertyFactoryUtil.forName(
						"recordId");

					DynamicQuery recordVersionDynamicQuery =
						ddlRecordVersionLocalService.dynamicQuery();

					recordVersionDynamicQuery.setProjection(
						ProjectionFactoryUtil.property("recordId"));

					dynamicQuery.add(
						recordIdProperty.in(recordVersionDynamicQuery));

					Property recordSetProperty = PropertyFactoryUtil.forName(
						"recordSetId");

					DynamicQuery recordSetDynamicQuery =
						ddlRecordSetLocalService.dynamicQuery();

					recordSetDynamicQuery.setProjection(
						ProjectionFactoryUtil.property("recordSetId"));

					Property scopeProperty = PropertyFactoryUtil.forName(
						"scope");

					recordSetDynamicQuery.add(
						scopeProperty.in(_REINDEX_SCOPES));

					dynamicQuery.add(
						recordSetProperty.in(recordSetDynamicQuery));
				}

			});
		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<DDLRecord>() {

				@Override
				public void performAction(DDLRecord record)
					throws PortalException {

					try {
						Document document = getDocument(record);

						if (document != null) {
							indexableActionableDynamicQuery.addDocuments(
								document);
						}
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index dynamic data lists record " +
									record.getRecordId(),
								pe);
						}
					}
				}

			});

		Indexer<?> indexer = indexerRegistry.nullSafeGetIndexer(getClassName());

		indexableActionableDynamicQuery.setSearchEngineId(
			indexer.getSearchEngineId());

		indexableActionableDynamicQuery.performActions();
	}

	@Reference
	protected ClassNameLocalService classNameLocalService;

	@Reference
	protected DDLRecordLocalService ddlRecordLocalService;

	@Reference
	protected DDLRecordSetLocalService ddlRecordSetLocalService;

	@Reference
	protected DDLRecordVersionLocalService ddlRecordVersionLocalService;

	@Reference
	protected DDMIndexer ddmIndexer;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	@Reference
	protected SearchPermissionChecker searchPermissionChecker;

	@Reference
	protected StorageEngine storageEngine;

	private static final int[] _REINDEX_SCOPES = new int[] {
		DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS,
		DDLRecordSetConstants.SCOPE_FORMS,
		DDLRecordSetConstants.SCOPE_KALEO_FORMS
	};

	private static final Log _log = LogFactoryUtil.getLog(
		DDLRecordIndexer.class);

	private ClassNameLocalService _classNameLocalService;
	private DDLRecordLocalService _ddlRecordLocalService;
	private DDLRecordSetLocalService _ddlRecordSetLocalService;
	private DDLRecordVersionLocalService _ddlRecordVersionLocalService;
	private DDMIndexer _ddmIndexer;
	private StorageEngine _storageEngine;

	@Reference
	protected DocumentHelper documentHelper;

	@Reference
	protected IndexerHelper indexerHelper;

	@Reference
	protected IndexerRegistry indexerRegistry;

}