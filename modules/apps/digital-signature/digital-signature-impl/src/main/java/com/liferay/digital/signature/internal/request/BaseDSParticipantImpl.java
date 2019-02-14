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

import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSParticipant;
import com.liferay.digital.signature.request.DSParticipantRole;

import java.util.Collection;

/**
 * @author Michael C. Han
 */
public abstract class BaseDSParticipantImpl implements DSParticipant {

	public BaseDSParticipantImpl(String name, String email, int routingOrder) {
		_name = name;
		_email = email;
		_routingOrder = routingOrder;
	}

	@Override
	public String getAccessCode() {
		return _accessCode;
	}

	@Override
	public String getClientUserId() {
		return _clientUserId;
	}

	@Override
	public Collection<String> getCustomFields() {
		return _customFields;
	}

	@Override
	public DSEmailNotification getDSEmailNotification() {
		return _dsEmailNotification;
	}

	@Override
	public DSParticipantRole getDSParticipantRole() {
		return _dsParticipantRole;
	}

	@Override
	public String getEmail() {
		return _email;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getNote() {
		return _note;
	}

	@Override
	public String getParticipantId() {
		return _participantId;
	}

	@Override
	public String getRoleName() {
		return _roleName;
	}

	public int getRoutingOrder() {
		return _routingOrder;
	}

	public void setAccessCode(String accessCode) {
		_accessCode = accessCode;
	}

	public void setClientUserId(String clientUserId) {
		_clientUserId = clientUserId;
	}

	public void setCustomFields(Collection<String> customFields) {
		_customFields = customFields;
	}

	public void setDSEmailNotification(
		DSEmailNotification dsEmailNotification) {

		_dsEmailNotification = dsEmailNotification;
	}

	public void setNote(String note) {
		_note = note;
	}

	public void setParticipantId(String participantId) {
		_participantId = participantId;
	}

	public void setRoleName(String roleName) {
		_roleName = roleName;
	}

	protected void setDSParticipantRole(DSParticipantRole dsParticipantRole) {
		_dsParticipantRole = dsParticipantRole;
	}

	private String _accessCode;
	private String _clientUserId;
	private Collection<String> _customFields;
	private DSEmailNotification _dsEmailNotification;
	private DSParticipantRole _dsParticipantRole;
	private final String _email;
	private final String _name;
	private String _note;
	private String _participantId;
	private String _roleName;
	private final int _routingOrder;

}