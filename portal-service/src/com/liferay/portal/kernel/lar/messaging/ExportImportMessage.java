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

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.text.DateFormat;

import java.util.Date;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessage implements Serializable {

	public ExportImportMessage() {
	}

	public ExportImportMessage(String message) throws JSONException {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(message);

		_action = jsonObject.getString(_ACTION);
		_message = jsonObject.getString(_MESSAGE);
		_portletDataHandlerClassName = jsonObject.getString(
			_PORTLET_DATA_HANDLER_CLASS_NAME);
		_stagedModelClassName = jsonObject.getString(_STAGED_MODEL_CLASS_NAME);
		_stagedModelUuid = jsonObject.getString(_STAGED_MODEL_UUID);
		_timestamp = GetterUtil.getDate(
			jsonObject.getString(_TIMESTAMP), _getDateFormat());
	}

	public String getAction() {

		return _action;
	}

	public String getMessage() {
		return _message;
	}

	public String getPortletDataHandlerClassName() {
		return _portletDataHandlerClassName;
	}

	public String getStagedModelClassName() {
		return _stagedModelClassName;
	}

	public String getStagedModelUuid() {
		return _stagedModelUuid;
	}

	public Date getTimestamp() {
		return _timestamp;
	}

	public void setAction(String action) {
		_action = action;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public void setPortletDataHandlerClassName(
		String portletDataHandlerClassName) {

		_portletDataHandlerClassName = portletDataHandlerClassName;
	}

	public void setStagedModelClassName(String stagedModelClassName) {
		_stagedModelClassName = stagedModelClassName;
	}

	public void setStagedModelUuid(String stagedModelUuid) {
		_stagedModelUuid = stagedModelUuid;
	}

	public void setTimestamp(Date timestamp) {
		_timestamp = timestamp;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put(_ACTION, _action);
		jsonObject.put(_MESSAGE, _message);
		jsonObject.put(
			_PORTLET_DATA_HANDLER_CLASS_NAME, _portletDataHandlerClassName);
		jsonObject.put(_STAGED_MODEL_CLASS_NAME, _stagedModelClassName);
		jsonObject.put(_STAGED_MODEL_UUID, _stagedModelUuid);
		jsonObject.put(_TIMESTAMP, _timestamp);

		return jsonObject;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{_action=");
		sb.append(_action);
		sb.append(", _message=");
		sb.append(_message);
		sb.append(", _portletDataHandlerClassName=");
		sb.append(_portletDataHandlerClassName);
		sb.append(", _stagedModelClassName=");
		sb.append(_stagedModelClassName);
		sb.append(", _stagedModelUuid=");
		sb.append(_stagedModelUuid);
		sb.append(", _timestamp=");
		sb.append(_timestamp);
		sb.append("}");

		return sb.toString();
	}

	private DateFormat _getDateFormat() {
		return DateFormatFactoryUtil.getSimpleDateFormat(_DATE_FORMAT);
	}

	private static final String _ACTION = "action";

	private static final String _DATE_FORMAT = "yyyyMMddkkmmssSSS";

	private static final String _MESSAGE = "message";

	private static final String _PORTLET_DATA_HANDLER_CLASS_NAME =
		"portletDataHandlerClassName";

	private static final String _STAGED_MODEL_CLASS_NAME =
		"stagedModelClassName";

	private static final String _STAGED_MODEL_UUID = "stagedModelUuid";

	private static final String _TIMESTAMP = "timestamp";

	private String _action;
	private String _message;
	private String _portletDataHandlerClassName;
	private String _stagedModelClassName;
	private String _stagedModelUuid;
	private Date _timestamp;

}