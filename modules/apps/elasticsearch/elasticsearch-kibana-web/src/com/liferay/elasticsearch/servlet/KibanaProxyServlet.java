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

package com.liferay.elasticsearch.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.elasticsearch.process.Kibana;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(immediate = true, property = {
	"osgi.http.whiteboard.context.select=kibana-web",
	"osgi.http.whiteboard.servlet.alias=KibanaProxyServlet",
	"osgi.http.whiteboard.servlet.name=Kibana Proxy Servlet",
	"osgi.http.whiteboard.servlet.pattern=/kibana-proxy/*"
}, service = Servlet.class)
public class KibanaProxyServlet extends ProxyServlet {

	@Override
	public void init()
		throws ServletException {

		this.targetUri = kibana.getUrl();
		super.init();
	}

	@Reference
	public void setKibana(Kibana kibana) {

		this.kibana = kibana;
	}

	protected Kibana kibana;
}
