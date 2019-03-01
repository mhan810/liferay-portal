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

import com.liferay.digital.signature.internal.model.field.DSRadioGroupFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSRadioField;
import com.liferay.digital.signature.model.field.DSRadioGroupField;
import com.liferay.digital.signature.model.field.builder.DSRadioGroupFieldBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class DSRadioGroupFieldBuilderImpl
	extends DSUserEntryFieldBuilderImpl<DSRadioGroupField>
	implements DSRadioGroupFieldBuilder {

	public DSRadioGroupFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSRadioGroupFieldBuilder addDSRadioField(DSRadioField dsRadioField) {
		_dsRadioFields.add(dsRadioField);

		return this;
	}

	@Override
	public DSRadioGroupFieldBuilder addDSRadioFields(
		DSRadioField... dsRadioFields) {

		Collections.addAll(_dsRadioFields, dsRadioFields);

		return this;
	}

	@Override
	public DSField<DSRadioGroupField> getDSField() {
		DSRadioGroupFieldImpl dsRadioGroupFieldImpl = new DSRadioGroupFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsRadioGroupFieldImpl.addDSRadioFields(_dsRadioFields);
		dsRadioGroupFieldImpl.setGroupName(_groupName);

		populateFields(dsRadioGroupFieldImpl);

		return dsRadioGroupFieldImpl;
	}

	@Override
	public DSRadioGroupFieldBuilder setGroupName(String groupName) {
		_groupName = groupName;

		return this;
	}

	private List<DSRadioField> _dsRadioFields = new ArrayList<>();
	private String _groupName;

}