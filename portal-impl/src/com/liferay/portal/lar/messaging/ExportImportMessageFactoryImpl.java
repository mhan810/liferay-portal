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
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;

/**
* @author Mate Thurzo
 */
public class ExportImportMessageFactoryImpl
	implements ExportImportMessageFactory {

	public ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception) {

		return doGetMessage(stagedModel, exception, null);
	}

	public ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception, String message) {

		return doGetMessage(stagedModel, exception, message);
	}

	public ExportImportMessage getMessage(
		StagedModel stagedModel, String message) {

		return doGetMessage(stagedModel, null, message);
	}

	public ExportImportMessage getMessage(
		String portletId, Exception exception) {

		return null;
	}

	public ExportImportMessage getMessage(
		String portletId, Exception exception, String message) {

		return null;
	}

	public ExportImportMessage getMessage(String portletId, String message) {
		return null;
	}

	public ExportImportMessage getProcessMessage(
		String process, Exception exception) {

		return null;
	}

	public ExportImportMessage getProcessMessage(
		String process, Exception exception, String message) {

		return null;
	}

	public ExportImportMessage getProcessMessage(
		String process, String message) {

		return null;
	}

	protected static void checkCompanyGroup() {
		if (_companyGroup != null) {
			return;
		}

		try {
			_companyGroup = GroupLocalServiceUtil.getCompanyGroup(
				CompanyThreadLocal.getCompanyId());
		}
		catch (Exception e) {
		}
	}

	protected ExportImportMessage doGetMessage(
		StagedModel stagedModel, Exception exception, String payload) {

		ExportImportMessage message = new ExportImportMessage();

		if (ExportImportThreadLocal.isExportInProcess()) {
			message.setDestination(DestinationNames.EXPORT);
		}
		else if (ExportImportThreadLocal.isImportInProcess()) {
			message.setDestination(DestinationNames.IMPORT);
		}
		else {
			return null;
		}

		if (stagedModel instanceof StagedGroupedModel) {
			StagedGroupedModel stagedGroupedModel =
				(StagedGroupedModel)stagedModel;

			message.setGroupId(stagedGroupedModel.getGroupId());
		}
		else {
			checkCompanyGroup();

			message.setGroupId(_companyGroup.getGroupId());
		}

		if (exception != null) {
			message.setException(exception);
		}

		message.setClassName(stagedModel.getModelClassName());
		message.setUuid(stagedModel.getUuid());
		message.setMessageLevel(ExportImportMessageLevel.MODEL);
		message.setPayload(payload);

		return message;
	}

	private static Group _companyGroup;

}