/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.notifications;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.notifications.BaseModelUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import javax.portlet.PortletURL;

/**
 * @author Roberto Díaz
 */
public class DocumentLibraryUserNotificationHandler
	extends BaseModelUserNotificationHandler<FileEntry> {

	public DocumentLibraryUserNotificationHandler() {
		setPortletId(PortletKeys.DOCUMENT_LIBRARY);
	}

	@Override
	protected FileEntry fetchBaseModel(long classPK) throws SystemException {
		try {
			return DLAppLocalServiceUtil.getFileEntry(classPK);
		}
		catch (PortalException pe) {
			return null;
		}
	}

	@Override
	protected String getTitle(
		FileEntry fileEntry, ServiceContext serviceContext) {

		return fileEntry.getTitle();
	}

	@Override
	protected String getTitle(int notificationType) {
		if (notificationType ==
				UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY) {

			return "x-added-a-new-document";
		}
		else if (notificationType ==
					UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY) {

			return "x-updated-a-document";
		}

		return StringPool.BLANK;
	}

	@Override
	protected void setLinkParameters(
		PortletURL portletURL, FileEntry fileEntry) {

		portletURL.setParameter(
			"struts_action", "/document_library/view_file_entry");
		portletURL.setParameter(
			"fileEntryId", String.valueOf(fileEntry.getFileEntryId()));
	}

}