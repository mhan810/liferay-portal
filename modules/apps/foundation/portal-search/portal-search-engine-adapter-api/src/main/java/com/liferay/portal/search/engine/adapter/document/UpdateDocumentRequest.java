/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.engine.adapter.document;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.engine.adapter.DocumentRequest;

/**
 * @author Michael C. Han
 */
public class UpdateDocumentRequest implements DocumentRequest {

	public UpdateDocumentRequest(
		String indexName, String uid, Document document) {

		_indexName = indexName;
		_uid = uid;
		_document = document;
	}

	@Override
	public String getIndexName() {
		return _indexName;
	}

	public String getUid() {
		return _uid;
	}

	public Document getDocument() {
		return _document;
	}

	public boolean isRefresh() {
		return _refresh;
	}

	public void setRefresh(boolean refresh) {
		_refresh = refresh;
	}

	private boolean _refresh;
	private final String _indexName;
	private final String _uid;
	private final Document _document;

}
