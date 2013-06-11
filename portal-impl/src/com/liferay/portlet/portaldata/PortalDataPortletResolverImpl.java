/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.portaldata;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalServiceUtil;

import java.util.Map;

/**
 * @author Edward C. Han
 */
public class PortalDataPortletResolverImpl
	implements PortalDataPortletResolver {

	public void addTypePortlet(Class type, String portletId) {
		_typePortletMap.put(type, portletId);
	}

	public Portlet resolvePortlet(long companyId, Class type)
		throws SystemException {

		String portletId = _typePortletMap.get(type);

		return PortletLocalServiceUtil.getPortletById(companyId, portletId);
	}

	public void setTypePortletMap(Map<Class, String> typePortletMap) {
		_typePortletMap = typePortletMap;
	}

	private Map<Class, String> _typePortletMap;
}