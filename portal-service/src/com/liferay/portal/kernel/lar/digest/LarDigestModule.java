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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Kocsis
 */
public interface LarDigestModule extends LarDigestElement {

	public void addEntry(LarDigestEntry entry);

	public void addMetadata(LarDigestMetadata metadata);

	public void addPortletPreference(LarDigestPortletPreference preference);

	public Map<String, Set<LarDigestEntry>> getEntries();

	public Set<LarDigestEntry> getEntriesByClassName(String className);

	public List<LarDigestMetadata> getMetadata();

	public String getMetadataValue(String name);

	public String getName();

	public List<LarDigestPortletPreference> getPortletPreferences();

	public void setEntries(Map<String, Set<LarDigestEntry>> entries);

	public void setModuleEntries(Set<LarDigestEntry> entries);

	public void setName(String name);

	public void setPortletPreferences(
		List<LarDigestPortletPreference> portletPreferences);

}