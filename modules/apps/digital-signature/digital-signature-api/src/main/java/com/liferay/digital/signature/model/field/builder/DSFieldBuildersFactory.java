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

package com.liferay.digital.signature.model.field.builder;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSFieldBuildersFactory {

	public DSApproveFieldBuilder getDSApproveFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSCheckboxFieldBuilder getDSCheckboxFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSDateFieldBuilder getDSDateFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSDateSignedFieldBuilder getDSDateSignedFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSDeclineFieldBuilder getDSDeclineFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSEmailFieldBuilder getDSEmailFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSFirstNameFieldBuilder getDSFirstNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSFormulaFieldBuilder getDSFormulaFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSFullNameFieldBuilder getDSFullNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSInitialHereFieldBuilder getDSInitialHereFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSLastNameFieldBuilder getDSLastNameFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSListFieldBuilder getDSListFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSNotarizeFieldBuilder getDSNotarizeFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSNoteFieldBuilder getDSNoteFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSNumberFieldBuilder getDSNumberFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSParticipantEmailFieldBuilder getDSParticipantEmailFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSPostalCodeFieldBuilder getDSPostalCodeFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSRadioFieldBuilder getDSRadioFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSRadioGroupFieldBuilder getDSRadioGroupFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSSignaturePackageIdFieldBuilder getDSSignaturePackageIdFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSSignerAttachmentFieldBuilder getDSSignerAttachmentFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSSignHereFieldBuilder getDSSignHereFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSSocialSecurityNumberFieldBuilder
		getDSSocialSecurityNumberFieldBuilder(
			String documentId, String fieldId, Integer pageNumber);

	public DSTextFieldBuilder getDSTextFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSTitleFieldBuilder getDSTitleFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DSViewFieldBuilder getDSViewFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

}