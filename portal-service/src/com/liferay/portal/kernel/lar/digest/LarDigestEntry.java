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

package com.liferay.portal.kernel.lar.digest;

import com.liferay.portal.lar.AssetAction;

import java.util.List;

/**
 * @author Mate Thurzo
 */
public interface LarDigestEntry extends LarDigestElement {

	public void addDependency(LarDigestEntryDependency dependency);

	public void addMetadata(LarDigestMetadata metadata);

	public AssetAction getAction();

	public String getClassName();

	public String getClassPK();

	public List<LarDigestEntryDependency> getDependencies();

	public List<LarDigestEntryDependency> getDependencies(String className);

	public List<LarDigestMetadata> getMetadata();

	public String getMetadataValue(String name);

	public String getPath();

	public String getUuid();

	public void setAction(AssetAction action);

	public void setClassName(String className);

	public void setClassPK(String classPK);

	public void setPath(String path);

	public void setUuid(String uuid);

}