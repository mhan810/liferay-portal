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
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.nio.ByteBuffer;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

		if (Validator.isNull(clearTextPassword)) {
			throw new PwdEncryptorException(
				"Unable to encrypt empty passwords.");
		}

		if (algorithm.startsWith(PwdEncryptor.TYPE_BCRYPT)) {
			return encryptUsingBCrypt(
				algorithm, clearTextPassword, currentEncryptedPassword);
		}

		if (algorithm.startsWith(PwdEncryptor.TYPE_PBKDF2)) {
			return encryptUsingPBKDF2(
				algorithm, clearTextPassword, currentEncryptedPassword);
		}

		return _parent.encrypt(
			algorithm, clearTextPassword, currentEncryptedPassword);
	}

	protected String encryptUsingBCrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		String salt;

		if (Validator.isNull(currentEncryptedPassword)) {
			int rounds = _DEFAULT_BCRYPT_ROUNDS;

			Matcher algorithmRoundsMatcher = _BCRYPT_PATTERN.matcher(algorithm);

			if (algorithmRoundsMatcher.matches()) {
				rounds = GetterUtil.getInteger(
					algorithmRoundsMatcher.group(1), rounds);
			}

			salt = BCrypt.gensalt(rounds);
		}
		else {
			salt = currentEncryptedPassword.substring(0, 29);
		}

		return BCrypt.hashpw(clearTextPassword, salt);
	}

	protected String encryptUsingPBKDF2(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		byte[] salt = new byte[_DEFAULT_PBKDF2_SALT_SIZE];
		int keySize = _DEFAULT_PBKDF2_KEY_SIZE;
		int rounds = _DEFAULT_PBKDF2_ROUNDS;

		if (Validator.isNull(currentEncryptedPassword)) {
			Matcher pbkdf2Matcher = _PBKDF2_PATTERN.matcher(algorithm);

			if (pbkdf2Matcher.matches()) {
				keySize = GetterUtil.getInteger(
					pbkdf2Matcher.group(1), _DEFAULT_PBKDF2_KEY_SIZE);

				rounds = GetterUtil.getInteger(
					pbkdf2Matcher.group(2), _DEFAULT_PBKDF2_ROUNDS);
			}

			SecureRandom random = new SecureRandom();
			random.nextBytes(salt);
		}
		else {
			byte[] configAndSalt = new byte[16];
			try {
				byte[] passwordBytes = Base64.decode(currentEncryptedPassword);

				System.arraycopy(
					passwordBytes, 0, configAndSalt, 0, configAndSalt.length);
			}
			catch (Exception e) {
				throw new PwdEncryptorException(
					"Unable to extract salt from encrypted password: " +
						e.getMessage());
			}

			ByteBuffer buff = ByteBuffer.wrap(configAndSalt);
			keySize = buff.getInt();
			rounds = buff.getInt();
			buff.get(salt);
		}

		PBEKeySpec keySpec = new PBEKeySpec(
			clearTextPassword.toCharArray(), salt, rounds, keySize);

		byte[] key;
		try {
			String algorithmName = algorithm;
			int slashIndex = algorithm.indexOf(CharPool.SLASH);
			if (slashIndex > -1) {
				algorithmName = algorithm.substring(0, slashIndex);
			}

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(
				algorithmName);

			key = keyFactory.generateSecret(keySpec).getEncoded();
		} catch (InvalidKeySpecException e) {
			throw new PwdEncryptorException(
				"Unable to generate hash: " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new PwdEncryptorException(
				"Unable to generate hash: " + e.getMessage(), e);
		}

		ByteBuffer result = ByteBuffer.allocate(2*4 + salt.length + key.length);
		result.putInt(keySize);
		result.putInt(rounds);
		result.put(salt);
		result.put(key);
		return Base64.encode(result.array());
	}

	private static final Pattern _BCRYPT_PATTERN = Pattern.compile(
		"^BCrypt/([0-9]+)$", Pattern.CASE_INSENSITIVE);
	private static final int _DEFAULT_BCRYPT_ROUNDS = 10;
	private static final int _DEFAULT_PBKDF2_KEY_SIZE = 160;
	private static final int _DEFAULT_PBKDF2_ROUNDS = 128000;
	private static final int _DEFAULT_PBKDF2_SALT_SIZE = 8;
	private static final Pattern _PBKDF2_PATTERN = Pattern.compile(
		"^.*/?([0-9]+)?/([0-9]+)$");

	private PasswordEncryptor _parent;

}