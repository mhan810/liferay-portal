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

package com.liferay.portalweb.socialofficesite.home.events;

import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.socialofficesite.home.events.sousviewcalendarevent2daysguestnoviewedsite.SOUs_ViewCalendarEvent2DaysGuestNoViewEDSiteTests;
import com.liferay.portalweb.socialofficesite.home.events.viewcalendareventsedsite.ViewCalendarEventsEDSiteTests;
import com.liferay.portalweb.socialofficesite.home.events.viewdeletecalendarfuture2dayseventedsite.ViewDeleteCalendarEventFuture2DaysEDSiteTests;
import com.liferay.portalweb.socialofficesite.home.events.vieweditcalendarfuture2dayseventedsite.ViewEditCalendarEventFuture2DaysEDSiteTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class EventsTestPlan extends BaseTestSuite {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();

		testSuite.addTest(
			SOUs_ViewCalendarEvent2DaysGuestNoViewEDSiteTests.suite());
		testSuite.addTest(ViewCalendarEventsEDSiteTests.suite());
		testSuite.addTest(
			ViewDeleteCalendarEventFuture2DaysEDSiteTests.suite());
		testSuite.addTest(ViewEditCalendarEventFuture2DaysEDSiteTests.suite());

		return testSuite;
	}

}