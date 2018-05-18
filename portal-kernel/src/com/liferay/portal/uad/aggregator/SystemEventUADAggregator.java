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

package com.liferay.portal.uad.aggregator;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEvent;
import com.liferay.portal.kernel.service.SystemEventLocalService;
import com.liferay.portal.uad.constants.PortalUADConstants;

import com.liferay.user.associated.data.aggregator.DynamicQueryUADAggregator;
import com.liferay.user.associated.data.aggregator.UADAggregator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(immediate = true, property =  {
	"model.class.name=" + PortalUADConstants.CLASS_NAME_SYSTEM_EVENT}, service = UADAggregator.class)
public class SystemEventUADAggregator extends DynamicQueryUADAggregator<SystemEvent> {
	@Override
	public String getApplicationName() {
		return PortalUADConstants.APPLICATION_NAME;
	}

	@Override
	public SystemEvent get(Serializable primaryKey) throws PortalException {
		return _systemEventLocalService.getSystemEvent(Long.valueOf(
				primaryKey.toString()));
	}

	@Override
	protected long doCount(DynamicQuery dynamicQuery) {
		return _systemEventLocalService.dynamicQueryCount(dynamicQuery);
	}

	@Override
	protected DynamicQuery doGetDynamicQuery() {
		return _systemEventLocalService.dynamicQuery();
	}

	@Override
	protected List<SystemEvent> doGetRange(DynamicQuery dynamicQuery,
		int start, int end) {
		return _systemEventLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_SYSTEM_EVENT;
	}

	@Reference
	private SystemEventLocalService _systemEventLocalService;
}