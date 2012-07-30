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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksFolderDataHandlerImpl
	extends BaseDataHandlerImpl<BookmarksFolder>
	implements BookmarksFolderDataHandler {

	@Override
	public LarDigestItem doDigest(BookmarksFolder folder) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(folder);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(getDigestAction(folder));
		digestItem.setPath(path);
		digestItem.setType(BookmarksFolder.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(folder.getFolderId()));

		return digestItem;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		BookmarksFolder folder = (BookmarksFolder)getZipEntryAsObject(
			item.getPath());

		long userId = context.getUserId(folder.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)context.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long parentFolderId = MapUtil.getLong(
			folderIds, folder.getParentFolderId(), folder.getParentFolderId());

		if ((parentFolderId !=
				BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
			(parentFolderId == folder.getParentFolderId())) {

			String path = getImportFolderPath(context, parentFolderId);

			LarDigest digest = context.getLarDigest();

			List<LarDigestItem> parentFolderItem = digest.findDigestItems(
				0, path, BookmarksFolder.class.getName(), null);

			doImport(parentFolderItem.get(0));

			parentFolderId = MapUtil.getLong(
				folderIds, folder.getParentFolderId(),
				folder.getParentFolderId());
		}

		ServiceContext serviceContext = createServiceContext(
			item.getPath(), folder, _NAMESPACE);

		BookmarksFolder importedFolder = null;

		if (context.isDataStrategyMirror()) {
			BookmarksFolder existingFolder = BookmarksFolderUtil.fetchByUUID_G(
				folder.getUuid(), context.getScopeGroupId());

			if (existingFolder == null) {
				serviceContext.setUuid(folder.getUuid());

				importedFolder = BookmarksFolderLocalServiceUtil.addFolder(
					userId, parentFolderId, folder.getName(),
					folder.getDescription(), serviceContext);
			}
			else {
				importedFolder = BookmarksFolderLocalServiceUtil.updateFolder(
					existingFolder.getFolderId(), parentFolderId,
					folder.getName(), folder.getDescription(), false,
					serviceContext);
			}
		}
		else {
			importedFolder = BookmarksFolderLocalServiceUtil.addFolder(
				userId, parentFolderId, folder.getName(),
				folder.getDescription(), serviceContext);
		}

		// toDo: put the classedModel methods to somewhere from PortletDataContext
		//context.importClassedModel(folder, importedFolder, _NAMESPACE);
	}

	public BookmarksFolder getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				long folderId = Long.valueOf(classPK);

				BookmarksFolder bookmarksFolder =
					BookmarksFolderLocalServiceUtil.getBookmarksFolder(
						folderId);

				return bookmarksFolder;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	protected String getImportFolderPath(
		DataHandlerContext context, long folderId) {

		StringBundler sb = new StringBundler(4);

		sb.append(getSourcePortletPath(PortletKeys.BOOKMARKS));
		sb.append("/folders/");
		sb.append(folderId);
		sb.append(".xml");

		return sb.toString();
	}

	private static final String _NAMESPACE = "bookmarks";

}
