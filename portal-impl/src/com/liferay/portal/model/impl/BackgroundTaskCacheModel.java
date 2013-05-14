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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing BackgroundTask in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see BackgroundTask
 * @generated
 */
public class BackgroundTaskCacheModel implements CacheModel<BackgroundTask>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{backgroundTaskId=");
		sb.append(backgroundTaskId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", type=");
		sb.append(type);
		sb.append(", status=");
		sb.append(status);
		sb.append(", name=");
		sb.append(name);
		sb.append(", completedDate=");
		sb.append(completedDate);
		sb.append(", data=");
		sb.append(data);
		sb.append("}");

		return sb.toString();
	}

	public BackgroundTask toEntityModel() {
		BackgroundTaskImpl backgroundTaskImpl = new BackgroundTaskImpl();

		backgroundTaskImpl.setBackgroundTaskId(backgroundTaskId);
		backgroundTaskImpl.setGroupId(groupId);
		backgroundTaskImpl.setCompanyId(companyId);
		backgroundTaskImpl.setUserId(userId);

		if (userName == null) {
			backgroundTaskImpl.setUserName(StringPool.BLANK);
		}
		else {
			backgroundTaskImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			backgroundTaskImpl.setCreateDate(null);
		}
		else {
			backgroundTaskImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			backgroundTaskImpl.setModifiedDate(null);
		}
		else {
			backgroundTaskImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (type == null) {
			backgroundTaskImpl.setType(StringPool.BLANK);
		}
		else {
			backgroundTaskImpl.setType(type);
		}

		backgroundTaskImpl.setStatus(status);

		if (name == null) {
			backgroundTaskImpl.setName(StringPool.BLANK);
		}
		else {
			backgroundTaskImpl.setName(name);
		}

		if (completedDate == Long.MIN_VALUE) {
			backgroundTaskImpl.setCompletedDate(null);
		}
		else {
			backgroundTaskImpl.setCompletedDate(new Date(completedDate));
		}

		if (data == null) {
			backgroundTaskImpl.setData(StringPool.BLANK);
		}
		else {
			backgroundTaskImpl.setData(data);
		}

		backgroundTaskImpl.resetOriginalValues();

		return backgroundTaskImpl;
	}

	public void readExternal(ObjectInput objectInput) throws IOException {
		backgroundTaskId = objectInput.readLong();
		groupId = objectInput.readLong();
		companyId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		type = objectInput.readUTF();
		status = objectInput.readInt();
		name = objectInput.readUTF();
		completedDate = objectInput.readLong();
		data = objectInput.readUTF();
	}

	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(backgroundTaskId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (type == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(type);
		}

		objectOutput.writeInt(status);

		if (name == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(name);
		}

		objectOutput.writeLong(completedDate);

		if (data == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(data);
		}
	}

	public long backgroundTaskId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String type;
	public int status;
	public String name;
	public long completedDate;
	public String data;
}