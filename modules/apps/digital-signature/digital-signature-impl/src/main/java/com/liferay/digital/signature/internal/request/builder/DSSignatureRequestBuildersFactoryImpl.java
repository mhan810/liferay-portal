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

import com.liferay.digital.signature.model.DSSessionId;
import com.liferay.digital.signature.request.builder.DSSignaturePackageCreateRequestBuilder;
import com.liferay.digital.signature.request.builder.DSSignaturePackageVoidRequestBuilder;
import com.liferay.digital.signature.request.builder.DSSignatureRequestBuildersFactory;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = DSSignatureRequestBuildersFactory.class)
public class DSSignatureRequestBuildersFactoryImpl
	implements DSSignatureRequestBuildersFactory {

	@Override
	public DSSignaturePackageCreateRequestBuilder
		createDSSignaturePackageCreateRequestBuilder(DSSessionId dsSessionId) {

		return new DSSignaturePackageCreateRequestBuilderImpl(dsSessionId);
	}

	@Override
	public DSSignaturePackageVoidRequestBuilder
		createDSSignaturePackageVoidRequestBuilder(DSSessionId dsSessionId) {

		return new DSSignaturePackageVoidRequestBuilderImpl(dsSessionId);
	}

}