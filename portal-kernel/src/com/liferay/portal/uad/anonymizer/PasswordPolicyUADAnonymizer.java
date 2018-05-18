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
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;
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
	"model.class.name=" + PortalUADConstants.CLASS_NAME_PASSWORD_POLICY}, service = UADAnonymizer.class)
public class PasswordPolicyUADAnonymizer extends DynamicQueryUADAnonymizer<PasswordPolicy> {
	@Override
	public void autoAnonymize(PasswordPolicy passwordPolicy, long userId)
		throws PortalException {
		User anonymousUser = _uadAnonymizerHelper.getAnonymousUser();

		if (passwordPolicy.getUserId() == userId) {
			passwordPolicy.setUserId(anonymousUser.getUserId());
			passwordPolicy.setUserName(anonymousUser.getFullName());
		}

		_passwordPolicyLocalService.updatePasswordPolicy(passwordPolicy);
	}

	@Override
	public void delete(PasswordPolicy passwordPolicy) throws PortalException {
		_passwordPolicyLocalService.deletePasswordPolicy(passwordPolicy);
	}

	@Override
	public List<String> getNonanonymizableFieldNames() {
		return Arrays.asList();
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return _passwordPolicyLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return PortalUADConstants.USER_ID_FIELD_NAMES_PASSWORD_POLICY;
	}

	@Reference
	private PasswordPolicyLocalService _passwordPolicyLocalService;
	@Reference
	private UADAnonymizerHelper _uadAnonymizerHelper;
}