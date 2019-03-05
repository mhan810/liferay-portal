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

import com.liferay.digital.signature.internal.request.VoidPackageDSSignatureRequestImpl;
import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.request.VoidPackageDSSignatureRequest;
import com.liferay.digital.signature.request.builder.VoidPackageDSSignatureRequestBuilder;

/**
 * @author Michael C. Han
 */
public class VoidPackageDSSignatureRequestBuilderImpl
	implements VoidPackageDSSignatureRequestBuilder {

	public VoidPackageDSSignatureRequestBuilderImpl(DSSessionId dsSessionId) {
		_dsSessionId = dsSessionId;
	}

	@Override
	public VoidPackageDSSignatureRequest getVoidPackageDSSignatureRequest() {
		VoidPackageDSSignatureRequestImpl dsSignaturePackageVoidRequestImpl =
			new VoidPackageDSSignatureRequestImpl(
				_dsSessionId, _dsSignaturePackageId);

		dsSignaturePackageVoidRequestImpl.setExternalReferenceId(
			_externalReferenceId);
		dsSignaturePackageVoidRequestImpl.setVoidReason(_voidReason);

		return dsSignaturePackageVoidRequestImpl;
	}

	@Override
	public VoidPackageDSSignatureRequestBuilder setDSSignaturePackageId(
		String dsSignaturePackageId) {

		_dsSignaturePackageId = dsSignaturePackageId;

		return this;
	}

	public VoidPackageDSSignatureRequestBuilder setExternalReferenceId(
		String externalReferenceId) {

		_externalReferenceId = externalReferenceId;

		return this;
	}

	@Override
	public VoidPackageDSSignatureRequestBuilder setVoidReason(
		String voidReason) {

		_voidReason = voidReason;

		return this;
	}

	private final DSSessionId _dsSessionId;
	private String _dsSignaturePackageId;
	private String _externalReferenceId;
	private String _voidReason;

}