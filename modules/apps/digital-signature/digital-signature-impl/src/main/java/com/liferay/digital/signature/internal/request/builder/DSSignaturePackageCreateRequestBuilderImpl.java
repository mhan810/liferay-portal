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
import com.liferay.digital.signature.internal.request.DSSignaturePackageCreateRequestImpl;
import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.model.DSSignaturePackage;
import com.liferay.digital.signature.request.DSSignaturePackageCreateRequest;
import com.liferay.digital.signature.request.builder.DSSignaturePackageCreateRequestBuilder;

/**
 * @author Michael C. Han
 */
public class DSSignaturePackageCreateRequestBuilderImpl
	implements DSSignaturePackageCreateRequestBuilder {

	public DSSignaturePackageCreateRequestBuilderImpl(DSSessionId dsSessionId) {
		_dsSessionId = dsSessionId;
	}

	@Override
	public DSSignaturePackageCreateRequest
		createDraftDSSignaturePackageCreateRequest() {

		return new DSSignaturePackageCreateRequestImpl(
			_dsSessionId, _dsSignaturePackage,
			DSSignaturePackageStatus.CREATED);
	}

	@Override
	public DSSignaturePackageCreateRequest
		createDSSignaturePackageCreateRequest() {

		return new DSSignaturePackageCreateRequestImpl(
			_dsSessionId, _dsSignaturePackage, DSSignaturePackageStatus.SENT);
	}

	@Override
	public DSSignaturePackageCreateRequestBuilder setDSSignaturePackage(
		DSSignaturePackage dsSignaturePackage) {

		_dsSignaturePackage = dsSignaturePackage;

		return this;
	}

	private final DSSessionId _dsSessionId;
	private DSSignaturePackage _dsSignaturePackage;

}