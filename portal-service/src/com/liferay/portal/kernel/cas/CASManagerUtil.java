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

package com.liferay.portal.kernel.cas;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Edward Han
 */
public class CASManagerUtil {

	public static void deleteCASServer(long companyId, String serverId)
		throws PortalException, SystemException {

		_portalCASManager.deleteCASServer(companyId, serverId);
	}

	public static CASServer getCASServer(long companyId, String casServerId) {
		return _portalCASManager.getCASServer(companyId, casServerId);
	}

	public static String[] getCASServerIds(long companyId) {
		return _portalCASManager.getCASServerIds(companyId);
	}

	public static Map<String, CASServer> getCASServers(long companyId) {
		return _portalCASManager.getCASServers(companyId);
	}

	public static CASServer getDefaultCASServer(long companyId) {
		return _portalCASManager.getDefaultCASServer(companyId);
	}

	public static String getLoginUrl(
			HttpServletRequest request, HttpServletResponse response,
			CASServer casServer) {

		return _portalCASManager.getLoginUrl(request, response, casServer);
	}

	public static String getServiceUrl(
			HttpServletRequest request, HttpServletResponse response,
			CASServer casServer) {

		return _portalCASManager.getServiceUrl(request, response, casServer);
	}

	public static void updateCASServer(
			long companyId, String casServerId, UnicodeProperties properties,
			boolean add)
		throws PortalException, SystemException {

		_portalCASManager.updateCASServer(
			companyId, casServerId, properties, add);
	}

	public void setPortalCASManager(CASManager portalCASManager) {
		_portalCASManager = portalCASManager;
	}

	private static CASManager _portalCASManager;

}