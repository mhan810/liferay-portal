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

import com.liferay.digital.signature.internal.model.field.DSNumberFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSNumberField;
import com.liferay.digital.signature.model.field.builder.DSNumberFieldBuilder;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSNumberFieldBuilderImpl
	extends DSUserEntryFieldBuilderImpl<DSNumberField>
	implements DSNumberFieldBuilder {

	public DSNumberFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSNumberField> getDSField() {
		DSNumberFieldImpl dsNumberFieldImpl = new DSNumberFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsNumberFieldImpl.setFormula(_formula);
		dsNumberFieldImpl.setSenderRequired(_senderRequired);

		populateFields(dsNumberFieldImpl);

		return dsNumberFieldImpl;
	}

	@Override
	public DSNumberFieldBuilder setFormula(String formula) {
		_formula = formula;

		return this;
	}

	@Override
	public DSNumberFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private String _formula;
	private Boolean _senderRequired;

}