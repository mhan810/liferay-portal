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
	public ApproveDSFieldBuilder getApproveDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ApproveDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public CheckboxDSFieldBuilder getCheckboxDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new CheckboxDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DateDSFieldBuilder getDateDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DateDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public DateSignedDSFieldBuilder getDateSignedDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DateSignedDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public DeclineDSFieldBuilder getDeclineDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new DeclineDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public EmailDSFieldBuilder getEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new EmailDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FirstNameDSFieldBuilder getFirstNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FirstNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FormulaDSFieldBuilder getFormulaDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FormulaDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public FullNameDSFieldBuilder getFullNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new FullNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public InitialHereDSFieldBuilder getInitialHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new InitialHereDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public LastNameDSFieldBuilder getLastNameDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new LastNameDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ListDSFieldBuilder getListDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ListDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NotarizeDSFieldBuilder getNotarizeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NotarizeDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NoteDSFieldBuilder getNoteDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NoteDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public NumberDSFieldBuilder getNumberDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new NumberDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ParticipantEmailDSFieldBuilder getParticipantEmailDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ParticipantEmailDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public PostalCodeDSFieldBuilder getPostalCodeDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new PostalCodeDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public RadioDSFieldBuilder getRadioDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new RadioDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public RadioGroupDSFieldBuilder getRadioGroupDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new RadioGroupDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignaturePackageIdDSFieldBuilder getSignaturePackageIdDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new SignaturePackageIdDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignerAttachmentDSFieldBuilder getSignerAttachmentDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new SignerAttachmentDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public SignHereDSFieldBuilder getSignHereDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new SignHereDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public SocialSecurityNumberDSFieldBuilder
		getSocialSecurityNumberDSFieldBuilder(
			String documentId, String fieldId, Integer pageNumber) {

		return new SocialSecurityNumberDSFieldBuilderImpl(
			documentId, fieldId, pageNumber);
	}

	@Override
	public TextDSFieldBuilder getTextDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new TextDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public TitleDSFieldBuilder getTitleDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new TitleDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

	@Override
	public ViewDSFieldBuilder getViewDSFieldBuilder(
		String documentId, String fieldId, Integer pageNumber) {

		return new ViewDSFieldBuilderImpl(documentId, fieldId, pageNumber);
	}

}