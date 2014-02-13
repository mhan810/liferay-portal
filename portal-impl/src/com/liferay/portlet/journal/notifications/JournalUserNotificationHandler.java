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

package com.liferay.portlet.journal.notifications;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.notifications.BaseModelUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;

import javax.portlet.PortletURL;

/**
 * @author Iván Zaera
 */
public class JournalUserNotificationHandler
	extends BaseModelUserNotificationHandler<JournalArticle> {

	public JournalUserNotificationHandler() {
		setPortletId(PortletKeys.JOURNAL);
	}

	@Override
	protected JournalArticle fetchBaseModel(long classPK)
		throws SystemException {

		try {
			return JournalArticleServiceUtil.getArticle(classPK);
		}
		catch (PortalException pe) {
			return null;
		}
	}

	@Override
	protected String getTitle(int notificationType) {
		if (notificationType ==
				UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY) {

			return "x-added-a-new-web-content";
		}
		else if (notificationType ==
					UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY) {

			return "x-updated-a-web-content";
		}

		return StringPool.BLANK;
	}

	@Override
	protected String getTitle(
		JournalArticle article, ServiceContext serviceContext) {

		return article.getTitle(serviceContext.getLocale());
	}

	@Override
	protected void setLinkParameters(
		PortletURL portletURL, JournalArticle article) {

		portletURL.setParameter("struts_action", "/journal/edit_article");
		portletURL.setParameter(
			"groupId", String.valueOf(article.getGroupId()));
		portletURL.setParameter(
			"articleId", String.valueOf(article.getArticleId()));
		portletURL.setParameter(
			"version", String.valueOf(article.getVersion()));
	}

}