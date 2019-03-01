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

package com.liferay.digital.signature.internal.request.builder;

import com.liferay.digital.signature.internal.request.DSSignaturePackageVoidRequestImpl;
import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.request.DSSignaturePackageVoidRequest;
import com.liferay.digital.signature.request.builder.DSSignaturePackageVoidRequestBuilder;

/**
 * @author Michael C. Han
 */
public class DSSignaturePackageVoidRequestBuilderImpl
	implements DSSignaturePackageVoidRequestBuilder {

	public DSSignaturePackageVoidRequestBuilderImpl(DSSessionId dsSessionId) {
		_dsSessionId = dsSessionId;
	}

	@Override
	public DSSignaturePackageVoidRequest getDSSignaturePackageVoidRequest() {
		DSSignaturePackageVoidRequestImpl dsSignaturePackageVoidRequestImpl =
			new DSSignaturePackageVoidRequestImpl(
				_dsSessionId, _dsSignaturePackageId);

		dsSignaturePackageVoidRequestImpl.setExternalReferenceId(
			_externalReferenceId);
		dsSignaturePackageVoidRequestImpl.setVoidReason(_voidReason);

		return dsSignaturePackageVoidRequestImpl;
	}

	@Override
	public DSSignaturePackageVoidRequestBuilder setDSSignaturePackageId(
		String dsSignaturePackageId) {

		_dsSignaturePackageId = dsSignaturePackageId;

		return this;
	}

	public DSSignaturePackageVoidRequestBuilder setExternalReferenceId(
		String externalReferenceId) {

		_externalReferenceId = externalReferenceId;

		return this;
	}

	@Override
	public DSSignaturePackageVoidRequestBuilder setVoidReason(
		String voidReason) {

		_voidReason = voidReason;

		return this;
	}

	private final DSSessionId _dsSessionId;
	private String _dsSignaturePackageId;
	private String _externalReferenceId;
	private String _voidReason;

}