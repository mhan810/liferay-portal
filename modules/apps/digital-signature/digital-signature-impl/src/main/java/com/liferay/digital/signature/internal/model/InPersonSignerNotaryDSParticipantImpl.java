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

package com.liferay.digital.signature.internal.model;

import com.liferay.digital.signature.model.DSInPersonSignerType;
import com.liferay.digital.signature.model.DSNotaryInfo;
import com.liferay.digital.signature.model.DSParticipantRole;
import com.liferay.digital.signature.model.DSParticipantVisitor;
import com.liferay.digital.signature.model.InPersonSignerNotaryDSParticipant;

/**
 * @author Michael C. Han
 */
public class InPersonSignerNotaryDSParticipantImpl
	extends SignerDSParticipantImpl
	implements InPersonSignerNotaryDSParticipant {

	public InPersonSignerNotaryDSParticipantImpl(
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

	@Override
	public <T> T translate(DSParticipantVisitor<T> dsParticipantVisitor) {
		return dsParticipantVisitor.visit(this);
	}

	private final DSNotaryInfo _dsNotaryInfo;

}