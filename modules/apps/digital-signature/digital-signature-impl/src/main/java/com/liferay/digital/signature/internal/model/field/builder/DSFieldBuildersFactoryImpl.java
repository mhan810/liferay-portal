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

import com.liferay.digital.signature.model.field.builder.ApproveDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.CheckboxDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.CompanyNameDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DSFieldBuildersFactory;
import com.liferay.digital.signature.model.field.builder.DateDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DateSignedDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.DeclineDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.EmailDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.FirstNameDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.FormulaDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.FullNameDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.InitialHereDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.LastNameDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.ListDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.NotarizeDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.NoteDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.NumberDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.ParticipantEmailDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.PostalCodeDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.RadioDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.RadioGroupDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.SignHereDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.SignaturePackageIdDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.SignerAttachmentDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.SocialSecurityNumberDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.TextDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.TitleDSFieldBuilder;
import com.liferay.digital.signature.model.field.builder.ViewDSFieldBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = DSFieldBuildersFactory.class)
public class DSFieldBuildersFactoryImpl implements DSFieldBuildersFactory {

	@Override
	public ApproveDSFieldBuilder createApproveDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ApproveDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public CheckboxDSFieldBuilder createCheckboxDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new CheckboxDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public CompanyNameDSFieldBuilder createCompanyNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new CompanyNameDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DateDSFieldBuilder createDateDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DateDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DateSignedDSFieldBuilder createDateSignedDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DateSignedDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DeclineDSFieldBuilder createDeclineDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DeclineDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public EmailDSFieldBuilder createEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new EmailDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FirstNameDSFieldBuilder createFirstNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FirstNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FormulaDSFieldBuilder createFormulaDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FormulaDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FullNameDSFieldBuilder createFullNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FullNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public InitialHereDSFieldBuilder createInitialHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new InitialHereDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public LastNameDSFieldBuilder createLastNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new LastNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ListDSFieldBuilder createListDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ListDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NotarizeDSFieldBuilder createNotarizeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NotarizeDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NoteDSFieldBuilder createNoteDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NoteDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NumberDSFieldBuilder createNumberDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NumberDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ParticipantEmailDSFieldBuilder createParticipantEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ParticipantEmailDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public PostalCodeDSFieldBuilder createPostalCodeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new PostalCodeDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public RadioDSFieldBuilder createRadioDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new RadioDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public RadioGroupDSFieldBuilder createRadioGroupDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new RadioGroupDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignaturePackageIdDSFieldBuilder
		createSignaturePackageIdDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber) {

		return new SignaturePackageIdDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignerAttachmentDSFieldBuilder createSignerAttachmentDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new SignerAttachmentDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignHereDSFieldBuilder createSignHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new SignHereDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public SocialSecurityNumberDSFieldBuilder
		createSocialSecurityNumberDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber) {

		return new SocialSecurityNumberDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public TextDSFieldBuilder createTextDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new TextDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public TitleDSFieldBuilder createTitleDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new TitleDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ViewDSFieldBuilder createViewDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ViewDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

}