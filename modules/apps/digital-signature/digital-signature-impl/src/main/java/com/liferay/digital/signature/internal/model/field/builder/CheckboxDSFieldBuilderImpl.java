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

import com.liferay.digital.signature.internal.model.field.CheckboxDSFieldImpl;
import com.liferay.digital.signature.model.field.CheckboxDSField;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.builder.CheckboxDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class CheckboxDSFieldBuilderImpl
	extends DSFieldBuilderImpl<CheckboxDSField>
	implements CheckboxDSFieldBuilder {

	public CheckboxDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<CheckboxDSField> getDSField() {
		CheckboxDSFieldImpl checkboxDSFieldImpl = new CheckboxDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		checkboxDSFieldImpl.setSelected(_selected);

		populateFields(checkboxDSFieldImpl);

		return checkboxDSFieldImpl;
	}

	@Override
	public CheckboxDSFieldBuilder setSelected(Boolean selected) {
		_selected = selected;

		return this;
	}

	private Boolean _selected;

}