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

	public ApproveDSFieldBuilder getApproveDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public CheckboxDSFieldBuilder getCheckboxDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DateDSFieldBuilder getDateDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DateSignedDSFieldBuilder getDateSignedDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public DeclineDSFieldBuilder getDeclineDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public EmailDSFieldBuilder getEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FirstNameDSFieldBuilder getFirstNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FormulaDSFieldBuilder getFormulaDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public FullNameDSFieldBuilder getFullNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public InitialHereDSFieldBuilder getInitialHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public LastNameDSFieldBuilder getLastNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ListDSFieldBuilder getListDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NotarizeDSFieldBuilder getNotarizeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NoteDSFieldBuilder getNoteDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public NumberDSFieldBuilder getNumberDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ParticipantEmailDSFieldBuilder getParticipantEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public PostalCodeDSFieldBuilder getPostalCodeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public RadioDSFieldBuilder getRadioDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public RadioGroupDSFieldBuilder getRadioGroupDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SignaturePackageIdDSFieldBuilder getSignaturePackageIdDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SignerAttachmentDSFieldBuilder getSignerAttachmentDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SignHereDSFieldBuilder getSignHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public SocialSecurityNumberDSFieldBuilder
		getSocialSecurityNumberDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber);

	public TextDSFieldBuilder getTextDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public TitleDSFieldBuilder getTitleDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

	public ViewDSFieldBuilder getViewDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber);

}