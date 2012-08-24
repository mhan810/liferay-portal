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
import com.liferay.portlet.asset.model.AssetCategory;

/**
 * @author Mate Thurzo
 */
public class AssetCategoryDataHandlerImpl
	extends StagedDataHandlerImpl<AssetCategory>
	implements AssetCategoryDataHandler {

	/*public LarDigestItem doDigest(
			AssetCategory category, DataHandlerContext context)
		throws Exception {

		/*AssetVocabulary vocabulary =
			AssetVocabularyLocalServiceUtil.getAssetVocabulary(
				category.getVocabularyId());

		assetVocabularyDataHandler.digest(vocabulary, context);

		if (category.getParentCategoryId() !=
				AssetCategoryConstants.DEFAULT_PARENT_CATEGORY_ID) {

			AssetCategory parentCategory =
				AssetCategoryLocalServiceUtil.getCategory(
					category.getParentCategoryId());

			digest(parentCategory, context);
		}

		String path = getEntityPath(category);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigest digest = context.getLarDigest();

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(getDigestAction(category, context));
		digestItem.setPath(path);
		digestItem.setClassName(AssetCategory.class.getName());
		digestItem.setClassPK(String.valueOf(category.getCategoryId()));

		return digestItem;
		return null;
	}*/

}