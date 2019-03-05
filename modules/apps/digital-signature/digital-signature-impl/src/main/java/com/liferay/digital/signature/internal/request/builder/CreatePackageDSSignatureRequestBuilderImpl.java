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

import com.liferay.digital.signature.DSSignaturePackageStatus;
import com.liferay.digital.signature.internal.request.CreatePackageDSSignatureRequestImpl;
import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.model.DSSignaturePackage;
import com.liferay.digital.signature.request.CreatePackageDSSignatureRequest;
import com.liferay.digital.signature.request.builder.CreatePackageDSSignatureRequestBuilder;

/**
 * @author Michael C. Han
 */
public class CreatePackageDSSignatureRequestBuilderImpl
	implements CreatePackageDSSignatureRequestBuilder {

	public CreatePackageDSSignatureRequestBuilderImpl(DSSessionId dsSessionId) {
		_dsSessionId = dsSessionId;
	}

	@Override
	public CreatePackageDSSignatureRequest
		getDraftCreatePackageDSSignatureRequest() {

		return new CreatePackageDSSignatureRequestImpl(
			_dsSessionId, _dsSignaturePackage,
			DSSignaturePackageStatus.CREATED);
	}

	@Override
	public CreatePackageDSSignatureRequest
		getSentCreatePackageDSSignatureRequest() {

		return new CreatePackageDSSignatureRequestImpl(
			_dsSessionId, _dsSignaturePackage, DSSignaturePackageStatus.SENT);
	}

	@Override
	public CreatePackageDSSignatureRequestBuilder setDSSignaturePackage(
		DSSignaturePackage dsSignaturePackage) {

		_dsSignaturePackage = dsSignaturePackage;

		return this;
	}

	private final DSSessionId _dsSessionId;
	private DSSignaturePackage _dsSignaturePackage;

}