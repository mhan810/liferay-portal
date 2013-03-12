/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.UnsupportedEncodingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.BCrypt;

/**
 * @author Tomas Polesovsky
 */
public class PasswordEncryptorImpl implements PasswordEncryptor {

	public PasswordEncryptorImpl() {
		_parent = new PasswordEncryptorOldImpl();
	}

	public String encrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		if (algorithm.startsWith(PwdEncryptor.TYPE_BCRYPT)) {
			return encryptUsingBCrypt(
				algorithm, clearTextPassword, currentEncryptedPassword);
		}

		return _parent.encrypt(
			algorithm, clearTextPassword, currentEncryptedPassword);
	}

	protected String encryptUsingBCrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		byte[] saltBytes = _getSaltFromBCrypt(
			algorithm, currentEncryptedPassword);

		String salt = new String(saltBytes);

		return BCrypt.hashpw(clearTextPassword, salt);
	}

	private byte[] _getSaltFromBCrypt(String algorithm, String bcryptString)
		throws PwdEncryptorException {

		byte[] saltBytes = null;

		try {
			if (Validator.isNull(bcryptString)) {
				int rounds = _DEFAULT_BCRYPT_ROUNDS;

				Matcher algorithmRoundsMatcher = _algorithmRounds.matcher(
					algorithm);

				if (algorithmRoundsMatcher.matches()) {
					rounds = GetterUtil.getInteger(
						algorithmRoundsMatcher.group(1), rounds);
				}

				String salt = BCrypt.gensalt(rounds);

				saltBytes = salt.getBytes(StringPool.UTF8);
			}
			else {
				String salt = bcryptString.substring(0, 29);

				saltBytes = salt.getBytes(StringPool.UTF8);
			}
		}
		catch (UnsupportedEncodingException uee) {
			throw new PwdEncryptorException(
				"Unable to extract salt from encrypted password: " +
					uee.getMessage());
		}

		return saltBytes;
	}

	private static final Pattern _algorithmRounds = Pattern.compile(
		"^.*/([0-9]+)$");

	private static final int _DEFAULT_BCRYPT_ROUNDS = 10;

	private PasswordEncryptor _parent;

}