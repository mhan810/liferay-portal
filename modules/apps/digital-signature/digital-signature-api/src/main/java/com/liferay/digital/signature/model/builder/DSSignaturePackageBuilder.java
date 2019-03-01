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

package com.liferay.digital.signature.model.builder;

import aQute.bnd.annotation.ProviderType;

import com.liferay.digital.signature.model.DSDocument;
import com.liferay.digital.signature.model.DSEmailNotification;
import com.liferay.digital.signature.model.DSParticipant;
import com.liferay.digital.signature.model.DSSignaturePackage;

import java.util.Collection;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSSignaturePackageBuilder {

	public DSSignaturePackageBuilder addDSDocuments(
		Collection<DSDocument> documents);

	public DSSignaturePackageBuilder addDSDocuments(DSDocument... documents);

	public DSSignaturePackageBuilder addDSParticipants(
		Collection<DSParticipant> participants);

	public DSSignaturePackageBuilder addDSParticipants(
		DSParticipant... participants);

	public DSSignaturePackage getDSSignatureRequest();

	public DSSignaturePackageBuilder setAuthoritative(Boolean authoritative);

	public DSSignaturePackageBuilder setDSEmailNotification(
		DSEmailNotification emailNotification);

	public DSSignaturePackageBuilder setDSSignatureRequestId(
		String dsSignatureRequestId);

}