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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.*;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.digest.LarDigest;
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

	public Date getEndDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_END_DATE);
	}

	public long getGroupId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_GROUP_ID));
	}

	public LarDigest getLarDigest() {
		return (LarDigest)getAttribute(ATTRIBUTE_NAME_LAR_DIGEST);
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

	public long getSourceGroupId() {
		return GetterUtil.getLong(getAttribute(ATTRIBUTE_NAME_SOURCE_GROUP_ID));
	}

	public Date getStartDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_START_DATE);
	}

	public String getDataStrategy() {
		return GetterUtil.getString(getAttribute(ATTRIBUTE_NAME_DATA_STRATEGY));
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

	public boolean isPathProcessed(String path) {
		if (_storedPaths.contains(path)) {
			return true;
		}

		return false;
	}

	public boolean isPrivateLayout() {
		return (Boolean)getAttribute(ATTRIBUTE_NAME_PRIVATE_LAYOUT);
	}

	public boolean hasDateRange() {
		if (getStartDate() != null) {
			return true;
		}
		else {
			return false;
		}
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

	public void resetAttribute(String key) {
		_attributes.remove(key);
	}

	public void setAttribute(String key, Object value) {
		_attributes.put(key, value);
	}

	public void setCompanyId(long companyId) {
		setAttribute(ATTRIBUTE_NAME_COMPANY_ID, companyId);
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

	public void setOldPlid(long oldPlid) {
		setAttribute(ATTRIBUTE_NAME_OLD_PLID, oldPlid);
	};

	public void setParameters(Map<String, String[]> parameters) {
		_paramaters = parameters;
	}

	public void setPlid(long plid) {
		setAttribute(ATTRIBUTE_NAME_PLID, plid);
	};

	public void setPrivateLayout(boolean privateLayout) {
		setAttribute(ATTRIBUTE_NAME_PRIVATE_LAYOUT, privateLayout);
	}

	public void setScopeGroupId(long scopeGroupId) {
		setAttribute(ATTRIBUTE_NAME_SCOPE_GROUP_ID, scopeGroupId);
	}

	public void setSourceGroupId(long sourceGroupId) {
		setAttribute(ATTRIBUTE_NAME_SOURCE_GROUP_ID, sourceGroupId);
	}

	public void setStartDate(Date startDate) {
		setAttribute(ATTRIBUTE_NAME_START_DATE, startDate);
	}

	public void setDataStrategy(String dataStrategy) {
		setAttribute(ATTRIBUTE_NAME_DATA_STRATEGY, dataStrategy);
	}

	public void setUser(User user) {
		setAttribute(ATTRIBUTE_NAME_USER, user);
	}

	public void setUserIdStrategy(UserIdStrategy strategy) {
		setAttribute(ATTRIBUTE_NAME_USER_ID_STRATEGY, strategy);
	}

	public void setZipReader(ZipReader zipReader) {
		setAttribute(ATTRIBUTE_NAME_ZIP_READER, zipReader);
	}

	public void setZipWriter(ZipWriter zipWriter) {
		setAttribute(ATTRIBUTE_NAME_ZIP_WRITER, zipWriter);
	}

	private Map<String, Object> _attributes = new HashMap<String, Object>();
	private Map<String, String[]> _paramaters;
	private Set<String> _storedPaths = new HashSet<String>();
	private Map<String, Map<?, ?>> _newPrimaryKeysMaps =
		new HashMap<String, Map<?, ?>>();

}