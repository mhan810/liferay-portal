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

import com.liferay.digital.signature.model.DSDocumentDisplay;
import com.liferay.digital.signature.model.DSSignerAcknowledgement;
import com.liferay.digital.signature.model.DSSupplementalDocumentInfo;

/**
 * @author Michael C. Han
 */
public class DSSupplementalDocumentInfoImpl
	implements DSSupplementalDocumentInfo {

	@Override
	public DSDocumentDisplay getDSDocumentDisplay() {
		return _dsDocumentDisplay;
	}

	@Override
	public DSSignerAcknowledgement getDSSignerAcknowledgement() {
		return _dsSignerAcknowledgement;
	}

	@Override
	public Boolean getIncludeInDownload() {
		return _includeInDownload;
	}

	public void setDSDocumentDisplay(DSDocumentDisplay dsDocumentDisplay) {
		_dsDocumentDisplay = dsDocumentDisplay;
	}

	public void setDSSignerAcknowledgement(
		DSSignerAcknowledgement dsSignerAcknowledgement) {

		_dsSignerAcknowledgement = dsSignerAcknowledgement;
	}

	public void setIncludeInDownload(Boolean includeInDownload) {
		_includeInDownload = includeInDownload;
	}

	private DSDocumentDisplay _dsDocumentDisplay;
	private DSSignerAcknowledgement _dsSignerAcknowledgement;
	private Boolean _includeInDownload;

}