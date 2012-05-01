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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.AutoResetThreadLocal;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.AuthenticationContext;
import com.liferay.portal.security.auth.AuthenticationResult;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PortalAuthenticator;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Tomas Polesovsky
 */
public class PortalAuthenticationManager {

	/**
	 * Temporary singleton.
	 */
	public static PortalAuthenticationManager getInstance() {
		if (_instance == null) {
			_instance = new PortalAuthenticationManager();
		}
		return _instance;
	}

	public AuthenticationResult authenticate(
			HttpServletRequest request, HttpServletResponse response,
			boolean requiredAuthenticator)
		throws SystemException, PortalException {

		if(getAuthenticationContext() == null){
			throw new IllegalStateException(
				"AuthenticationContext is not set!");
		}

		AuthenticationResult result =
			createGuestAuthenticationResult(request);

		try {
			// create guest authorization context
			createAuthorizationContext(result);
		} catch (Exception e) {
			throw new RuntimeException(
				"Cannot create authorization context for guest: "
					+ e.getMessage(), e);
		}

		if(requiredAuthenticator){
			result = authenticateRequiredPipeline();
		} else {
			result = authenticateOptionalPipeline();
		}

		if(result != null &&
			result.getState() == AuthenticationResult.State.SUCCESS){
			try {
				// create successful authorization context
				createAuthorizationContext(result);
			} catch (Exception e) {
				throw new RuntimeException(
					"Cannot create authorization context for guest: "
						+ e.getMessage(), e);
			}
		}

		return result;
	}

	private AuthenticationResult authenticateOptionalPipeline() {
		AuthenticationResult result = null;

		List<PortalAuthenticator> authenticators = getAuthenticationContext()
				.getAuthenticationConfig()
				.getOptionalAuthenticators();

		for(PortalAuthenticator authenticator : authenticators){
			try {
				result = authenticator.authenticate(
					getAuthenticationContext());

				switch (result.getState()){
					// in optional pipeline it is sufficient
					// to finish with first successful authenticator
					case SUCCESS: {
						createAuthorizationContext(result);
						return result;
					}
					case IN_PROGRESS: {
						return result;
					}
					// no problem - let's try another authenticator
					case INVALID_CREDENTIALS:
					default: break;
				}

			} catch (AuthException e) {
				_log.error(e.getMessage(), e);
			}
		}

		return result;
	}

	protected AuthenticationResult authenticateRequiredPipeline() throws SystemException, PortalException {
		AuthenticationResult result = null;

		List<PortalAuthenticator> authenticators = getAuthenticationContext()
				.getAuthenticationConfig()
				.getRequiredAuthenticators();

		for(PortalAuthenticator authenticator : authenticators){
			try {
				result = authenticator.authenticate(
					getAuthenticationContext());

				switch (result.getState()){
					// all authenticators must be successful
					case SUCCESS: {
						break;
					}
					// if any is in progress or fail => we return
					case IN_PROGRESS:
					case INVALID_CREDENTIALS: {
						return result;
					}
				}

			} catch (AuthException e) {
				_log.error(e.getMessage(), e);

				// we can't omit exception in the required pipeline
				return result;
			}
		}

		return result;
	}

	public void createAuthorizationContext(AuthenticationResult result)
		throws AuthException{

		if(getAuthenticationContext().getAuthenticationConfig()
			.isRemoteAccess()){

			PortalSecurityManager.getInstance().setRemoteAccess();
		}

		long userId = result.getUserId();
		try {
			User user = UserLocalServiceUtil.getUser(userId);
			CompanyThreadLocal.setCompanyId(user.getCompanyId());
			PrincipalThreadLocal.setName(userId);
			PrincipalThreadLocal.setPassword(result.getPassword());
			if(result.getPassword() == null){
				// fallback - authentication didn't provide password
				PrincipalThreadLocal.setPassword(user.getPasswordUnencrypted());
			}

			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		} catch (Exception e) {
			throw new AuthException(e.getMessage(), e);
		}

	}

	public AuthenticationResult createGuestAuthenticationResult(
		HttpServletRequest request) throws SystemException, PortalException {

		AuthenticationResult result = new AuthenticationResult();

		long companyId = PortalUtil.getCompanyId(request);
		long guestId = UserLocalServiceUtil.getDefaultUserId(companyId);
		result.setUserId(guestId);
		result.setState(AuthenticationResult.State.SUCCESS);

		return result;
	}

	public AuthenticationContext getAuthenticationContext(){
		return _authenticationContext.get();
	}

	public void setAuthenticationContext(AuthenticationContext ctx) {
		_authenticationContext.set(ctx);
	}


	private static final Log _log =
		LogFactoryUtil.getLog(PortalAuthenticationManager.class);

	private static PortalAuthenticationManager _instance;

	private static ThreadLocal<AuthenticationContext>
		_authenticationContext =
		new AutoResetThreadLocal<AuthenticationContext>(
			PortalAuthenticationManager.class + "._authenticationContext");

}
