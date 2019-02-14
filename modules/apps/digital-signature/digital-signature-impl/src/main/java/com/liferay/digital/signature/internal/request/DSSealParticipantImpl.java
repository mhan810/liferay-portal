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

import com.liferay.digital.signature.request.DSParticipantRole;
import com.liferay.digital.signature.request.DSSealInfo;
import com.liferay.digital.signature.request.DSSealParticipant;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael C. Han
 */
public class DSSealParticipantImpl
	extends BaseDSParticipantImpl implements DSSealParticipant {

	public DSSealParticipantImpl(
		String participantId, String name, String email, int routingOrder) {

		super(name, email, routingOrder);

		setDSParticipantRole(DSParticipantRole.SEAL);
		setParticipantId(participantId);
	}

	public void addDSSealInfos(Collection<DSSealInfo> dsSealInfos) {
		_dsSealInfos.addAll(dsSealInfos);
	}

	@Override
	public Collection<DSSealInfo> getDSSealInfos() {
		return Collections.unmodifiableCollection(_dsSealInfos);
	}

	private Set<DSSealInfo> _dsSealInfos = new HashSet<>();

}