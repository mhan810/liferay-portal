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

package com.liferay.portal.security.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Tomas Polesovsky
 */
public interface PortalAuthenticator {

	/**
	 * Performs authentication.<br />
	 * <br />
	 * Can return {@link AuthenticationResult} with
	 * {@link AuthenticationResult.State} defining actual state:
	 * <ul><li>{@link AuthenticationResult.State#SUCCESS} when user
	 * was authenticated</li>
	 * <li>{@link AuthenticationResult.State#INVALID_CREDENTIALS}
	 * when user credentials are invalid</li>
	 * <li>{@link AuthenticationResult.State#IN_PROGRESS} when
	 * authentication is in progress and there is pending interaction with
	 * browser</li></ul>
	 * <br />
	 * Can also return {@code null} when authentication is
	 * not applicable and we want to continue in the pipeline.
	 *
	 * @param authenticationContext Current authentication context
	 * @return null when authentication is not applicable right now,
	 * {@link AuthenticationResult} otherwise
	 * @throws AuthException represents some internal exception,
	 * authentication is then interrupted
	 */
	public AuthenticationResult authenticate(
			AuthenticationContext authenticationContext)
		throws AuthException;
}

