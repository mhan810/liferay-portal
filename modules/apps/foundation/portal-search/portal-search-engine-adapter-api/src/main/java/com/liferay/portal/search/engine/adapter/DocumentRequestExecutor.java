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

package com.liferay.portal.search.engine.adapter;

import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;

/**
 * @author Dylan Rebelak
 */
public interface DocumentRequestExecutor<T> {

	public T executeDocumentRequest(DeleteByQueryDocumentRequest deleteByQueryDocumentRequest);

	public T executeDocumentRequest(DeleteDocumentRequest deleteDocumentRequest);

	public T executeDocumentRequest(IndexDocumentRequest indexDocumentRequest);

	public T executeDocumentRequest(UpdateByQueryDocumentRequest updateByQueryDocumentRequest);

	public T executeDocumentRequest(UpdateDocumentRequest updateDocumentRequest);
}
