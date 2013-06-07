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
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.SystemEventEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing SystemEventEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntry
 * @generated
 */
public class SystemEventEntryCacheModel implements CacheModel<SystemEventEntry>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{systemEventId=");
		sb.append(systemEventId);
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
		sb.append(", eventType=");
		sb.append(eventType);
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", classUuid=");
		sb.append(classUuid);
		sb.append(", eventSet=");
		sb.append(eventSet);
		sb.append(", eventId=");
		sb.append(eventId);
		sb.append(", parentEventId=");
		sb.append(parentEventId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public SystemEventEntry toEntityModel() {
		SystemEventEntryImpl systemEventEntryImpl = new SystemEventEntryImpl();

		systemEventEntryImpl.setSystemEventId(systemEventId);
		systemEventEntryImpl.setGroupId(groupId);
		systemEventEntryImpl.setCompanyId(companyId);
		systemEventEntryImpl.setUserId(userId);

		if (userName == null) {
			systemEventEntryImpl.setUserName(StringPool.BLANK);
		}
		else {
			systemEventEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			systemEventEntryImpl.setCreateDate(null);
		}
		else {
			systemEventEntryImpl.setCreateDate(new Date(createDate));
		}

		systemEventEntryImpl.setEventType(eventType);
		systemEventEntryImpl.setClassNameId(classNameId);
		systemEventEntryImpl.setClassPK(classPK);

		if (classUuid == null) {
			systemEventEntryImpl.setClassUuid(StringPool.BLANK);
		}
		else {
			systemEventEntryImpl.setClassUuid(classUuid);
		}

		if (eventSet == null) {
			systemEventEntryImpl.setEventSet(StringPool.BLANK);
		}
		else {
			systemEventEntryImpl.setEventSet(eventSet);
		}

		if (eventId == null) {
			systemEventEntryImpl.setEventId(StringPool.BLANK);
		}
		else {
			systemEventEntryImpl.setEventId(eventId);
		}

		if (parentEventId == null) {
			systemEventEntryImpl.setParentEventId(StringPool.BLANK);
		}
		else {
			systemEventEntryImpl.setParentEventId(parentEventId);
		}

		systemEventEntryImpl.resetOriginalValues();

		return systemEventEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		systemEventId = objectInput.readLong();
		groupId = objectInput.readLong();
		companyId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		eventType = objectInput.readInt();
		classNameId = objectInput.readLong();
		classPK = objectInput.readLong();
		classUuid = objectInput.readUTF();
		eventSet = objectInput.readUTF();
		eventId = objectInput.readUTF();
		parentEventId = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(systemEventId);
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
		objectOutput.writeInt(eventType);
		objectOutput.writeLong(classNameId);
		objectOutput.writeLong(classPK);

		if (classUuid == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(classUuid);
		}

		if (eventSet == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(eventSet);
		}

		if (eventId == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(eventId);
		}

		if (parentEventId == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(parentEventId);
		}
	}

	public long systemEventId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public int eventType;
	public long classNameId;
	public long classPK;
	public String classUuid;
	public String eventSet;
	public String eventId;
	public String parentEventId;
}