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

package com.liferay.digital.signature.internal.builder;

import com.liferay.digital.signature.builder.DSSignatureRequestBuilder;
import com.liferay.digital.signature.internal.request.DSSignatureRequestImpl;
import com.liferay.digital.signature.request.DSDocument;
import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSParticipant;
import com.liferay.digital.signature.request.DSParticipantRole;
import com.liferay.digital.signature.request.DSSessionId;
import com.liferay.digital.signature.request.DSSignatureRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class DSSignatureRequestBuilderImpl
	implements DSSignatureRequestBuilder {

	@Override
	public DSSignatureRequestBuilder addDSDocuments(
		Collection<DSDocument> dsDocuments) {

		_dsDocuments.addAll(dsDocuments);

		return this;
	}

	@Override
	public DSSignatureRequestBuilder addDSDocuments(DSDocument... dsDocuments) {
		Collections.addAll(_dsDocuments, dsDocuments);

		return this;
	}

	@Override
	public DSSignatureRequestBuilder addDSParticipants(
		Collection<DSParticipant> dsParticipants) {

		dsParticipants.forEach(this::addDSParticipant);

		return this;
	}

	@Override
	public DSSignatureRequestBuilder addDSParticipants(
		DSParticipant... dsParticipants) {

		for (final DSParticipant dsParticipant : dsParticipants) {
			addDSParticipant(dsParticipant);
		}

		return this;
	}

	@Override
	public DSSignatureRequest getDSSignatureRequest(DSSessionId dsSessionId) {
		DSSignatureRequestImpl dsSignatureRequest =
			new DSSignatureRequestImpl();

		dsSignatureRequest.setAuthoritative(_authoritative);
		dsSignatureRequest.setDSEmailNotification(_dsEmailNotification);
		dsSignatureRequest.addDSDocuments(_dsDocuments);
		dsSignatureRequest.setDSParticipantMap(_dsParticipantsMap);
		dsSignatureRequest.setDSSessionId(dsSessionId);

		return dsSignatureRequest;
	}

	@Override
	public DSSignatureRequestBuilder setAuthoritative(Boolean authoritative) {
		_authoritative = authoritative;

		return this;
	}

	@Override
	public DSSignatureRequestBuilder setDSEmailNotification(
		DSEmailNotification dsEmailNotification) {

		_dsEmailNotification = dsEmailNotification;

		return this;
	}

	protected void addDSParticipant(DSParticipant dsParticipant) {
		Collection<DSParticipant> dsParticipantsSet =
			_dsParticipantsMap.computeIfAbsent(
				dsParticipant.getDSParticipantRole(),
				participantRole -> new HashSet<>());

		dsParticipantsSet.add(dsParticipant);
	}

	private Boolean _authoritative;
	private final Set<DSDocument> _dsDocuments = new HashSet<>();
	private DSEmailNotification _dsEmailNotification;
	private final Map<DSParticipantRole, Collection<DSParticipant>>
		_dsParticipantsMap = new HashMap<>();

}