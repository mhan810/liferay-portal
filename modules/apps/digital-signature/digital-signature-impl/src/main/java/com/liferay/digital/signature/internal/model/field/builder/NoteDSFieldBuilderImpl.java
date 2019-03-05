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

import com.liferay.digital.signature.internal.model.field.NoteDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.NoteDSField;
import com.liferay.digital.signature.model.field.builder.NoteDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class NoteDSFieldBuilderImpl
	extends UserEntryDSFieldBuilderImpl<NoteDSField>
	implements NoteDSFieldBuilder {

	public NoteDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<NoteDSField> getDSField() {
		NoteDSFieldImpl noteDSFieldImpl = new NoteDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		noteDSFieldImpl.setSenderRequired(_senderRequired);

		populateFields(noteDSFieldImpl);

		return noteDSFieldImpl;
	}

	@Override
	public NoteDSFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private Boolean _senderRequired;

}