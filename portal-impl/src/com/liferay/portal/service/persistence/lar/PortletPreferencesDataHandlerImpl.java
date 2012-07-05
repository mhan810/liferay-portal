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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;

/**
 * @author Mate Thurzo
 */
public class PortletPreferencesDataHandlerImpl
	extends BaseDataHandlerImpl<PortletPreferences>
	implements PortletPreferencesDataHandler {

	@Override
	public void doDigest(PortletPreferences preferences) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(preferences);

		if (!context.isPathProcessed(path)) {
			LarDigestItem digestItem = new LarDigestItemImpl();

			digestItem.setAction(LarDigesterConstants.ACTION_ADD);
			digestItem.setPath(path);
			digestItem.setType(PortletPreferences.class.getName());
			digestItem.setClassPK(
				StringUtil.valueOf(preferences.getPortletPreferencesId()));

			digest.write(digestItem);
		}
	}

	public PortletPreferences getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				long preferencesId = Long.valueOf(classPK);

				PortletPreferences preferences =
					PortletPreferencesLocalServiceUtil.getPortletPreferences(
						preferencesId);

				return preferences;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

}
