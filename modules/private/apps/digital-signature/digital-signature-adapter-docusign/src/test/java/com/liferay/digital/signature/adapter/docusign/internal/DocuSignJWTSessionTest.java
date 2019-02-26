/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.digital.signature.adapter.docusign.internal;

import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class DocuSignJWTSessionTest {

	@Before
	public void setUp() throws Exception {
		Map<String, Object> properties = new HashMap<>();

		properties.put("accountId", "200934c5-a5e4-4326-9710-295d45b704a0");
		properties.put(
			"authenticationServerURL", "https://account-d.docusign.com");
		properties.put("clientId", "bcdc7ac8-5479-4631-91e0-742b48e8eb3e");
		properties.put("companyId", 1234L);
		properties.put("expiration", 3600L);

		ClassLoader classLoader = DocuSignJWTSessionTest.class.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"com/liferay/digital/signature/adapter/docusign/internal/test.pem");

		String privateKey = StringUtil.read(inputStream);

		properties.put("privateKey", privateKey);

		properties.put("userId", "be1187aa-2a1b-4e0c-b4c5-b36ae58939e3");

		_docuSignJWTSession = new DocuSignJWTSession();

		_docuSignJWTSession.activate(properties);
	}

	@Test
	public void testConnect() {
		_docuSignJWTSession.connect();

		Assert.assertFalse(_docuSignJWTSession.isExpired());
	}

	@Test
	public void testExpiration() throws Exception {
		_docuSignJWTSession.connect();

		Assert.assertFalse(_docuSignJWTSession.isExpired());

		_docuSignJWTSession.setExpirationTime(
			System.currentTimeMillis() + 5000);

		Thread.sleep(10000);

		Assert.assertTrue(_docuSignJWTSession.isExpired());

		String oldAccessToken = _docuSignJWTSession.getAccessToken();

		_docuSignJWTSession.connect();

		Assert.assertFalse(_docuSignJWTSession.isExpired());

		String newAccessToken = _docuSignJWTSession.getAccessToken();

		Assert.assertNotEquals(oldAccessToken, newAccessToken);
	}

	private DocuSignJWTSession _docuSignJWTSession;

}