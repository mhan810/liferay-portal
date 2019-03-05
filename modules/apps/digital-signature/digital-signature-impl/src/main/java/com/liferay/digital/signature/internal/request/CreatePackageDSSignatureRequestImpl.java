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

import com.liferay.digital.signature.DSSignaturePackageStatus;
import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.model.DSSignaturePackage;
import com.liferay.digital.signature.request.CreatePackageDSSignatureRequest;

/**
 * @author Michael C. Han
 */
public class CreatePackageDSSignatureRequestImpl
	implements CreatePackageDSSignatureRequest {

	public CreatePackageDSSignatureRequestImpl(
		DSSessionId dsSessionId, DSSignaturePackage dsSignaturePackage,
		DSSignaturePackageStatus dsSignaturePackageStatus) {

		_dsSessionId = dsSessionId;
		_dsSignaturePackage = dsSignaturePackage;
		_dsSignaturePackageStatus = dsSignaturePackageStatus;
	}

	@Override
	public DSSessionId getDSSessionId() {
		return _dsSessionId;
	}

	@Override
	public DSSignaturePackage getDSSignaturePackage() {
		return _dsSignaturePackage;
	}

	public DSSignaturePackageStatus getDSSignaturePackageStatus() {
		return _dsSignaturePackageStatus;
	}

	private final DSSessionId _dsSessionId;
	private final DSSignaturePackage _dsSignaturePackage;
	private final DSSignaturePackageStatus _dsSignaturePackageStatus;

}