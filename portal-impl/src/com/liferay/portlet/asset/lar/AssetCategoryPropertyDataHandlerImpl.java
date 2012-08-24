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

package com.liferay.portlet.asset.lar;

import com.liferay.portal.kernel.lar.StagedDataHandlerImpl;
import com.liferay.portlet.asset.model.AssetCategoryProperty;

/**
 * @author Mate Thurzo
 */
public class AssetCategoryPropertyDataHandlerImpl
	extends StagedDataHandlerImpl<AssetCategoryProperty>
	implements AssetCategoryPropertyDataHandler {

	/*public LarDigestItem doDigest(
			AssetCategoryProperty categoryProperty, DataHandlerContext context)
		throws Exception {

		/*LarDigest digest = context.getLarDigest();

		String path = getEntityPath(categoryProperty);

		if (!context.isPathProcessed(path)) {
			LarDigestItem digestItem = new LarDigestItemImpl();

			digestItem.setAction(getDigestAction(categoryProperty, context));
			digestItem.setPath(path);
			digestItem.setClassName(AssetCategoryProperty.class.getName());
			digestItem.setClassPK(
				StringUtil.valueOf(categoryProperty.getCategoryPropertyId()));

			return digestItem;
		}

		return null;
	}*/

}