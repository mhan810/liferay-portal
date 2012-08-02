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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Portlet;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public interface BookmarksPortletDataHandler extends PortletDataHandler {
	public static final boolean _ALWAYS_EXPORTABLE = true;

	public static final boolean _ALWAYS_STAGED = false;

	public static final boolean _DATA_LOCALIZED = false;

	public static final String _NAMESPACE = "bookmarks";

	public static final boolean _PUBLISH_TO_LIVE_BY_DEFAULT = true;

	public void doImportData(LarDigestItem item, DataHandlerContext context)
		throws Exception;

	public abstract PortletDataHandlerControl[] getExportControls();

	public abstract PortletDataHandlerControl[] getExportMetadataControls();

	public abstract PortletDataHandlerControl[] getImportControls();

	public abstract PortletDataHandlerControl[] getImportMetadataControls();

	public abstract boolean isAlwaysExportable();

	public abstract boolean isAlwaysStaged();

	public abstract boolean isDataLocalized();

	public abstract boolean isPublishToLiveByDefault();
}
