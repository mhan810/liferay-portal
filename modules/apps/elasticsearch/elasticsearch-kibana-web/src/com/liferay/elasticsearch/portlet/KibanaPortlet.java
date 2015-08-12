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

package com.liferay.elasticsearch.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(immediate = true, property = {
	"com.liferay.portlet.css-class-wrapper=portlet-kibana",
	"com.liferay.portlet.display-category=category.tools",
	"com.liferay.portlet.header-portlet-css=/css/main.css",
	"com.liferay.portlet.maximize-edit=true",
	"com.liferay.portlet.maximize-help=true",
	"com.liferay.portlet.preferences-owned-by-group=true",
	"com.liferay.portlet.private-request-attributes=false",
	"com.liferay.portlet.private-session-attributes=false",
	"com.liferay.portlet.render-weight=50",
	"com.liferay.portlet.show-portlet-access-denied=false",
	"com.liferay.portlet.show-portlet-inactive=false",
	"com.liferay.portlet.use-default-template=true",
	"javax.portlet.display-name=Kibana", "javax.portlet.expiration-cache=0",
	"javax.portlet.init-param.template-path=/",
	"javax.portlet.init-param.view-template=/view.jsp",
	"javax.portlet.resource-bundle=content.Language",
	"javax.portlet.security-role-ref=power-user,user,guest",
	"javax.portlet.supports.mime-type=text/html",
	"javax.portlet.window-state=maximized"
}, service = Portlet.class)
public class KibanaPortlet extends MVCPortlet {
}
