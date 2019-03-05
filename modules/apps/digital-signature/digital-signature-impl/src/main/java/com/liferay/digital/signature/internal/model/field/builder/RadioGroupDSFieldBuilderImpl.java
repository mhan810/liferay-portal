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

import com.liferay.digital.signature.internal.model.field.RadioGroupDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.RadioDSField;
import com.liferay.digital.signature.model.field.RadioGroupDSField;
import com.liferay.digital.signature.model.field.builder.RadioGroupDSFieldBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class RadioGroupDSFieldBuilderImpl
	extends UserEntryDSFieldBuilderImpl<RadioGroupDSField>
	implements RadioGroupDSFieldBuilder {

	public RadioGroupDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public RadioGroupDSFieldBuilder addDSRadioField(RadioDSField radioDSField) {
		_radioDSFields.add(radioDSField);

		return this;
	}

	@Override
	public RadioGroupDSFieldBuilder addDSRadioFields(
		RadioDSField... radioDSFields) {

		Collections.addAll(_radioDSFields, radioDSFields);

		return this;
	}

	@Override
	public DSField<RadioGroupDSField> getDSField() {
		RadioGroupDSFieldImpl radioGroupDSFieldImpl = new RadioGroupDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		radioGroupDSFieldImpl.addDSRadioFields(_radioDSFields);
		radioGroupDSFieldImpl.setGroupName(_groupName);

		populateFields(radioGroupDSFieldImpl);

		return radioGroupDSFieldImpl;
	}

	@Override
	public RadioGroupDSFieldBuilder setGroupName(String groupName) {
		_groupName = groupName;

		return this;
	}

	private String _groupName;
	private List<RadioDSField> _radioDSFields = new ArrayList<>();

}