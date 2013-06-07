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

package com.liferay.portal.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.portal.service.http.SystemEventEntryServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.http.SystemEventEntryServiceSoap
 * @generated
 */
public class SystemEventEntrySoap implements Serializable {
	public static SystemEventEntrySoap toSoapModel(SystemEventEntry model) {
		SystemEventEntrySoap soapModel = new SystemEventEntrySoap();

		soapModel.setSystemEventId(model.getSystemEventId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setEventType(model.getEventType());
		soapModel.setClassNameId(model.getClassNameId());
		soapModel.setClassPK(model.getClassPK());
		soapModel.setClassUuid(model.getClassUuid());
		soapModel.setEventSet(model.getEventSet());
		soapModel.setEventId(model.getEventId());
		soapModel.setParentEventId(model.getParentEventId());

		return soapModel;
	}

	public static SystemEventEntrySoap[] toSoapModels(SystemEventEntry[] models) {
		SystemEventEntrySoap[] soapModels = new SystemEventEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static SystemEventEntrySoap[][] toSoapModels(
		SystemEventEntry[][] models) {
		SystemEventEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new SystemEventEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new SystemEventEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static SystemEventEntrySoap[] toSoapModels(
		List<SystemEventEntry> models) {
		List<SystemEventEntrySoap> soapModels = new ArrayList<SystemEventEntrySoap>(models.size());

		for (SystemEventEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new SystemEventEntrySoap[soapModels.size()]);
	}

	public SystemEventEntrySoap() {
	}

	public long getPrimaryKey() {
		return _systemEventId;
	}

	public void setPrimaryKey(long pk) {
		setSystemEventId(pk);
	}

	public long getSystemEventId() {
		return _systemEventId;
	}

	public void setSystemEventId(long systemEventId) {
		_systemEventId = systemEventId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public int getEventType() {
		return _eventType;
	}

	public void setEventType(int eventType) {
		_eventType = eventType;
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;
	}

	public long getClassPK() {
		return _classPK;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public String getClassUuid() {
		return _classUuid;
	}

	public void setClassUuid(String classUuid) {
		_classUuid = classUuid;
	}

	public String getEventSet() {
		return _eventSet;
	}

	public void setEventSet(String eventSet) {
		_eventSet = eventSet;
	}

	public String getEventId() {
		return _eventId;
	}

	public void setEventId(String eventId) {
		_eventId = eventId;
	}

	public String getParentEventId() {
		return _parentEventId;
	}

	public void setParentEventId(String parentEventId) {
		_parentEventId = parentEventId;
	}

	private long _systemEventId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private int _eventType;
	private long _classNameId;
	private long _classPK;
	private String _classUuid;
	private String _eventSet;
	private String _eventId;
	private String _parentEventId;
}