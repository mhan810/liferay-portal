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

import com.liferay.portal.SecureMethodInvocationException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ProtectedServletRequest;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.PortalAuthenticationManager;
import com.liferay.portal.security.auth.AuthenticationConfig;
import com.liferay.portal.security.auth.AuthenticationContext;
import com.liferay.portal.security.auth.AuthenticationResult;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PropsValues;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Tomas Polesovsky
 */
public class PortalAuthenticationFilter extends BasePortalFilter {

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		AuthenticationConfig authenticationConfig =
			new ServletFilterAuthenticationLoader().load(request,
				getFilterConfig());

		PortalAuthenticationManager pam =
			PortalAuthenticationManager.getInstance();

		AuthenticationContext authCtx = new AuthenticationContext();
		authCtx.setAuthenticationConfig(authenticationConfig);
		authCtx.setRequest(request);
		authCtx.setResponse(response);

		pam.setAuthenticationContext(authCtx);

		if (applyHttps(request, response)) {
			return;
		}

		if (PropsValues.PORTAL_JAAS_ENABLE) {
			processFilter(getClass(), request, response, filterChain);
			return;
		}

		try {
			authenticate(pam, true, request, response, filterChain);
		} catch (Throwable ex) {
			Throwable cause = ex;

			while(!(cause instanceof SecureMethodInvocationException) &&
				cause.getCause() != null){

				cause = cause.getCause();
			}
			if(!(cause instanceof SecureMethodInvocationException)){
				throw new RuntimeException(ex);
			}

			authenticate(pam, false, request, response, filterChain);
		}

	}

	protected void authenticate(PortalAuthenticationManager pam,
			boolean applyRequiredAuthenticators, HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
		throws Exception {

		AuthenticationResult result = pam.authenticate(
			request, response, applyRequiredAuthenticators);

		// there was an authenticator in the pipeline
		if(result != null){
			if(result.getState() == AuthenticationResult.State.IN_PROGRESS){
				return;
			}

			if(result.getState() == AuthenticationResult.State.INVALID_CREDENTIALS
				&& applyRequiredAuthenticators){

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			if(result.getState() == AuthenticationResult.State.SUCCESS){
				request = new ProtectedServletRequest(request,
					String.valueOf(result.getUserId()));
			}
		}

		// we don't need to continue if optional authentication didn't
		// changed current user
		if(result == null && !applyRequiredAuthenticators){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// call inner API
		processFilter(getClass(), request, response, filterChain);
	}


	private boolean applyHttps(HttpServletRequest request,
		   HttpServletResponse response)
		throws IOException {

		PortalAuthenticationManager pam =
			PortalAuthenticationManager.getInstance();

		if (!pam.getAuthenticationContext().getAuthenticationConfig()
			.isSecure() || request.isSecure()) {

			return false;
		}

		if (_log.isDebugEnabled()) {
			String completeURL = HttpUtil.getCompleteURL(request);

			_log.debug("Securing " + completeURL);
		}

		StringBundler redirectURL = new StringBundler(5);

		redirectURL.append(Http.HTTPS_WITH_SLASH);
		redirectURL.append(request.getServerName());
		redirectURL.append(request.getServletPath());

		String queryString = request.getQueryString();

		if (Validator.isNotNull(queryString)) {
			redirectURL.append(StringPool.QUESTION);
			redirectURL.append(request.getQueryString());
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Redirect to " + redirectURL);
		}

		response.sendRedirect(redirectURL.toString());

		return true;
	}

	private static Log _log = LogFactoryUtil.getLog
		(PortalAuthenticationFilter.class.getName());
}
