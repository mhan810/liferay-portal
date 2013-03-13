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

import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.DigesterImpl;
import com.liferay.portal.util.PropsUtil;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Tomas Polesovsky
 */
@PowerMockIgnore({"javax.crypto.*", "javax.xml.*" })
@PrepareForTest({PropsUtil.class})
@RunWith(PowerMockRunner.class)
public class PwdEncryptorTest extends PowerMockito {

	@BeforeClass
	public static void init() {
		new DigesterUtil().setDigester(new DigesterImpl());
	}

	@Test
	public void testEdgeCases() throws Exception {
		testFail(
			"Some nonexistent algorithm", StringPool.BLANK, StringPool.BLANK);
	}

	@Test
	public void testEncryptBCrypt() throws Exception {
		String algorithm = PwdEncryptor.TYPE_BCRYPT;
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"$2a$10$/ST7LsB.7AAHsn/tlK6hr.nudQaBbJhPX9KfRSSzsn.1ij45lVzaK");
		testUpgradeAlgorithm(
			algorithm, "password",
			"$2a$10$/ST7LsB.7AAHsn/tlK6hr.nudQaBbJhPX9KfRSSzsn.1ij45lVzaK");
	}

	@Test
	public void testEncryptBCryptWith10Rounds() throws Exception {
		String algorithm = PwdEncryptor.TYPE_BCRYPT + "/10";
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"$2a$10$/ST7LsB.7AAHsn/tlK6hr.nudQaBbJhPX9KfRSSzsn.1ij45lVzaK");
	}

	@Test
	public void testEncryptBCryptWith12Rounds() throws Exception {
		String algorithm = PwdEncryptor.TYPE_BCRYPT + "/12";
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"$2a$12$2dD/NrqCEBlVgFEkkFCbzOll2a9vrdl8tTTqGosm26wJK1eCtsjnO");
	}

	@Test
	public void testEncryptCRYPT() throws Exception {
		String algorithm = PwdEncryptor.TYPE_CRYPT;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "SNbUMVY9kKQpY");
		testUpgradeAlgorithm(algorithm, "password", "SNbUMVY9kKQpY");
	}

	@Test
	public void testEncryptMD2() throws Exception {
		String algorithm = PwdEncryptor.TYPE_MD2;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "8DiBqIxuORNfDsxg79YJuQ==");
		testUpgradeAlgorithm(algorithm, "password", "8DiBqIxuORNfDsxg79YJuQ==");
	}

	@Test
	public void testEncryptMD5() throws Exception {
		String algorithm = PwdEncryptor.TYPE_MD5;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "X03MO1qnZdYdgyfeuILPmQ==");
		testUpgradeAlgorithm(algorithm, "password", "X03MO1qnZdYdgyfeuILPmQ==");
	}

	@Test
	public void testEncryptNONE() throws Exception {
		String algorithm = PwdEncryptor.TYPE_NONE;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "password");
		testUpgradeAlgorithm(algorithm, "password", "password");
	}

	@Test
	public void testEncryptPBKDF2() throws Exception {
		String algorithm = "PBKDF2WithHmacSHA1";
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"AAAAoAAB9ADJZ16OuMAPPHe2CUbP0HPyXvagoKHumh7iHU3c");
	}

	@Test
	public void testEncryptPBKDF2With50000Rounds() throws Exception {
		String algorithm = "PBKDF2WithHmacSHA1/50000";
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"AAAAoAAAw1B+jxO3UiVsWdBk4B9xGd/Ko3GKHW2afYhuit49");
	}

	@Test
	public void testEncryptPBKDF2With50000RoundsAnd128Key() throws Exception {
		String algorithm = "PBKDF2WithHmacSHA1/128/50000";
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"AAAAoAAAw1AbW1e1Str9wSLWIX5X9swLn+j5/5+m6auSPdva");
	}

	@Test
	public void testEncryptSHA() throws Exception {
		String algorithm = PwdEncryptor.TYPE_SHA;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
		testUpgradeAlgorithm(
			algorithm, "password", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
	}

	@Test
	public void testEncryptSHA1() throws Exception {
		String algorithm = "SHA-1";
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
		testUpgradeAlgorithm(
			algorithm, "password", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");
	}

	@Test
	public void testEncryptSHA256() throws Exception {
		String algorithm = PwdEncryptor.TYPE_SHA_256;
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=");
		testUpgradeAlgorithm(
			algorithm, "password",
			"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=");
	}

	@Test
	public void testEncryptSHA384() throws Exception {
		String algorithm = PwdEncryptor.TYPE_SHA_384;
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password",
			"qLZLq9CsqRpZvbt3YbQh1PK7OCgNOnW6DyHyvrxFWD1EbFmGYMl" +
				"M5oDEfRnDB4On");
		testUpgradeAlgorithm(
			algorithm, "password",
			"qLZLq9CsqRpZvbt3YbQh1PK7OCgNOnW6DyHyvrxFWD1EbFmGYMl" +
				"M5oDEfRnDB4On");
	}

	@Test
	public void testEncryptSSHA() throws Exception {
		String algorithm = PwdEncryptor.TYPE_SSHA;
		testAlgorithm(algorithm);
		testAlgorithm(
			algorithm, "password", "2EWEKeVpSdd79PkTX5vaGXH5uQ028Smy/H1NmA==");
		testUpgradeAlgorithm(
			algorithm, "password", "2EWEKeVpSdd79PkTX5vaGXH5uQ028Smy/H1NmA==");
	}

	@Test
	public void testEncryptUFCCRYPT() throws Exception {
		String algorithm = PwdEncryptor.TYPE_UFC_CRYPT;
		testAlgorithm(algorithm);
		testAlgorithm(algorithm, "password", "2lrTlR/pWPUOQ");
		testUpgradeAlgorithm(algorithm, "password", "2lrTlR/pWPUOQ");
	}

	@Test
	public void testUpgradeCustomAlgorithm() throws Exception {
		PwdEncryptor.PASSWORDS_ENCRYPTION_ALGORITHM = PwdEncryptor.TYPE_SHA_256;

		String sha256Encrypted =
			PwdEncryptor.encrypt(
				"password", "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=");

		Assert.assertEquals(
			"XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=", sha256Encrypted);

		String newPassword = PwdEncryptor.encrypt("password");

		Assert.assertTrue(newPassword.startsWith("{PBKDF2WITHHMACSHA1}"));
	}

	@Test
	public void testUpgradeStandardAlgorithm() throws Exception {
		PwdEncryptor.PASSWORDS_ENCRYPTION_ALGORITHM = null;

		String shaEncrypted = PwdEncryptor.encrypt(
			"password", "W6ph5Mm5Pz8GgiULbPgzG37mj9g=");

		Assert.assertEquals("W6ph5Mm5Pz8GgiULbPgzG37mj9g=", shaEncrypted);
	}

	protected void testAlgorithm(String algorithm) throws Exception {
		String password = "password";
		String encrypted = PwdEncryptor.encrypt(algorithm, password, null);

		Assert.assertTrue(
			encrypted.startsWith(
				"{" + PwdEncryptor.getAlgorithmName(algorithm) + "}"));

		testAlgorithm(algorithm, password, encrypted);
	}

	protected String testAlgorithm(
			String algorithm, String password, String encrypted)
		throws Exception {

		long time = System.currentTimeMillis();

		String actual = PwdEncryptor.encrypt(algorithm, password, encrypted);

		System.out.println(
			"Hash [algorithm, time, result]: [" + algorithm +
				", " + (System.currentTimeMillis() - time) + ", " + actual +
				"]");

		Assert.assertEquals(encrypted, actual);

		return actual;
	}

	protected void testFail(
		String algorithm, String password, String encryptedPassword) {

		try {
			PwdEncryptor.encrypt(algorithm, password, encryptedPassword);

			Assert.fail();
		} catch (Exception e){}
	}

	protected void testUpgradeAlgorithm(
			String algorithm, String password, String encrypted)
		throws Exception {

		PwdEncryptor.PASSWORDS_ENCRYPTION_ALGORITHM = algorithm;

		String actual = PwdEncryptor.encrypt(password, encrypted);

		Assert.assertEquals(encrypted, actual);
	}

}