/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.tools.releasecheck;

import com.liferay.portal.events.StartupHelperUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.model.Release;
import com.liferay.portal.model.ReleaseConstants;
import com.liferay.portal.service.ReleaseLocalServiceUtil;
import com.liferay.portal.util.PropsValues;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class ReleaseVerifier extends ReleaseChecker {

	public void verify() throws Exception {
		// Check release

		Release release = ReleaseLocalServiceUtil.fetchRelease(
			ReleaseConstants.DEFAULT_SERVLET_CONTEXT_NAME);

		if (release == null) {
			release = ReleaseLocalServiceUtil.addRelease(
				ReleaseConstants.DEFAULT_SERVLET_CONTEXT_NAME,
				ReleaseInfo.getParentBuildNumber());
		}

		checkReleaseState(release.getState());

		// Update indexes

		if (PropsValues.DATABASE_INDEXES_UPDATE_ON_STARTUP) {
			StartupHelperUtil.setDropIndexes(true);

			StartupHelperUtil.updateIndexes();
		}
		else if (StartupHelperUtil.isUpgraded()) {
			StartupHelperUtil.updateIndexes();
		}

		// Verify

		if (PropsValues.VERIFY_DATABASE_TRANSACTIONS_DISABLED) {
			disableTransactions();
		}

		boolean newBuildNumber = false;

		if (ReleaseInfo.getBuildNumber() > release.getBuildNumber()) {
			newBuildNumber = true;
		}

		try {
			StartupHelperUtil.verifyProcess(
				newBuildNumber, release.isVerified());
		}
		catch (Exception e) {
			updateReleaseState(ReleaseConstants.STATE_VERIFY_FAILURE);

			_log.error(
				"Unable to execute verify process: " + e.getMessage(), e);

			throw e;
		}
		finally {
			if (PropsValues.VERIFY_DATABASE_TRANSACTIONS_DISABLED) {
				enableTransactions();
			}
		}

		// Update indexes

		if (PropsValues.DATABASE_INDEXES_UPDATE_ON_STARTUP ||
			StartupHelperUtil.isUpgraded()) {

			StartupHelperUtil.updateIndexes(false);
		}

		// Update release

		boolean verified = StartupHelperUtil.isVerified();

		if (release.isVerified()) {
			verified = true;
		}

		ReleaseLocalServiceUtil.updateRelease(
			release.getReleaseId(), ReleaseInfo.getParentBuildNumber(),
			ReleaseInfo.getBuildDate(), verified);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReleaseVerifier.class);

}
