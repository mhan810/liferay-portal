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

import com.liferay.digital.signature.internal.model.InPersonSignerDSParticipantImpl;
import com.liferay.digital.signature.internal.model.SignerDSParticipantImpl;
import com.liferay.digital.signature.model.InPersonSignerDSParticipant;
import com.liferay.digital.signature.model.builder.InPersonSignerDSParticipantBuilder;

/**
 * @author Michael C. Han
 */
public class InPersonSignerDSParticipantBuilderImpl
	extends BaseSigningDSParticipantBuilder<InPersonSignerDSParticipant>
	implements InPersonSignerDSParticipantBuilder {

	public InPersonSignerDSParticipantBuilderImpl(
		String hostName, String hostEmail, String signerName,
		String signerEmail, int routingOrder) {

		super(signerName, signerEmail, routingOrder);

		_hostName = hostName;
		_hostEmail = hostEmail;
	}

	@Override
	protected SignerDSParticipantImpl createDSSignerParticipantImpl() {
		return new InPersonSignerDSParticipantImpl(
			_hostName, _hostEmail, getName(), getEmail(), getRoutingOrder());
	}

	private final String _hostEmail;
	private final String _hostName;

}