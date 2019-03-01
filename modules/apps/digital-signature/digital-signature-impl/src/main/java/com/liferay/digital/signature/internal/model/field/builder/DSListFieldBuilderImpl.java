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

import com.liferay.digital.signature.internal.model.field.DSListFieldImpl;
import com.liferay.digital.signature.internal.model.field.DSListItemImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSListField;
import com.liferay.digital.signature.model.field.DSListItem;
import com.liferay.digital.signature.model.field.builder.DSListFieldBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSListFieldBuilderImpl
	extends DSUserEntryFieldBuilderImpl<DSListField>
	implements DSListFieldBuilder {

	public DSListFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSListItem addDSListItem(String text, String value) {
		return addDSListItem(text, value, false);
	}

	@Override
	public DSListItem addDSListItem(
		String text, String value, boolean selected) {

		DSListItem dsListItem = new DSListItemImpl(text, value, selected);

		_dsListItems.add(dsListItem);

		return dsListItem;
	}

	@Override
	public DSField<DSListField> getDSField() {
		DSListFieldImpl dsListFieldImpl = new DSListFieldImpl(
			getDocumentId(), getFieldId(), getPageNumber());

		dsListFieldImpl.addDSListItems(_dsListItems);
		dsListFieldImpl.setSenderRequired(_senderRequired);

		populateFields(dsListFieldImpl);

		return dsListFieldImpl;
	}

	@Override
	public DSListFieldBuilder setSenderRequired(Boolean senderRequired) {
		_senderRequired = senderRequired;

		return this;
	}

	private List<DSListItem> _dsListItems = new ArrayList<>();
	private Boolean _senderRequired;

}