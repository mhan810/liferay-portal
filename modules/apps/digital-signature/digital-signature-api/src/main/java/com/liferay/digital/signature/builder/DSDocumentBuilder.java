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

package com.liferay.digital.signature.builder;

import aQute.bnd.annotation.ProviderType;

import com.liferay.digital.signature.request.DSDocument;
import com.liferay.digital.signature.request.DSSignerAcknowledgement;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface DSDocumentBuilder {

	public DSDocument getDSDocument();

	public DSDocumentBuilder setAuthoritative(Boolean authoritative);

	public DSDocumentBuilder setBase64String(String base64String);

	public DSDocumentBuilder setDescription(String description);

	public DSDocumentBuilder setDocumentURL(String documentURL);

	public DSDocumentBuilder setExtension(String extension);

	public DSDocumentBuilder setSupplementalDocumentInfo(
		Boolean display, Boolean includeInDownload,
		DSSignerAcknowledgement dsSignerAcknowledgement);

}