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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.digest.LarDigestEntry;
import com.liferay.portal.kernel.lar.digest.LarDigestModule;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.service.ServiceContext;

/**
 * @author Mate Thurzo
 */
public abstract interface StagedDataHandler<T extends BaseModel<T>> {

	public static final String ROOT_PATH_GROUPS = "/groups/";

	public static final String ROOT_PATH_PORTLETS = "/portlets/";

	public void addZipEntry(ZipWriter writer, String path, T object)
		throws SystemException;

	public ServiceContext createServiceContext(
		String path, ClassedModel classedModel, String namespace,
		DataHandlerContext context);

	public void export(
			T object, DataHandlerContext context,
			LarDigestModule parentPortletModule)
		throws Exception;

	public String getNamespace();

	public String getPermissionResourceName();

	public XStreamWrapper getXstreamWrapper();

	public byte[] getZipEntryAsByteArray(ZipReader reader, String path);

	public Object getZipEntryAsObject(ZipReader reader, String path);

	public String getZipEntryAsString(ZipReader reader, String path);

	public void importData(LarDigestEntry entry, DataHandlerContext context)
		throws Exception;

	public void setXstreamWrapper(XStreamWrapper xStreamWrapper);

}