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

package com.liferay.portlet.journal.lar;

import com.liferay.portal.kernel.lar.StagedDataHandlerImpl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.journal.model.JournalArticle;

/**
 * @author Mate Thurzo
 */
public class JournalArticleDataHandlerImpl
	extends StagedDataHandlerImpl<JournalArticle>
	implements JournalArticleDataHandler {

	private static Log _log = LogFactoryUtil.getLog(
		JournalArticleDataHandlerImpl.class);

}