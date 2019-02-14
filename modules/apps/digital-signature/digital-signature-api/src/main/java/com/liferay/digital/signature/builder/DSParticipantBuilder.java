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

import com.liferay.digital.signature.request.DSEmailNotification;
import com.liferay.digital.signature.request.DSParticipant;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSParticipantBuilder {

	public <T extends DSParticipant> T getDSParticipant();

	public DSParticipantBuilder setAccessCode(String accessCode);

	public DSParticipantBuilder setClientUserId(String clientId);

	public DSParticipantBuilder setCustomFields(String... customFields);

	public DSParticipantBuilder setDSEmailNotification(
		DSEmailNotification dsEmailNotification);

	public DSParticipantBuilder setNote(String note);

	public DSParticipantBuilder setParticipantId(String participantId);

	public DSParticipantBuilder setRoleName(String roleName);

}