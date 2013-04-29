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

package com.liferay.portal.kernel.lar.messaging;

import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageHandlerUtil {

	public static void exportFinished(PortletDataContext portletDataContext) {
		getExportImportMessageHandler().exportFinished(portletDataContext);
	}

	public static void exportStarted(PortletDataContext portletDataContext) {
		getExportImportMessageHandler().exportStarted(portletDataContext);
	}

	public static void finished(
		PortletDataContext portletDataContext, StagedModel stagedModel) {

		getExportImportMessageHandler().finished(
			portletDataContext, stagedModel);
	}

	public static ExportImportMessageHandler getExportImportMessageHandler() {
		PortalRuntimePermission.checkGetBeanProperty(
			ExportImportMessageHandlerUtil.class);

		return _exportImportMessageHandler;
	}

	public static void importFinished(PortletDataContext portletDataContext) {
		getExportImportMessageHandler().importFinished(portletDataContext);
	}

	public static void importStarted(PortletDataContext portletDataContext) {
		getExportImportMessageHandler().importStarted(portletDataContext);
	}

	public static void sendMessage(ExportImportMessage message) {
		getExportImportMessageHandler().sendMessage(message);
	}

	public static void started(
		PortletDataContext portletDataContext, StagedModel stagedModel) {

		getExportImportMessageHandler().started(
			portletDataContext, stagedModel);
	}

	public void setExportImportMessageHandler(
		ExportImportMessageHandler exportImportMessageHandler) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_exportImportMessageHandler = exportImportMessageHandler;
	}

	private static ExportImportMessageHandler _exportImportMessageHandler;

}