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

import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.model.User;

import java.util.Date;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public interface LarPersistenceContext {

	public static final String ATTRIBUTE_NAME_COMPANY_ID = "COMPANY_ID";

	public static final String ATTRIBUTE_NAME_END_DATE = "END_DATE";

	public static final String ATTRIBUTE_NAME_GROUP_ID = "GROUP_ID";

	public static final String ATTRIBUTE_NAME_LAR_DIGEST = "LAR_DIGEST";

	public static final String ATTRIBUTE_NAME_PRIVATE_LAYOUT = "PRIVATE_LAYOUT";

	public static final String ATTRIBUTE_NAME_SCOPE_GROUP_ID = "SCOPE_GROUP_ID";

	public static final String ATTRIBUTE_NAME_SOURCE_GROUP_ID =
		"SOURCE_GROUP_ID";

	public static final String ATTRIBUTE_NAME_START_DATE = "START_DATE";

	public static final String ATTRIBUTE_NAME_USER = "USER";

	public static final String ATTRIBUTE_NAME_ZIP_READER = "ZIP_READER";

	public static final String ATTRIBUTE_NAME_ZIP_WRITER = "ZIP_WRITER";

	public Object getAttribute(String key);

	public boolean getBooleanParameter(String namespace, String name);

	public long getCompanyId();

	public Date getEndDate();

	public long getGroupId();

	public LarDigest getLarDigest();

	public Map<?, ?> getNewPrimaryKeysMap(Class<?> clazz);

	public Map<?, ?> getNewPrimaryKeysMap(String className);

	public Map<String, String[]> getParameters();

	public long getScopeGroupId();

	public long getSourceGroupId();

	public Date getStartDate();

	public User getUser();

	public ZipReader getZipReader();

	public ZipWriter getZipWriter();

	public boolean hasDateRange();

	public boolean isPrivateLayout();

	public boolean isWithinDateRange(Date modifiedDate);

	public void setAttribute(String key, Object value);

	public void setCompanyId(long companyId);

	public void setEndDate(Date endDate);

	public void setGroupId(long groupId);

	public void setLarDigest(LarDigest digest);

	public void setParameters(Map<String, String[]> parameters);

	public void setPrivateLayout(boolean privateLayout);

	public void setScopeGroupId(long scopeGroupId);

	public void setSourceGroupId(long sourceGroupId);

	public void setStartDate(Date startDate);

	public void setUser(User user);

	public void setZipReader(ZipReader zipReader);

	public void setZipWriter(ZipWriter zipWriter);

}