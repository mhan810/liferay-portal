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

package com.liferay.portal.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * General security access manager. It can do authentication checks, but
 * also any other security check to allow/deny access to some request.
 */
public interface SecurityAccessManager {

	public static final int ACCESS_DENIED = 0;

	public static final int ACCESS_GRANTED = -1;

	/**
	 * Performs security check. Optionally authenticates user.
	 * Detects if user access is already granted.
	 * Returns user id of authenticated user. If user authentication fails,
	 * or if access is denied, returns 0.
	 * <p>
	 * Therefore, when request is consumed, return value will be >=0.
	 * <p>
	 * If user is not authenticated at all, and request is not consumed,
	 * returns -1. This also means access is granted.
	 * <p>
	 * If required, then filter checks user in the filter BEFORE we reach the
	 * service layer. There is an additional check on service layer too.
	 *
	 * If optional, then public accessed requests do not throw exceptions
	 * and are allowed; but due to service layer check, only public methods
	 * can be invoked this way.
	 */
	public long accept(HttpServletRequest request, HttpServletResponse response,
		boolean required);


}