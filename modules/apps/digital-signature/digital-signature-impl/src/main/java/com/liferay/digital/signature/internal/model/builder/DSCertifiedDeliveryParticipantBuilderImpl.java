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

import com.liferay.digital.signature.internal.model.DSCertifiedDeliveryParticipantImpl;
import com.liferay.digital.signature.model.DSCertifiedDeliveryParticipant;
import com.liferay.digital.signature.model.builder.DSCertifiedDeliveryParticipantBuilder;

/**
 * @author Michael C. Han
 */
public class DSCertifiedDeliveryParticipantBuilderImpl
	extends BaseDSParticipantBuilder<DSCertifiedDeliveryParticipant>
	implements DSCertifiedDeliveryParticipantBuilder {

	public DSCertifiedDeliveryParticipantBuilderImpl(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	@Override
	protected DSCertifiedDeliveryParticipant createDSParticipant() {
		return new DSCertifiedDeliveryParticipantImpl(
			getName(), getEmail(), getRoutingOrder());
	}

}