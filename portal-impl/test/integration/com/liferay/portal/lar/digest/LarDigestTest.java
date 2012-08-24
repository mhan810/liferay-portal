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

import com.liferay.portal.kernel.lar.digest.LarDigest;
import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.lar.digest.LarDigestEntryImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestMetadata;
import com.liferay.portal.kernel.lar.digest.LarDigestMetadataImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.lar.digest.LarDigestModuleImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestPortletPreference;
import com.liferay.portal.kernel.lar.digest.LarDigestPortletPreferenceImpl;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.lar.AssetAction;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.io.File;

import java.net.URL;

import java.util.List;
import java.util.Set;

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

		larDigest.addModule(new LarDigestModuleImpl(StringPool.BLANK));

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

		List<LarDigestEntry> result = digest.findDigestItems(
			null, StringPool.BLANK, Portlet.class.getName(), "12345",
			StringPool.BLANK);

		Assert.assertEquals(1, result.size());

		LarDigestEntry resultEntry = result.get(0);

		Assert.assertEquals(AssetAction.ADD, resultEntry.getAction());
		Assert.assertEquals("12345", resultEntry.getClassPK());
		Assert.assertEquals("here/i/am.xml", resultEntry.getPath());
		Assert.assertEquals(
			Portlet.class.getName(), resultEntry.getClassName());

		LarDigestMetadata metadata = resultEntry.getMetadata().get(0);
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

		Set<LarDigestEntry> entries = module.getEntriesByClassName(
			Portlet.class.getName());

		Assert.assertEquals(1, entries.size());

		List<LarDigestPortletPreference> portletPreferences =
			module.getPortletPreferences();

		Assert.assertEquals(2, portletPreferences.size());

		LarDigestEntry entry = entries.iterator().next();

		Assert.assertEquals(1, entry.getMetadata().size());
	}

	private LarDigestModule getPortletModule() {
		LarDigestModule module = new LarDigestModuleImpl("test.module");

		module.addPortletPreference(
			new LarDigestPortletPreferenceImpl("test/module/preference/path1"));

		module.addPortletPreference(
			new LarDigestPortletPreferenceImpl("test/module/preference/path2"));

		LarDigestEntry digestEntry = new LarDigestEntryImpl();

		digestEntry.setAction(AssetAction.ADD);
		digestEntry.setPath("here/i/am.xml");
		digestEntry.setClassPK("12345");
		digestEntry.setClassName(Portlet.class.getName());
		digestEntry.setUuid("123-123-123");

		digestEntry.addMetadata(new LarDigestMetadataImpl("test1", "test1"));

		module.addEntry(digestEntry);

		module.addEntry(new LarDigestEntryImpl());

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