/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portalweb.socialoffice.users.sites.viewremoverolesososites;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class SOUs_ViewRemoveRoleSOSOSitesTest extends BaseTestCase {
	public void testSOUs_ViewRemoveRoleSOSOSites() throws Exception {
		selenium.selectWindow("null");
		selenium.selectFrame("relative=top");
		selenium.open("/web/guest/home/");
		selenium.clickAt("//div[@id='dockbar']",
			RuntimeVariables.replace("Dockbar"));
		selenium.waitForElementPresent(
			"//script[contains(@src,'/liferay/dockbar_underlay.js')]");
		assertEquals(RuntimeVariables.replace("Dashboard"),
			selenium.getText("//li/a[contains(.,'Dashboard')]"));
		selenium.mouseOver("//li/a[contains(.,'Dashboard')]");
		selenium.clickAt("//li/a[contains(.,'Dashboard')]",
			RuntimeVariables.replace("Dashboard"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Social01 Office01 User01"),
			selenium.getText("//a[@class='profile-name']"));
		assertEquals(RuntimeVariables.replace("Dashboard"),
			selenium.getText("//nav/ul/li/a/span"));
		assertEquals(RuntimeVariables.replace("Contacts Center"),
			selenium.getText("//nav/ul/li[2]/a/span"));
		assertEquals(RuntimeVariables.replace("Microblogs"),
			selenium.getText("//nav/ul/li[3]/a/span"));
		assertEquals(RuntimeVariables.replace("Messages"),
			selenium.getText("//nav/ul/li[4]/a/span"));
		assertEquals(RuntimeVariables.replace("My Documents"),
			selenium.getText("//nav/ul/li[5]/a"));
		assertEquals(RuntimeVariables.replace("Tasks"),
			selenium.getText("//li[6]/a/span"));
		selenium.open("/user/socialoffice01/so/dashboard");
		assertEquals(RuntimeVariables.replace("Social01 Office01 User01"),
			selenium.getText("//a[@class='profile-name']"));
		assertEquals(RuntimeVariables.replace("Dashboard"),
			selenium.getText("//nav/ul/li/a/span"));
		assertEquals(RuntimeVariables.replace("Contacts Center"),
			selenium.getText("//nav/ul/li[2]/a/span"));
		assertEquals(RuntimeVariables.replace("Microblogs"),
			selenium.getText("//nav/ul/li[3]/a/span"));
		assertEquals(RuntimeVariables.replace("Messages"),
			selenium.getText("//nav/ul/li[4]/a/span"));
		assertEquals(RuntimeVariables.replace("My Documents"),
			selenium.getText("//nav/ul/li[5]/a"));
		assertEquals(RuntimeVariables.replace("Tasks"),
			selenium.getText("//li[6]/a/span"));
		selenium.clickAt("//div[@id='dockbar']",
			RuntimeVariables.replace("Dockbar"));
		selenium.waitForElementPresent(
			"//script[contains(@src,'/liferay/dockbar_underlay.js')]");
		assertEquals(RuntimeVariables.replace("Social01 Office01 User01"),
			selenium.getText("//li[@id='_145_userMenu']"));
		selenium.mouseOver("//li[@id='_145_userMenu']");
		selenium.waitForVisible("link=My Profile");
		assertEquals(RuntimeVariables.replace("My Profile"),
			selenium.getText("link=My Profile"));
		assertEquals(RuntimeVariables.replace("My Account"),
			selenium.getText("link=My Account"));
		assertEquals(RuntimeVariables.replace("Sign Out"),
			selenium.getText("link=Sign Out"));
		selenium.clickAt("link=My Profile",
			RuntimeVariables.replace("My Profile"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Social01 Office01 User01"),
			selenium.getText("//div[@class='lfr-contact-name']"));
		assertEquals(RuntimeVariables.replace("socialoffice01@liferay.com"),
			selenium.getText("//div[@class='lfr-contact-extra']"));
		assertEquals(RuntimeVariables.replace(
				"You do not have any microblog entries."),
			selenium.getText(
				"//div[@class='microblogs-container microblogs-status-container']"));
		assertEquals(RuntimeVariables.replace("About"),
			selenium.getText(
				"xPath=(//div[@class='user-information-title'])[contains(.,'About')]"));
		assertEquals(RuntimeVariables.replace("Introduction"),
			selenium.getText("//li[@data-title='Introduction']"));
		assertEquals(RuntimeVariables.replace("Tags"),
			selenium.getText("//li[@data-title='Tags']"));
		assertEquals(RuntimeVariables.replace("Phones"),
			selenium.getText("//li[@data-title='Phone Numbers']"));
		assertEquals(RuntimeVariables.replace("Email Address"),
			selenium.getText("//li[@data-title='Additional Email Addresses']"));
		assertEquals(RuntimeVariables.replace("Instant Messenger"),
			selenium.getText("//li[@data-title='Instant Messenger']"));
		assertEquals(RuntimeVariables.replace("Addresses"),
			selenium.getText("//li[@data-title='Addresses']"));
		assertEquals(RuntimeVariables.replace("Websites"),
			selenium.getText("//li[@data-title='Websites']"));
		assertEquals(RuntimeVariables.replace("Social Network"),
			selenium.getText("//li[@data-title='Social Network']"));
		assertEquals(RuntimeVariables.replace("SMS"),
			selenium.getText("//li[@data-title='SMS']"));
		assertEquals(RuntimeVariables.replace("Projects"),
			selenium.getText(
				"xPath=(//div[@class='user-information-title'])[contains(.,'Projects')]"));
		assertEquals(RuntimeVariables.replace("Add"),
			selenium.getText("//li[@data-title='Projects']"));
		assertEquals(RuntimeVariables.replace("Activities"),
			selenium.getText(
				"xPath=(//span[@class='portlet-title-text'])[contains(.,'Activities')]"));
		assertEquals(RuntimeVariables.replace("There are no recent activities."),
			selenium.getText("//div[@class='portrait-social-activities']"));
		assertEquals(RuntimeVariables.replace("Profile"),
			selenium.getText("link=Profile"));
		assertEquals(RuntimeVariables.replace("Contacts"),
			selenium.getText("link=Contacts"));
		assertEquals(RuntimeVariables.replace("Microblogs"),
			selenium.getText("link=Microblogs"));
		selenium.clickAt("//div[@id='dockbar']",
			RuntimeVariables.replace("Dockbar"));
		selenium.waitForElementPresent(
			"//script[contains(@src,'/liferay/dockbar_underlay.js')]");
		assertEquals(RuntimeVariables.replace("Go to"),
			selenium.getText("//li[@id='_145_mySites']/a/span"));
		selenium.mouseOver("//li[@id='_145_mySites']/a/span");
		selenium.waitForVisible("link=Control Panel");
		assertFalse(selenium.isTextPresent("My Public Pages"));
		assertFalse(selenium.isTextPresent("My Private Pages"));
		assertTrue(selenium.isVisible("link=Sites Directory"));
		selenium.clickAt("link=Sites Directory",
			RuntimeVariables.replace("Sites Directory"));
		selenium.waitForVisible("//ul[@class='directory-list']");
		assertFalse(selenium.isTextPresent("My Public Pages"));
		assertFalse(selenium.isTextPresent("My Private Pages"));
	}
}