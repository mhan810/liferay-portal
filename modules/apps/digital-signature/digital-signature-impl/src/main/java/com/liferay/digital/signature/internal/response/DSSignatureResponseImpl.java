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

package com.liferay.digital.signature.internal.response;

import com.liferay.digital.signature.response.DSSignatureRequestStatus;
import com.liferay.digital.signature.response.DSSignatureResponse;

/**
 * @author Michael C. Han
 */
public class DSSignatureResponseImpl implements DSSignatureResponse {

	public DSSignatureResponseImpl(
		String requestUUID, String externalReferenceId) {

		_requestUUID = requestUUID;
		_externalReferenceId = externalReferenceId;
	}

	@Override
	public DSSignatureRequestStatus getDSRequestStatus() {
		return _dsSignatureRequestStatus;
	}

	@Override
	public String getErrorMessage() {
		return _errorMessage;
	}

	@Override
	public String getExternalReferenceId() {
		return _externalReferenceId;
	}

	@Override
	public String getRequestUUID() {
		return _requestUUID;
	}

	public void setDSSignatureRequestStatus(
		DSSignatureRequestStatus dsSignatureRequestStatus) {

		_dsSignatureRequestStatus = dsSignatureRequestStatus;
	}

	public void setErrorMessage(String errorMessage) {
		_errorMessage = errorMessage;
	}

	private DSSignatureRequestStatus _dsSignatureRequestStatus;
	private String _errorMessage;
	private final String _externalReferenceId;
	private final String _requestUUID;

}