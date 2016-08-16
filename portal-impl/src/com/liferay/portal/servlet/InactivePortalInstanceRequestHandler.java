/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalInstances;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
public class InactivePortalInstanceRequestHandler
	extends InactiveRequestHandler {

	public static boolean processCompanyInactiveRequest(
			HttpServletRequest request, HttpServletResponse response,
			long companyId)
		throws IOException {

		if (PortalInstances.isCompanyActive(companyId)) {
			return false;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		processInactiveRequest(
			request, response,
			"this-instance-is-inactive-please-contact-the-administrator");

		if (_log.isDebugEnabled()) {
			_log.debug("Processed company inactive request");
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InactivePortalInstanceRequestHandler.class);

}