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
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;

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

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("emptyLar.xml"), result);
	}

	@Test
	public void testPortletNodeDigest() throws Exception {
		LarDigest larDigest = new LarDigestImpl();

		larDigest.write(getDigestItem());
		larDigest.close();

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("portletNode.xml"), result);
	}

	@Test
	public void testFindDigestItems() throws Exception {
		File testFile = getTestFile("digest.xml");

		LarDigest digest = new LarDigestImpl(testFile);

		LarDigestItem expectedItem = getDigestItem();

		List<LarDigestItem> result = digest.findDigestItems(
			expectedItem.getAction(), expectedItem.getPath(),
			expectedItem.getType(), expectedItem.getClassPK());

		Assert.assertEquals(1, result.size());

		LarDigestItem resultItem = result.get(0);

		Assert.assertEquals(expectedItem.getAction(), resultItem.getAction());
		Assert.assertEquals(expectedItem.getClassPK(), resultItem.getClassPK());
		Assert.assertEquals(expectedItem.getPath(), resultItem.getPath());
		Assert.assertEquals(expectedItem.getType(), resultItem.getType());
		Assert.assertEquals(
			expectedItem.getPermissions(), resultItem.getPermissions());
		Assert.assertEquals(
			expectedItem.getMetadata(), resultItem.getMetadata());

		digest.close();
	}

	private LarDigestItem getDigestItem() {
		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath("here/i/am.xml");
		digestItem.setClassPK("12345");
		digestItem.setType(Portlet.class.getName());

		Map metadata = new HashMap<String, String>();

		metadata.put("test-metadata", "test-value");

		digestItem.setMetadata(metadata);

		Map permissions = new HashMap<String, List<String>>();

		List actionKeys = new ArrayList<String>();

		actionKeys.add("test-action");

		permissions.put("test-role", actionKeys);

		digestItem.setPermissions(permissions);

		return digestItem;
	}

	private File getTestFile(String filename) throws Exception {
		Class<?> clazz = LarDigestTest.class;

		URL resourceUrl = clazz.getResource("dependencies/" + filename);

		return new File(resourceUrl.toURI());
	}

	private String getTestFileContent(String filename) throws Exception {
		File resourceFile = getTestFile(filename);

		String xml = new String(FileUtil.getBytes(resourceFile));

		return xml;
	}

}