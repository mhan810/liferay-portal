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

/**
 * @author Michael C. Han
 */
public class BaseParticipantModifyingDSParticipant
	extends BaseDSParticipantImpl {

	public BaseParticipantModifyingDSParticipant(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	public Boolean getCanEditParticipantEmails() {
		return _canEditParticipantEmails;
	}

	public Boolean getCanEditParticipantNames() {
		return _canEditParticipantNames;
	}

	public void setCanEditParticipantEmails(Boolean canEditParticipantEmails) {
		_canEditParticipantEmails = canEditParticipantEmails;
	}

	public void setCanEditParticipantNames(Boolean canEditParticipantNames) {
		_canEditParticipantNames = canEditParticipantNames;
	}

	private Boolean _canEditParticipantEmails;
	private Boolean _canEditParticipantNames;

}