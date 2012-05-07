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

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;

import java.io.File;

import java.util.List;

/**
 * @author Daniel Kocsis
 */
public interface LarDigest {

	public void addChildEntry(Element child, Element parent);

	public Element addEntry(
		int action, String path, String type, String classPK);

	public Element addRootEntry(
		int action, String path, String type, String classPK);

	public List<Node> getAllNodes();

	public File getDigestFile();

	public List<Node> getEntriesByAction(int action);

	public List<Node> getEntriesByClassPK(String classPK);

	public Element getRootEntry();

	public void setAttribute(Element element, String name, String value);

}