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

package com.liferay.portal.search.engine.adapter.index;

import com.liferay.portal.search.engine.adapter.IndexRequest;
import com.liferay.portal.search.engine.adapter.IndexRequestExecutor;

/**
 * @author Dylan Rebelak
 */
public class GetFieldMappingIndexRequest implements IndexRequest {

	public GetFieldMappingIndexRequest(
		String indexName, String mappingName, String[] fields) {

		_indexName = indexName;
		_mappingName = mappingName;
		_fields = fields;
	}

	@Override
	public <T> T accept(IndexRequestExecutor<T> indexRequestExecutor) {
		return indexRequestExecutor.executeIndexRequest(this);
	}

	public String[] getFields() {
		return _fields;
	}

	@Override
	public String getIndexName() {
		return _indexName;
	}

	@Override
	public String getMappingName() {
		return _mappingName;
	}

	private final String[] _fields;
	private final String _indexName;
	private final String _mappingName;

}