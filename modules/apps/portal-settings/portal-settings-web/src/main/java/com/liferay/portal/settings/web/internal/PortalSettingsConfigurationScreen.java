/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.settings.web.internal;

import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.settings.portlet.action.PortalSettingsConfigurationScreenContributor;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
public class PortalSettingsConfigurationScreen implements ConfigurationScreen {

	public PortalSettingsConfigurationScreen(
		PortalSettingsConfigurationScreenContributor
			portalSettingsConfigurationScreenContributor,
		JSPRenderer jspRenderer) {

		_portalSettingsConfigurationScreenContributor =
			portalSettingsConfigurationScreenContributor;
		_jspRenderer = jspRenderer;
	}

	@Override
	public String getCategoryKey() {
		return _portalSettingsConfigurationScreenContributor.getCategoryKey();
	}

	@Override
	public String getKey() {
		return _portalSettingsConfigurationScreenContributor.getKey();
	}

	@Override
	public String getName(Locale locale) {
		return _portalSettingsConfigurationScreenContributor.getName(locale);
	}

	@Override
	public String getScope() {
		return "company";
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		request.setAttribute(
			"deleteConfirmationText",
			UnicodeLanguageUtil.get(
				ResourceBundleUtil.getBundle(
					request.getLocale(),
					PortalSettingsConfigurationScreen.class),
				"are-you-sure-you-want-to-reset-the-configured-values"));
		request.setAttribute(
			"deleteMVCActionCommandName",
			_portalSettingsConfigurationScreenContributor.
				getDeleteMVCActionCommandName());
		request.setAttribute(
			"jspPath",
			_portalSettingsConfigurationScreenContributor.getJspPath());
		request.setAttribute(
			"saveMVCActionCommandName",
			_portalSettingsConfigurationScreenContributor.
				getSaveMVCActionCommandName());
		request.setAttribute(
			"servletContext",
			_portalSettingsConfigurationScreenContributor.getServletContext());
		request.setAttribute(
			"title",
			_portalSettingsConfigurationScreenContributor.getName(
				request.getLocale()));

		_jspRenderer.renderJSP(
			request, response, "/configuration/screen/entry.jsp");
	}

	private final JSPRenderer _jspRenderer;
	private final PortalSettingsConfigurationScreenContributor
		_portalSettingsConfigurationScreenContributor;

}