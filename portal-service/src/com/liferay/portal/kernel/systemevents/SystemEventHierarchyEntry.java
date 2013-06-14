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

package com.liferay.portal.kernel.systemevents;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Zsolt Berentey
 */
public class SystemEventHierarchyEntry {

	public SystemEventHierarchyEntry(
		long classNameId, long classPK, long eventSetId, long eventId,
		long parentEventId, int action) {

		_action = action;
		_classNameId = classNameId;
		_classPK = classPK;
		_eventId = eventId;
		_eventSetId = eventSetId;
		_parentEventId = parentEventId;
	}

	public int getAction() {
		return _action;
	}

	public String getClassName() {
		return PortalUtil.getClassName(_classNameId);
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public long getEventId() {
		return _eventId;
	}

	public long getEventSetId() {
		return _eventSetId;
	}

	public String getExtraData() {
		if (_extraDataJSONObject == null) {
			return StringPool.BLANK;
		}

		return _extraDataJSONObject.toString();
	}

	public long getParentEventId() {
		return _parentEventId;
	}

	public String getUuid() {
		return _uuid;
	}

	public boolean isAsset(long classNameId, long classPK) {
		return ((_classNameId == classNameId) && (_classPK == classPK));
	}

	public boolean isAsset(String className, long classPK) {
		long classNameId = PortalUtil.getClassNameId(className);

		return isAsset(classNameId, classPK);
	}

	public void setClassName(String className) {
		_classNameId = PortalUtil.getClassNameId(className);
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;
	}

	public void setExtraDataValue(String key, String value) {
		if (_extraDataJSONObject == null) {
			_extraDataJSONObject = JSONFactoryUtil.createJSONObject();
		}

		_extraDataJSONObject.put(key, value);
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private int _action;
	private long _classNameId;
	private long _classPK;
	private long _eventId;
	private long _eventSetId;
	private JSONObject _extraDataJSONObject;
	private long _parentEventId;
	private String _uuid;

}