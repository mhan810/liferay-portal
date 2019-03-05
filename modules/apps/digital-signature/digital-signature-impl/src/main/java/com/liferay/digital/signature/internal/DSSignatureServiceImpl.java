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

package com.liferay.digital.signature.internal;

import com.liferay.digital.signature.DSSignaturePackageStatus;
import com.liferay.digital.signature.DSSignatureService;
import com.liferay.digital.signature.adapter.spi.DSSignatureAdapter;
import com.liferay.digital.signature.internal.response.DSSignatureResponseImpl;
import com.liferay.digital.signature.request.PackageDSSignatureRequest;
import com.liferay.digital.signature.response.DSSignatureResponse;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(service = DSSignatureService.class)
public class DSSignatureServiceImpl implements DSSignatureService {

	@Override
	public DSSignatureResponse execute(
		PackageDSSignatureRequest packageDSSignatureRequest) {

		Optional<DSSignatureResponse> dsSignatureResponseOptional =
			validateDSSignatureAdapter();

		return dsSignatureResponseOptional.orElse(
			_dsSignatureAdapter.execute(packageDSSignatureRequest));
	}

	protected void setDSSignatureAdapter(
		DSSignatureAdapter dsSignatureAdapter) {

		_dsSignatureAdapter = dsSignatureAdapter;
	}

	protected Optional<DSSignatureResponse> validateDSSignatureAdapter() {
		if (_dsSignatureAdapter != null) {
			return Optional.empty();
		}

		if (_log.isWarnEnabled()) {
			_log.warn("No DSSignatureAdapter configured");
		}

		DSSignatureResponseImpl dsSignatureResponseImpl =
			new DSSignatureResponseImpl(null, null);

		dsSignatureResponseImpl.setDSSignatureRequestStatus(
			DSSignaturePackageStatus.FAILED);

		dsSignatureResponseImpl.setErrorMessage(
			"No DSSignatureAdapter configured");

		return Optional.of(dsSignatureResponseImpl);
	}

	private static volatile Log _log = LogFactoryUtil.getLog(
		DSSignatureServiceImpl.class);

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile DSSignatureAdapter _dsSignatureAdapter;

}