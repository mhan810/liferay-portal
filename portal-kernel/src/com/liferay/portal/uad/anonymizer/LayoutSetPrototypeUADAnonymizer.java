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
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutSetPrototypeLocalService;
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
	"model.class.name=" + PortalUADConstants.CLASS_NAME_LAYOUT_SET_PROTOTYPE}, service = UADAnonymizer.class)
public class LayoutSetPrototypeUADAnonymizer extends DynamicQueryUADAnonymizer<LayoutSetPrototype> {
	@Override
	public void autoAnonymize(LayoutSetPrototype layoutSetPrototype, long userId)
		throws PortalException {
		User anonymousUser = _uadAnonymizerHelper.getAnonymousUser();

		if (layoutSetPrototype.getUserId() == userId) {
			layoutSetPrototype.setUserId(anonymousUser.getUserId());
			layoutSetPrototype.setUserName(anonymousUser.getFullName());
		}

		_layoutSetPrototypeLocalService.updateLayoutSetPrototype(layoutSetPrototype);
	}

	@Override
	public void delete(LayoutSetPrototype layoutSetPrototype)
		throws PortalException {
		_layoutSetPrototypeLocalService.deleteLayoutSetPrototype(layoutSetPrototype);
	}

	@Override
	public List<String> getNonanonymizableFieldNames() {
		return Arrays.asList();
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return _layoutSetPrototypeLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_LAYOUT_SET_PROTOTYPE;
	}

	@Reference
	private LayoutSetPrototypeLocalService _layoutSetPrototypeLocalService;
	@Reference
	private UADAnonymizerHelper _uadAnonymizerHelper;
}