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

package com.liferay.digital.signature.internal.model.builder;

import com.liferay.digital.signature.internal.model.DSNotaryInfoImpl;
import com.liferay.digital.signature.internal.model.InPersonSignerNotaryDSParticipantImpl;
import com.liferay.digital.signature.internal.model.SignerDSParticipantImpl;
import com.liferay.digital.signature.model.InPersonSignerNotaryDSParticipant;
import com.liferay.digital.signature.model.builder.InPersonSignerNotaryDSParticipantBuilder;

/**
 * @author Michael C. Han
 */
public class InPersonSignerNotaryDSParticipantBuilderImpl
	extends BaseSigningDSParticipantBuilder<InPersonSignerNotaryDSParticipant>
	implements InPersonSignerNotaryDSParticipantBuilder {

	public InPersonSignerNotaryDSParticipantBuilderImpl(
		String name, String email, int routingOrder, String notaryParticipantId,
		String notaryName, String notaryEmail) {

		super(name, email, routingOrder);

		_notaryParticipantId = notaryParticipantId;
		_notaryName = notaryName;
		_notaryEmail = notaryEmail;
	}

	@Override
	protected SignerDSParticipantImpl createDSSignerParticipantImpl() {
		DSNotaryInfoImpl dsNotaryInfoImpl = new DSNotaryInfoImpl(
			_notaryParticipantId, _notaryName, _notaryEmail);

		return new InPersonSignerNotaryDSParticipantImpl(
			getName(), getEmail(), getRoutingOrder(), dsNotaryInfoImpl);
	}

	private final String _notaryEmail;
	private final String _notaryName;
	private final String _notaryParticipantId;

}