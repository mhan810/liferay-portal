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

package com.liferay.portlet.bookmarks.lar;

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.StagedDataHandlerImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryDependency;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryDependencyImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksFolderDataHandlerImpl
	extends StagedDataHandlerImpl<BookmarksFolder>
	implements BookmarksFolderDataHandler {

	public void export(
			BookmarksFolder folder, DataHandlerContext context,
			LarDigestModule parentPortletModule)
		throws Exception {

		String path = ExportImportPathUtil.getEntityPath(folder);

		if (context.isPathProcessed(path) ||
			!context.isWithinDateRange(folder.getModifiedDate())) {

			return;
		}

		// Digesting

		LarDigestEntry digestEntry = new LarDigestEntryImpl();

		digestEntry.setAction(getAssetAction(context, folder));
		digestEntry.setPath(path);
		digestEntry.setClassName(BookmarksFolder.class.getName());
		digestEntry.setClassPK(StringUtil.valueOf(folder.getFolderId()));
		digestEntry.setUuid(folder.getUuid());

		List<BookmarksFolder> children = BookmarksFolderUtil.findByG_P(
			folder.getGroupId(), folder.getFolderId());

		LarDigestEntryDependency dependency = null;

		if (children != null) {
			for (BookmarksFolder childFolder : children) {
				dependency = new LarDigestEntryDependencyImpl(
					BookmarksFolder.class.getName(), childFolder.getUuid());

				digestEntry.addDependency(dependency);

				export(childFolder, context, parentPortletModule);
			}
		}

		List<BookmarksEntry> childEntries = BookmarksEntryUtil.findByG_F(
			folder.getGroupId(), folder.getFolderId());

		if (childEntries != null) {
			for (BookmarksEntry childEntry : childEntries) {
				dependency = new LarDigestEntryDependencyImpl(
					BookmarksEntry.class.getName(), childEntry.getUuid());

				digestEntry.addDependency(dependency);

				bookmarksEntryDataHandler.export(
					childEntry, context, parentPortletModule);
			}
		}

		parentPortletModule.addEntry(digestEntry);

		// Serialization

		serialize(folder, context);

		context.addProcessedPath(path);
	}

	@Override
	public void importData(LarDigestEntry entry, DataHandlerContext context)
		throws Exception {

		BookmarksFolder folder = (BookmarksFolder)getZipEntryAsObject(
			context.getZipReader(), entry.getPath());

		String path = ExportImportPathUtil.getEntityPath(folder);

		if (context.isPathProcessed(path)) {
			return;
		}

		long userId = context.getUserId(folder.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)context.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long parentFolderId = MapUtil.getLong(
			folderIds, folder.getParentFolderId(), folder.getParentFolderId());

		ServiceContext serviceContext = createServiceContext(
			entry.getPath(), folder, BookmarksPortletDataHandler._NAMESPACE,
			context);

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

		importDependencies(entry, context);
	}

	protected String getImportFolderPath(
		DataHandlerContext context, long folderId) {

		StringBundler sb = new StringBundler(4);

		sb.append(
			getSourcePortletPath(
				context.getScopeGroupId(), PortletKeys.BOOKMARKS));
		sb.append("/folders/");
		sb.append(folderId);
		sb.append(".xml");

		return sb.toString();
	}

}