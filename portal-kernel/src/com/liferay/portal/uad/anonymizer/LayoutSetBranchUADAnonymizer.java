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

package com.liferay.portal.uad.anonymizer;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutSetBranch;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutSetBranchLocalService;
import com.liferay.portal.uad.constants.PortalUADConstants;

import com.liferay.user.associated.data.anonymizer.DynamicQueryUADAnonymizer;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.util.UADAnonymizerHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(immediate = true, property =  {
	"model.class.name=" + PortalUADConstants.CLASS_NAME_LAYOUT_SET_BRANCH}, service = UADAnonymizer.class)
public class LayoutSetBranchUADAnonymizer extends DynamicQueryUADAnonymizer<LayoutSetBranch> {
	@Override
	public void autoAnonymize(LayoutSetBranch layoutSetBranch, long userId)
		throws PortalException {
		User anonymousUser = _uadAnonymizerHelper.getAnonymousUser();

		if (layoutSetBranch.getUserId() == userId) {
			layoutSetBranch.setUserId(anonymousUser.getUserId());
			layoutSetBranch.setUserName(anonymousUser.getFullName());
		}

		_layoutSetBranchLocalService.updateLayoutSetBranch(layoutSetBranch);
	}

	@Override
	public void delete(LayoutSetBranch layoutSetBranch)
		throws PortalException {
		_layoutSetBranchLocalService.deleteLayoutSetBranch(layoutSetBranch);
	}

	@Override
	public List<String> getNonanonymizableFieldNames() {
		return Arrays.asList();
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return _layoutSetBranchLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_LAYOUT_SET_BRANCH;
	}

	@Reference
	private LayoutSetBranchLocalService _layoutSetBranchLocalService;
	@Reference
	private UADAnonymizerHelper _uadAnonymizerHelper;
}