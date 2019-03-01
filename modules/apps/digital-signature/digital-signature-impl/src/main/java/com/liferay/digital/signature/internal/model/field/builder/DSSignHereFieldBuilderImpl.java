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

import com.liferay.digital.signature.internal.model.field.DSSignHereFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSSignHereField;
import com.liferay.digital.signature.model.field.builder.DSSignHereFieldBuilder;

/**
 * @author Michael C. Han
 */
public class DSSignHereFieldBuilderImpl
	extends DSFieldBuilderImpl<DSSignHereField>
	implements DSSignHereFieldBuilder {

	public DSSignHereFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSSignHereField> getDSField() {
		DSSignHereFieldImpl dsSignHereFieldImpl = new DSSignHereFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsSignHereFieldImpl.setOptional(_optional);

		populateFields(dsSignHereFieldImpl);

		return dsSignHereFieldImpl;
	}

	@Override
	public DSSignHereFieldBuilder setOptional(Boolean optional) {
		_optional = optional;

		return this;
	}

	private Boolean _optional;

}