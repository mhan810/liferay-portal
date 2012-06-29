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

import com.liferay.portal.kernel.lar.LarPersistenceContext;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.List;

/**
 * @author Mate Thurzo
 */
public class BookmarksPortletLarPersistenceImpl
	extends PortletLarPersistenceImpl
	implements BookmarksPortletLarPersistence {

	@Override
	public void doDigest(Portlet object) throws Exception {
		LarPersistenceContext context =
			getLarPersistenceContext();

		/*portletDataContext.addPermissions(
			"com.liferay.portlet.bookmarks",
			portletDataContext.getScopeGroupId());*/

		List<BookmarksFolder> folders = BookmarksFolderUtil.findByGroupId(
			context.getScopeGroupId());

		for (BookmarksFolder folder : folders) {
			exportFolder(folder, context);
		}

		List<BookmarksEntry> entries = BookmarksEntryUtil.findByG_F(
			context.getScopeGroupId(),
			BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		for (BookmarksEntry entry : entries) {
			exportEntry(entry, context);
		}
	}

	protected void exportEntry(
			BookmarksEntry entry, LarPersistenceContext context)
		throws Exception {

		if (!context.isWithinDateRange(entry.getModifiedDate())) {
			return;
		}

		long parentForlderId = entry.getFolderId();

		if (parentForlderId > 0) {
			exportParentFolder(context.getLarDigest(), parentForlderId);
		}

		bookmarksEntryLarPersistence.digest(entry);
	}

	private void exportFolder(
			BookmarksFolder folder, LarPersistenceContext context)
		throws Exception{

		if (context.isWithinDateRange(folder.getModifiedDate())) {
			exportParentFolder(
				context.getLarDigest(), folder.getParentFolderId());

			bookmarksFolderLarPersistence.digest(folder);
		}

		List<BookmarksEntry> entries = BookmarksEntryUtil.findByG_F(
			folder.getGroupId(), folder.getFolderId());

		for (BookmarksEntry entry : entries) {
			bookmarksEntryLarPersistence.digest(entry);
		}
	}

	private void exportParentFolder(LarDigest larDigest, long folderId)
		throws Exception {

		if (folderId == BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return;
		}

		BookmarksFolder folder = BookmarksFolderUtil.findByPrimaryKey(folderId);

		exportParentFolder(larDigest, folder.getParentFolderId());

		bookmarksFolderLarPersistence.digest(folder);
	}

}
