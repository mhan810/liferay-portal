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

	public ApproveDSFieldBuilder createApproveDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public CheckboxDSFieldBuilder createCheckboxDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public CompanyNameDSFieldBuilder createCompanyNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DateDSFieldBuilder createDateDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DateSignedDSFieldBuilder createDateSignedDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DeclineDSFieldBuilder createDeclineDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public EmailDSFieldBuilder createEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FirstNameDSFieldBuilder createFirstNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FormulaDSFieldBuilder createFormulaDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FullNameDSFieldBuilder createFullNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public InitialHereDSFieldBuilder createInitialHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public LastNameDSFieldBuilder createLastNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ListDSFieldBuilder createListDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NotarizeDSFieldBuilder createNotarizeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NoteDSFieldBuilder createNoteDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NumberDSFieldBuilder createNumberDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ParticipantEmailDSFieldBuilder createParticipantEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public PostalCodeDSFieldBuilder createPostalCodeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public RadioDSFieldBuilder createRadioDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public RadioGroupDSFieldBuilder createRadioGroupDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SignaturePackageIdDSFieldBuilder
		createSignaturePackageIdDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber);

	public SignerAttachmentDSFieldBuilder createSignerAttachmentDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SignHereDSFieldBuilder createSignHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SocialSecurityNumberDSFieldBuilder
		createSocialSecurityNumberDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber);

	public TextDSFieldBuilder createTextDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public TitleDSFieldBuilder createTitleDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ViewDSFieldBuilder createViewDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

}