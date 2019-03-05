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

import com.liferay.digital.signature.internal.model.field.FormulaDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.FormulaDSField;
import com.liferay.digital.signature.model.field.builder.FormulaDSFieldBuilder;

/**
 * @author Michael C. Han
 */
@ProviderType
public class FormulaDSFieldBuilderImpl
	extends UserEntryDSFieldBuilderImpl<FormulaDSField>
	implements FormulaDSFieldBuilder {

	public FormulaDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<FormulaDSField> getDSField() {
		FormulaDSFieldImpl formulaDSFieldImpl = new FormulaDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		formulaDSFieldImpl.setFormula(_formula);
		formulaDSFieldImpl.setRoundDecimalPlaces(_roundDecimalPlaces);
		formulaDSFieldImpl.setSenderRequired(_senderRequired);

		populateFields(formulaDSFieldImpl);

		return formulaDSFieldImpl;
	}

	@Override
	public FormulaDSFieldBuilder setFormula(String formula) {
		_formula = formula;

		return this;
	}

	@Override
	public FormulaDSFieldBuilder setRoundDecimalPlaces(
		Boolean roundDecimalPlaces) {

		_roundDecimalPlaces = roundDecimalPlaces;

		return this;
	}

	@Override
	public FormulaDSFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private String _formula;
	private Boolean _roundDecimalPlaces;
	private Boolean _senderRequired;

}