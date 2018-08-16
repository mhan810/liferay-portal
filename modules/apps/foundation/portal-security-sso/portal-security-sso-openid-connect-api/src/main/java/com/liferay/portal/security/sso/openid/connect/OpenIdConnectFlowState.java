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

package com.liferay.portal.security.sso.openid.connect;

/**
 * @author Edward C. Han
 */
public enum OpenIdConnectFlowState {

	//	2 - Completed OIDC Auth, Token for OIDC has been validated
	AUTH_COMPLETE,
	//	1 - Request for authentication has been sent to OIDC endpoint
	AUTH_REQUESTED,
	//	0 - Initial State - OIDC Session initialized
	INITIALIZED,
	//	3 - Complete Auth flow - user signed into portal
	PORTAL_AUTH_COMPLETE

}