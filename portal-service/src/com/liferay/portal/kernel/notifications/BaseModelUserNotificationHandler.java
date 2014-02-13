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

package com.liferay.portal.kernel.notifications;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

/**
 * @author Brian Wing Shun Chan
 * @author Sergio González
 */
public abstract class BaseModelUserNotificationHandler<T extends AuditedModel>
	extends BaseUserNotificationHandler {

	protected abstract T fetchBaseModel(long classPK)
		throws SystemException;

	@Override
	protected String getBody(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			userNotificationEvent.getPayload());

		long classPK = jsonObject.getLong("classPK");

		T baseModel = fetchBaseModel(classPK);

		if (baseModel == null) {
			UserNotificationEventLocalServiceUtil.deleteUserNotificationEvent(
				userNotificationEvent.getUserNotificationEventId());

			return null;
		}

		StringBundler sb = new StringBundler(5);

		sb.append("<div class=\"title\">");

		int notificationType = jsonObject.getInt("notificationType");

		String title = getTitle(notificationType);

		sb.append(
			serviceContext.translate(
				title, HtmlUtil.escape(getUserName(baseModel))));

		sb.append("</div><div class=\"body\">");
		sb.append(
			HtmlUtil.escape(
				StringUtil.shorten(getTitle(baseModel, serviceContext)), 50));
		sb.append("</div>");

		return sb.toString();
	}

	@Override
	protected String getLink(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			userNotificationEvent.getPayload());

		long classPK = jsonObject.getLong("classPK");

		T baseModel = fetchBaseModel(classPK);

		if (baseModel == null) {
			return null;
		}

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		User user = themeDisplay.getUser();

		Group group = user.getGroup();

		long portletPlid = PortalUtil.getPlidFromPortletId(
			group.getGroupId(), true, getPortletId());

		PortletURL portletURL = null;

		if (portletPlid != 0) {
			portletURL = PortletURLFactoryUtil.create(
				serviceContext.getLiferayPortletRequest(), getPortletId(),
				portletPlid, PortletRequest.RENDER_PHASE);

			setLinkParameters(portletURL, baseModel);
		}
		else {
			LiferayPortletResponse liferayPortletResponse =
				serviceContext.getLiferayPortletResponse();

			portletURL = liferayPortletResponse.createRenderURL(getPortletId());

			setLinkParameters(portletURL, baseModel);

			portletURL.setWindowState(WindowState.MAXIMIZED);
		}

		return portletURL.toString();
	}

	protected abstract String getTitle(int notificationType);

	protected abstract String getTitle(
		T baseModel, ServiceContext serviceContext);

	protected String getUserName(T baseModel) {
		return PortalUtil.getUserName(baseModel.getUserId(), StringPool.BLANK);
	}

	protected abstract void setLinkParameters(
		PortletURL portletURL, T baseModel);

}