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
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigest;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestItemImpl;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class AssetVocabuaryDataHandlerImpl
	extends BaseDataHandlerImpl<AssetVocabulary>
	implements AssetVocabularyDataHandler {

	@Override
	public LarDigestItem doDigest(AssetVocabulary vocabulary) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		LarDigest digest = context.getLarDigest();

		String path = getEntityPath(vocabulary);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(getDigestAction(vocabulary));
		digestItem.setPath(path);
		digestItem.setType(AssetVocabulary.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(vocabulary.getVocabularyId()));

		return digestItem;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		return;
	}

	@Override
	public AssetVocabulary getEntity(String classPK) {
		if (Validator.isNull(classPK)) {
			return null;
		}

		try {
			long vocabularyId = Long.valueOf(classPK);

			AssetVocabulary assetVocabulary =
				AssetVocabularyLocalServiceUtil.getAssetVocabulary(
					vocabularyId);

			return assetVocabulary;
		}
		catch (Exception e) {
			return null;
		}
	}

}
