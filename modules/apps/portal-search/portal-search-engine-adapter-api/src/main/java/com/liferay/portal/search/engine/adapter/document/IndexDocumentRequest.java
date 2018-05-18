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

package com.liferay.portal.search.engine.adapter.document;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.engine.adapter.DocumentRequest;
import com.liferay.portal.search.engine.adapter.DocumentRequestExecutor;

/**
 * @author Michael C. Han
 */
public class IndexDocumentRequest
	implements DocumentRequest<IndexDocumentResponse> {

	public IndexDocumentRequest(String indexName, Document document) {
		_indexName = indexName;
		_document = document;
	}

	@Override
	public IndexDocumentResponse accept(
		DocumentRequestExecutor documentRequestExecutor) {

		return documentRequestExecutor.executeDocumentRequest(this);
	}

	public Document getDocument() {
		return _document;
	}

	@Override
	public String getIndexName() {
		return _indexName;
	}

	public boolean isRefresh() {
		return _refresh;
	}

	public void setRefresh(boolean refresh) {
		_refresh = refresh;
	}

	private final Document _document;
	private final String _indexName;
	private boolean _refresh;

}