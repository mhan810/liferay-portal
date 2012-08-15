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

package com.liferay.portal.lar.digest;

import java.io.File;

import java.util.List;

/**
 * @author Daniel Kocsis
 */
public interface LarDigest {

	public void addMetadata(LarDigestMetadata metadata);

	public void addModule(LarDigestModule module);

	public void close() throws Exception;

	public LarDigestItem findDigestItem(
		int action, String path, String type, String classPK, String uuid);

	public List<LarDigestItem> findDigestItems(
		int action, String path, String type, String classPK, String uuid);

	public LarDigestModule findDigestModule(String name);

	public List<LarDigestModule> getAllModules();

	public File getDigestFile();

	public String getDigestString();

	public List<LarDigestMetadata> getMetaData();

	public String getMetadataValue(String name);

	public void write() throws Exception;

}