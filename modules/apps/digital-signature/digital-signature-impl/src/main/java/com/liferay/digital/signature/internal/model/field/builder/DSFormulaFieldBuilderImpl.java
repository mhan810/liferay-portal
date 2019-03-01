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

import com.liferay.digital.signature.internal.model.field.DSFormulaFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSFormulaField;
import com.liferay.digital.signature.model.field.builder.DSFormulaFieldBuilder;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSFormulaFieldBuilderImpl
	extends DSUserEntryFieldBuilderImpl<DSFormulaField>
	implements DSFormulaFieldBuilder {

	public DSFormulaFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSFormulaField> getDSField() {
		DSFormulaFieldImpl dsFormulaFieldImpl = new DSFormulaFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsFormulaFieldImpl.setFormula(_formula);
		dsFormulaFieldImpl.setRoundDecimalPlaces(_roundDecimalPlaces);
		dsFormulaFieldImpl.setSenderRequired(_senderRequired);

		populateFields(dsFormulaFieldImpl);

		return dsFormulaFieldImpl;
	}

	@Override
	public DSFormulaFieldBuilder setFormula(String formula) {
		_formula = formula;

		return this;
	}

	@Override
	public DSFormulaFieldBuilder setRoundDecimalPlaces(
		Boolean roundDecimalPlaces) {

		_roundDecimalPlaces = roundDecimalPlaces;

		return this;
	}

	@Override
	public DSFormulaFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private String _formula;
	private Boolean _roundDecimalPlaces;
	private Boolean _senderRequired;

}