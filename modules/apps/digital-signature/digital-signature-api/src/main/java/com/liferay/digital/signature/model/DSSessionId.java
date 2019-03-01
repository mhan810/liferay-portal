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

package com.liferay.digital.signature.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringBundler;

import java.util.Objects;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSSessionId {

	public DSSessionId(long companyId, String userName, String accountId) {
		_companyId = companyId;
		_userName = userName;
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

		if (!Objects.equals(getUserName(), dsSessionId.getUserName())) {
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

	public String getUserName() {
		return _userName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_companyId, _accountId, _userName);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{className=");

		Class<?> clazz = getClass();

		sb.append(clazz.getSimpleName());

		sb.append(", accountId=");
		sb.append(_accountId);
		sb.append(", companyId=");
		sb.append(_companyId);
		sb.append(", userName=");
		sb.append(_userName);
		sb.append("}");

		return sb.toString();
	}

	private final String _accountId;
	private final long _companyId;
	private final String _userName;

}