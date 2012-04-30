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

import com.liferay.portal.SecureMethodInvocationException;
import jodd.util.Wildcard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Set of requests patterns and an access manager.
 */
public class AuthenticationRule {

	/**
	 * {@link #match(String) Matches} request of this pattern set and invokes
	 * assigned access manager(s).
	 */
	public long accept(HttpServletRequest request, HttpServletResponse response) {

		if (!match(request)) {
			return -1;	// not consumed, therefore access is granted
		}

		long userId = 0;

		try {
			userId = _Security_accessManager.accept(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (userId != 0) {
			// returns when A) request not consumed or B) user authenticated
			return userId;
		}

		// user auth failed
		if (_required) {
			throw new SecureMethodInvocationException(
				"Authentication required!");
		}

		return 0;
	}

	/**
	 * Adds requests URI matching pattern to this set.
	 * <p/>
	 * Custom user-defined pattern matchers are going to be added later.
	 */
	public void addPattern(
		String requestUri/*, RequestPatternMatcher requestMatcher*/) {
		_patterns.add(requestUri);
	}

	public boolean isRequired() {
		return _required;
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
	public void registerSecurityAccessManager
	(SecurityAccessManager securityAccessManager) {

		_Security_accessManager = securityAccessManager;
	}

	public void setRequired(boolean required) {
		this._required = required;
	}

	private SecurityAccessManager _Security_accessManager;

	private List<String> _patterns = new ArrayList<String>();

	private boolean _required;
}