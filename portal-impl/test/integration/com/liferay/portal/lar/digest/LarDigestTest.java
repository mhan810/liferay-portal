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

package com.liferay.portal.lar.digest;

import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class LarDigestTest extends PowerMockito {

	@Test
	public void testEmptyDigest() throws Exception {
		LarDigest larDigest = new LarDigestImpl();
		larDigest.close();

		File digestFile = larDigest.getDigestFile();

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("emptyLar.xml"), result);
	}

	@Test
	public void testPortletNodeDigest() throws Exception {
		LarDigest larDigest = new LarDigestImpl();

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath("here/i/am.xml");
		digestItem.setClassPK("12345");
		digestItem.setType(Portlet.class.getName());

		larDigest.write(digestItem);
		larDigest.close();

		File digestFile = larDigest.getDigestFile();

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("portletNode.xml"), result);
	}

	private String getTestFileContent(String filename) throws Exception {
		Class<?> clazz = LarDigestTest.class;

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + filename);

		String xml = new String(FileUtil.getBytes(inputStream));

		return xml;
	}

}