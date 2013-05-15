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
 * This class is used by SOAP remote services.
 *
 * @author    Brian Wing Shun Chan
 * @generated
 */
public class BackgroundTaskSoap implements Serializable {
	public static BackgroundTaskSoap toSoapModel(BackgroundTask model) {
		BackgroundTaskSoap soapModel = new BackgroundTaskSoap();

		soapModel.setBackgroundTaskId(model.getBackgroundTaskId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setType(model.getType());
		soapModel.setStatus(model.getStatus());
		soapModel.setName(model.getName());
		soapModel.setCompletedDate(model.getCompletedDate());
		soapModel.setData(model.getData());

		return soapModel;
	}

	public static BackgroundTaskSoap[] toSoapModels(BackgroundTask[] models) {
		BackgroundTaskSoap[] soapModels = new BackgroundTaskSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static BackgroundTaskSoap[][] toSoapModels(BackgroundTask[][] models) {
		BackgroundTaskSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new BackgroundTaskSoap[models.length][models[0].length];
		}
		else {
			soapModels = new BackgroundTaskSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static BackgroundTaskSoap[] toSoapModels(List<BackgroundTask> models) {
		List<BackgroundTaskSoap> soapModels = new ArrayList<BackgroundTaskSoap>(models.size());

		for (BackgroundTask model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new BackgroundTaskSoap[soapModels.size()]);
	}

	public BackgroundTaskSoap() {
	}

	public long getPrimaryKey() {
		return _backgroundTaskId;
	}

	public void setPrimaryKey(long pk) {
		setBackgroundTaskId(pk);
	}

	public long getBackgroundTaskId() {
		return _backgroundTaskId;
	}

	public void setBackgroundTaskId(long backgroundTaskId) {
		_backgroundTaskId = backgroundTaskId;
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

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Date getCompletedDate() {
		return _completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		_completedDate = completedDate;
	}

	public String getData() {
		return _data;
	}

	public void setData(String data) {
		_data = data;
	}

	private long _backgroundTaskId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _type;
	private int _status;
	private String _name;
	private Date _completedDate;
	private String _data;
}