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

package com.liferay.portal.service.persistence;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.service.ServiceContext;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mate Thurzo
 */
public abstract interface BaseDataHandler<T extends BaseModel<T>> {

	public static final String ROOT_PATH_GROUPS = "/groups/";

	public static final String ROOT_PATH_PORTLETS = "/portlets/";

	public void addZipEntry(ZipWriter writer, String path, T object)
		throws SystemException;

	public ServiceContext createServiceContext(
		String path, ClassedModel classedModel, String namespace,
		DataHandlerContext context);

	public LarDigestItem digest(
			T object, DataHandlerContext context)
		throws Exception;

	public abstract LarDigestItem doDigest(
			T object, DataHandlerContext context)
		throws Exception;

	public abstract T getEntity(String classPK);

	public String getEntityPath(T object);

	public String getNamespace();

	public String getPermissionResourceName();

	public byte[] getZipEntryAsByteArray(ZipReader reader, String path);

	public Object getZipEntryAsObject(ZipReader reader, String path);

	public String getZipEntryAsString(ZipReader reader, String path);

	public void importData(LarDigestItem item, DataHandlerContext context)
		throws Exception;

	public XStreamWrapper getXstreamWrapper();

	public void serialize(LarDigestItem item, DataHandlerContext context);

	public void setXstreamWrapper(XStreamWrapper xStreamWrapper);

}
