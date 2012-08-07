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

import java.util.List;

/**
 * @author Daniel Kocsis
 */
public interface LarDigestModule extends LarDigestElement {

	public void addItem(LarDigestItem item);

	public void addPortletPreference(String path);

	public List<LarDigestItem> getItems();

	public String getName();

	public List<String> getPortletPreferences();

	public void setItems(List<LarDigestItem> items);

	public void setName(String name);

	public void setPortletPreferences(List<String> portletPreferences);

}