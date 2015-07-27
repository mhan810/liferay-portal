/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.exportimport.staging;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portlet.exportimport.lar.MissingReference;
import com.liferay.portlet.exportimport.model.ExportImportConfiguration;

import java.io.Serializable;

import java.util.Locale;
import java.util.Map;

/**
 * @author Andrew Betts
 */
public class StagingBackgroundTaskDisplayHelperUtil {

	public static JSONArray getErrorMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _stagingBackgroundTaskHelper.getErrorMessagesJSONArray(
			locale, missingReferences);
	}

	public static JSONObject getExceptionMessagesJSONObject(
		Locale locale, Exception e,
		ExportImportConfiguration exportImportConfiguration) {

		return _stagingBackgroundTaskHelper.getExceptionMessagesJSONObject(
			locale, e, exportImportConfiguration);
	}

	public static JSONArray getWarningMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _stagingBackgroundTaskHelper.getWarningMessagesJSONArray(
			locale, missingReferences);
	}

	private static StagingBackgroundTaskDisplayHelper
		_stagingBackgroundTaskHelper = ProxyFactory.newServiceTrackedInstance(
			StagingBackgroundTaskDisplayHelper.class);
}