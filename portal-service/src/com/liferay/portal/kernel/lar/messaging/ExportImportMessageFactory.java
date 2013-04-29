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

import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public interface ExportImportMessageFactory {

	public static final String PROCESS_EXPORT = "EXPORT";
	public static final String PROCESS_IMPORT = "IMPORT";

	public ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception);

	public ExportImportMessage getMessage(
		StagedModel stagedModel, Exception exception, String message);

	public ExportImportMessage getMessage(
		StagedModel stagedModel, String message);

	public ExportImportMessage getMessage(
		String portletId, Exception exception);

	public ExportImportMessage getMessage(
		String portletId, Exception exception, String message);

	public ExportImportMessage getMessage(String portletId, String message);

	public ExportImportMessage getProcessMessage(
		String process, Exception exception);

	public ExportImportMessage getProcessMessage(
		String process, Exception exception, String message);
	public ExportImportMessage getProcessMessage(
		String process, String message);

}