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

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.StagedPortletDataHandlerImpl;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Portlet;

/**
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
public class JournalPortletDataHandlerImpl extends StagedPortletDataHandlerImpl
	implements JournalPortletDataHandler {

	@Override
	public void doExport(
			Portlet portlet, DataHandlerContext context,
			LarDigestModule digestModule)
		throws Exception {

		return;
	}

	@Override
	public String[] getDataPortletPreferences() {
		return new String[0];
	}

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public boolean isAlwaysExportable() {
		return ALWAYS_EXPORTABLE;
	}

	@Override
	public boolean isAlwaysStaged() {
		return ALWAYS_STAGED;
	}

	@Override
	public boolean isDataLocalized() {
		return DATA_LOCALIZED;
	}

	@Override
	public boolean isPublishToLiveByDefault() {
		return PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	private static Log _log = LogFactoryUtil.getLog(
		JournalPortletDataHandlerImpl.class);

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(NAMESPACE, "images"),
			new PortletDataHandlerBoolean(NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(NAMESPACE, "comments"),
			new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(NAMESPACE, "tags")
		};

	private static PortletDataHandlerBoolean
		_structuresTemplatesAndFeeds = new PortletDataHandlerBoolean(
			NAMESPACE, "structures-templates-and-feeds", true, true);

}