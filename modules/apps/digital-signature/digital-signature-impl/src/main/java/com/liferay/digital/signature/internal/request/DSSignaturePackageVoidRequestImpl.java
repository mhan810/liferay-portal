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

import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.request.DSSignaturePackageVoidRequest;

/**
 * @author Michael C. Han
 */
public class DSSignaturePackageVoidRequestImpl
	implements DSSignaturePackageVoidRequest {

	public DSSignaturePackageVoidRequestImpl(
		DSSessionId dsSessionId, String dsSignaturePackageId) {

		_dsSessionId = dsSessionId;
		_dsSignaturePackageId = dsSignaturePackageId;
	}

	@Override
	public DSSessionId getDSSessionId() {
		return _dsSessionId;
	}

	@Override
	public String getDSSignaturePackageId() {
		return _dsSignaturePackageId;
	}

	@Override
	public String getExternalReferenceId() {
		return _externalReferenceId;
	}

	@Override
	public String getVoidReason() {
		return _voidReason;
	}

	public void setExternalReferenceId(String externalReferenceId) {
		_externalReferenceId = externalReferenceId;
	}

	public void setVoidReason(String voidReason) {
		_voidReason = voidReason;
	}

	private final DSSessionId _dsSessionId;
	private final String _dsSignaturePackageId;
	private String _externalReferenceId;
	private String _voidReason;

}