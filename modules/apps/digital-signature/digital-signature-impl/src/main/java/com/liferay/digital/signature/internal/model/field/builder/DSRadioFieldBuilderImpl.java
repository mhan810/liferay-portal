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

import com.liferay.digital.signature.internal.model.field.DSRadioFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSRadioField;
import com.liferay.digital.signature.model.field.builder.DSRadioFieldBuilder;

/**
 * @author Michael C. Han
 */
public class DSRadioFieldBuilderImpl
	extends DSFieldBuilderImpl<DSRadioField> implements DSRadioFieldBuilder {

	public DSRadioFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSRadioField> getDSField() {
		DSRadioFieldImpl dsRadioFieldImpl = new DSRadioFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsRadioFieldImpl.setValue(_value);

		populateFields(dsRadioFieldImpl);

		return dsRadioFieldImpl;
	}

	@Override
	public DSRadioFieldBuilder setValue(String value) {
		_value = value;

		return this;
	}

	private String _value;

}