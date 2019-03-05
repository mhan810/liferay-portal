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

package com.liferay.digital.signature.internal.model.field.builder;

import com.liferay.digital.signature.internal.model.field.SignerAttachmentDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.SignerAttachmentDSField;
import com.liferay.digital.signature.model.field.builder.SignerAttachmentDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class SignerAttachmentDSFieldBuilderImpl
	extends DSFieldBuilderImpl<SignerAttachmentDSField>
	implements SignerAttachmentDSFieldBuilder {

	public SignerAttachmentDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<SignerAttachmentDSField> getDSField() {
		SignerAttachmentDSFieldImpl signerAttachmentDSFieldImpl =
			new SignerAttachmentDSFieldImpl(
				getDocumentId(), getFieldId(), getPageNumber());

		signerAttachmentDSFieldImpl.setOptional(_optional);

		populateFields(signerAttachmentDSFieldImpl);

		return signerAttachmentDSFieldImpl;
	}

	@Override
	public SignerAttachmentDSFieldBuilder setOptional(Boolean optional) {
		_optional = optional;

		return this;
	}

	private Boolean _optional;

}