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

import com.liferay.portal.kernel.lar.LarPersistenceContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class LarPersistenceContextImpl implements LarPersistenceContext {

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
		return (Long)getAttribute(ATTRIBUTE_NAME_COMPANY_ID);
	}

	public Date getEndDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_END_DATE);
	}

	public long getGroupId() {
		return (Long)getAttribute(ATTRIBUTE_NAME_GROUP_ID);
	}

	public LarDigest getLarDigest() {
		return (LarDigest)getAttribute(ATTRIBUTE_NAME_LAR_DIGEST);
	}

	public Map<String, String[]> getParameters() {
		return _paramaters;
	}

	public long getScopeGroupId() {
		return (Long)getAttribute(ATTRIBUTE_NAME_SCOPE_GROUP_ID);
	}

	public Date getStartDate() {
		return (Date)getAttribute(ATTRIBUTE_NAME_START_DATE);
	}

	public User getUser() {
		return (User)getAttribute(ATTRIBUTE_NAME_USER);
	}

	public ZipWriter getZipWriter() {
		return (ZipWriter)getAttribute(ATTRIBUTE_NAME_ZIP_WRITER);
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

	public void setParameters(Map<String, String[]> parameters) {
		_paramaters = parameters;
	}

	public void setPrivateLayout(boolean privateLayout) {
		setAttribute(ATTRIBUTE_NAME_PRIVATE_LAYOUT, privateLayout);
	}

	public void setScopeGroupId(long scopeGroupId) {
		setAttribute(ATTRIBUTE_NAME_SCOPE_GROUP_ID, scopeGroupId);
	}

	public void setStartDate(Date startDate) {
		setAttribute(ATTRIBUTE_NAME_START_DATE, startDate);
	}

	public void setUser(User user) {
		setAttribute(ATTRIBUTE_NAME_USER, user);
	}

	public void setZipWriter(ZipWriter zipWriter) {
		setAttribute(ATTRIBUTE_NAME_ZIP_WRITER, zipWriter);
	}

	private boolean hasDateRange() {
		if (getStartDate() != null) {
			return true;
		}
		else {
			return false;
		}
	}

	private Map<String, Object> _attributes = new HashMap<String, Object>();
	private Map<String, String[]> _paramaters;

}