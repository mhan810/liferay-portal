/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.exportimport.resources.importer.internal.portlet.preferences;

import com.liferay.exportimport.resources.importer.portlet.preferences.PortletPreferencesRetriever;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {"rootPortletId=com_liferay_rss_web_portlet_RSSPortlet"},
	service = PortletPreferencesRetriever.class
)
public class RSSPortletPreferencesRetriever
	implements PortletPreferencesRetriever {

	@Override
	public void updatePortletPreferences(
			JSONObject portletPreferencesJSONObject, String key,
			PortletPreferences portletPreferences)
		throws PortletException {

		if (!key.equals("urls") && !key.equals("titles")) {
			String value = portletPreferencesJSONObject.getString(key);
			portletPreferences.setValue(key, value);

			return;
		}

		List<String> preferenceValues = new ArrayList<>();

		JSONObject preferenceValueJSONObject =
			portletPreferencesJSONObject.getJSONObject(key);

		Iterator<String> jsonObjectIterator = preferenceValueJSONObject.keys();

		while (jsonObjectIterator.hasNext()) {
			String objectKeyString = jsonObjectIterator.next();

			preferenceValues.add(
				preferenceValueJSONObject.getString(objectKeyString));
		}

		portletPreferences.setValues(
			key, preferenceValues.toArray(new String[preferenceValues.size()]));
	}

}