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

package com.liferay.taglib.theme;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletSettings;
import com.liferay.portlet.PortletSettingsFactoryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Brian Wing Shun Chan
 */
public class DefineObjectsTag extends TagSupport {

	@Override
	public int doStartTag() {
		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay == null) {
			return SKIP_BODY;
		}

		pageContext.setAttribute("account", themeDisplay.getAccount());
		pageContext.setAttribute("colorScheme", themeDisplay.getColorScheme());
		pageContext.setAttribute("company", themeDisplay.getCompany());
		pageContext.setAttribute("contact", themeDisplay.getContact());

		if (themeDisplay.getLayout() != null) {
			pageContext.setAttribute("layout", themeDisplay.getLayout());
		}

		if (themeDisplay.getLayouts() != null) {
			pageContext.setAttribute("layouts", themeDisplay.getLayouts());
		}

		if (themeDisplay.getLayoutTypePortlet() != null) {
			pageContext.setAttribute(
				"layoutTypePortlet", themeDisplay.getLayoutTypePortlet());
		}

		pageContext.setAttribute("locale", themeDisplay.getLocale());
		pageContext.setAttribute(
			"permissionChecker", themeDisplay.getPermissionChecker());
		pageContext.setAttribute("plid", new Long(themeDisplay.getPlid()));
		pageContext.setAttribute(
			"portletDisplay", themeDisplay.getPortletDisplay());
		pageContext.setAttribute("realUser", themeDisplay.getRealUser());
		pageContext.setAttribute(
			"scopeGroupId", new Long(themeDisplay.getScopeGroupId()));
		pageContext.setAttribute("theme", themeDisplay.getTheme());
		pageContext.setAttribute("themeDisplay", themeDisplay);
		pageContext.setAttribute("timeZone", themeDisplay.getTimeZone());
		pageContext.setAttribute("user", themeDisplay.getUser());

		setPortletSettings(themeDisplay);

		// Deprecated

		pageContext.setAttribute(
			"portletGroupId", themeDisplay.getScopeGroupId());

		return SKIP_BODY;
	}

	protected void setPortletSettings(ThemeDisplay themeDisplay) {
		try {
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

			PortletSettings companyPortletSettings =
				PortletSettingsFactoryUtil.getCompanyPortletSettings(
					themeDisplay.getCompanyId(), portletDisplay.getId());

			pageContext.setAttribute(
				"companyPortletSettings", companyPortletSettings);

			PortletSettings groupPortletSettings =
				PortletSettingsFactoryUtil.getGroupPortletSettings(
					themeDisplay.getSiteGroupId(), portletDisplay.getId());

			pageContext.setAttribute(
				"groupPortletSettings", groupPortletSettings);

			PortletSettings portletInstancePortletSettings =
				PortletSettingsFactoryUtil.getPortletInstancePortletSettings(
					themeDisplay.getLayout(), portletDisplay.getId());

			pageContext.setAttribute(
				"portletInstancePortletSettings",
				portletInstancePortletSettings);
		}
		catch (SystemException se) {
			throw new RuntimeException(se);
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

}