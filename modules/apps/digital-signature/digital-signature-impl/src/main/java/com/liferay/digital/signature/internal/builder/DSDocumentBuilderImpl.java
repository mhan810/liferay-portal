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

package com.liferay.digital.signature.internal.builder;

import com.liferay.digital.signature.builder.DSDocumentBuilder;
import com.liferay.digital.signature.internal.request.DSDocumentImpl;
import com.liferay.digital.signature.internal.request.DSSupplementalDocumentInfoImpl;
import com.liferay.digital.signature.request.DSDocument;
import com.liferay.digital.signature.request.DSSignerAcknowledgement;
import com.liferay.digital.signature.request.DSSupplementalDocumentInfo;

/**
 * @author Michael C. Han
 */
public class DSDocumentBuilderImpl implements DSDocumentBuilder {

	public DSDocumentBuilderImpl(String documentId, String name) {
		_documentId = documentId;
		_name = name;
	}

	@Override
	public DSDocument getDSDocument() {
		DSDocumentImpl dsDocumentImpl = new DSDocumentImpl(_documentId, _name);

		dsDocumentImpl.setAuthoritative(_authoritative);
		dsDocumentImpl.setBase64String(_base64String);
		dsDocumentImpl.setDescription(_description);
		dsDocumentImpl.setDocumentURL(_documentURL);
		dsDocumentImpl.setDSSupplementalDocumentInfo(
			_dsSupplementalDocumentInfo);
		dsDocumentImpl.setExtension(_extension);

		return dsDocumentImpl;
	}

	@Override
	public DSDocumentBuilder setAuthoritative(Boolean authoritative) {
		_authoritative = authoritative;

		return this;
	}

	@Override
	public DSDocumentBuilder setBase64String(String base64String) {
		_base64String = base64String;

		return this;
	}

	@Override
	public DSDocumentBuilder setDescription(String description) {
		_description = description;

		return this;
	}

	@Override
	public DSDocumentBuilder setDocumentURL(String documentURL) {
		_documentURL = documentURL;

		return this;
	}

	@Override
	public DSDocumentBuilder setExtension(String extension) {
		_extension = extension;

		return this;
	}

	@Override
	public DSDocumentBuilder setSupplementalDocumentInfo(
		Boolean display, Boolean includeInDownload,
		DSSignerAcknowledgement dsSignerAcknowledgement) {

		DSSupplementalDocumentInfoImpl dsSupplementalDocumentInfoImpl =
			new DSSupplementalDocumentInfoImpl();

		dsSupplementalDocumentInfoImpl.setDisplay(display);
		dsSupplementalDocumentInfoImpl.setIncludeInDownload(includeInDownload);
		dsSupplementalDocumentInfoImpl.setDSSignerAcknowledgement(
			dsSignerAcknowledgement);

		_dsSupplementalDocumentInfo = dsSupplementalDocumentInfoImpl;

		return this;
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