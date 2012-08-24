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
import com.liferay.portal.kernel.lar.digest.LarDigestEntryImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksEntryDataHandlerImpl
	extends StagedDataHandlerImpl<BookmarksEntry>
	implements BookmarksEntryDataHandler {

	@Override
	public void export(
			BookmarksEntry entry, DataHandlerContext context,
			LarDigestModule parentPortletModule)
		throws Exception {

		String path = ExportImportPathUtil.getEntityPath(entry);

		if (context.isPathProcessed(path) ||
			!context.isWithinDateRange(entry.getModifiedDate())) {

			return;
		}

		LarDigestEntry digestEntry = new LarDigestEntryImpl();

		digestEntry.setAction(getAssetAction(context, entry));
		digestEntry.setPath(path);
		digestEntry.setClassName(BookmarksEntry.class.getName());
		digestEntry.setClassPK(StringUtil.valueOf(entry.getEntryId()));
		digestEntry.setUuid(entry.getUuid());

		parentPortletModule.addEntry(digestEntry);

		// Serializing

		serialize(entry, context);

		context.addProcessedPath(path);
	}

	@Override
	public void importData(
			LarDigestEntry digestEntry, DataHandlerContext context)
		throws Exception {

		BookmarksEntry entry = (BookmarksEntry)getZipEntryAsObject(
			context.getZipReader(), digestEntry.getPath());

		String path = ExportImportPathUtil.getEntityPath(entry);

		if (context.isPathProcessed(path)) {
			return;
		}

		long userId = context.getUserId(entry.getUserUuid());

		Map<Long, Long> folderIds =
			(Map<Long, Long>)context.getNewPrimaryKeysMap(
				BookmarksFolder.class);

		long folderId = MapUtil.getLong(
			folderIds, entry.getFolderId(), entry.getFolderId());

		ServiceContext serviceContext = createServiceContext(
			digestEntry.getPath(), entry,
			BookmarksPortletDataHandler._NAMESPACE, context);

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
					context.getScopeGroupId(), folderId, entry.getName(),
					entry.getUrl(), entry.getDescription(), serviceContext);
			}
		}
		else {
			importedEntry = BookmarksEntryLocalServiceUtil.addEntry(
				userId, context.getScopeGroupId(), folderId, entry.getName(),
				entry.getUrl(), entry.getDescription(), serviceContext);
		}
	}

}