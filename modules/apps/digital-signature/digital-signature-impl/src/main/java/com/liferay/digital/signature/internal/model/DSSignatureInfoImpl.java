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

import com.liferay.digital.signature.model.DSSignatureInfo;

/**
 * @author Michael C. Han
 */
public class DSSignatureInfoImpl implements DSSignatureInfo {

	@Override
	public String getSignatureFont() {
		return _signatureFont;
	}

	@Override
	public String getSignatureInitials() {
		return _signatureInitials;
	}

	@Override
	public String getSignatureName() {
		return _signatureName;
	}

	public void setSignatureFont(String signatureFont) {
		_signatureFont = signatureFont;
	}

	public void setSignatureInitials(String signatureInitials) {
		_signatureInitials = signatureInitials;
	}

	public void setSignatureName(String signatureName) {
		_signatureName = signatureName;
	}

	private String _signatureFont;
	private String _signatureInitials;
	private String _signatureName;

}