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

package com.liferay.portal.servlet.filters.secure;

import com.liferay.portal.security.auth.AuthenticationConfig;
import jodd.util.Wildcard;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Set of requests patterns and an access manager.
 * @author Igor Spasic
 * @author Tomas Polesovsky
 */
public class AuthenticationRule {

	/**
	 * Adds requests URI matching pattern to this set.
	 * <p/>
	 * Custom user-defined pattern matchers are going to be added later.
	 */
	public void addPattern(
		String requestUri/*, RequestPatternMatcher requestMatcher*/) {
		_patterns.add(requestUri);
	}

	/**
	 * See {@link #match(String)}.
	 */
	public boolean match(HttpServletRequest request) {
		return match(request.getRequestURI());
	}

	/**
	 * Matches provided URI against this set. Returns <code>true</code> if URI
	 * matches the set definition.
	 */
	public boolean match(String requestUri) {

		for (String requestPattern : _patterns) {
			// for the sake of simplicity now use just wildcards
			if (Wildcard.match(requestUri, requestPattern)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Registers new authenticator.
	 */
	public void registerAuthenticationConfig(AuthenticationConfig config) {
		_authenticationConfig = config;
	}

	public AuthenticationConfig getAuthenticationConfig(){
		return _authenticationConfig;
	}

	private AuthenticationConfig _authenticationConfig;

	private List<String> _patterns = new ArrayList<String>();
}