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
import com.liferay.digital.signature.builder.ParticipantModifyingDSParticipantBuilder;
import com.liferay.digital.signature.request.DSParticipant;

/**
 * @author Michael C. Han
 */
public abstract class BaseParticipantModifyingDSParticipantBuilder
	<T extends DSParticipant>
		extends BaseDSParticipantBuilder
		implements ParticipantModifyingDSParticipantBuilder {

	public BaseParticipantModifyingDSParticipantBuilder(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setCanEditParticipantEmails(
		Boolean canEditParticipantEmails) {

		_canEditParticipantEmails = canEditParticipantEmails;

		return (S)this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends DSParticipantBuilder> S setCanEditParticipantNames(
		Boolean canEditParticipantNames) {

		_canEditParticipantNames = canEditParticipantNames;

		return (S)this;
	}

	protected Boolean getCanEditParticipantEmails() {
		return _canEditParticipantEmails;
	}

	protected Boolean getCanEditParticipantNames() {
		return _canEditParticipantNames;
	}

	private Boolean _canEditParticipantEmails;
	private Boolean _canEditParticipantNames;

}