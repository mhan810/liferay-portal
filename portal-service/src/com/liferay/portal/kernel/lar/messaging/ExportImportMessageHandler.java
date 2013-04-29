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
import com.liferay.portal.model.StagedModel;

/**
 * @author Mate Thurzo
 */
public interface ExportImportMessageHandler {

	public void exportFinished(PortletDataContext portletDataContext);

	public void exportStarted(PortletDataContext portletDataContext);

	public void finished(
		PortletDataContext portletDataContext, StagedModel stagedModel);

	public void finished(
		PortletDataContext portletDataContext, String contentGroup);

	public void importFinished(PortletDataContext portletDataContext);

	public void importStarted(PortletDataContext portletDataContext);

	public void sendMessage(ExportImportMessage message);

	public void started(
		PortletDataContext portletDataContext, StagedModel stagedModel);

	public void started(
		PortletDataContext portletDataContext, String contentGroup);

}