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

package com.liferay.journal.search;

import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalService;
import com.liferay.journal.service.permission.JournalFolderPermission;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.FolderIndexer;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.document.DocumentHelper;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.IndexerHelper;
import com.liferay.portal.search.indexer.IndexerPropertyKeys;
import com.liferay.portal.search.indexer.ModelIndexer;
import com.liferay.portal.search.indexer.PermissionAwareModelIndexer;
import com.liferay.trash.kernel.util.TrashUtil;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		IndexerPropertyKeys.DEFAULT_SELECTED_FIELD_NAMES + "=" +
			Field.COMPANY_ID + "|" + Field.DESCRIPTION + "|" +
				Field.ENTRY_CLASS_NAME + "|" + Field.ENTRY_CLASS_PK + "|" +
					Field.TITLE + "|" + Field.UID,
		IndexerPropertyKeys.FILTER_SEARCH + "=true",
		IndexerPropertyKeys.PERMISSION_AWARE + "=true"
	},
	service = ModelIndexer.class
)
public class JournalFolderModelIndexer
	implements ModelIndexer<JournalFolder>, FolderIndexer,
			   PermissionAwareModelIndexer {

	public static final String CLASS_NAME = JournalFolder.class.getName();

	@Override
	public void delete(JournalFolder journalFolder) throws Exception {
		indexerHelper.deleteDocument(
			journalFolder.getCompanyId(), journalFolder.getFolderId(), this);
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public Document getDocument(JournalFolder journalFolder)
		throws PortalException {

		if (_log.isDebugEnabled()) {
			_log.debug("Indexing journalFolder " + journalFolder);
		}

		Document document = documentHelper.getBaseModelDocument(
			CLASS_NAME, journalFolder);

		document.addText(Field.DESCRIPTION, journalFolder.getDescription());
		document.addKeyword(Field.FOLDER_ID, journalFolder.getParentFolderId());

		String title = journalFolder.getName();

		if (journalFolder.isInTrash()) {
			title = TrashUtil.getOriginalTitle(title);
		}

		document.addText(Field.TITLE, title);

		document.addKeyword(
			Field.TREE_PATH,
			StringUtil.split(journalFolder.getTreePath(), CharPool.SLASH));

		if (_log.isDebugEnabled()) {
			_log.debug("Document " + journalFolder + " indexed successfully");
		}

		return document;
	}

	@Override
	public String[] getFolderClassNames() {
		return new String[] {CLASS_NAME};
	}

	@Override
	public Summary getSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		Summary summary = documentHelper.createSummary(
			document, Field.TITLE, Field.DESCRIPTION);

		summary.setMaxContentLength(200);

		return summary;
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, String entryClassName,
			long entryClassPK, String actionId)
		throws Exception {

		JournalFolder folder = _journalFolderLocalService.getFolder(
			entryClassPK);

		return JournalFolderPermission.contains(
			permissionChecker, folder, ActionKeys.VIEW);
	}

	@Override
	public boolean isVisible(long classPK, int status) throws Exception {
		return true;
	}

	@Override
	public boolean isVisibleRelatedEntry(long classPK, int status)
		throws Exception {

		return true;
	}

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		indexerHelper.addStatus(contextBooleanFilter, searchContext);
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {
	}

	@Override
	public void reindex(JournalFolder journalFolder) throws Exception {
		Document document = getDocument(journalFolder);

		final Indexer<JournalFolder> indexer =
			indexerRegistry.nullSafeGetIndexer(JournalFolder.class);

		indexWriterHelper.updateDocument(
			indexer.getSearchEngineId(), journalFolder.getCompanyId(), document,
			indexer.isCommitImmediately());
	}

	@Override
	public void reindex(String className, long classPK) throws Exception {
		JournalFolder folder = _journalFolderLocalService.getFolder(classPK);

		reindex(folder);
	}

	@Override
	public void reindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		reindexFolders(companyId);
	}

	protected void reindexFolders(long companyId) throws PortalException {
		final IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			_journalFolderLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<JournalFolder>() {

				@Override
				public void performAction(JournalFolder folder) {
					try {
						Document document = getDocument(folder);

						if (document != null) {
							indexableActionableDynamicQuery.addDocuments(
								document);
						}
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index journal folder " +
									folder.getFolderId(),
								pe);
						}
					}
				}

			});

		final Indexer<JournalFolder> indexer =
			indexerRegistry.nullSafeGetIndexer(JournalFolder.class);

		indexableActionableDynamicQuery.setSearchEngineId(
			indexer.getSearchEngineId());

		indexableActionableDynamicQuery.performActions();
	}

	@Reference(unbind = "-")
	protected void setJournalFolderLocalService(
		JournalFolderLocalService journalFolderLocalService) {

		_journalFolderLocalService = journalFolderLocalService;
	}

	@Reference
	protected DocumentHelper documentHelper;

	@Reference
	protected IndexerHelper indexerHelper;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected IndexStatusManager indexStatusManager;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	private static final Log _log = LogFactoryUtil.getLog(
		JournalFolderModelIndexer.class);

	private JournalFolderLocalService _journalFolderLocalService;

}