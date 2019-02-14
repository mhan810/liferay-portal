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

import com.liferay.digital.signature.builder.DSInPersonSignerParticipantBuilder;
import com.liferay.digital.signature.internal.request.DSInPersonSignerParticipantImpl;
import com.liferay.digital.signature.internal.request.DSSignerParticipantImpl;
import com.liferay.digital.signature.request.DSInPersonSignerParticipant;

/**
 * @author Michael C. Han
 */
public class DSInPersonSignerParticipantBuilderImpl
	extends BaseSigningDSParticipantBuilder<DSInPersonSignerParticipant>
	implements DSInPersonSignerParticipantBuilder {

	public DSInPersonSignerParticipantBuilderImpl(
		String hostName, String hostEmail, String signerName,
		String signerEmail, int routingOrder) {

		super(signerName, signerEmail, routingOrder);

		_hostName = hostName;
		_hostEmail = hostEmail;
	}

	@Override
	protected DSSignerParticipantImpl createDSSignerParticipantImpl() {
		DSInPersonSignerParticipantImpl dsInPersonSignerParticipantImpl =
			new DSInPersonSignerParticipantImpl(
				_hostName, _hostEmail, getName(), getEmail(),
				getRoutingOrder());

		return dsInPersonSignerParticipantImpl;
	}

	private final String _hostEmail;
	private final String _hostName;

}