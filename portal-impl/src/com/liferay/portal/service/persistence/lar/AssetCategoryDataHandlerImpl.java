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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetCategoryConstants;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

/**
 * @author Mate Thurzo
 */
public class AssetCategoryDataHandlerImpl
	extends BaseDataHandlerImpl<AssetCategory>
	implements AssetCategoryDataHandler {

	@Override
	public LarDigestItem doDigest(
			AssetCategory category, DataHandlerContext context)
		throws Exception {

		AssetVocabulary vocabulary =
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
		digestItem.setType(AssetCategory.class.getName());
		digestItem.setClassPK(String.valueOf(category.getCategoryId()));

		return digestItem;
	}

	@Override
	public void doImportData(LarDigestItem item, DataHandlerContext context)
		throws Exception {

		return;
	}

	@Override
	public AssetCategory getEntity(String classPK) {
		if (Validator.isNull(classPK)) {
			return null;
		}

		try {
			long categoryId = Long.valueOf(classPK);

			AssetCategory category =
				AssetCategoryLocalServiceUtil.getCategory(categoryId);

			return category;
		}
		catch (Exception e) {
			return null;
		}
	}

}
