/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.security.pwd;

import com.liferay.portal.PwdEncryptorException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;

/**
 * @author Tomas Polesovsky
 */
public class UpgradePasswordEncryptor extends BasePasswordEncryptor {

	public UpgradePasswordEncryptor() {
		_passwordsEncryptionAlgorithmStrong =
			GetterUtil.getString(
				PropsUtil.get(PropsKeys.PASSWORDS_ENCRYPTION_ALGORITHM_STRONG)).
				toUpperCase();

		_passwordsEncryptionAlgorithmOld = "SHA";

		_passwordsEncryptionAlgorithm =
			GetterUtil.getString(
				PropsUtil.get(PropsKeys.PASSWORDS_ENCRYPTION_ALGORITHM)).
				toUpperCase();
	}

	public String getPasswordsEncryptionAlgorithm() {
		return _passwordsEncryptionAlgorithm;
	}

	public String getPasswordsEncryptionAlgorithmOld() {
		return _passwordsEncryptionAlgorithmOld;
	}

	public String getPasswordsEncryptionAlgorithmStrong() {
		return _passwordsEncryptionAlgorithmStrong;
	}

	public String[] getSupportedAlgorithmTypes() {
		return _parentPasswordEncryptor.getSupportedAlgorithmTypes();
	}

	public void setParentPasswordEncryptor(
		PasswordEncryptor defaultPasswordEncryptor) {

		_parentPasswordEncryptor = defaultPasswordEncryptor;
	}

	public void setPasswordsEncryptionAlgorithm(
		String passwordsEncryptionAlgorithm) {

		_passwordsEncryptionAlgorithm = passwordsEncryptionAlgorithm;
	}

	public void setPasswordsEncryptionAlgorithmOld(
		String passwordsEncryptionAlgorithmOld) {

		_passwordsEncryptionAlgorithmOld = passwordsEncryptionAlgorithmOld;
	}

	public void setPasswordsEncryptionAlgorithmStrong(
		String passwordsEncryptionAlgorithmStrong) {

		_passwordsEncryptionAlgorithmStrong =
			passwordsEncryptionAlgorithmStrong;
	}

	@Override
	protected String doEncrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		if (Validator.isNull(algorithm)) {
			algorithm = getAlgorithmAfterUpgrade(currentEncryptedPassword);
		}

		return _parentPasswordEncryptor.encrypt(
			algorithm, clearTextPassword, currentEncryptedPassword);
	}

	protected String getAlgorithmAfterUpgrade(String oldPassword) {

		// there isn't the old password - no backwards compatibility

		if (Validator.isNull(oldPassword)) {
			return getPasswordsEncryptionAlgorithmStrong();
		}

		// customer changed the default old algorithm

		String customDeprecatedAlgorithm = getPasswordsEncryptionAlgorithm();

		if (Validator.isNotNull(customDeprecatedAlgorithm)) {
			return customDeprecatedAlgorithm;
		}

		// use default old algorithm

		return getPasswordsEncryptionAlgorithmOld();
	}

	private PasswordEncryptor _parentPasswordEncryptor;

	private String _passwordsEncryptionAlgorithm;

	private String _passwordsEncryptionAlgorithmOld;

	private String _passwordsEncryptionAlgorithmStrong;

}