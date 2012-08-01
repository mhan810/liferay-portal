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
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portlet.asset.model.AssetCategoryProperty;
import com.liferay.portlet.asset.service.AssetCategoryPropertyLocalServiceUtil;

/**
 * @author Mate Thurzo
 */
public class AssetCategoryPropertyDataHandlerImpl
	extends BaseDataHandlerImpl<AssetCategoryProperty>
	implements AssetCategoryPropertyDataHandler{

	@Override
	public LarDigestItem doDigest(AssetCategoryProperty categoryProperty)
		throws Exception {

		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(categoryProperty);

		if (!context.isPathProcessed(path)) {
			LarDigestItem digestItem = new LarDigestItemImpl();

			digestItem.setAction(getDigestAction(categoryProperty));
			digestItem.setPath(path);
			digestItem.setType(AssetCategoryProperty.class.getName());
			digestItem.setClassPK(
				StringUtil.valueOf(categoryProperty.getCategoryPropertyId()));

			return digestItem;
		}

		return null;
	}

	@Override
	public void doImportData(LarDigestItem item) throws Exception {
		return;
	}

	@Override
	public AssetCategoryProperty getEntity(String classPK) {
		if (Validator.isNull(classPK)) {
			return null;
		}

		try {
			long categoryPropertyId = Long.valueOf(classPK);

			AssetCategoryProperty categoryProperty =
				AssetCategoryPropertyLocalServiceUtil.getCategoryProperty(
					categoryPropertyId);

			return categoryProperty;
		}
		catch(Exception e) {
			return null;
		}
	}

}
