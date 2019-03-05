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

package com.liferay.digital.signature.model.field;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSFieldVisitor<S> {

	public S getObject();

	public S visit(ApproveDSField approveDSField);

	public S visit(CheckboxDSField checkboxDSField);

	public S visit(CompanyNameDSField companyNameDSField);

	public S visit(DateDSField dateDSField);

	public S visit(DateSignedDSField dateSignedDSField);

	public S visit(DeclineDSField declineDSField);

	public S visit(EmailDSField emailDSField);

	public S visit(FirstNameDSField firstNameDSField);

	public S visit(FormulaDSField formulaDSField);

	public S visit(FullNameDSField fullNameDSField);

	public S visit(InitialHereDSField initialHereDSField);

	public S visit(LastNameDSField lastNameDSField);

	public S visit(ListDSField listDSField);

	public S visit(NotarizeDSField notarizeDSField);

	public S visit(NoteDSField noteDSField);

	public S visit(NumberDSField numberDSField);

	public S visit(ParticipantEmailDSField participantEmailDSField);

	public S visit(PostalCodeDSField postalCodeDSField);

	public S visit(RadioDSField radioDSField);

	public S visit(RadioGroupDSField radioGroupDSField);

	public S visit(SignaturePackageIdDSField signaturePackageIdDSField);

	public S visit(SignerAttachmentDSField signerAttachmentDSField);

	public S visit(SignHereDSField signHereDSField);

	public S visit(SocialSecurityNumberDSField dsSocialSecurityNumberField);

	public S visit(TextDSField dsTextField);

	public S visit(TitleDSField dsTitleField);

	public S visit(ViewDSField dsViewField);

}