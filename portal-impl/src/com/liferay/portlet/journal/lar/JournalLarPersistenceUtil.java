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

import com.liferay.portal.kernel.lar.BaseLarPersistenceUtil;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.regex.Pattern;

/**
 * @author Mate Thurzo
 */
public class JournalLarPersistenceUtil extends BaseLarPersistenceUtil {

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[] {
			_articles, _structuresTemplatesAndFeeds, _embeddedAssets
		};
	}

	private static final boolean _ALWAYS_EXPORTABLE = true;

	private static final String _NAMESPACE = "journal";

	private static Log _log = LogFactoryUtil.getLog(
		JournalPortletDataHandlerImpl.class);

	private static PortletDataHandlerBoolean _articles =
		new PortletDataHandlerBoolean(_NAMESPACE, "web-content", true, false);

	private static PortletDataHandlerBoolean _embeddedAssets =
		new PortletDataHandlerBoolean(_NAMESPACE, "embedded-assets");

	private static Pattern _exportLinksToLayoutPattern = Pattern.compile(
		"\\[([0-9]+)@(public|private\\-[a-z]*)\\]");

	private static Pattern _importLinksToLayoutPattern = Pattern.compile(
		"\\[([0-9]+)@(public|private\\-[a-z]*)@(\\p{XDigit}{8}\\-" +
			"(?:\\p{XDigit}{4}\\-){3}\\p{XDigit}{12})@([^\\]]*)\\]");

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(_NAMESPACE, "images"),
			new PortletDataHandlerBoolean(_NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(_NAMESPACE, "comments"),
			new PortletDataHandlerBoolean(_NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(_NAMESPACE, "tags")
		};

	private static PortletDataHandlerBoolean
		_structuresTemplatesAndFeeds = new PortletDataHandlerBoolean(
		_NAMESPACE, "structures-templates-and-feeds", true, true);

}