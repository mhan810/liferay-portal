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

package com.liferay.portal.servlet.filters.inactiveportalinstance;

import com.liferay.portal.servlet.InactivePortalInstanceRequestHandler;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PortalInstances;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
public class InactivePortalInstanceFilter extends BasePortalFilter {

	@Override
	public void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		long companyId = PortalInstances.getCompanyId(request);

		if (InactivePortalInstanceRequestHandler.processCompanyInactiveRequest(
				request, response, companyId)) {

			return;
		}

		processFilter(
			InactivePortalInstanceFilter.class.getName(), request, response,
			filterChain);
	}

}