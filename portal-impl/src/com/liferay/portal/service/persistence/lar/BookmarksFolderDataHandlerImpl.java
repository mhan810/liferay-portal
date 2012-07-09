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
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class BookmarksFolderDataHandlerImpl
	extends BaseDataHandlerImpl<BookmarksFolder>
	implements BookmarksFolderDataHandler {


	public void deserialize(Document document) {
	}

	@Override
	public void doDigest(BookmarksFolder folder) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		boolean exportPermissions = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PERMISSIONS);

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(folder);

		if (context.isPathProcessed(path)) {
			return;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		if (exportPermissions) {
			Map permissionsMap = digestEntityPermissions(
				BookmarksFolder.class.getName(), context.getScopeGroupId());

			digestItem.setPermissions(permissionsMap);
		}

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath(path);
		digestItem.setType(BookmarksFolder.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(folder.getFolderId()));

		digest.write(digestItem);
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

}
