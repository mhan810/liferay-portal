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

	public S visit(DSApproveField dsApproveField);

	public S visit(DSCheckboxField dsCheckboxField);

	public S visit(DSCompanyNameField dsCompanyNameField);

	public S visit(DSDateField dsDateField);

	public S visit(DSDateSignedField dsDateSignedField);

	public S visit(DSDeclineField dsDeclineField);

	public S visit(DSEmailField dsEmailField);

	public S visit(DSFirstNameField dsFirstNameField);

	public S visit(DSFormulaField dsFormulaField);

	public S visit(DSFullNameField dsFullNameField);

	public S visit(DSInitialHereField dsInitialHereField);

	public S visit(DSLastNameField dsLastNameField);

	public S visit(DSListField dsListField);

	public S visit(DSNotarizeField dsNotarizeField);

	public S visit(DSNoteField dsNoteField);

	public S visit(DSNumberField dsNumberField);

	public S visit(DSParticipantEmailField dsParticipantEmailField);

	public S visit(DSPostalCodeField dsPostalCodeField);

	public S visit(DSRadioField dsRadioField);

	public S visit(DSRadioGroupField dsRadioGroupField);

	public S visit(DSSignaturePackageIdField dsSignaturePackageIdField);

	public S visit(DSSignerAttachmentField dsSignerAttachmentField);

	public S visit(DSSignHereField dsSignHereField);

	public S visit(DSSocialSecurityNumberField dsSocialSecurityNumberField);

	public S visit(DSTextField dsTextField);

	public S visit(DSTitleField dsTitleField);

	public S visit(DSViewField dsViewField);

}