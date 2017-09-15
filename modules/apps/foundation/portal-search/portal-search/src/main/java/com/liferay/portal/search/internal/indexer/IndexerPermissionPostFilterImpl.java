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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.search.indexer.IndexerPermissionPostFilter;
import com.liferay.portal.search.spi.model.result.contributor.ModelPermissionPostFilter;

/**
 * @author Michael C. Han
 */
public class IndexerPermissionPostFilterImpl
	implements IndexerPermissionPostFilter {

	public IndexerPermissionPostFilterImpl(
		ModelPermissionPostFilter modelPermissionPostFilter) {

		_modelPermissionPostFilter = modelPermissionPostFilter;
	}

	@Override
	public boolean hasPermission(
		PermissionChecker permissionChecker, String entryClassName,
		long entryClassPK, String actionId) {

		if (_modelPermissionPostFilter != null) {
			return _modelPermissionPostFilter.hasPermission(
				permissionChecker, entryClassName, entryClassPK, actionId);
		}

		return true;
	}

	@Override
	public boolean isPermissionAware() {
		if (_modelPermissionPostFilter != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isVisible(long classPK, int status) {
		if (_modelPermissionPostFilter != null) {
			return _modelPermissionPostFilter.isVisible(classPK, status);
		}

		return true;
	}

	private final ModelPermissionPostFilter _modelPermissionPostFilter;

}