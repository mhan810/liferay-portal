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
import com.liferay.portal.kernel.servlet.ProtectedServletRequest;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * New, policy-based <code>SecureFilter</code>.
 * @author Igor Spasic
 */
public class PortalSecureFilter extends BasePortalFilter {

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);

		// read policy file name and parse it
		// for now we will hard code definition here

		// for now AuthenticationRule can register just one security manager
		// this will be expanded.

		BasicAuthSecurityAccessManager basicAuth =
			new BasicAuthSecurityAccessManager();
		AllowedHostsSecurityAccessManager allowedHosts =
			new AllowedHostsSecurityAccessManager();

		AuthenticationRule rule1 = new AuthenticationRule();

		rule1.addPattern("/api/jsonws/*");
		rule1.registerSecurityAccessManager(allowedHosts);

		AuthenticationRule rule2 = new AuthenticationRule();

		rule2.addPattern("/api/jsonws/*");
		rule2.registerSecurityAccessManager(basicAuth);
		rule2.setRequired(true);

		_authenticationRules.add(rule1);
		_authenticationRules.add(rule2);
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		long authenticatedUserId = 0;

		// check all rules
		for (AuthenticationRule rule : _authenticationRules) {
			long userId = rule.accept(request, response);

			// if user is authenticated, check if it is the same user
			if (userId > 0) {
				if (authenticatedUserId > 0 && userId != authenticatedUserId) {
					throw new RuntimeException(
						"different user already authenticated!");
				}

				authenticatedUserId = userId;
			}
		}

		if (authenticatedUserId > 0) {
			// user is authenticated
			request = setCredentials(request, authenticatedUserId);
		}

		// remote api is ready to be invoked
		PortalSecurityManager.getInstance().setRemoteAccess();

		try {
			processFilter(getClass(), request, response, filterChain);
		} catch(SecureMethodInvocationException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	protected HttpServletRequest setCredentials(
			HttpServletRequest request, long userId)
		throws Exception {

		User user = UserLocalServiceUtil.getUser(userId);

		String userIdString = String.valueOf(userId);

		request = new ProtectedServletRequest(request, userIdString);

		HttpSession session = request.getSession();

		session.setAttribute(WebKeys.USER, user);

//		if (_usePermissionChecker) {
			PrincipalThreadLocal.setName(userId);

			PrincipalThreadLocal.setPassword(
				PortalUtil.getUserPassword(request));

			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
//		}

		return request;
	}


	private List<AuthenticationRule> _authenticationRules =
		new ArrayList<AuthenticationRule>();

}