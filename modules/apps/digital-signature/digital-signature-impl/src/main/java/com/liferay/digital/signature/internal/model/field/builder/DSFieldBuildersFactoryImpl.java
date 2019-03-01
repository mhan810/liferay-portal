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

import com.liferay.digital.signature.model.field.builder.DSApproveFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSCheckboxFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSDateFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSDateSignedFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSDeclineFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSEmailFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSFieldBuildersFactory;
import com.liferay.digital.signature.model.field.builder.DSFirstNameFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSFormulaFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSFullNameFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSInitialHereFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSLastNameFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSListFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSNotarizeFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSNoteFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSNumberFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSParticipantEmailFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSPostalCodeFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSRadioFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSRadioGroupFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSSignHereFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSSignaturePackageIdFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSSignerAttachmentFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSSocialSecurityNumberFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSTextFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSTitleFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSViewFieldBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = DSFieldBuildersFactory.class)
public class DSFieldBuildersFactoryImpl implements DSFieldBuildersFactory {

	@Override
	public DSApproveFieldBuilder getDSApproveFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSApproveFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSCheckboxFieldBuilder getDSCheckboxFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSCheckboxFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSDateFieldBuilder getDSDateFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSDateFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSDateSignedFieldBuilder getDSDateSignedFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSDateSignedFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSDeclineFieldBuilder getDSDeclineFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSDeclineFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSEmailFieldBuilder getDSEmailFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSEmailFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSFirstNameFieldBuilder getDSFirstNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSFirstNameFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSFormulaFieldBuilder getDSFormulaFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSFormulaFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSFullNameFieldBuilder getDSFullNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSFullNameFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSInitialHereFieldBuilder getDSInitialHereFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSInitialHereFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSLastNameFieldBuilder getDSLastNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSLastNameFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSListFieldBuilder getDSListFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSListFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSNotarizeFieldBuilder getDSNotarizeFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSNotarizeFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSNoteFieldBuilder getDSNoteFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSNoteFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSNumberFieldBuilder getDSNumberFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSNumberFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSParticipantEmailFieldBuilder getDSParticipantEmailFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSParticipantEmailFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSPostalCodeFieldBuilder getDSPostalCodeFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSPostalCodeFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSRadioFieldBuilder getDSRadioFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSRadioFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSRadioGroupFieldBuilder getDSRadioGroupFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSRadioGroupFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSSignaturePackageIdFieldBuilder getDSSignaturePackageIdFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSSignaturePackageIdFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSSignerAttachmentFieldBuilder getDSSignerAttachmentFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSSignerAttachmentFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSSignHereFieldBuilder getDSSignHereFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSSignHereFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSSocialSecurityNumberFieldBuilder
		getDSSocialSecurityNumberFieldBuilder(
			String documentId, String fieldId, Integer pageNumber) {

		return new DSSocialSecurityNumberFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DSTextFieldBuilder getDSTextFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSTextFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSTitleFieldBuilder getDSTitleFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSTitleFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DSViewFieldBuilder getDSViewFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DSViewFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

}