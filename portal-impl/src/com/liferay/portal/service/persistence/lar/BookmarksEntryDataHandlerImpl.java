/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksEntryDataHandlerImpl
	extends BaseDataHandlerImpl<BookmarksEntry>
	implements BookmarksEntryDataHandler {

	public void deserialize(Document document) {
	}

	@Override
	public LarDigestItem doDigest(BookmarksEntry entry) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(entry);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		boolean exportPermissions = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PERMISSIONS);

		if (exportPermissions) {
			Map permissionsMap = digestEntityPermissions(
				BookmarksEntry.class.getName(), entry.getEntryId(),
				context);

			digestItem.setPermissions(permissionsMap);
		}

		digestItem.setAction(getDigestAction(entry));
		digestItem.setPath(path);
		digestItem.setType(BookmarksEntry.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(entry.getEntryId()));

		return digestItem;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		BookmarksEntry entry = (BookmarksEntry)getZipEntryAsObject(
			item.getPath());

		long userId = context.getUserId(entry.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)context.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long folderId = MapUtil.getLong(
			folderIds, entry.getFolderId(), entry.getFolderId());

		if ((folderId != BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(folderId == entry.getFolderId())) {


			LarDigest digest = context.getLarDigest();

			List<LarDigestItem> parentFolderItem = digest.findDigestItems(
				0, null, BookmarksFolder.class.getName(),
				StringUtil.valueOf(folderId));

			bookmarksFolderDataHandler.importData(parentFolderItem.get(0));

			folderId = MapUtil.getLong(
				folderIds, entry.getFolderId(), entry.getFolderId());
		}

		ServiceContext serviceContext = createServiceContext(
			item.getPath(), entry, _NAMESPACE);

		BookmarksEntry importedEntry = null;

		if (context.isDataStrategyMirror()) {
			BookmarksEntry existingEntry = BookmarksEntryUtil.fetchByUUID_G(
				entry.getUuid(), context.getScopeGroupId());

			if (existingEntry == null) {
				serviceContext.setUuid(entry.getUuid());

				importedEntry = BookmarksEntryLocalServiceUtil.addEntry(
					userId, context.getScopeGroupId(), folderId,
					entry.getName(), entry.getUrl(), entry.getDescription(),
					serviceContext);
			}
			else {
				importedEntry = BookmarksEntryLocalServiceUtil.updateEntry(
					userId, existingEntry.getEntryId(),
					context.getScopeGroupId(), folderId,
					entry.getName(), entry.getUrl(), entry.getDescription(),
					serviceContext);
			}
		}
		else {
			importedEntry = BookmarksEntryLocalServiceUtil.addEntry(
				userId, context.getScopeGroupId(), folderId,
				entry.getName(), entry.getUrl(), entry.getDescription(),
				serviceContext);
		}

		// toDo: getClassedModel again...
		//context.importClassedModel(entry, importedEntry, _NAMESPACE);
	}

	public BookmarksEntry getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				long entryId = Long.valueOf(classPK);

				BookmarksEntry bookmarksEntry =
					BookmarksEntryLocalServiceUtil.getBookmarksEntry(entryId);

				return bookmarksEntry;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	private static final String _NAMESPACE = "bookmarks";

}
