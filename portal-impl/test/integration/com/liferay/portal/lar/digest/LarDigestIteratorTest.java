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

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class LarDigestIteratorTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		_larDigest = new LarDigestImpl();
	}

	@Test
	public void testDigest() throws Exception {
		addTestData();

		int itemCount = 0;

		for (LarDigestItem item : _larDigest) {
			itemCount++;

			int action = item.getAction();

			switch (action) {
				case LarDigesterConstants.ACTION_ADD:
					Assert.assertEquals(
						item.getPath(),
						"/10190/" + Layout.class.getName() + "/1");

					Assert.assertEquals(item.getType(), Layout.class.getName());

					Assert.assertEquals(item.getClassPK(), "1000");

					System.out.println(item.getMetadata());

					System.out.println(item.getPermissions());

					break;
				case LarDigesterConstants.ACTION_DELETE:
					Assert.assertEquals(
						item.getPath(),
						"/10190/" + Portlet.class.getName() + "/2");

					Assert.assertEquals(
						item.getType(), Portlet.class.getName());

					Assert.assertEquals(item.getClassPK(), "2000");

					break;
				case LarDigesterConstants.ACTION_UPDATE:
					Assert.assertEquals(
						item.getPath(),
						"/10190/" + BookmarksEntry.class.getName() + "/3");

					Assert.assertEquals(
						item.getType(), BookmarksEntry.class.getName());

					Assert.assertEquals(item.getClassPK(), "3000");
					break;
				default:
					Assert.fail("Unexpected value returned by iterator");
			}
		}

		Assert.assertEquals(3, itemCount);
	}

	@Test
	public void testEmptyDigest() throws Exception {
		_larDigest.close();

		int itemCount = 0;

		for (LarDigestItem item : _larDigest) {
			itemCount++;
		}

		Assert.assertEquals(0, itemCount);
	}

	private void addTestData() throws Exception {
		LarDigestItem item = new LarDigestItemImpl();

		item.setAction(LarDigesterConstants.ACTION_ADD);
		item.setPath("/10190/" + Layout.class.getName() + "/1");
		item.setType(Layout.class.getName());
		item.setClassPK("1000");

		Map metadata = new HashMap<String, String>();

		item.setMetadata(null);

		Map permissions = new HashMap<String, List<String>>();

		List actionKeys = new ArrayList<String>();

		actionKeys = new ArrayList<String>();

		actionKeys.add("action1");
		actionKeys.add("action2");
		actionKeys.add("action3");

		permissions.put("test-role1", actionKeys);

		actionKeys = new ArrayList<String>();
		actionKeys.add("action1");

		permissions.put("test-role2", null);

		item.setPermissions(permissions);

		_larDigest.write(item);

		item = new LarDigestItemImpl();

		item.setAction(LarDigesterConstants.ACTION_DELETE);
		item.setPath("/10190/" + Portlet.class.getName() + "/2");
		item.setType(Portlet.class.getName());
		item.setClassPK("2000");

		_larDigest.write(item);

		item = new LarDigestItemImpl();

		item.setAction(LarDigesterConstants.ACTION_UPDATE);
		item.setPath("/10190/" + BookmarksEntry.class.getName() + "/3");
		item.setType(BookmarksEntry.class.getName());
		item.setClassPK("3000");

		_larDigest.write(item);

		_larDigest.close();

		System.out.println(_larDigest.getDigestString());
	}

	private LarDigest _larDigest;

}