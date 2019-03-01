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

import com.liferay.digital.signature.internal.model.field.DSPostalCodeFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSPostalCodeField;
import com.liferay.digital.signature.model.field.builder.DSPostalCodeFieldBuilder;

/**
 * @author Michael C. Han
 */
public class DSPostalCodeFieldBuilderImpl
	extends DSUserEntryFieldBuilderImpl<DSPostalCodeField>
	implements DSPostalCodeFieldBuilder {

	public DSPostalCodeFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSPostalCodeField> getDSField() {
		DSPostalCodeFieldImpl dsPostalCodeFieldImpl = new DSPostalCodeFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsPostalCodeFieldImpl.setSenderRequired(_senderRequired);

		populateFields(dsPostalCodeFieldImpl);

		return dsPostalCodeFieldImpl;
	}

	@Override
	public DSPostalCodeFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private Boolean _senderRequired;

}