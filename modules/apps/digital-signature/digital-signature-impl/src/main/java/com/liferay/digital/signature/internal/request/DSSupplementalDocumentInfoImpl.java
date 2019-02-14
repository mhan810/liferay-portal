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

import com.liferay.digital.signature.request.DSSignerAcknowledgement;
import com.liferay.digital.signature.request.DSSupplementalDocumentInfo;

/**
 * @author Michael C. Han
 */
public class DSSupplementalDocumentInfoImpl
	implements DSSupplementalDocumentInfo {

	@Override
	public Boolean getDisplay() {
		return _display;
	}

	@Override
	public DSSignerAcknowledgement getDSSignerAcknowledgement() {
		return _dsSignerAcknowledgement;
	}

	@Override
	public Boolean getIncludeInDownload() {
		return _includeInDownload;
	}

	public void setDisplay(Boolean display) {
		_display = display;
	}

	public void setDSSignerAcknowledgement(
		DSSignerAcknowledgement dsSignerAcknowledgement) {

		_dsSignerAcknowledgement = dsSignerAcknowledgement;
	}

	public void setIncludeInDownload(Boolean includeInDownload) {
		_includeInDownload = includeInDownload;
	}

	private Boolean _display;
	private DSSignerAcknowledgement _dsSignerAcknowledgement;
	private Boolean _includeInDownload;

}