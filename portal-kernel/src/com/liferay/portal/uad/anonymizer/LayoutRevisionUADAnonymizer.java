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
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
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
	"model.class.name=" + PortalUADConstants.CLASS_NAME_LAYOUT_REVISION}, service = UADAnonymizer.class)
public class LayoutRevisionUADAnonymizer extends DynamicQueryUADAnonymizer<LayoutRevision> {
	@Override
	public void autoAnonymize(LayoutRevision layoutRevision, long userId)
		throws PortalException {
		User anonymousUser = _uadAnonymizerHelper.getAnonymousUser();

		if (layoutRevision.getUserId() == userId) {
			layoutRevision.setUserId(anonymousUser.getUserId());
			layoutRevision.setUserName(anonymousUser.getFullName());
		}

		if (layoutRevision.getStatusByUserId() == userId) {
			layoutRevision.setStatusByUserId(anonymousUser.getUserId());
			layoutRevision.setStatusByUserName(anonymousUser.getFullName());
		}

		_layoutRevisionLocalService.updateLayoutRevision(layoutRevision);
	}

	@Override
	public void delete(LayoutRevision layoutRevision) throws PortalException {
		_layoutRevisionLocalService.deleteLayoutRevision(layoutRevision);
	}

	@Override
	public List<String> getNonanonymizableFieldNames() {
		return Arrays.asList();
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return _layoutRevisionLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_LAYOUT_REVISION;
	}

	@Reference
	private LayoutRevisionLocalService _layoutRevisionLocalService;
	@Reference
	private UADAnonymizerHelper _uadAnonymizerHelper;
}