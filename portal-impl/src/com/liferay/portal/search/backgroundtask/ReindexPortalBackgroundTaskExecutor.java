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

package com.liferay.portal.search.backgroundtask;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.SearchEngineInitializer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrew Betts
 */
public class ReindexPortalBackgroundTaskExecutor
	extends ReindexBackgroundTaskExecutor {

	@Override
	protected Set<String> doReindex(String className, long[] companyIds)
		throws Exception {

		Set<String> usedSearchEngineIds = new HashSet<>();

		for (long companyId : companyIds) {
			try {
				SearchEngineInitializer searchEngineInitializer =
					new SearchEngineInitializer(companyId);

				searchEngineInitializer.reindex();

				usedSearchEngineIds.addAll(
					searchEngineInitializer.getUsedSearchEngineIds());
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		return usedSearchEngineIds;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReindexPortalBackgroundTaskExecutor.class);

}