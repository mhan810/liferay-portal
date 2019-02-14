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

import com.liferay.digital.signature.builder.DSSealParticipantBuilder;
import com.liferay.digital.signature.internal.request.DSSealInfoImpl;
import com.liferay.digital.signature.internal.request.DSSealParticipantImpl;
import com.liferay.digital.signature.request.DSSealInfo;
import com.liferay.digital.signature.request.DSSealParticipant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class DSSealParticipantBuilderImpl
	extends BaseDSParticipantBuilder<DSSealParticipant>
	implements DSSealParticipantBuilder {

	public DSSealParticipantBuilderImpl(
		String participantId, String name, String email, int routingOrder) {

		super(name, email, routingOrder);

		setParticipantId(participantId);
	}

	@Override
	public DSSealParticipantBuilder addDSSealInfo(String sealName) {
		DSSealInfoImpl dsSealInfoImpl = new DSSealInfoImpl(sealName);

		_dsSealInfos.add(dsSealInfoImpl);

		return this;
	}

	@Override
	public DSSealParticipantBuilder addDSSealInfo(
		String sealName, Boolean sealDocumentsWithFieldsOnly) {

		DSSealInfoImpl dsSealInfoImpl = new DSSealInfoImpl(
			sealName, sealDocumentsWithFieldsOnly);

		_dsSealInfos.add(dsSealInfoImpl);

		return this;
	}

	@Override
	protected DSSealParticipant createDSParticipant() {
		DSSealParticipantImpl dsSealParticipantImpl = new DSSealParticipantImpl(
			getParticipantId(), getName(), getEmail(), getRoutingOrder());

		dsSealParticipantImpl.addDSSealInfos(_dsSealInfos);

		return dsSealParticipantImpl;
	}

	private Set<DSSealInfo> _dsSealInfos = new HashSet<>();

}