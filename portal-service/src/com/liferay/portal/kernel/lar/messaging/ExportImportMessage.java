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
import com.liferay.portal.kernel.messaging.MessageStatus;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessage extends MessageStatus {

	public ExportImportMessage() {
	}

	public ExportImportMessage(String json) throws JSONException {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

		_className = jsonObject.getString(_CLASS_NAME);
		setEndTime(jsonObject.getLong(_END_TIME));
		setExceptionMessage(jsonObject.getString(_EXCEPTION_MESSAGE));
		setExceptionStackTrace(jsonObject.getString(_EXCEPTION_STACKTRACE));
		_groupId = jsonObject.getLong(_GROUP_ID);
		_messageLevel =
			(ExportImportMessageLevel)JSONFactoryUtil.deserialize(
				jsonObject.getString(_MESSAGE_LEVEL));
		_uuid = jsonObject.getString(_UUID);
	}

	public ExportImportMessage(
		String statusMessage, String destination,
		ExportImportMessageLevel messageLevel) {

		setPayload(statusMessage);
		_destination = destination;
		_messageLevel = messageLevel;
	}

	public String getClassName() {
		return _className;
	}

	public String getDestination() {
		return _destination;
	}

	public long getGroupId() {
		return _groupId;
	}

	public ExportImportMessageLevel getMessageLevel() {
		return _messageLevel;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setDestination(String destination) {
		_destination = destination;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setMessageLevel(ExportImportMessageLevel messageLevel) {
		_messageLevel = messageLevel;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put(_CLASS_NAME, _className);
		jsonObject.put(_END_TIME, getEndTime());
		jsonObject.put(_EXCEPTION_MESSAGE, getExceptionMessage());
		jsonObject.put(_EXCEPTION_STACKTRACE, getExceptionStackTrace());
		jsonObject.put(_GROUP_ID, _groupId);
		jsonObject.put(
			_MESSAGE_LEVEL, JSONFactoryUtil.serialize(_messageLevel));
		jsonObject.put(_START_TIME, getStartTime());
		jsonObject.put(
			_STATUS_MESSAGE, JSONFactoryUtil.serialize(getPayload()));
		jsonObject.put(_UUID, _uuid);

		return jsonObject;
	}

	private static final String _CLASS_NAME = "className";

	private static final String _END_TIME = "endTime";

	private static final String _EXCEPTION_MESSAGE = "exceptionMessage";

	private static final String _EXCEPTION_STACKTRACE = "exceptionStackTrace";

	private static final String _GROUP_ID = "groupId";

	private static final String _MESSAGE_LEVEL = "messageLevel";

	private static final String _START_TIME = "startTime";

	private static final String _STATUS_MESSAGE = "statusMessage";

	private static final String _UUID = "uuid";

	private static final long serialVersionUID = 1986083020110801800L;

	private String _className;
	private String _destination;
	private long _groupId;
	private ExportImportMessageLevel _messageLevel;
	private String _uuid;

}