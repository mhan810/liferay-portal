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

import com.liferay.digital.signature.internal.model.field.ViewDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.ViewDSField;
import com.liferay.digital.signature.model.field.builder.ViewDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class ViewDSFieldBuilderImpl
	extends StyledDSFieldBuilderImpl<ViewDSField>
	implements ViewDSFieldBuilder {

	public ViewDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<ViewDSField> getDSField() {
		ViewDSFieldImpl viewDSFieldImpl = new ViewDSFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		viewDSFieldImpl.setRequiredRead(_requiredRead);
		viewDSFieldImpl.setViewText(_viewText);

		populateFields(viewDSFieldImpl);

		return viewDSFieldImpl;
	}

	@Override
	public ViewDSFieldBuilder setRequiredRead(Boolean requiredRead) {
		_requiredRead = requiredRead;

		return this;
	}

	@Override
	public ViewDSFieldBuilder setViewText(String viewText) {
		_viewText = viewText;

		return this;
	}

	private Boolean _requiredRead;
	private String _viewText;

}