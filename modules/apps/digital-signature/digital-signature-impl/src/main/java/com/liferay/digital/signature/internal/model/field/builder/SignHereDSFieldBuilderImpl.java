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

import com.liferay.digital.signature.internal.model.field.SignHereDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.SignHereDSField;
import com.liferay.digital.signature.model.field.builder.SignHereDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class SignHereDSFieldBuilderImpl
	extends DSFieldBuilderImpl<SignHereDSField>
	implements SignHereDSFieldBuilder {

	public SignHereDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<SignHereDSField> getDSField() {
		SignHereDSFieldImpl signHereDSFieldImpl = new SignHereDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		signHereDSFieldImpl.setOptional(_optional);

		populateFields(signHereDSFieldImpl);

		return signHereDSFieldImpl;
	}

	@Override
	public SignHereDSFieldBuilder setOptional(Boolean optional) {
		_optional = optional;

		return this;
	}

	private Boolean _optional;

}