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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerProxy;

import javax.portlet.PortletPreferences;

/**
 * @author Mate Thurzo
 */
public class PortletDataHandlerProxyImpl implements PortletDataHandlerProxy {

	public PortletPreferences deleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		return _portletDataHandler.deleteData(
			portletDataContext, portletId, portletPreferences);
	}

	public String exportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws PortletDataException {

		return _portletDataHandler.exportData(
			portletDataContext, portletId, portletPreferences);
	}

	public String[] getDataPortletPreferences() {
		return _portletDataHandler.getDataPortletPreferences();
	}

	public PortletDataHandlerControl[] getExportControls()
		throws PortletDataException {

		return _portletDataHandler.getExportControls();
	}

	public PortletDataHandlerControl[] getExportMetadataControls()
		throws PortletDataException {

		return _portletDataHandler.getExportMetadataControls();
	}

	public PortletDataHandlerControl[] getImportControls()
		throws PortletDataException {

		return _portletDataHandler.getImportControls();
	}

	public PortletDataHandlerControl[] getImportMetadataControls()
		throws PortletDataException {

		return _portletDataHandler.getImportMetadataControls();
	}

	public PortletDataHandler getPortletDataHandler() {
		return _portletDataHandler;
	}

	public PortletPreferences importData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws PortletDataException {

		return _portletDataHandler.importData(
			portletDataContext, portletId, portletPreferences, data);
	}

	public boolean isAlwaysExportable() {
		return _portletDataHandler.isAlwaysExportable();
	}

	public boolean isAlwaysStaged() {
		return _portletDataHandler.isAlwaysStaged();
	}

	public boolean isDataLocalized() {
		return _portletDataHandler.isDataLocalized();
	}

	public boolean isDataPortalLevel() {
		return _portletDataHandler.isDataPortalLevel();
	}

	public boolean isPublishToLiveByDefault() {
		return _portletDataHandler.isPublishToLiveByDefault();
	}

	public void prepareManifestSummary(PortletDataContext portletDataContext)
		throws PortletDataException {

		_portletDataHandler.prepareManifestSummary(portletDataContext);
	}

	public void setPortletDataHandler(PortletDataHandler portletDataHandler) {
		_portletDataHandler = portletDataHandler;
	}

	private PortletDataHandler _portletDataHandler;

}