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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.lar.ExportImportTask;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.BackgroundTaskConstants;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.ServiceContext;

import java.io.File;

import java.util.Date;
import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class PortletExportTask implements ExportImportTask {

	public PortletExportTask(BackgroundTask backgroundTask)
		throws PortalException {

		_backgroundTask = backgroundTask;

		JSONObject data = JSONFactoryUtil.createJSONObject(
			backgroundTask.getData());

		_plid = data.getLong("plid");
		_groupId = data.getLong("groupId");
		_portletId = data.getString("portletId");

		JSONObject jsonObject = data.getJSONObject("parameterMap");

		_parameterMap = (Map<String, String[]>)JSONFactoryUtil.deserialize(
			jsonObject);

		jsonObject = data.getJSONObject("startDate");

		if (jsonObject != null) {
			_startDate = (Date)JSONFactoryUtil.deserialize(jsonObject);
		}

		jsonObject = data.getJSONObject("endDate");

		if (jsonObject != null) {
			_endDate = (Date)JSONFactoryUtil.deserialize(jsonObject);
		}

	}

	public void run() throws PortalException, SystemException {
		JSONObject data = JSONFactoryUtil.createJSONObject(
			_backgroundTask.getData());

		BackgroundTaskLocalServiceUtil.updateTask(
			_backgroundTask.getBackgroundTaskId(),
			BackgroundTaskConstants.STATUS_IN_PROGRESS,
			_backgroundTask.getName(), data.toString(), new ServiceContext());

		int status = BackgroundTaskConstants.STATUS_SUCCEDED;

		File larFile = null;

		try {
			larFile = LayoutServiceUtil.exportPortletInfoAsFile(
				_plid, _groupId, _portletId, _parameterMap, _startDate,
				_endDate);
		}
		catch (Exception e) {
			data.putException(e);

			status = BackgroundTaskConstants.STATUS_FAILED;

			_log.error("Unable to run portlet export task", e);
		}

		data.put("larFile", larFile.getAbsolutePath());

		BackgroundTaskLocalServiceUtil.updateTask(
			_backgroundTask.getBackgroundTaskId(), status,
			_backgroundTask.getName(), data.toString(), new ServiceContext());
	}

	private static Log _log = LogFactoryUtil.getLog(LayoutsExportTask.class);

	private BackgroundTask _backgroundTask;
	private Date _endDate;
	private long _groupId;
	private Map<String, String[]> _parameterMap;
	private long _plid;
	private String _portletId;
	private Date _startDate;

}