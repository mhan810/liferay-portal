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

import com.liferay.digital.signature.internal.model.field.SocialSecurityNumberDSFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.SocialSecurityNumberDSField;
import com.liferay.digital.signature.model.field.builder.SocialSecurityNumberDSFieldBuilder;

/**
 * @author Michael C. Han
 */
public class SocialSecurityNumberDSFieldBuilderImpl
	extends UserEntryDSFieldBuilderImpl<SocialSecurityNumberDSField>
	implements SocialSecurityNumberDSFieldBuilder {

	public SocialSecurityNumberDSFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<SocialSecurityNumberDSField> getDSField() {
		SocialSecurityNumberDSFieldImpl socialSecurityNumberDSFieldImpl =
			new SocialSecurityNumberDSFieldImpl(
				getDocumentId(), getFieldId(), getPageNumber());

		socialSecurityNumberDSFieldImpl.setSenderRequired(_senderRequired);

		populateFields(socialSecurityNumberDSFieldImpl);

		return socialSecurityNumberDSFieldImpl;
	}

	@Override
	public SocialSecurityNumberDSFieldBuilder setSenderRequired(
		Boolean senderRequired) {

		_senderRequired = senderRequired;

		return this;
	}

	private Boolean _senderRequired;

}