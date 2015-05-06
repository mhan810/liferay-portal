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

package com.liferay.portal.tools;

import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.events.StartupHelperUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.tools.releasecheck.ReleaseUpgrader;
import com.liferay.portal.tools.releasecheck.ReleaseVerifier;
import com.liferay.portal.util.InitUtil;

import com.liferay.util.dao.orm.CustomSQLUtil;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class DBUpgrader {

	public static void main(String[] args) {
		try {
			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			InitUtil.initWithSpring(true);

			upgrade();
			verify();

			System.out.println(
				"\nCompleted upgrade and verify processes in " +
					(stopWatch.getTime() / Time.SECOND) + " seconds");

			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	public static void upgrade() throws Exception {
		// Disable database caching before upgrade

		if (_log.isDebugEnabled()) {
			_log.debug("Disable cache registry");
		}

		CacheRegistryUtil.setActive(false);

		_releaseUpgrader.upgrade();

		// Reload SQL

		CustomSQLUtil.reloadCustomSQL();
		SQLTransformer.reloadSQLTransformer();

		// Update company key

		if (StartupHelperUtil.isUpgraded()) {
			if (_log.isDebugEnabled()) {
				_log.debug("Update company key");
			}

			_updateCompanyKey();
		}

		// Check class names

		if (_log.isDebugEnabled()) {
			_log.debug("Check class names");
		}

		ClassNameLocalServiceUtil.checkClassNames();

		// Check resource actions

		if (_log.isDebugEnabled()) {
			_log.debug("Check resource actions");
		}

		ResourceActionLocalServiceUtil.checkResourceActions();

		// Delete temporary images

		if (_log.isDebugEnabled()) {
			_log.debug("Delete temporary images");
		}

		_deleteTempImages();

		// Clear the caches only if the upgrade process was run

		if (_log.isDebugEnabled()) {
			_log.debug("Clear cache if upgrade process was run");
		}

		if (StartupHelperUtil.isUpgraded()) {
			MultiVMPoolUtil.clear();
		}
	}

	public static void verify() throws Exception {
		_releaseVerifier.verify();

		// Enable database caching after verify

		if (_log.isDebugEnabled()) {
			_log.debug("Re-enable cache registry");
		}

		CacheRegistryUtil.setActive(true);
	}

	public void setReleaseUpgrader(ReleaseUpgrader releaseUpgrader) {
		_releaseUpgrader = releaseUpgrader;
	}

	public void setReleaseVerifier(ReleaseVerifier releaseVerifier) {
		_releaseVerifier = releaseVerifier;
	}

	private static void _deleteTempImages() throws Exception {
		DB db = DBFactoryUtil.getDB();

		db.runSQL(_DELETE_TEMP_IMAGES_1);
		db.runSQL(_DELETE_TEMP_IMAGES_2);
	}

	private static void _updateCompanyKey() throws Exception {
		DB db = DBFactoryUtil.getDB();

		db.runSQL("update Company set key_ = null");
	}

	private static final String _DELETE_TEMP_IMAGES_1 =
		"delete from Image where imageId IN (SELECT articleImageId FROM " +
			"JournalArticleImage where tempImage = TRUE)";

	private static final String _DELETE_TEMP_IMAGES_2 =
		"delete from JournalArticleImage where tempImage = TRUE";


	private static ReleaseUpgrader _releaseUpgrader;
	private static ReleaseVerifier _releaseVerifier;

	private static final Log _log = LogFactoryUtil.getLog(DBUpgrader.class);

}