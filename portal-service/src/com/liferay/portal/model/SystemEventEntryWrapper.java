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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link SystemEventEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SystemEventEntry
 * @generated
 */
public class SystemEventEntryWrapper implements SystemEventEntry,
	ModelWrapper<SystemEventEntry> {
	public SystemEventEntryWrapper(SystemEventEntry systemEventEntry) {
		_systemEventEntry = systemEventEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return SystemEventEntry.class;
	}

	@Override
	public String getModelClassName() {
		return SystemEventEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("systemEventId", getSystemEventId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("eventType", getEventType());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("classUuid", getClassUuid());
		attributes.put("eventSet", getEventSet());
		attributes.put("eventId", getEventId());
		attributes.put("parentEventId", getParentEventId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long systemEventId = (Long)attributes.get("systemEventId");

		if (systemEventId != null) {
			setSystemEventId(systemEventId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Integer eventType = (Integer)attributes.get("eventType");

		if (eventType != null) {
			setEventType(eventType);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		String classUuid = (String)attributes.get("classUuid");

		if (classUuid != null) {
			setClassUuid(classUuid);
		}

		String eventSet = (String)attributes.get("eventSet");

		if (eventSet != null) {
			setEventSet(eventSet);
		}

		String eventId = (String)attributes.get("eventId");

		if (eventId != null) {
			setEventId(eventId);
		}

		String parentEventId = (String)attributes.get("parentEventId");

		if (parentEventId != null) {
			setParentEventId(parentEventId);
		}
	}

	/**
	* Returns the primary key of this system event entry.
	*
	* @return the primary key of this system event entry
	*/
	@Override
	public long getPrimaryKey() {
		return _systemEventEntry.getPrimaryKey();
	}

	/**
	* Sets the primary key of this system event entry.
	*
	* @param primaryKey the primary key of this system event entry
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_systemEventEntry.setPrimaryKey(primaryKey);
	}

	/**
	* Returns the system event ID of this system event entry.
	*
	* @return the system event ID of this system event entry
	*/
	@Override
	public long getSystemEventId() {
		return _systemEventEntry.getSystemEventId();
	}

	/**
	* Sets the system event ID of this system event entry.
	*
	* @param systemEventId the system event ID of this system event entry
	*/
	@Override
	public void setSystemEventId(long systemEventId) {
		_systemEventEntry.setSystemEventId(systemEventId);
	}

	/**
	* Returns the group ID of this system event entry.
	*
	* @return the group ID of this system event entry
	*/
	@Override
	public long getGroupId() {
		return _systemEventEntry.getGroupId();
	}

	/**
	* Sets the group ID of this system event entry.
	*
	* @param groupId the group ID of this system event entry
	*/
	@Override
	public void setGroupId(long groupId) {
		_systemEventEntry.setGroupId(groupId);
	}

	/**
	* Returns the company ID of this system event entry.
	*
	* @return the company ID of this system event entry
	*/
	@Override
	public long getCompanyId() {
		return _systemEventEntry.getCompanyId();
	}

	/**
	* Sets the company ID of this system event entry.
	*
	* @param companyId the company ID of this system event entry
	*/
	@Override
	public void setCompanyId(long companyId) {
		_systemEventEntry.setCompanyId(companyId);
	}

	/**
	* Returns the user ID of this system event entry.
	*
	* @return the user ID of this system event entry
	*/
	@Override
	public long getUserId() {
		return _systemEventEntry.getUserId();
	}

	/**
	* Sets the user ID of this system event entry.
	*
	* @param userId the user ID of this system event entry
	*/
	@Override
	public void setUserId(long userId) {
		_systemEventEntry.setUserId(userId);
	}

	/**
	* Returns the user uuid of this system event entry.
	*
	* @return the user uuid of this system event entry
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public java.lang.String getUserUuid()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _systemEventEntry.getUserUuid();
	}

	/**
	* Sets the user uuid of this system event entry.
	*
	* @param userUuid the user uuid of this system event entry
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_systemEventEntry.setUserUuid(userUuid);
	}

	/**
	* Returns the user name of this system event entry.
	*
	* @return the user name of this system event entry
	*/
	@Override
	public java.lang.String getUserName() {
		return _systemEventEntry.getUserName();
	}

	/**
	* Sets the user name of this system event entry.
	*
	* @param userName the user name of this system event entry
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_systemEventEntry.setUserName(userName);
	}

	/**
	* Returns the create date of this system event entry.
	*
	* @return the create date of this system event entry
	*/
	@Override
	public java.util.Date getCreateDate() {
		return _systemEventEntry.getCreateDate();
	}

	/**
	* Sets the create date of this system event entry.
	*
	* @param createDate the create date of this system event entry
	*/
	@Override
	public void setCreateDate(java.util.Date createDate) {
		_systemEventEntry.setCreateDate(createDate);
	}

	/**
	* Returns the event type of this system event entry.
	*
	* @return the event type of this system event entry
	*/
	@Override
	public int getEventType() {
		return _systemEventEntry.getEventType();
	}

	/**
	* Sets the event type of this system event entry.
	*
	* @param eventType the event type of this system event entry
	*/
	@Override
	public void setEventType(int eventType) {
		_systemEventEntry.setEventType(eventType);
	}

	/**
	* Returns the fully qualified class name of this system event entry.
	*
	* @return the fully qualified class name of this system event entry
	*/
	@Override
	public java.lang.String getClassName() {
		return _systemEventEntry.getClassName();
	}

	@Override
	public void setClassName(java.lang.String className) {
		_systemEventEntry.setClassName(className);
	}

	/**
	* Returns the class name ID of this system event entry.
	*
	* @return the class name ID of this system event entry
	*/
	@Override
	public long getClassNameId() {
		return _systemEventEntry.getClassNameId();
	}

	/**
	* Sets the class name ID of this system event entry.
	*
	* @param classNameId the class name ID of this system event entry
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_systemEventEntry.setClassNameId(classNameId);
	}

	/**
	* Returns the class p k of this system event entry.
	*
	* @return the class p k of this system event entry
	*/
	@Override
	public long getClassPK() {
		return _systemEventEntry.getClassPK();
	}

	/**
	* Sets the class p k of this system event entry.
	*
	* @param classPK the class p k of this system event entry
	*/
	@Override
	public void setClassPK(long classPK) {
		_systemEventEntry.setClassPK(classPK);
	}

	/**
	* Returns the class uuid of this system event entry.
	*
	* @return the class uuid of this system event entry
	*/
	@Override
	public java.lang.String getClassUuid() {
		return _systemEventEntry.getClassUuid();
	}

	/**
	* Sets the class uuid of this system event entry.
	*
	* @param classUuid the class uuid of this system event entry
	*/
	@Override
	public void setClassUuid(java.lang.String classUuid) {
		_systemEventEntry.setClassUuid(classUuid);
	}

	/**
	* Returns the event set of this system event entry.
	*
	* @return the event set of this system event entry
	*/
	@Override
	public java.lang.String getEventSet() {
		return _systemEventEntry.getEventSet();
	}

	/**
	* Sets the event set of this system event entry.
	*
	* @param eventSet the event set of this system event entry
	*/
	@Override
	public void setEventSet(java.lang.String eventSet) {
		_systemEventEntry.setEventSet(eventSet);
	}

	/**
	* Returns the event ID of this system event entry.
	*
	* @return the event ID of this system event entry
	*/
	@Override
	public java.lang.String getEventId() {
		return _systemEventEntry.getEventId();
	}

	/**
	* Sets the event ID of this system event entry.
	*
	* @param eventId the event ID of this system event entry
	*/
	@Override
	public void setEventId(java.lang.String eventId) {
		_systemEventEntry.setEventId(eventId);
	}

	/**
	* Returns the parent event ID of this system event entry.
	*
	* @return the parent event ID of this system event entry
	*/
	@Override
	public java.lang.String getParentEventId() {
		return _systemEventEntry.getParentEventId();
	}

	/**
	* Sets the parent event ID of this system event entry.
	*
	* @param parentEventId the parent event ID of this system event entry
	*/
	@Override
	public void setParentEventId(java.lang.String parentEventId) {
		_systemEventEntry.setParentEventId(parentEventId);
	}

	@Override
	public boolean isNew() {
		return _systemEventEntry.isNew();
	}

	@Override
	public void setNew(boolean n) {
		_systemEventEntry.setNew(n);
	}

	@Override
	public boolean isCachedModel() {
		return _systemEventEntry.isCachedModel();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_systemEventEntry.setCachedModel(cachedModel);
	}

	@Override
	public boolean isEscapedModel() {
		return _systemEventEntry.isEscapedModel();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _systemEventEntry.getPrimaryKeyObj();
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_systemEventEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _systemEventEntry.getExpandoBridge();
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_systemEventEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_systemEventEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_systemEventEntry.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public java.lang.Object clone() {
		return new SystemEventEntryWrapper((SystemEventEntry)_systemEventEntry.clone());
	}

	@Override
	public int compareTo(
		com.liferay.portal.model.SystemEventEntry systemEventEntry) {
		return _systemEventEntry.compareTo(systemEventEntry);
	}

	@Override
	public int hashCode() {
		return _systemEventEntry.hashCode();
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.portal.model.SystemEventEntry> toCacheModel() {
		return _systemEventEntry.toCacheModel();
	}

	@Override
	public com.liferay.portal.model.SystemEventEntry toEscapedModel() {
		return new SystemEventEntryWrapper(_systemEventEntry.toEscapedModel());
	}

	@Override
	public com.liferay.portal.model.SystemEventEntry toUnescapedModel() {
		return new SystemEventEntryWrapper(_systemEventEntry.toUnescapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _systemEventEntry.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _systemEventEntry.toXmlString();
	}

	@Override
	public void persist()
		throws com.liferay.portal.kernel.exception.SystemException {
		_systemEventEntry.persist();
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	public SystemEventEntry getWrappedSystemEventEntry() {
		return _systemEventEntry;
	}

	@Override
	public SystemEventEntry getWrappedModel() {
		return _systemEventEntry;
	}

	@Override
	public void resetOriginalValues() {
		_systemEventEntry.resetOriginalValues();
	}

	private SystemEventEntry _systemEventEntry;
}