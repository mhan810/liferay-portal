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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.ClassLoaderUtil;
import com.liferay.portal.util.PropsUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brian Wing Shun Chan
 * @author Scott Lee
 */
public class PwdEncryptor {

	public static final String PASSWORDS_ENCRYPTION_ALGORITHM_LIFERAY =
		GetterUtil.getString(
			PropsUtil.get(
				PropsKeys.PASSWORDS_ENCRYPTION_ALGORITHM_LIFERAY))
			.toUpperCase();

	public static final String PASSWORDS_ENCRYPTION_ALGORITHM_OLD = "SHA";

	public static final char[] SALT_CHARS =
		"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./"
			.toCharArray();

	public static final String TYPE_BCRYPT = "BCRYPT";

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #TYPE_UFC_CRYPT}
	 */
	public static final String TYPE_CRYPT = "CRYPT";

	public static final String TYPE_MD2 = "MD2";

	public static final String TYPE_MD5 = "MD5";

	public static final String TYPE_NONE = "NONE";

	public static final String TYPE_PBKDF2 = "PBKDF2";

	public static final String TYPE_SHA = "SHA";

	public static final String TYPE_SHA_256 = "SHA-256";

	public static final String TYPE_SHA_384 = "SHA-384";

	public static final String TYPE_SSHA = "SSHA";

	public static final String TYPE_UFC_CRYPT = "UFC-CRYPT";

	public static String PASSWORDS_ENCRYPTION_ALGORITHM =
		GetterUtil.getString(
			PropsUtil.get(
				PropsKeys.PASSWORDS_ENCRYPTION_ALGORITHM)).toUpperCase();

	public static String encrypt(String clearTextPassword)
		throws PwdEncryptorException {

		return encrypt(null, clearTextPassword, null);
	}

	public static String encrypt(
			String clearTextPassword, String currentEncryptedPassword)
		throws PwdEncryptorException {

		return encrypt(null, clearTextPassword, currentEncryptedPassword);
	}

	public static String encrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		_initialize();

		if (Validator.isNull(algorithm)) {
			algorithm = getAlgorithmAfterUpgrade(currentEncryptedPassword);
		}

		boolean algorithmInsideHash = false;

		if (Validator.isNull(currentEncryptedPassword)) {
			algorithmInsideHash = true;
		}
		else {
			Matcher m = _PASSWORD_WITH_ALGORITHM.matcher(
				currentEncryptedPassword);

			if (m.matches()) {
				algorithmInsideHash = true;
				algorithm = m.group(1);
				currentEncryptedPassword = m.group(2);
			}
		}

		String hash = _passwordEncryptor.encrypt(
			algorithm, clearTextPassword, currentEncryptedPassword);

		if (!algorithmInsideHash) {
			return hash;
		}

		StringBuilder result = new StringBuilder(4);
		result.append(StringPool.OPEN_CURLY_BRACE);
		result.append(getAlgorithmName(algorithm));
		result.append(StringPool.CLOSE_CURLY_BRACE);
		result.append(hash);

		return result.toString();
	}

	protected static String getAlgorithmAfterUpgrade(String oldPassword) {

		// there isn't the old password - no backwards compatibility

		if (Validator.isNull(oldPassword)) {
			return PASSWORDS_ENCRYPTION_ALGORITHM_LIFERAY;
		}

		// customer changed the default old algorithm

		String customDeprecatedAlgorithm = PASSWORDS_ENCRYPTION_ALGORITHM;

		if (Validator.isNotNull(customDeprecatedAlgorithm)) {
			return customDeprecatedAlgorithm;
		}

		// use default old algorithm

		return PASSWORDS_ENCRYPTION_ALGORITHM_OLD;
	}

	protected static String getAlgorithmName(String algorithm) {
		int slashIndex = algorithm.indexOf(CharPool.SLASH);
		if (slashIndex > 0) {
			return algorithm.substring(0, slashIndex);
		}

		return algorithm;
	}

	private static void _initialize() throws PwdEncryptorException {
		if (_passwordEncryptor != null) {
			return;
		}

		synchronized (PwdEncryptor.class) {
			if (_passwordEncryptor != null) {
				return;
			}

			try {
				String passwordEncryptorClassName = PropsUtil.get(
					PropsKeys.PASSWORDS_ENCRYPTOR);

				_passwordEncryptor =
					(PasswordEncryptor)InstanceFactory.newInstance(
						ClassLoaderUtil.getPortalClassLoader(),
						passwordEncryptorClassName);

			}
			catch (Exception e) {
				throw new PwdEncryptorException(
					"Unable to initialize encryptor " + e.getMessage(), e);
			}
		}
	}

	private static final Pattern _PASSWORD_WITH_ALGORITHM = Pattern.compile(
		"^\\{(.+)\\}(.+)$");

	private static Log _log = LogFactoryUtil.getLog(PwdEncryptor.class);

	private static PasswordEncryptor _passwordEncryptor;

}