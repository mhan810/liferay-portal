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
import com.liferay.portal.kernel.model.RepositoryEntry;
import com.liferay.portal.kernel.service.RepositoryEntryLocalService;
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
	"model.class.name=" + PortalUADConstants.CLASS_NAME_REPOSITORY_ENTRY}, service = UADAggregator.class)
public class RepositoryEntryUADAggregator extends DynamicQueryUADAggregator<RepositoryEntry> {
	@Override
	public String getApplicationName() {
		return PortalUADConstants.APPLICATION_NAME;
	}

	@Override
	public RepositoryEntry get(Serializable primaryKey)
		throws PortalException {
		return _repositoryEntryLocalService.getRepositoryEntry(Long.valueOf(
				primaryKey.toString()));
	}

	@Override
	protected long doCount(DynamicQuery dynamicQuery) {
		return _repositoryEntryLocalService.dynamicQueryCount(dynamicQuery);
	}

	@Override
	protected DynamicQuery doGetDynamicQuery() {
		return _repositoryEntryLocalService.dynamicQuery();
	}

	@Override
	protected List<RepositoryEntry> doGetRange(DynamicQuery dynamicQuery,
		int start, int end) {
		return _repositoryEntryLocalService.dynamicQuery(dynamicQuery, start,
			end);
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_REPOSITORY_ENTRY;
	}

	@Reference
	private RepositoryEntryLocalService _repositoryEntryLocalService;
}