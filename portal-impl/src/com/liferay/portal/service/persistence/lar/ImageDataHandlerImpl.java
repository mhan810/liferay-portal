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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Image;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;

/**
 * @author Mate Thurzo
 */
public class ImageDataHandlerImpl extends BaseDataHandlerImpl<Image>
	implements ImageDataHandler {

	@Override
	public LarDigestItem doDigest(Image object) throws Exception {
		return null;
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception {
		// toDo: implement
	}

	public Image getEntity(String classPK) {
		if (Validator.isNotNull(classPK)) {
			try {
				long imageId = Long.valueOf(classPK);

				Image image = ImageLocalServiceUtil.getImage(imageId);

				return image;
			}
			catch (Exception e) {
				return null;
			}
		}

		return null;
	}

}
