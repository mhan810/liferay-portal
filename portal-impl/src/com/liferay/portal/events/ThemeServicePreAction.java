/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.events;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.SessionParamUtil;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.impl.ColorSchemeImpl;
import com.liferay.portal.model.impl.ThemeImpl;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Edward Han
 */
public class ThemeServicePreAction extends Action {
	public void run(
			HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(
				WebKeys.THEME_DISPLAY);

			// Theme and color scheme

			Theme theme = themeDisplay.getTheme();
			ColorScheme colorScheme = themeDisplay.getColorScheme();

			if (theme != null || colorScheme != null) {
				if (_log.isInfoEnabled()) {
					_log.info("Theme already set, skipping");
				}

				return;
			}

			Layout layout = themeDisplay.getLayout();

			Group group = layout.getGroup();

			long companyId = themeDisplay.getCompanyId();

			boolean signedIn = themeDisplay.isSignedIn();

			boolean wapTheme = BrowserSnifferUtil.isWap(request);

			String contextPath = PortalUtil.getPathContext();

			if ((layout != null) &&
				group.isControlPanel()) {

				String themeId = PrefsPropsUtil.getString(
					companyId, PropsKeys.CONTROL_PANEL_LAYOUT_REGULAR_THEME_ID);
				String colorSchemeId =
					ColorSchemeImpl.getDefaultRegularColorSchemeId();

				theme = ThemeLocalServiceUtil.getTheme(
					companyId, themeId, wapTheme);
				colorScheme = ThemeLocalServiceUtil.getColorScheme(
					companyId, theme.getThemeId(), colorSchemeId, wapTheme);

				if (!wapTheme && theme.isWapTheme()) {
					theme = ThemeLocalServiceUtil.getTheme(
						companyId,
						PropsValues.CONTROL_PANEL_LAYOUT_REGULAR_THEME_ID, false);
					colorScheme = ThemeLocalServiceUtil.getColorScheme(
						companyId, theme.getThemeId(), colorSchemeId, false);
				}
			}
			else if (layout != null) {
				if (wapTheme) {
					theme = layout.getWapTheme();
					colorScheme = layout.getWapColorScheme();
				}
				else {
					theme = layout.getTheme();
					colorScheme = layout.getColorScheme();
				}
			}
			else {
				String themeId = null;
				String colorSchemeId = null;

				if (wapTheme) {
					themeId = ThemeImpl.getDefaultWapThemeId(companyId);
					colorSchemeId = ColorSchemeImpl.getDefaultWapColorSchemeId();
				}
				else {
					themeId = ThemeImpl.getDefaultRegularThemeId(companyId);
					colorSchemeId =
						ColorSchemeImpl.getDefaultRegularColorSchemeId();
				}

				theme = ThemeLocalServiceUtil.getTheme(
					companyId, themeId, wapTheme);
				colorScheme = ThemeLocalServiceUtil.getColorScheme(
					companyId, theme.getThemeId(), colorSchemeId, wapTheme);
			}

			request.setAttribute(WebKeys.THEME, theme);
			request.setAttribute(WebKeys.COLOR_SCHEME, colorScheme);

			boolean themeCssFastLoad = SessionParamUtil.getBoolean(
				request, "css_fast_load", PropsValues.THEME_CSS_FAST_LOAD);
			boolean themeImagesFastLoad = SessionParamUtil.getBoolean(
				request, "images_fast_load", PropsValues.THEME_IMAGES_FAST_LOAD);

			boolean themeJsBarebone = PropsValues.JAVASCRIPT_BAREBONE_ENABLED;

			if (themeJsBarebone) {
				if (signedIn) {
					themeJsBarebone = false;
				}
			}

			boolean themeJsFastLoad = SessionParamUtil.getBoolean(
				request, "js_fast_load", PropsValues.JAVASCRIPT_FAST_LOAD);

			String lifecycle = ParamUtil.getString(request, "p_p_lifecycle", "0");

			lifecycle = ParamUtil.getString(request, "p_t_lifecycle", lifecycle);

			boolean isolated = ParamUtil.getBoolean(request, "p_p_isolated");

			String facebookCanvasPageURL = (String)request.getAttribute(
				WebKeys.FACEBOOK_CANVAS_PAGE_URL);

			boolean widget = false;

			Boolean widgetObj = (Boolean)request.getAttribute(WebKeys.WIDGET);

			if (widgetObj != null) {
				widget = widgetObj.booleanValue();
			}

			themeDisplay.setFacebookCanvasPageURL(facebookCanvasPageURL);
			themeDisplay.setWidget(widget);

			themeDisplay.setLookAndFeel(contextPath, theme, colorScheme);
			themeDisplay.setThemeCssFastLoad(themeCssFastLoad);
			themeDisplay.setThemeImagesFastLoad(themeImagesFastLoad);
			themeDisplay.setThemeJsBarebone(themeJsBarebone);
			themeDisplay.setThemeJsFastLoad(themeJsFastLoad);
			themeDisplay.setLifecycle(lifecycle);
			themeDisplay.setLifecycleAction(lifecycle.equals("1"));
			themeDisplay.setLifecycleRender(lifecycle.equals("0"));
			themeDisplay.setLifecycleResource(lifecycle.equals("2"));
			themeDisplay.setIsolated(isolated);

		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ThemeServicePreAction.class);
}