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

package com.liferay.portal.search.internal.document;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetTagLocalServiceUtil;
import com.liferay.expando.kernel.util.ExpandoBridgeIndexerUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.AttachedModel;
import com.liferay.portal.kernel.model.AuditedModel;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ResourcedModel;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.WorkflowedModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.document.DocumentHelper;
import com.liferay.ratings.kernel.model.RatingsStats;
import com.liferay.ratings.kernel.service.RatingsStatsLocalServiceUtil;
import com.liferay.trash.kernel.model.TrashEntry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = DocumentHelper.class)
public class DocumentHelperImpl implements DocumentHelper {

	@Override
	public void addAttachmentOwnerKey(
		Document document, long classNameId, long classPK) {

		document.addKeyword(Field.CLASS_NAME_ID, String.valueOf(classNameId));
		document.addKeyword(Field.CLASS_PK, String.valueOf(classPK));
	}

	@Override
	public void addEntryKey(Document document, String className, long classPK) {
		document.addKeyword(Field.ENTRY_CLASS_NAME, className);
		document.addKeyword(Field.ENTRY_CLASS_PK, String.valueOf(classPK));
	}

	@Override
	public void addAssetFields(Document document, String className, long classPK) {
		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isSelectable()) {

			return;
		}

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(
			className, classPK);

		if (assetEntry == null) {
			return;
		}

		if (!document.hasField(Field.CREATE_DATE)) {
			document.addDate(Field.CREATE_DATE, assetEntry.getCreateDate());
		}

		if (assetEntry.getExpirationDate() != null) {
			document.addDate(
				Field.EXPIRATION_DATE, assetEntry.getExpirationDate());
		}
		else {
			document.addDate(Field.EXPIRATION_DATE, new Date(Long.MAX_VALUE));
		}

		if (!document.hasField(Field.MODIFIED_DATE)) {
			document.addDate(Field.MODIFIED_DATE, assetEntry.getModifiedDate());
		}

		document.addNumber(Field.PRIORITY, assetEntry.getPriority());

		if (assetEntry.getPublishDate() != null) {
			document.addDate(Field.PUBLISH_DATE, assetEntry.getPublishDate());
		}
		else {
			document.addDate(Field.PUBLISH_DATE, new Date(0));
		}

		RatingsStats ratingsStats = RatingsStatsLocalServiceUtil.fetchStats(
			className, classPK);

		if (ratingsStats != null) {
			document.addNumber(Field.RATINGS, ratingsStats.getAverageScore());
		}
		else {
			document.addNumber(Field.RATINGS, 0.0f);
		}

		document.addNumber(Field.VIEW_COUNT, assetEntry.getViewCount());

		document.addLocalizedKeyword(
			"localized_title",
			populateMap(assetEntry, assetEntry.getTitleMap()), true, true);
		document.addKeyword("visible", assetEntry.isVisible());
	}

	@Override
	public void addTrashFields(Document document, TrashedModel trashedModel) {
		TrashEntry trashEntry = null;

		try {
			trashEntry = trashedModel.getTrashEntry();
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get trash entry for " + trashedModel, pe);
			}
		}

		if (trashEntry == null) {
			document.addDate(Field.REMOVED_DATE, new Date());

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if (serviceContext != null) {
				try {
					User user = UserLocalServiceUtil.getUser(
						serviceContext.getUserId());

					document.addKeyword(
						Field.REMOVED_BY_USER_NAME, user.getFullName(), true);
				}
				catch (PortalException pe) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Unable to locate user: " +
								serviceContext.getUserId(),
							pe);
					}
				}
			}
		}
		else {
			document.addDate(Field.REMOVED_DATE, trashEntry.getCreateDate());
			document.addKeyword(
				Field.REMOVED_BY_USER_NAME, trashEntry.getUserName(), true);

			if (trashedModel.isInTrash() &&
				!trashedModel.isInTrashExplicitly()) {

				document.addKeyword(
					Field.ROOT_ENTRY_CLASS_NAME, trashEntry.getClassName());
				document.addKeyword(
					Field.ROOT_ENTRY_CLASS_PK, trashEntry.getClassPK());
			}
		}

		TrashHandler trashHandler = trashedModel.getTrashHandler();

		try {
			TrashRenderer trashRenderer = null;

			if ((trashHandler != null) && (trashEntry != null)) {
				trashRenderer = trashHandler.getTrashRenderer(
					trashEntry.getClassPK());
			}

			if (trashRenderer != null) {
				document.addKeyword(Field.TYPE, trashRenderer.getType(), true);
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to get trash renderer for " +
						trashEntry.getClassName(),
					pe);
			}
		}
	}

	@Override
	public void addLocalizedField(Document document, String field, Locale siteDefaultLocale, Map<Locale, String> map) {
		for (Map.Entry<Locale, String> entry : map.entrySet()) {
			Locale locale = entry.getKey();

			if (locale.equals(siteDefaultLocale)) {
				document.addText(field, entry.getValue());
			}

			String languageId = LocaleUtil.toLanguageId(locale);

			document.addText(
				LocalizationUtil.getLocalizedName(field, languageId),
				entry.getValue());
		}
	}

	@Override
	public void addAssetCategoryTitles(Document document, String field, List<AssetCategory> assetCategories) {

	}

	@Override
	public void addAssetTagNames(BooleanFilter queryBooleanFilter, SearchContext searchContext)
		throws Exception {

	}

	@Override
	public Document getBaseModelDocument(
		String portletId, BaseModel<?> baseModel) {

		return getBaseModelDocument(portletId, baseModel, baseModel);
	}

	@Override
	public Document getBaseModelDocument(
		String portletId, BaseModel<?> baseModel,
		BaseModel<?> workflowedBaseModel) {

		Document document = (Document) this.document.clone();

		String className = baseModel.getModelClassName();

		long classPK = 0;
		long resourcePrimKey = 0;

		if (baseModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)baseModel;

			classPK = resourcedModel.getResourcePrimKey();
			resourcePrimKey = resourcedModel.getResourcePrimKey();
		}
		else {
			classPK = (Long)baseModel.getPrimaryKeyObj();
		}

		com.liferay.portal.kernel.search.DocumentHelper documentHelper = new com.liferay.portal.kernel.search.DocumentHelper(document);

		documentHelper.setEntryKey(className, classPK);

		document.addUID(className, classPK);

		List<AssetCategory> assetCategories =
			AssetCategoryLocalServiceUtil.getCategories(className, classPK);

		long[] assetCategoryIds = ListUtil.toLongArray(
			assetCategories, AssetCategory.CATEGORY_ID_ACCESSOR);

		document.addKeyword(Field.ASSET_CATEGORY_IDS, assetCategoryIds);

		addSearchAssetCategoryTitles(
			document, Field.ASSET_CATEGORY_TITLES, assetCategories);

		long classNameId = PortalUtil.getClassNameId(className);

		List<AssetTag> assetTags = AssetTagLocalServiceUtil.getTags(
			classNameId, classPK);

		String[] assetTagNames = ListUtil.toArray(
			assetTags, AssetTag.NAME_ACCESSOR);

		document.addText(Field.ASSET_TAG_NAMES, assetTagNames);

		long[] assetTagsIds = ListUtil.toLongArray(
			assetTags, AssetTag.TAG_ID_ACCESSOR);

		document.addKeyword(Field.ASSET_TAG_IDS, assetTagsIds);

		if (resourcePrimKey > 0) {
			document.addKeyword(Field.ROOT_ENTRY_CLASS_PK, resourcePrimKey);
		}

		if (baseModel instanceof AttachedModel) {
			AttachedModel attachedModel = (AttachedModel)baseModel;

			documentHelper.setAttachmentOwnerKey(
				attachedModel.getClassNameId(), attachedModel.getClassPK());
		}

		if (baseModel instanceof AuditedModel) {
			AuditedModel auditedModel = (AuditedModel)baseModel;

			document.addKeyword(Field.COMPANY_ID, auditedModel.getCompanyId());
			document.addDate(Field.CREATE_DATE, auditedModel.getCreateDate());
			document.addDate(
				Field.MODIFIED_DATE, auditedModel.getModifiedDate());
			document.addKeyword(Field.USER_ID, auditedModel.getUserId());

			String userName = PortalUtil.getUserName(
				auditedModel.getUserId(), auditedModel.getUserName());

			document.addKeyword(Field.USER_NAME, userName, true);
		}

		GroupedModel groupedModel = null;

		if (baseModel instanceof GroupedModel) {
			groupedModel = (GroupedModel)baseModel;

			document.addKeyword(
				Field.GROUP_ID, getSiteGroupId(groupedModel.getGroupId()));
			document.addKeyword(
				Field.SCOPE_GROUP_ID, groupedModel.getGroupId());
		}

		if (workflowedBaseModel instanceof WorkflowedModel) {
			WorkflowedModel workflowedModel =
				(WorkflowedModel)workflowedBaseModel;

			document.addKeyword(Field.STATUS, workflowedModel.getStatus());
		}

		if ((groupedModel != null) && (baseModel instanceof TrashedModel)) {
			TrashedModel trashedModel = (TrashedModel)baseModel;

			if (trashedModel.isInTrash()) {
				addTrashFields(document, trashedModel);
			}
		}

		addAssetFields(document, className, classPK);

		ExpandoBridgeIndexerUtil.addAttributes(
			document, baseModel.getExpandoBridge());

		return document;
	}

	@Override
	public Locale getSnippetLocale(Document document, Locale locale) {
		String prefix = Field.SNIPPET + StringPool.UNDERLINE;

		String localizedAssetCategoryTitlesName =
			prefix +
				DocumentImpl.getLocalizedName(
					locale, Field.ASSET_CATEGORY_TITLES);
		String localizedContentName =
			prefix + DocumentImpl.getLocalizedName(locale, Field.CONTENT);
		String localizedDescriptionName =
			prefix + DocumentImpl.getLocalizedName(locale, Field.DESCRIPTION);
		String localizedTitleName =
			prefix + DocumentImpl.getLocalizedName(locale, Field.TITLE);

		if ((document.getField(localizedAssetCategoryTitlesName) != null) ||
			(document.getField(localizedContentName) != null) ||
			(document.getField(localizedDescriptionName) != null) ||
			(document.getField(localizedTitleName) != null)) {

			return locale;
		}

		return null;
	}



	@Override
	public void addSearchAssetCategoryTitles(
		Document document, String field, List<AssetCategory> assetCategories) {

		Map<Locale, List<String>> assetCategoryTitles = new HashMap<>();

		Locale defaultLocale = LocaleUtil.getDefault();

		for (AssetCategory assetCategory : assetCategories) {
			Map<Locale, String> titleMap = assetCategory.getTitleMap();

			for (Map.Entry<Locale, String> entry : titleMap.entrySet()) {
				Locale locale = entry.getKey();
				String title = entry.getValue();

				if (Validator.isNull(title)) {
					continue;
				}

				List<String> titles = assetCategoryTitles.get(locale);

				if (titles == null) {
					titles = new ArrayList<>();

					assetCategoryTitles.put(locale, titles);
				}

				titles.add(StringUtil.toLowerCase(title));
			}
		}

		for (Map.Entry<Locale, List<String>> entry :
			assetCategoryTitles.entrySet()) {

			Locale locale = entry.getKey();
			List<String> titles = entry.getValue();

			String[] titlesArray = titles.toArray(new String[titles.size()]);

			if (locale.equals(defaultLocale)) {
				document.addText(field, titlesArray);
			}

			document.addText(
				field.concat(StringPool.UNDERLINE).concat(locale.toString()),
				titlesArray);
		}
	}

	@Override
	public Summary createSummary(Document document) {
		return createSummary(document, Field.TITLE, Field.CONTENT);
	}

	@Override
	public Summary createSummary(
		Document document, String titleField, String contentField) {

		String prefix = Field.SNIPPET + StringPool.UNDERLINE;

		String title = document.get(prefix + titleField, titleField);
		String content = document.get(prefix + contentField, contentField);

		return new Summary(title, content);
	}

	@Override
	public Map<Locale, String> populateMap(
		AssetEntry assetEntry, Map<Locale, String> map) {

		String defaultValue = map.get(
			LocaleUtil.fromLanguageId(assetEntry.getDefaultLanguageId()));

		for (Locale availableLocale : LanguageUtil.getAvailableLocales(
			assetEntry.getGroupId())) {

			if (!map.containsKey(availableLocale) ||
				Validator.isNull(map.get(availableLocale))) {

				map.put(availableLocale, defaultValue);
			}
		}

		return map;
	}

	protected Group getSiteGroup(long groupId) {
		Group group = null;

		try {
			group = groupLocalService.fetchGroup(groupId);

			if (group != null) {
				if (group.isLayout()) {
					group = group.getParentGroup();
				}
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get site group", pe);
			}
		}

		return group;
	}

	protected long getSiteGroupId(long groupId) {
		Group group = getSiteGroup(groupId);

		if (group == null) {
			return groupId;
		}

		return group.getGroupId();
	}

	protected Document document = new DocumentImpl();

	@Reference
	protected GroupLocalService groupLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		DocumentHelperImpl.class);

}
