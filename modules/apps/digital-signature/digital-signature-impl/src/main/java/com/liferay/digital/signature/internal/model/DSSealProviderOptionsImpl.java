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

package com.liferay.digital.signature.internal.model;

import com.liferay.digital.signature.model.DSSealProviderOptions;

/**
 * @author Michael C. Han
 */
public class DSSealProviderOptionsImpl implements DSSealProviderOptions {

	@Override
	public String getMobilePhoneNumber() {
		return _mobilePhoneNumber;
	}

	@Override
	public String getOneTimePassword() {
		return _oneTimePassword;
	}

	@Override
	public String getRole() {
		return _role;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		_mobilePhoneNumber = mobilePhoneNumber;
	}

	public void setOneTimePassword(String oneTimePassword) {
		_oneTimePassword = oneTimePassword;
	}

	public void setRole(String role) {
		_role = role;
	}

	private String _mobilePhoneNumber;
	private String _oneTimePassword;
	private String _role;

}