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

import com.liferay.digital.signature.builder.DSParticipantBuilder;
import com.liferay.digital.signature.internal.request.BaseDSParticipantImpl;
import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSParticipant;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Michael C. Han
 */
public abstract class BaseDSParticipantBuilder<T extends DSParticipant>
	implements DSParticipantBuilder {

	public BaseDSParticipantBuilder(
		String name, String email, int routingOrder) {

		_name = name;
		_email = email;
		_routingOrder = routingOrder;
	}

	public String getAccessCode() {
		return _accessCode;
	}

	public String getClientUserId() {
		return _clientUserId;
	}

	public Collection<String> getCustomFields() {
		return _customFields;
	}

	public DSEmailNotification getDSEmailNotification() {
		return _dsEmailNotification;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getDSParticipant() {
		BaseDSParticipantImpl baseDSParticipantImpl =
			(BaseDSParticipantImpl)createDSParticipant();

		baseDSParticipantImpl.setAccessCode(getAccessCode());
		baseDSParticipantImpl.setClientUserId(getClientUserId());
		baseDSParticipantImpl.setCustomFields(getCustomFields());
		baseDSParticipantImpl.setDSEmailNotification(getDSEmailNotification());
		baseDSParticipantImpl.setNote(getNote());
		baseDSParticipantImpl.setParticipantId(getParticipantId());
		baseDSParticipantImpl.setRoleName(getRoleName());

		return (T)baseDSParticipantImpl;
	}

	public String getEmail() {
		return _email;
	}

	public String getName() {
		return _name;
	}

	public String getNote() {
		return _note;
	}

	public String getParticipantId() {
		return _participantId;
	}

	public String getRoleName() {
		return _roleName;
	}

	public int getRoutingOrder() {
		return _routingOrder;
	}

	@Override
	public DSParticipantBuilder setAccessCode(String accessCode) {
		_accessCode = accessCode;

		return this;
	}

	@Override
	public DSParticipantBuilder setClientUserId(String clientUserId) {
		_clientUserId = clientUserId;

		return this;
	}

	@Override
	public DSParticipantBuilder setCustomFields(String... customFields) {
		Collections.addAll(_customFields, customFields);

		return this;
	}

	@Override
	public DSParticipantBuilder setDSEmailNotification(
		DSEmailNotification dsEmailNotification) {

		_dsEmailNotification = dsEmailNotification;

		return this;
	}

	@Override
	public DSParticipantBuilder setNote(String note) {
		_note = note;

		return this;
	}

	@Override
	public DSParticipantBuilder setParticipantId(String participantId) {
		_participantId = participantId;

		return this;
	}

	@Override
	public DSParticipantBuilder setRoleName(String roleName) {
		_roleName = roleName;

		return this;
	}

	protected abstract T createDSParticipant();

	private String _accessCode;
	private String _clientUserId;
	private Collection<String> _customFields = new HashSet<>();
	private DSEmailNotification _dsEmailNotification;
	private final String _email;
	private final String _name;
	private String _note;
	private String _participantId;
	private String _roleName;
	private final int _routingOrder;

}