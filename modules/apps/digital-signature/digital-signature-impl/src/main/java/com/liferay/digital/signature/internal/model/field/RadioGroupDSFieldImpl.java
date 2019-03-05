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

package com.liferay.digital.signature.internal.model.field;

import aQute.bnd.annotation.ProviderType;

import com.liferay.digital.signature.model.field.RadioDSField;
import com.liferay.digital.signature.model.field.RadioGroupDSField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class RadioGroupDSFieldImpl
	extends UserEntryDSFieldImpl<RadioGroupDSField>
	implements RadioGroupDSField {

	public RadioGroupDSFieldImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	public void addDSRadioField(RadioDSField radioDSField) {
		_radioDSFields.add(radioDSField);
	}

	public void addDSRadioFields(List<RadioDSField> radioDSFields) {
		_radioDSFields.addAll(radioDSFields);
	}

	@Override
	public List<RadioDSField> getDSRadioFields() {
		return _radioDSFields;
	}

	@Override
	public String getGroupName() {
		return _groupName;
	}

	public void setGroupName(String groupName) {
		_groupName = groupName;
	}

	private String _groupName;
	private List<RadioDSField> _radioDSFields = new ArrayList<>();

}