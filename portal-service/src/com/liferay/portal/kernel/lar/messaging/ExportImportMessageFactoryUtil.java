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

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.model.StagedModel;

/**
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
public class ExportImportMessageFactoryUtil {

	public static ExportImportMessageFactory getExportImportMessageFactory() {
		return _exportImportMessageFactory;
	}

	public static ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception) {

		return getExportImportMessageFactory().getMessage(
			stagedModel, exception);
	}

	public static ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception, String message) {

		return getExportImportMessageFactory().getMessage(
			stagedModel, exception, message);
	}

	public static ExportImportMessage getMessage(
		StagedModel stagedModel, String message) {

		return getExportImportMessageFactory().getMessage(stagedModel, message);
	}

	public static ExportImportMessage getMessage(
		String portletId, Exception exception) {

		return getExportImportMessageFactory().getMessage(portletId, exception);
	}

	public static ExportImportMessage getMessage(
		String portletId, Exception exception, String message) {

		return getExportImportMessageFactory().getMessage(
			portletId, exception, message);
	}

	public static ExportImportMessage getMessage(
		String portletId, String message) {

		return getExportImportMessageFactory().getMessage(portletId, message);
	}

	public static ExportImportMessage getProcessMessage(
		String process, Exception exception) {

		return getExportImportMessageFactory().getMessage(process, exception);
	}

	public static ExportImportMessage getProcessMessage(
		String process, Exception exception, String message) {

		return getExportImportMessageFactory().getProcessMessage(
			process, exception, message);
	}

	public static ExportImportMessage getProcessMessage(
		String process, String message) {

		return getExportImportMessageFactory().getProcessMessage(
			process, message);
	}

	public void setExportImportMessageFactory(
		ExportImportMessageFactory exportImportMessageFactory) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_exportImportMessageFactory = exportImportMessageFactory;
	}

	private static ExportImportMessageFactory _exportImportMessageFactory;

}