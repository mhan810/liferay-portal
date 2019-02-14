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

import com.liferay.digital.signature.request.DSInPersonSignerNotaryParticipant;
import com.liferay.digital.signature.request.DSInPersonSignerType;
import com.liferay.digital.signature.request.DSNotaryInfo;
import com.liferay.digital.signature.request.DSParticipantRole;

/**
 * @author Michael C. Han
 */
public class DSInPersonSignerNotaryParticipantImpl
	extends DSSignerParticipantImpl
	implements DSInPersonSignerNotaryParticipant {

	public DSInPersonSignerNotaryParticipantImpl(
		String name, String email, int routingOrder,
		DSNotaryInfo dsNotaryInfo) {

		super(name, email, routingOrder);

		setDSParticipantRole(DSParticipantRole.IN_PERSON_SIGNER);

		_dsNotaryInfo = dsNotaryInfo;
	}

	@Override
	public DSInPersonSignerType getDSInPersonSignerType() {
		return DSInPersonSignerType.NOTARY;
	}

	@Override
	public DSNotaryInfo getDSNotaryInfo() {
		return _dsNotaryInfo;
	}

	private final DSNotaryInfo _dsNotaryInfo;

}