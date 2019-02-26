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

package com.liferay.digital.signature.internal.request;

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
public class DSSignatureRequestImpl implements DSSignatureRequest {

	public void addDSDocuments(Collection<DSDocument> dsDocuments) {
		_dsDocuments.addAll(dsDocuments);
	}

	@Override
	public Boolean getAuthoritative() {
		return _authoritative;
	}

	@Override
	public Collection<DSDocument> getDSDocuments() {
		return Collections.unmodifiableCollection(_dsDocuments);
	}

	@Override
	public DSEmailNotification getDSEmailNotification() {
		return _dsEmailNotification;
	}

	@Override
	public Map<DSParticipantRole, Collection<DSParticipant>>
		getDSParticipants() {

		return Collections.unmodifiableMap(_dsParticipantsMap);
	}

	@Override
	public DSSessionId getDSSessionId() {
		return _dsSessionId;
	}

	public void setAuthoritative(Boolean authoritative) {
		_authoritative = authoritative;
	}

	public void setDSEmailNotification(
		DSEmailNotification dsEmailNotification) {

		_dsEmailNotification = dsEmailNotification;
	}

	public void setDSParticipantMap(
		Map<DSParticipantRole, Collection<DSParticipant>> dsParticipantMap) {

		_dsParticipantsMap = dsParticipantMap;
	}

	public void setDSSessionId(DSSessionId dsSessionId) {
		_dsSessionId = dsSessionId;
	}

	private Boolean _authoritative;
	private final Set<DSDocument> _dsDocuments = new HashSet<>();
	private DSEmailNotification _dsEmailNotification;
	private Map<DSParticipantRole, Collection<DSParticipant>>
		_dsParticipantsMap = new HashMap<>();
	private DSSessionId _dsSessionId;

}