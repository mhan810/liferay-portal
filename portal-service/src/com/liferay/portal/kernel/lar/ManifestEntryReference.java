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

/**
 * @author Daniel Kocsis
 */
public interface ManifestEntryReference {

	public static int REFERENCE_TYPE_HARD = 1;

	public static int REFERENCE_TYPE_SOFT = 0;

	public Element asXmlElement();

	public long getGroupId();

	public String getPath();

	public int getType();

	public String getUuid();

	public void setGroupId(long groupId);

	public void setPath(String path);

	public void setType(int type);

	public void setUuid(String uuid);

}