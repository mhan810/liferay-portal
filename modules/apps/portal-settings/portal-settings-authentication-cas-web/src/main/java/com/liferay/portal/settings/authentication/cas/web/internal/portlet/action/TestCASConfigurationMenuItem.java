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

package com.liferay.portal.settings.authentication.cas.web.internal.portlet.action;

import com.liferay.configuration.admin.menu.item.ConfigurationMenuItem;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;

import java.util.Dictionary;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Drew Brokke
 */
@Component(
	property = "configuration.pid=com.liferay.portal.security.sso.cas.configuration.CASConfiguration",
	service = ConfigurationMenuItem.class
)
public class TestCASConfigurationMenuItem implements ConfigurationMenuItem {

	@Override
	public String getLabel(Locale locale) {
		return "test-cas-configuration";
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String pid, String factoryPid, Dictionary<String, Object> properties) {

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			portletRequest,
			"com_liferay_users_admin_web_portlet_UsersAdminPortlet",
			PortletRequest.RENDER_PHASE);

		return liferayPortletURL.toString();
	}

}