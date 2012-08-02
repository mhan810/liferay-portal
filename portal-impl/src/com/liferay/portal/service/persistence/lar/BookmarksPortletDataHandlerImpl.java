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
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksEntryUtil;
import com.liferay.portlet.bookmarks.service.persistence.BookmarksFolderUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksPortletDataHandlerImpl
	extends PortletDataHandlerImpl
	implements BookmarksPortletDataHandler {

	@Override
	public String[] getDataPortletPreferences() {
		return new String[0];
	}

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[] {
			_foldersAndEntries
		};
	}

	@Override
	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(
				_NAMESPACE, "bookmarks", true, _metadataControls)
		};
	}

	@Override
	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[] {
			_foldersAndEntries
		};
	}

	@Override
	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(
				_NAMESPACE, "bookmarks", true, _metadataControls)
		};
	}

	@Override
	public boolean isAlwaysExportable() {
		return _ALWAYS_EXPORTABLE;
	}

	@Override
	public boolean isAlwaysStaged() {
		return _ALWAYS_STAGED;
	}

	public boolean isDataLocalized() {
		return _DATA_LOCALIZED;
	}

	@Override
	public boolean isPublishToLiveByDefault() {
		return _PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	public String getNamespace() {
		return _NAMESPACE;
	}

	public String getPermissionResourceName() {
		return "com.liferay.portlet.bookmarks";
	}

	@Override
	protected LarDigestItem doDigestPortlet(
			Portlet portlet, LarDigestItem item, DataHandlerContext context)
		throws Exception {

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

		return item;
	}

	protected void exportEntry(
			BookmarksEntry entry, DataHandlerContext context)
		throws Exception {

		if (!context.isWithinDateRange(entry.getModifiedDate())) {
			return;
		}

		long parentForlderId = entry.getFolderId();

		if (parentForlderId > 0) {
			exportParentFolder(parentForlderId, context);
		}

		bookmarksEntryDataHandler.digest(entry, context);
	}

	protected void exportFolder(
			BookmarksFolder folder, DataHandlerContext context)
		throws Exception{

		if (context.isWithinDateRange(folder.getModifiedDate())) {
			exportParentFolder(folder.getParentFolderId(), context);

			bookmarksFolderDataHandler.digest(folder, context);
		}

		List<BookmarksEntry> entries = BookmarksEntryUtil.findByG_F(
			folder.getGroupId(), folder.getFolderId());

		for (BookmarksEntry entry : entries) {
			bookmarksEntryDataHandler.digest(entry, context);
		}
	}

	protected void exportParentFolder(long folderId, DataHandlerContext context)
		throws Exception {

		if (folderId == BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return;
		}

		BookmarksFolder folder = BookmarksFolderUtil.findByPrimaryKey(folderId);

		exportParentFolder(folder.getParentFolderId(), context);

		bookmarksFolderDataHandler.digest(folder, context);
	}

	private static PortletDataHandlerBoolean _foldersAndEntries =
		new PortletDataHandlerBoolean(
			_NAMESPACE, "folders-and-entries", true, true);

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(_NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(_NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(_NAMESPACE, "tags")
		};

}
