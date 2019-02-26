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

package com.liferay.digital.signature.request;

import aQute.bnd.annotation.ProviderType;

import java.util.Objects;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSSessionId {

	public DSSessionId(long companyId, String userId, String accountId) {
		_companyId = companyId;
		_userId = userId;
		_accountId = accountId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if ((object == null) || (getClass() != object.getClass())) {
			return false;
		}

		final DSSessionId dsSessionId = (DSSessionId)object;

		if (getCompanyId() != dsSessionId.getCompanyId()) {
			return false;
		}

		if (!Objects.equals(getAccountId(), dsSessionId.getAccountId())) {
			return false;
		}

		if (!Objects.equals(getUserId(), dsSessionId.getUserId())) {
			return false;
		}

		return true;
	}

	public String getAccountId() {
		return _accountId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_companyId, _accountId, _userId);
	}

	private final String _accountId;
	private final long _companyId;
	private final String _userId;

}