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

package com.liferay.portal.lar.messaging;

import com.liferay.portal.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessage;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageFactory;
import com.liferay.portal.kernel.lar.messaging.ExportImportMessageLevel;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;

/**
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
public class ExportImportMessageFactoryImpl
	implements ExportImportMessageFactory {

	public ExportImportMessage getErrorMessage(
		StagedModel stagedModel, Exception exception) {

		return doGetExportImportMessage(
			stagedModel, exception, null, ExportImportMessageLevel.ERROR);
	}

	public ExportImportMessage getErrorMessage(
		StagedModel stagedModel, String message) {

		return doGetExportImportMessage(
			stagedModel, null, message, ExportImportMessageLevel.ERROR);
	}

	public ExportImportMessage getErrorMessage(
		String portletId, Exception exception) {

		return doGetExportImportMessage(
			0, Portlet.class.getName(), portletId, exception, null,
			ExportImportMessageLevel.ERROR);
	}

	public ExportImportMessage getErrorMessage(
		String portletId, String message) {

		return doGetExportImportMessage(
			0, Portlet.class.getName(), portletId, null, message,
			ExportImportMessageLevel.ERROR);
	}

	public ExportImportMessage getInfoMessage(
		StagedModel stagedModel, String message) {

		return doGetExportImportMessage(
			stagedModel, null, message, ExportImportMessageLevel.INFO);
	}

	public ExportImportMessage getInfoMessage(String message) {
		return doGetExportImportMessage(
			0, null, null, null, message, ExportImportMessageLevel.INFO);
	}

	public ExportImportMessage getInfoMessage(
		String portletId, String message) {

		return doGetExportImportMessage(
			0, Portlet.class.getName(), portletId, null, message,
			ExportImportMessageLevel.INFO);
	}

	public ExportImportMessage getWarningMessage(
		StagedModel stagedModel, String message) {

		return doGetExportImportMessage(
			stagedModel, null, message, ExportImportMessageLevel.WARNING);
	}

	public ExportImportMessage getWarningMessage(String message) {
		return doGetExportImportMessage(
			0, null, null, null, message, ExportImportMessageLevel.WARNING);
	}

	public ExportImportMessage getWarningMessage(
		String portletId, String message) {

		return doGetExportImportMessage(
			0, Portlet.class.getName(), portletId, null, message,
			ExportImportMessageLevel.WARNING);
	}

	protected ExportImportMessage doGetExportImportMessage(
		long groupId, String className, String uuid, Exception exception,
		String message, ExportImportMessageLevel messageLevel) {

		String destination = null;

		if (ExportImportThreadLocal.isExportInProcess()) {
			destination = DestinationNames.EXPORT;
		}
		else if (ExportImportThreadLocal.isImportInProcess()) {
			destination = DestinationNames.IMPORT;
		}

		destination = "liferay/batch_test";

		ExportImportMessage exportImportMessage = new ExportImportMessage(
			message, destination, messageLevel);

		exportImportMessage.setClassName(className);

		if (exception != null) {
			exportImportMessage.setException(exception);
		}

		exportImportMessage.setGroupId(groupId);
		exportImportMessage.setUuid(uuid);

		return exportImportMessage;
	}

	protected ExportImportMessage doGetExportImportMessage(
		StagedModel stagedModel, Exception exception, String message,
		ExportImportMessageLevel messageLevel) {

		long groupId = 0;

		if (stagedModel instanceof StagedGroupedModel) {
			StagedGroupedModel stagedGroupedModel =
				(StagedGroupedModel)stagedModel;

			groupId = stagedGroupedModel.getGroupId();
		}
		else {
			try {
				Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
					CompanyThreadLocal.getCompanyId());

				groupId = companyGroup.getGroupId();
			}
			catch (Exception e) {
			}
		}

		return doGetExportImportMessage(
			groupId, stagedModel.getModelClassName(), stagedModel.getUuid(),
			exception, message, messageLevel);
	}

}