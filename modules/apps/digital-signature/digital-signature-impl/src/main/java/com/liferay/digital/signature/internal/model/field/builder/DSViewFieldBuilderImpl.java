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

import com.liferay.digital.signature.internal.model.field.DSViewFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSViewField;
import com.liferay.digital.signature.model.field.builder.DSViewFieldBuilder;

/**
 * @author Michael C. Han
 */
public class DSViewFieldBuilderImpl
	extends DSStyledFieldBuilderImpl<DSViewField>
	implements DSViewFieldBuilder {

	public DSViewFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSViewField> getDSField() {
		DSViewFieldImpl dsViewFieldImpl = new DSViewFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsViewFieldImpl.setRequiredRead(_requiredRead);
		dsViewFieldImpl.setViewText(_viewText);

		populateFields(dsViewFieldImpl);

		return dsViewFieldImpl;
	}

	@Override
	public DSViewFieldBuilder setRequiredRead(Boolean requiredRead) {
		_requiredRead = requiredRead;

		return this;
	}

	@Override
	public DSViewFieldBuilder setViewText(String viewText) {
		_viewText = viewText;

		return this;
	}

	private Boolean _requiredRead;
	private String _viewText;

}