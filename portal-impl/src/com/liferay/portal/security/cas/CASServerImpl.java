/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.security.cas;

import com.liferay.portal.kernel.cas.CASServer;

import org.jasig.cas.client.validation.TicketValidator;

/**
 * @author Edward C. Han
 */
public class CASServerImpl implements CASServer {

	public CASServerImpl(
			String loginUrl, String logoutUrl, String serverId,
			String serverUrl, String serviceUrl,
			TicketValidator ticketValidator) {

		_loginUrl = loginUrl;
		_logoutUrl = logoutUrl;
		_serverId = serverId;
		_serverUrl = serverUrl;
		_serviceUrl = serviceUrl;
		_ticketValidator = ticketValidator;
	}

	public String getLoginUrl() {
		return _loginUrl;
	}

	public String getLogoutUrl() {
		return _logoutUrl;
	}

	public String getServerId() {
		return _serverId;
	}

	public String getServerUrl() {
		return _serverUrl;
	}

	public String getServiceUrl() {
		return _serviceUrl;
	}

	public TicketValidator getTicketValidator() {
		return _ticketValidator;
	}

	private String _loginUrl;
	private String _logoutUrl;
	private String _serverId;
	private String _serverUrl;
	private String _serviceUrl;

	private TicketValidator _ticketValidator;

}