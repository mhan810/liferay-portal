/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.lar.digest.LarDigest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 */
public class DataHandlerContextImpl implements DataHandlerContext {

	public DataHandlerContextImpl(long companyId) {
		setCompanyId(companyId);

		_primaryKeys = new HashSet<String>();
	}

	public DataHandlerContextImpl(
			long companyId, long groupId, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws PortletDataException {

		setCompanyId(companyId);
		setGroupId(groupId);
		setScopeGroupId(groupId);
		setParameters(parameterMap);
		setStartDate(startDate);
		setEndDate(endDate);
	}

	public DataHandlerContextImpl(
		long companyId, long groupId, Map<String, String[]> parameterMap) {

		setCompanyId(companyId);
		setGroupId(groupId);
		setScopeGroupId(groupId);
		setParameters(parameterMap);
		_primaryKeys = new HashSet<String>();
		setDataStrategy(
			MapUtil.getString(
				parameterMap, PortletDataHandlerKeys.DATA_STRATEGY,
				PortletDataHandlerKeys.DATA_STRATEGY_MIRROR));
	}

	public boolean addPrimaryKey(Class<?> clazz, String primaryKey) {
		boolean value = hasPrimaryKey(clazz, primaryKey);

		if (!value) {
			_primaryKeys.add(getPrimaryKeyString(clazz, primaryKey));
		}

		return value;
	}

	public void addProcessedPath(String path) {
		if (!_storedPaths.contains(path)) {
			_storedPaths.add(path);
		}
	}

	public Object getAttribute(String key) {
		return _attributes.get(key);
	}

	public boolean getBooleanParameter(String namespace, String name) {
		boolean defaultValue = MapUtil.getBoolean(
			getParameters(),
			PortletDataHandlerKeys.PORTLET_DATA_CONTROL_DEFAULT, true);

		return MapUtil.getBoolean(
			getParameters(),
			PortletDataHandlerControl.getNamespacedControlName(namespace, name),
			defaultValue);
	}

	public long getCompanyId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_COMPANY_ID));
	}

	public String getDataStrategy() {
		return GetterUtil.getString(getAttribute(ATTRIBUTE_NAME_DATA_STRATEGY));
	}

	public Date getEndDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_END_DATE);
	}

	public long getGroupId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_GROUP_ID));
	}

	public LarDigest getLarDigest() {
		return (LarDigest)getAttribute(ATTRIBUTE_NAME_LAR_DIGEST);
	}

	public Date getLastPublishDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_LAST_PUBLISH_DATE);
	}

	public Map<?, ?> getNewPrimaryKeysMap(Class<?> clazz) {
		return getNewPrimaryKeysMap(clazz.getName());
	}

	public Map<?, ?> getNewPrimaryKeysMap(String className) {
		Map<?, ?> map = _newPrimaryKeysMaps.get(className);

		if (map == null) {
			map = new HashMap<Object, Object>();

			_newPrimaryKeysMaps.put(className, map);
		}

		return map;
	}

	public long getOldPlid() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_OLD_PLID));
	}

	public Map<String, String[]> getParameters() {
		return _paramaters;
	}

	public long getPlid() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_PLID));
	}

	public long getScopeGroupId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_SCOPE_GROUP_ID));
	}

	public String getScopeLayoutUuid() {
		return GetterUtil.getString(
			getAttribute(ATTRIBUTE_NAME_SCOPE_LAYOUT_UUID));
	}

	public String getScopeType() {
		return GetterUtil.getString(getAttribute(ATTRIBUTE_NAME_SCOPE_TYPE));
	}

	public long getSourceGroupId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_SOURCE_GROUP_ID));
	}

	public Date getStartDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_START_DATE);
	}

	public User getUser() {
		return (User)getAttribute(ATTRIBUTE_NAME_USER);
	}

	public long getUserId(String userUuid) {
		return getUserIdStrategy().getUserId(userUuid);
	}

	public UserIdStrategy getUserIdStrategy() {
		return (UserIdStrategy)getAttribute(ATTRIBUTE_NAME_USER_ID_STRATEGY);
	}

	public ZipReader getZipReader() {
		return (ZipReader)getAttribute(ATTRIBUTE_NAME_ZIP_READER);
	}

	public ZipWriter getZipWriter() {
		return (ZipWriter)getAttribute(ATTRIBUTE_NAME_ZIP_WRITER);
	}

	public boolean hasDateRange() {
		if (getStartDate() != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasNotUniquePerLayout(String dataKey) {
		return _notUniquePerLayout.contains(dataKey);
	}

	public boolean hasPrimaryKey(Class<?> clazz, String primaryKey) {
		return _primaryKeys.contains(getPrimaryKeyString(clazz, primaryKey));
	}

	public boolean isDataStrategyMirror() {
		if (getDataStrategy().equals(DATA_STRATEGY_MIRROR) ||
			getDataStrategy().equals(DATA_STRATEGY_MIRROR_OVERWRITE)) {

			return true;
		}
		else {
			return false;
		}
	}

	public boolean isDataStrategyMirrorWithOverwriting() {
		if (getDataStrategy().equals(DATA_STRATEGY_MIRROR_OVERWRITE)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isPathProcessed(String path) {
		if (_storedPaths.contains(path)) {
			return true;
		}

		return false;
	}

	public boolean isPrivateLayout() {
		return (Boolean)getAttribute(ATTRIBUTE_NAME_PRIVATE_LAYOUT);
	}

	public boolean isWithinDateRange(Date modifiedDate) {
		if (!hasDateRange()) {
			return true;
		}
		else if ((getStartDate().compareTo(modifiedDate) <= 0) &&
				getEndDate().after(modifiedDate)) {

			return true;
		}
		else {
			return false;
		}
	}

	public void putNotUniquePerLayout(String dataKey) {
		_notUniquePerLayout.add(dataKey);
	}

	public void resetAttribute(String key) {
		_attributes.remove(key);
	}

	public void setAttribute(String key, Object value) {
		_attributes.put(key, value);
	}

	public void setCompanyId(long companyId) {
		setAttribute(ATTRIBUTE_NAME_COMPANY_ID, companyId);
	}

	public void setDataStrategy(String dataStrategy) {
		setAttribute(ATTRIBUTE_NAME_DATA_STRATEGY, dataStrategy);
	}

	public void setEndDate(Date endDate) {
		setAttribute(ATTRIBUTE_NAME_END_DATE, endDate);
	}

	public void setGroupId(long groupId) {
		setAttribute(ATTRIBUTE_NAME_GROUP_ID, groupId);
	}

	public void setLarDigest(LarDigest digest) {
		setAttribute(ATTRIBUTE_NAME_LAR_DIGEST, digest);
	}

	public void setLastPublishDate(Date lastPublishDate) {
		setAttribute(ATTRIBUTE_NAME_LAST_PUBLISH_DATE, lastPublishDate);
	}

	public void setOldPlid(long oldPlid) {
		setAttribute(ATTRIBUTE_NAME_OLD_PLID, oldPlid);
	}

	public void setParameters(Map<String, String[]> parameters) {
		_paramaters = parameters;
	}

	public void setPlid(long plid) {
		setAttribute(ATTRIBUTE_NAME_PLID, plid);
	}

	public void setPrivateLayout(boolean privateLayout) {
		setAttribute(ATTRIBUTE_NAME_PRIVATE_LAYOUT, privateLayout);
	}

	public void setScopeGroupId(long scopeGroupId) {
		setAttribute(ATTRIBUTE_NAME_SCOPE_GROUP_ID, scopeGroupId);
	}

	public void setScopeLayoutUuid(String scopeLayoutUuid) {
		setAttribute(ATTRIBUTE_NAME_SCOPE_LAYOUT_UUID, scopeLayoutUuid);
	}

	public void setScopeType(String scopeType) {
		setAttribute(ATTRIBUTE_NAME_SCOPE_TYPE, scopeType);
	}

	public void setSourceGroupId(long sourceGroupId) {
		setAttribute(ATTRIBUTE_NAME_SOURCE_GROUP_ID, sourceGroupId);
	}

	public void setStartDate(Date startDate) {
		setAttribute(ATTRIBUTE_NAME_START_DATE, startDate);
	}

	public void setUser(User user) {
		setAttribute(ATTRIBUTE_NAME_USER, user);
	}

	public void setUserIdStrategy(UserIdStrategy userIdStrategy) {
		setAttribute(ATTRIBUTE_NAME_USER_ID_STRATEGY, userIdStrategy);
	}

	public void setZipReader(ZipReader zipReader) {
		setAttribute(ATTRIBUTE_NAME_ZIP_READER, zipReader);
	}

	public void setZipWriter(ZipWriter zipWriter) {
		setAttribute(ATTRIBUTE_NAME_ZIP_WRITER, zipWriter);
	}

	protected String getPrimaryKeyString(Class<?> clazz, long classPK) {
		return getPrimaryKeyString(clazz.getName(), String.valueOf(classPK));
	}

	protected String getPrimaryKeyString(Class<?> clazz, String primaryKey) {
		return getPrimaryKeyString(clazz.getName(), primaryKey);
	}

	protected String getPrimaryKeyString(String className, long classPK) {
		return getPrimaryKeyString(className, String.valueOf(classPK));
	}

	protected String getPrimaryKeyString(String className, String primaryKey) {
		return className.concat(StringPool.POUND).concat(primaryKey);
	}

	private Map<String, Object> _attributes = new HashMap<String, Object>();
	private Map<String, Map<?, ?>> _newPrimaryKeysMaps =
		new HashMap<String, Map<?, ?>>();
	private Set<String> _notUniquePerLayout = new HashSet<String>();
	private Map<String, String[]> _paramaters;
	private Set<String> _primaryKeys;
	private Set<String> _storedPaths = new HashSet<String>();

}