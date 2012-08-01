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

import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portlet.asset.model.AssetVocabulary;

/**
 * @author Mate Thurzo
 */
public interface AssetVocabularyDataHandler
	extends BaseDataHandler<AssetVocabulary> {

	public LarDigestItem doDigest(AssetVocabulary assetVocabulary)
		throws Exception;

	public void doImport(LarDigestItem item) throws Exception;

	public AssetVocabulary getEntity(String classPK);

}
