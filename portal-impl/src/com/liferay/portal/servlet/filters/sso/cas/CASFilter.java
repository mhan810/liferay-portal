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

package com.liferay.portal.servlet.filters.sso.cas;

import com.liferay.portal.kernel.cas.CASManagerUtil;
import com.liferay.portal.kernel.cas.CASServer;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.cas.CASServerImpl;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;

import java.io.IOException;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

/**
 * @author Michael Young
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Tina Tian
 * @author Zsolt Balogh
 * @author Edward Han
 */
public class CASFilter extends BasePortalFilter {

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		try {
			long companyId = PortalUtil.getCompanyId(request);

			if (PrefsPropsUtil.getBoolean(
					companyId, PropsKeys.CAS_AUTH_ENABLED,
					PropsValues.CAS_AUTH_ENABLED)) {

				return true;
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return false;
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected boolean isLogout(HttpServletRequest request) throws IOException {
		boolean logout = false;

		HttpSession session = request.getSession();

		Object forceLogout = session.getAttribute(WebKeys.CAS_FORCE_LOGOUT);

		if (forceLogout != null) {
			session.removeAttribute(WebKeys.CAS_FORCE_LOGOUT);

			logout = true;
		}

		String pathInfo = request.getPathInfo();

		if ((pathInfo != null) && pathInfo.contains("/portal/logout")) {
			logout = true;
		}

		return logout;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		HttpSession session = request.getSession();

		long companyId = PortalUtil.getCompanyId(request);

		boolean casAutoRedirect = PrefsPropsUtil.getBoolean(
			companyId, PropsKeys.CAS_AUTO_REDIRECT);

		if (processLogout(request, response)) {
			if (!response.isCommitted()) {
				processFilter(CASFilter.class, request, response, filterChain);
			}

			return;
		}

		String login = (String)session.getAttribute(WebKeys.CAS_LOGIN);

		if (Validator.isNotNull(login)) {
			processFilter(CASFilter.class, request, response, filterChain);

			return;
		}

		String ticket = ParamUtil.getString(request, "ticket");

		if (Validator.isNotNull(ticket)) {
			processLogin(request, response, ticket, companyId);
		}
		else if (casAutoRedirect) {
			if (processRedirect(request, response, companyId)) {
				return;
			}
		}

		processFilter(CASFilter.class, request, response, filterChain);
	}

	protected void processLogin(
			HttpServletRequest request, HttpServletResponse response,
			String ticket, long companyId)
		throws Exception {

		String casServerId = ParamUtil.getString(request, "casServerId");

		boolean strictLogin = PrefsPropsUtil.getBoolean(
			companyId, PropsKeys.CAS_AUTH_STRICT);

		boolean isDefaultConfig =
			request.getParameterMap().containsKey("casServerId") &&
				Validator.isNull(casServerId);

		CASServer casServer;

		if (isDefaultConfig) {
			casServer = CASManagerUtil.getDefaultCASServer(companyId);
		}
		else {
			casServer = CASManagerUtil.getCASServer(companyId, casServerId);
		}

		boolean valid = validateTicket(
			request, response, companyId, casServer, ticket);

		if (!strictLogin && !valid) {
			Map<String, CASServer> casServers = CASManagerUtil.getCASServers(
				companyId);

			for (CASServer otherCASServer : casServers.values()) {
				if (!otherCASServer.getServerId().equals(casServerId)) {
					valid = validateTicket(
						request, response, companyId, otherCASServer, ticket);

					if (valid) {
						break;
					}
				}
			}

			if (!valid && !isDefaultConfig) {
				casServer = CASManagerUtil.getDefaultCASServer(companyId);

				valid = validateTicket(
					request, response, companyId, casServer, ticket);
			}
		}

		if (!valid && _log.isInfoEnabled()) {
			_log.info(
				"Failed to authenticate ticket [" + ticket +
					"] for companyId " + companyId);
		}
	}

	protected boolean processLogout(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, SystemException {

		if (!isLogout(request)) {
			return false;
		}

		HttpSession session = request.getSession();

		try {
			long companyId = PortalUtil.getCompanyId(request);

			Object casServerId = session.getAttribute(WebKeys.CAS_SERVER_ID);

			if (casServerId != null) {
				CASServer casServer;

				if (!casServerId.equals(StringPool.BLANK)) {
					casServer = CASManagerUtil.getCASServer(
						companyId, (String)casServerId);
				}
				else {
					casServer = CASManagerUtil.getDefaultCASServer(companyId);
				}

				if (casServer != null) {
					String logoutUrl = casServer.getLogoutUrl();

					response.sendRedirect(logoutUrl);

					return true;
				}
				else {
					throw new SystemException(
						"Unable to locate cas server with serverId: " +
							casServerId);
				}
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug("User did not sign in using CAS");
				}
			}
		}
		finally {
			session.invalidate();
		}

		return true;
	}

	protected boolean processRedirect(
			HttpServletRequest request, HttpServletResponse response,
			long companyId)
		throws IOException, SystemException {

		String pathInfo = request.getPathInfo();

		if (Validator.isNotNull(pathInfo) && pathInfo.equals("/portal/login")) {
			String redirectServerId = PrefsPropsUtil.getString(
				companyId, PropsKeys.CAS_AUTO_REDIRECT_SERVER_ID);

			CASServer casServer;

			if (Validator.isNull(redirectServerId)) {
				casServer = CASManagerUtil.getDefaultCASServer(companyId);
			}
			else {
				casServer = CASManagerUtil.getCASServer(
					companyId, redirectServerId);
			}

			if (casServer != null) {
				String loginURL = CASManagerUtil.getLoginUrl(
					request, response, casServer);

				response.sendRedirect(loginURL);

				return true;
			}
			else {
				if (_log.isErrorEnabled()) {
					_log.error(
						"CAS auto redirect is enabled but no suitable " +
							"CAS server to redirect to found");
				}
			}
		}

		return false;
	}

	protected boolean validateTicket(
			HttpServletRequest request, HttpServletResponse response,
			long companyId, CASServer casServer, String ticket)
		throws Exception {

		if (casServer == null) {
			return false;
		}

		String serviceUrl = CASManagerUtil.getServiceUrl(
			request, response, casServer);

		String login = validateTicket(companyId, casServer, serviceUrl, ticket);

		if (Validator.isNotNull(login)) {
			HttpSession session = request.getSession();

			session.setAttribute(WebKeys.CAS_LOGIN, login);
			session.setAttribute(
				WebKeys.CAS_SERVER_ID, casServer.getServerId());

			return true;
		}

		return false;
	}

	protected String validateTicket(
			long companyId, CASServer casServer, String serviceUrl,
			String ticket)
		throws Exception {

		String login = null;

		TicketValidator ticketValidator =
			((CASServerImpl)casServer).getTicketValidator();

		try {
			Assertion assertion = ticketValidator.validate(ticket, serviceUrl);

			if (assertion != null) {
				AttributePrincipal attributePrincipal =
					assertion.getPrincipal();

				login = attributePrincipal.getName();
			}
		}
		catch (TicketValidationException tve) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Ticket " + ticket + " failed auth against " +
						casServer.getServerId() + " for companyId " +
						companyId);
			}
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				"Ticket " + ticket + " successfully authenticated against " +
					casServer.getServerId() + " for companyId " + companyId);
		}

		return login;
	}

	private static Log _log = LogFactoryUtil.getLog(CASFilter.class);

}