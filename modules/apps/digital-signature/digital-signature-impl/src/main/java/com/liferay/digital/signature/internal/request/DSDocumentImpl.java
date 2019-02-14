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

import com.liferay.digital.signature.request.DSDocument;
import com.liferay.digital.signature.request.DSSupplementalDocumentInfo;

/**
 * @author Michael C. Han
 */
public class DSDocumentImpl implements DSDocument {

	public DSDocumentImpl(String documentId, String name) {
		_documentId = documentId;
		_name = name;
	}

	@Override
	public Boolean getAuthoritative() {
		return _authoritative;
	}

	@Override
	public String getBase64String() {
		return _base64String;
	}

	@Override
	public String getDescription() {
		return _description;
	}

	@Override
	public String getDocumentId() {
		return _documentId;
	}

	@Override
	public String getDocumentURL() {
		return _documentURL;
	}

	@Override
	public DSSupplementalDocumentInfo getDSSupplementalDocumentInfo() {
		return _dsSupplementalDocumentInfo;
	}

	@Override
	public String getExtension() {
		return _extension;
	}

	@Override
	public String getName() {
		return _name;
	}

	public void setAuthoritative(Boolean authoritative) {
		_authoritative = authoritative;
	}

	public void setBase64String(String base64String) {
		_base64String = base64String;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDocumentURL(String documentURL) {
		_documentURL = documentURL;
	}

	public void setDSSupplementalDocumentInfo(
		DSSupplementalDocumentInfo dsSupplementalDocumentInfo) {

		_dsSupplementalDocumentInfo = dsSupplementalDocumentInfo;
	}

	public void setExtension(String extension) {
		_extension = extension;
	}

	private Boolean _authoritative;
	private String _base64String;
	private String _description;
	private final String _documentId;
	private String _documentURL;
	private DSSupplementalDocumentInfo _dsSupplementalDocumentInfo;
	private String _extension;
	private final String _name;

}