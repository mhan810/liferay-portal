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

import com.liferay.digital.signature.builder.DSIntermediaryParticipantBuilder;
import com.liferay.digital.signature.internal.request.DSIntermediaryParticipantImpl;
import com.liferay.digital.signature.request.DSIntermediaryParticipant;

/**
 * @author Michael C. Han
 */
public class DSIntermediaryParticipantBuilderImpl
	extends BaseParticipantModifyingDSParticipantBuilder
		<DSIntermediaryParticipant>
	implements DSIntermediaryParticipantBuilder {

	public DSIntermediaryParticipantBuilderImpl(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	@Override
	protected DSIntermediaryParticipant createDSParticipant() {
		DSIntermediaryParticipantImpl dsIntermediaryParticipantImpl =
			new DSIntermediaryParticipantImpl(
				getName(), getEmail(), getRoutingOrder());

		dsIntermediaryParticipantImpl.setCanEditParticipantEmails(
			getCanEditParticipantEmails());

		dsIntermediaryParticipantImpl.setCanEditParticipantNames(
			getCanEditParticipantNames());

		return dsIntermediaryParticipantImpl;
	}

}