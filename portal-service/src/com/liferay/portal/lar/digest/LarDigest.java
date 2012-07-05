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
import java.util.Map;

import javax.xml.stream.XMLStreamException;

/**
 * @author Daniel Kocsis
 */
public interface LarDigest extends Iterable<LarDigestItem> {

	public void addMetaData(Map<String, String> metadata) throws Exception;

	public void close() throws Exception;

	public List<LarDigestItem> findDigestItems(
		int action, String path, String type, String classPK);

	public File getDigestFile();

	public String getDigestString();

	public Map<String, String> getMetaData() throws Exception;

	public void write(LarDigestItem digestItem) throws Exception;

}