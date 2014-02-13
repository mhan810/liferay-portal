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

package com.liferay.portlet.wiki.notifications;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.notifications.BaseModelUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;

import javax.portlet.PortletURL;

/**
 * @author Roberto Díaz
 */
public class WikiUserNotificationHandler
	extends BaseModelUserNotificationHandler<WikiPage> {

	public WikiUserNotificationHandler() {
		setPortletId(PortletKeys.WIKI);
	}

	@Override
	protected WikiPage fetchBaseModel(long classPK) throws SystemException {
		return WikiPageLocalServiceUtil.fetchWikiPage(classPK);
	}

	@Override
	protected String getTitle(int notificationType) {
		if (notificationType ==
				UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY) {

			return "x-added-a-new-wiki-page";
		}
		else if (notificationType ==
					UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY) {

			return "x-updated-a-wiki-page";
		}

		return StringPool.BLANK;
	}

	@Override
	protected String getTitle(WikiPage page, ServiceContext serviceContext) {
		return page.getTitle();
	}

	@Override
	protected void setLinkParameters(PortletURL portletURL, WikiPage page) {
		portletURL.setParameter("struts_action", "/wiki/view");
		portletURL.setParameter("nodeId", String.valueOf(page.getNodeId()));
		portletURL.setParameter("title", page.getTitle());
	}

}