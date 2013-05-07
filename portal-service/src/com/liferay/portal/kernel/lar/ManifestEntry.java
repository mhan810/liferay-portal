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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.ClassedModel;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public interface ManifestEntry {

	public void addModelAttribute(String key, String value);

	public void addReference(ManifestEntryReference manifestEntryReference);

	public void addReferencedModel(ClassedModel classedModel, int type);

	public void addReferencedModel(
		ClassedModel classedModel, String path, int type);

	public Element asXmlElement();
	public long getGroupId();

	public ClassedModel getModel();

	public String getModelAttribute(String key);

	public Map<String, String> getModelAttributes();

	public String getModelClassName();

	public String getPath();

	public List<ManifestEntryReference> getReferencedEntries();

	public String getUuid();

	public void setModel(ClassedModel classedModel, String path);

}