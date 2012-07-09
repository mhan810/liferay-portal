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

import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portlet.asset.model.AssetLink;

/**
 * @author Mate Thurzo
 */
public class AssetLinkDataHandlerImpl
	extends BaseDataHandlerImpl<AssetLink>
	implements AssetLinkDataHandler {

	@Override
	public void doDigest(AssetLink assetLink) throws Exception {
		return;
	}

	@Override
	public AssetLink getEntity(String classPK) {
		return null;
	}

}
