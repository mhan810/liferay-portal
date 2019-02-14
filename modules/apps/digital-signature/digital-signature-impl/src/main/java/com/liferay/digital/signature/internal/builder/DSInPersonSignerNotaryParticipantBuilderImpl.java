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

import com.liferay.digital.signature.builder.DSInPersonSignerNotaryParticipantBuilder;
import com.liferay.digital.signature.internal.request.DSInPersonSignerNotaryParticipantImpl;
import com.liferay.digital.signature.internal.request.DSNotaryInfoImpl;
import com.liferay.digital.signature.internal.request.DSSignerParticipantImpl;
import com.liferay.digital.signature.request.DSInPersonSignerNotaryParticipant;

/**
 * @author Michael C. Han
 */
public class DSInPersonSignerNotaryParticipantBuilderImpl
	extends BaseSigningDSParticipantBuilder<DSInPersonSignerNotaryParticipant>
	implements DSInPersonSignerNotaryParticipantBuilder {

	public DSInPersonSignerNotaryParticipantBuilderImpl(
		String name, String email, int routingOrder, String notaryHost,
		String notaryName, String notaryEmail) {

		super(name, email, routingOrder);

		_notaryHost = notaryHost;
		_notaryName = notaryName;
		_notaryEmail = notaryEmail;
	}

	@Override
	protected DSSignerParticipantImpl createDSSignerParticipantImpl() {
		DSNotaryInfoImpl dsNotaryInfoImpl = new DSNotaryInfoImpl(
			_notaryHost, _notaryName, _notaryEmail);

		DSInPersonSignerNotaryParticipantImpl
			dsInPersonSignerNotaryParticipantImpl =
				new DSInPersonSignerNotaryParticipantImpl(
					getName(), getEmail(), getRoutingOrder(), dsNotaryInfoImpl);

		return dsInPersonSignerNotaryParticipantImpl;
	}

	private final String _notaryEmail;
	private final String _notaryHost;
	private final String _notaryName;

}