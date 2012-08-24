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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.model.Portlet;

/**
 * @author Mate Thurzo
 */
public interface StagedPortletDataHandler extends StagedDataHandler<Portlet> {
	public PortletDataHandlerControl[] getExportControls();

	public PortletDataHandlerControl[] getExportMetadataControls();

	public PortletDataHandlerControl[] getImportControls();

	public PortletDataHandlerControl[] getImportMetadataControls();

	public void importData(
			LarDigestModule portletModule, DataHandlerContext context)
		throws Exception;

	public boolean isAlwaysExportable();

	public boolean isAlwaysStaged();

	public boolean isDataLocalized();

	public boolean isPublishToLiveByDefault();

}