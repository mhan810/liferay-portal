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
import com.liferay.portlet.asset.model.AssetVocabulary;

/**
 * @author Mate Thurzo
 */
public class AssetVocabularyDataHandlerImpl
	extends StagedDataHandlerImpl<AssetVocabulary>
	implements AssetVocabularyDataHandler {

	/*public LarDigestItem doDigest(
			AssetVocabulary vocabulary, DataHandlerContext context)
		throws Exception {

		LarDigest digest = context.getLarDigest();

		String path = ExportImportPathUtil.getEntityPath(vocabulary);

		if (context.isPathProcessed(path)) {
			return null;
		}

		LarDigestItem digestItem = new LarDigestItemImpl();

		digestItem.setAction(getDigestAction(vocabulary, context));
		digestItem.setPath(path);
		digestItem.setClassName(AssetVocabulary.class.getName());
		digestItem.setClassPK(StringUtil.valueOf(vocabulary.getVocabularyId()));

		return digestItem;
	}*/

}