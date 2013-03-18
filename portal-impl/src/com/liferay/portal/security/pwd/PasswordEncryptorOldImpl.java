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
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Random;

import jodd.util.BCrypt;

import org.vps.crypt.Crypt;

/**
 * @author Tomas Polesovsky
 */
public class PasswordEncryptorOldImpl implements PasswordEncryptor {

	public String encrypt(
			String algorithm, String clearTextPassword,
			String currentEncryptedPassword)
		throws PwdEncryptorException {

		if (algorithm.equals(PwdEncryptor.TYPE_BCRYPT)) {
			byte[] saltBytes = _getSaltFromBCrypt(currentEncryptedPassword);

			return encodePassword(algorithm, clearTextPassword, saltBytes);
		}
		else if (algorithm.equals(PwdEncryptor.TYPE_CRYPT) ||
				 algorithm.equals(PwdEncryptor.TYPE_UFC_CRYPT)) {

			byte[] saltBytes = _getSaltFromCrypt(currentEncryptedPassword);

			return encodePassword(algorithm, clearTextPassword, saltBytes);
		}
		else if (algorithm.equals(PwdEncryptor.TYPE_NONE)) {
			return clearTextPassword;
		}
		else if (algorithm.equals(PwdEncryptor.TYPE_SSHA)) {
			byte[] saltBytes = _getSaltFromSSHA(currentEncryptedPassword);

			return encodePassword(algorithm, clearTextPassword, saltBytes);
		}
		else {
			return encodePassword(algorithm, clearTextPassword, null);
		}
	}

	protected String encodePassword(
			String algorithm, String clearTextPassword, byte[] saltBytes)
		throws PwdEncryptorException {

		try {
			if (algorithm.equals(PwdEncryptor.TYPE_BCRYPT)) {
				String salt = new String(saltBytes);

				return BCrypt.hashpw(clearTextPassword, salt);
			}
			else if (algorithm.equals(PwdEncryptor.TYPE_CRYPT) ||
					algorithm.equals(PwdEncryptor.TYPE_UFC_CRYPT)) {

				return Crypt.crypt(
					saltBytes, clearTextPassword.getBytes(Digester.ENCODING));
			}
			else if (algorithm.equals(PwdEncryptor.TYPE_SSHA)) {
				byte[] clearTextPasswordBytes = clearTextPassword.getBytes(
					Digester.ENCODING);

				// Create a byte array of salt bytes appended to password bytes

				byte[] pwdPlusSalt =
					new byte[clearTextPasswordBytes.length + saltBytes.length];

				System.arraycopy(
					clearTextPasswordBytes, 0, pwdPlusSalt, 0,
					clearTextPasswordBytes.length);

				System.arraycopy(
					saltBytes, 0, pwdPlusSalt, clearTextPasswordBytes.length,
					saltBytes.length);

				// Digest byte array

				MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");

				byte[] pwdPlusSaltHash = sha1Digest.digest(pwdPlusSalt);

				// Appends salt bytes to the SHA-1 digest.

				byte[] digestPlusSalt =
					new byte[pwdPlusSaltHash.length + saltBytes.length];

				System.arraycopy(
					pwdPlusSaltHash, 0, digestPlusSalt, 0,
					pwdPlusSaltHash.length);

				System.arraycopy(
					saltBytes, 0, digestPlusSalt, pwdPlusSaltHash.length,
					saltBytes.length);

				// Base64 encode and format string

				return Base64.encode(digestPlusSalt);
			}
			else {
				return DigesterUtil.digest(algorithm, clearTextPassword);
			}
		}
		catch (NoSuchAlgorithmException nsae) {
			throw new PwdEncryptorException(nsae.getMessage());
		}
		catch (UnsupportedEncodingException uee) {
			throw new PwdEncryptorException(uee.getMessage());
		}
	}

	private byte[] _getSaltFromBCrypt(String bcryptString)
		throws PwdEncryptorException {

		byte[] saltBytes = null;

		try {
			if (Validator.isNull(bcryptString)) {
				String salt = BCrypt.gensalt();

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

	private byte[] _getSaltFromCrypt(String cryptString)
		throws PwdEncryptorException {

		byte[] saltBytes = null;

		try {
			if (Validator.isNull(cryptString)) {

				// Generate random salt

				Random random = new Random();

				int numSaltChars = PwdEncryptor.SALT_CHARS.length;

				StringBuilder sb = new StringBuilder();

				int x = random.nextInt(Integer.MAX_VALUE) % numSaltChars;
				int y = random.nextInt(Integer.MAX_VALUE) % numSaltChars;

				sb.append(PwdEncryptor.SALT_CHARS[x]);
				sb.append(PwdEncryptor.SALT_CHARS[y]);

				String salt = sb.toString();

				saltBytes = salt.getBytes(Digester.ENCODING);
			}
			else {

				// Extract salt from encrypted password

				String salt = cryptString.substring(0, 2);

				saltBytes = salt.getBytes(Digester.ENCODING);
			}
		}
		catch (UnsupportedEncodingException uee) {
			throw new PwdEncryptorException(
				"Unable to extract salt from encrypted password: " +
					uee.getMessage());
		}

		return saltBytes;
	}

	private byte[] _getSaltFromSSHA(String sshaString)
		throws PwdEncryptorException {

		byte[] saltBytes = new byte[8];

		if (Validator.isNull(sshaString)) {

			// Generate random salt

			Random random = new SecureRandom();

			random.nextBytes(saltBytes);
		}
		else {

			// Extract salt from encrypted password

			try {
				byte[] digestPlusSalt = Base64.decode(sshaString);
				byte[] digestBytes = new byte[digestPlusSalt.length - 8];

				System.arraycopy(
					digestPlusSalt, 0, digestBytes, 0, digestBytes.length);

				System.arraycopy(
					digestPlusSalt, digestBytes.length, saltBytes, 0,
					saltBytes.length);
			}
			catch (Exception e) {
				throw new PwdEncryptorException(
					"Unable to extract salt from encrypted password: " +
						e.getMessage());
			}
		}

		return saltBytes;
	}

}