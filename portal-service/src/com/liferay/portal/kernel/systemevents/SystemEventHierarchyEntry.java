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

	public long getEventId() {
		return _eventId;
	}

	public long getEventSetId() {
		return _eventSetId;
	}

	public long getParentEventId() {
		return _parentEventId;
	}

	public boolean isAsset(long classNameId, long classPK) {
		return ((_classNameId == classNameId) && (_classPK == classPK));
	}

	public boolean isAsset(String className, long classPK) {
		long classNameId = PortalUtil.getClassNameId(className);

		return isAsset(classNameId, classPK);
	}

	private int _action;
	private long _classNameId;
	private long _classPK;
	private long _eventId;
	private long _eventSetId;
	private long _parentEventId;

}