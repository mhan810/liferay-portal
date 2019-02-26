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

package com.liferay.digital.signature.builder;

import aQute.bnd.annotation.ProviderType;

import com.liferay.digital.signature.request.DSDocument;
import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSParticipant;
import com.liferay.digital.signature.request.DSSessionId;
import com.liferay.digital.signature.request.DSSignatureRequest;

import java.util.Collection;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSSignatureRequestBuilder {

	public DSSignatureRequestBuilder addDSDocuments(
		Collection<DSDocument> documents);

	public DSSignatureRequestBuilder addDSDocuments(DSDocument... documents);

	public DSSignatureRequestBuilder addDSParticipants(
		Collection<DSParticipant> participants);

	public DSSignatureRequestBuilder addDSParticipants(
		DSParticipant... participants);

	public DSSignatureRequest getDSSignatureRequest(DSSessionId dsSessionId);

	public DSSignatureRequestBuilder setAuthoritative(Boolean authoritative);

	public DSSignatureRequestBuilder setDSEmailNotification(
		DSEmailNotification emailNotification);

}