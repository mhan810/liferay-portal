/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.digital.signature.internal.model.field;

import com.liferay.digital.signature.model.field.SignerAttachmentDSField;

/**
 * @author Michael C. Han
 */
public class SignerAttachmentDSFieldImpl
	extends DSFieldImpl<SignerAttachmentDSField>
	implements SignerAttachmentDSField {

	public SignerAttachmentDSFieldImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public Boolean getOptional() {
		return _optional;
	}

	public void setOptional(Boolean optional) {
		_optional = optional;
	}

	private Boolean _optional;

}