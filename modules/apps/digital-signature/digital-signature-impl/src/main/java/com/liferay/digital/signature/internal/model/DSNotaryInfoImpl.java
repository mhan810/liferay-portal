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

import com.liferay.digital.signature.model.DSNotaryInfo;

/**
 * @author Michael C. Han
 */
public class DSNotaryInfoImpl implements DSNotaryInfo {

	public DSNotaryInfoImpl(String participantId, String name, String email) {
		_participantId = participantId;
		_name = name;
		_email = email;
	}

	@Override
	public String getEmail() {
		return _email;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getParticipantId() {
		return _participantId;
	}

	private final String _email;
	private final String _name;
	private final String _participantId;

}