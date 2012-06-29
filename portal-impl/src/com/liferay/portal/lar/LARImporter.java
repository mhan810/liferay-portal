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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.ImportExportThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.permission.PermissionCacheUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;

import java.io.File;

import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class LARImporter {

	public void importa(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, File file)
			throws Exception {

		try {
			ImportExportThreadLocal.setLayoutImportInProcess(true);

			doImport(userId, groupId, privateLayout, parameterMap, file);
		}
		finally {
			ImportExportThreadLocal.setLayoutImportInProcess(false);

			CacheUtil.clearCache();
			JournalContentUtil.clearCache();
			PermissionCacheUtil.clearCache();
		}
	}

	protected void doImport(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, File file)
			throws Exception {

	}

	private static Log _log = LogFactoryUtil.getLog(LayoutImporter.class);

}