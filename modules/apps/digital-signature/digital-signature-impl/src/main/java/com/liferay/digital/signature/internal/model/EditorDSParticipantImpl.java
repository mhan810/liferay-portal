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

import com.liferay.digital.signature.model.DSParticipantVisitor;
import com.liferay.digital.signature.model.EditorDSParticipant;

/**
 * @author Michael C. Han
 */
public class EditorDSParticipantImpl
	extends BaseParticipantModifyingDSParticipant
	implements EditorDSParticipant {

	public EditorDSParticipantImpl(
		String name, String email, int routingOrder) {

		super(name, email, routingOrder);
	}

	@Override
	public <T> T translate(DSParticipantVisitor<T> dsParticipantVisitor) {
		return dsParticipantVisitor.visit(this);
	}

}