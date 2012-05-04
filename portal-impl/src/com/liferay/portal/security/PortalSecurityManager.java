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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.auth.AuthSettingsUtil;
import com.liferay.portal.security.auth.AuthenticationConfig;
import com.liferay.portal.security.auth.AuthenticationContext;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Igor Spasic
 */
public class PortalSecurityManager {

	/**
	 * Temporary singleton.
	 */
	public static PortalSecurityManager getInstance() {
		if (_instance == null) {
			_instance = new PortalSecurityManager();
		}
		return _instance;
	}

	/**
	 * Checks is some method is allowed to be run.
	 * Throws {@link com.liferay.portal.SecureMethodInvocationException} if access is denied.
	 * If method is checked, current thread will be marked as
	 * local, to prevent further authentication.
	 */
	public void accept(Method method) {
		if (!isRemoteCall(method)) {
			return;
		}

		System.out.println("---> remote call, check method: " + method);

		checkAllowedHosts();

		checkPrivateMethod(method);

		REMOTE_FLAG.set(Boolean.FALSE);
	}

	/**
	 * Marks thread to be remote. Needed to be called by any API
	 * to turn on the access check.
	 * Or, even better, called inside filter, on one place.
	 */
	public void setRemoteAccess() {
		REMOTE_FLAG.set(Boolean.TRUE);
	}

	protected void checkAllowedHosts() {
		AuthenticationContext ctx = PortalAuthenticationManager.getInstance()
			.getAuthenticationContext();

		if(ctx == null){
			// TODO: PortalAuthenticationFilter & PortalAuthenticationManager is
			// not mapped to all URLs!!!!
			return;
		}

		AuthenticationConfig config = ctx.getAuthenticationConfig();

		Properties props = config.getSettings();
		if(Boolean.valueOf(
			props.getProperty("remote.hosts.enabled", "false"))){

			String[] hosts = StringUtil.split(
				props.getProperty("remote.hosts.allowed"));
			Set<String> hostsAllowed = new HashSet(Arrays.asList(hosts));

			HttpServletRequest request = ctx.getRequest();
			boolean allowed = AuthSettingsUtil.isAccessAllowed(request,
				hostsAllowed);

			if(!allowed){
				throw new RuntimeException("Access denied for " + request
					.getRemoteAddr());
			}
		}
	}

	protected boolean isRemoteCall(Method method){

		Boolean isRemote = REMOTE_FLAG.get();

		if (isRemote == null || !isRemote.booleanValue()) {
			return false;
		}

		// it's sufficient to check declaringClass because calls from
		// ServiceUtil use always interface methods only
		Class<?> declaringClass = method.getDeclaringClass();

		if (_localServiceInterfaces.contains(declaringClass)) {
			return false;
		}

		if (declaringClass.getName().endsWith(_LOCAL_SERVICE)) {
			_localServiceInterfaces.add(declaringClass);

			return false;
		}

		return true;
	}

	/**
	 * Lookups for method secure data.
	 */
	private SecureMethodData _lookupSecureData(Method method) {
		SecureMethodData secureMethodData = _secureMethodDataCache.get(method);

		if (secureMethodData != null) {
			return secureMethodData;
		}

		secureMethodData = new SecureMethodData();

		SecureMethod secureMethod = method.getAnnotation(SecureMethod.class);

		if (secureMethod != null) {
			secureMethodData.setAuthentication(secureMethod.authentication());
		}

		_secureMethodDataCache.put(method, secureMethodData);

		return secureMethodData;
	}

	private void checkPrivateMethod(Method method) {
		SecureMethodData secureMethodData = _lookupSecureData(method);

		Authentication requiredAuthentication =
			secureMethodData.getAuthentication();

		if (requiredAuthentication == Authentication.PRIVATE) {
			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if (permissionChecker == null || !permissionChecker.isSignedIn()) {
				throw new SecureMethodInvocationException(
					"Access denied, user not authenticated.");
			}
		}
	}

	private static PortalSecurityManager _instance;

	private static final ThreadLocal<Boolean> REMOTE_FLAG
		= new ThreadLocal<Boolean>() {

		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	private static final String _LOCAL_SERVICE = "LocalService";

	/**
	 * Cache for localService interfaces
	 */
	private Set<Class> _localServiceInterfaces = new HashSet<Class>();

	/**
	 * Cache for various method data, read from annotation/configuration files.
	 */
	private Map<Method, SecureMethodData> _secureMethodDataCache =
		new HashMap<Method, SecureMethodData>();

	private static class SecureMethodData {

		public Authentication getAuthentication() {
			return _authentication;
		}

		public void setAuthentication(Authentication authentication) {
			this._authentication = authentication;
		}

		private Authentication _authentication = Authentication.PRIVATE;
	}

}