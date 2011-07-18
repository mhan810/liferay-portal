/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.mobile.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author    Edward C. Han
 * @generated
 */
public class DeviceProfileSoap implements Serializable {
	public static DeviceProfileSoap toSoapModel(DeviceProfile model) {
		DeviceProfileSoap soapModel = new DeviceProfileSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setDeviceProfileId(model.getDeviceProfileId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setName(model.getName());
		soapModel.setDescription(model.getDescription());

		return soapModel;
	}

	public static DeviceProfileSoap[] toSoapModels(DeviceProfile[] models) {
		DeviceProfileSoap[] soapModels = new DeviceProfileSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DeviceProfileSoap[][] toSoapModels(DeviceProfile[][] models) {
		DeviceProfileSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new DeviceProfileSoap[models.length][models[0].length];
		}
		else {
			soapModels = new DeviceProfileSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DeviceProfileSoap[] toSoapModels(List<DeviceProfile> models) {
		List<DeviceProfileSoap> soapModels = new ArrayList<DeviceProfileSoap>(models.size());

		for (DeviceProfile model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DeviceProfileSoap[soapModels.size()]);
	}

	public DeviceProfileSoap() {
	}

	public long getPrimaryKey() {
		return _deviceProfileId;
	}

	public void setPrimaryKey(long pk) {
		setDeviceProfileId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getDeviceProfileId() {
		return _deviceProfileId;
	}

	public void setDeviceProfileId(long deviceProfileId) {
		_deviceProfileId = deviceProfileId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	private String _uuid;
	private long _deviceProfileId;
	private long _groupId;
	private String _name;
	private String _description;
}