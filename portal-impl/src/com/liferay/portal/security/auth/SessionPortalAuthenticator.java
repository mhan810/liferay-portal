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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 */
public class SessionPortalAuthenticator implements PortalAuthenticator {

	public AuthenticationResult authenticate(AuthenticationContext ctx)
		throws AuthException {

		try {
			HttpServletRequest request = ctx.getRequest();
			User user = PortalUtil.getUser(request);
			if(user == null){
				return null;
			}

			AuthenticationResult result = new AuthenticationResult();
			result.setState(AuthenticationResult.State.SUCCESS);
			result.setUserId(user.getUserId());
			return result;
		} catch (PortalException e) {
			throw new AuthException(e);
		} catch (SystemException e) {
			throw new AuthException(e);
		}
	}
}
