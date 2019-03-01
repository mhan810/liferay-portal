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

import com.liferay.digital.signature.internal.model.field.DSParticipantEmailFieldImpl;
import com.liferay.digital.signature.model.field.DSField;
import com.liferay.digital.signature.model.field.DSParticipantEmailField;
import com.liferay.digital.signature.model.field.builder.DSParticipantEmailFieldBuilder;

/**
 * @author Michael C. Han
 */
@ProviderType
public class DSParticipantEmailFieldBuilderImpl
	extends DSStyledFieldBuilderImpl<DSParticipantEmailField>
	implements DSParticipantEmailFieldBuilder {

	public DSParticipantEmailFieldBuilderImpl(
		String documentId, String fieldId, Integer pageNumber) {

		super(documentId, fieldId, pageNumber);
	}

	@Override
	public DSField<DSParticipantEmailField> getDSField() {
		DSParticipantEmailFieldImpl dsParticipantEmailFieldImpl =
			new DSParticipantEmailFieldImpl(
				getDocumentId(), getFieldId(), getPageNumber());

		populateFields(dsParticipantEmailFieldImpl);

		return dsParticipantEmailFieldImpl;
	}

}