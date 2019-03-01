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

import aQute.bnd.annotation.ProviderType;

import com.liferay.digital.signature.internal.model.field.DSDateSignedFieldImpl;
import com.liferay.digital.signature.model.field.DSDateSignedField;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.builder.DSDateSignedFieldBuilder;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSDateSignedFieldBuilderImpl
	extends DSStyledFieldBuilderImpl<DSDateSignedField>
	implements DSDateSignedFieldBuilder {

	public DSDateSignedFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSDateSignedField> getDSField() {
		DSDateSignedFieldImpl dsDateSignedFieldImpl = new DSDateSignedFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		populateFields(dsDateSignedFieldImpl);

		return dsDateSignedFieldImpl;
	}

}