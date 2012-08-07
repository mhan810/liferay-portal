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
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.io.File;

import java.net.URL;

import java.util.List;

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
	public void testCreateDigest() throws Exception {
		LarDigest larDigest = new LarDigestImpl();

		larDigest.addMetadata(new LarDigestMetadataImpl("test", "test"));

		larDigest.addModule(getPortletModule());

		larDigest.addModule(new LarDigestModuleImpl());

		larDigest.write();

		larDigest.close();

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("digest.xml"), result);
	}

	@Test
	public void testEmptyDigest() throws Exception {
		LarDigest larDigest = new LarDigestImpl();
		larDigest.close();

		String result = larDigest.getDigestString();

		Assert.assertEquals(getTestFileContent("emptyLar.xml"), result);
	}

	@Test
	public void testFindDigestItems() throws Exception {
		String xml = getTestFileContent("digest.xml");

		LarDigest digest = new LarDigestImpl(xml);

		List<LarDigestItem> result = digest.findDigestItems(
			0, StringPool.BLANK, Portlet.class.getName(), "12345",
			StringPool.BLANK);

		Assert.assertEquals(1, result.size());

		LarDigestItem resultItem = result.get(0);

		Assert.assertEquals(
			LarDigesterConstants.ACTION_ADD, resultItem.getAction());
		Assert.assertEquals("12345", resultItem.getClassPK());
		Assert.assertEquals("here/i/am.xml", resultItem.getPath());
		Assert.assertEquals(Portlet.class.getName(), resultItem.getType());
		Assert.assertEquals(2, resultItem.getPermissions().size());

		LarDigestMetadata metadata = resultItem.getMetadata().get(0);
		Assert.assertEquals("test1", metadata.getValue());

		digest.close();
	}

	@Test
	public void testParseDigest() throws Exception {
		String xml = getTestFileContent("digest.xml");

		LarDigest digest = new LarDigestImpl(xml);

		Assert.assertEquals(1, digest.getMetaData().size());

		List<LarDigestModule> modules = digest.getAllModules();

		Assert.assertEquals(2, modules.size());

		LarDigestModule module = modules.get(0);

		List<LarDigestItem> items = module.getItems();

		Assert.assertEquals(2, items.size());

		List<String> portletPreferences = module.getPortletPreferences();

		Assert.assertEquals(2, portletPreferences.size());

		LarDigestItem item = items.get(0);

		Assert.assertEquals(1, item.getMetadata().size());

		Assert.assertEquals(2, item.getPermissions().size());
	}

	private LarDigestModule getPortletModule() {
		LarDigestModule module = new LarDigestModuleImpl();

		module.setName("test.module");

		module.addPortletPreference("test/module/preference/path1");

		module.addPortletPreference("test/module/preference/path2");

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(LarDigesterConstants.ACTION_ADD);
		digestItem.setPath("here/i/am.xml");
		digestItem.setClassPK("12345");
		digestItem.setType(Portlet.class.getName());
		digestItem.setUuid("123-123-123");

		digestItem.addMetadata(new LarDigestMetadataImpl("test1", "test1"));

		LarDigestPermission permission = new LarDigestPermissionImpl();
		permission.addActionId("001");
		permission.setRoleName("TestRole1");

		digestItem.addPermission(permission);

		permission = new LarDigestPermissionImpl();
		permission.addActionId("001");
		permission.addActionId("002");
		permission.setRoleName("TestRole2");

		digestItem.addPermission(permission);

		module.addItem(digestItem);

		module.addItem(new LarDigestItemImpl());

		return module;
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