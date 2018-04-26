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

package com.liferay.portal.search.elasticsearch6.internal.adapter;

import com.liferay.portal.search.engine.adapter.ClusterRequest;
import com.liferay.portal.search.engine.adapter.DocumentRequest;
import com.liferay.portal.search.engine.adapter.DocumentRequestExecutor;
import com.liferay.portal.search.engine.adapter.DocumentResponse;
import com.liferay.portal.search.engine.adapter.IndexRequest;
import com.liferay.portal.search.engine.adapter.IndexRequestExecutor;
import com.liferay.portal.search.engine.adapter.IndexResponse;
import com.liferay.portal.search.engine.adapter.SearchEngine;
import com.liferay.portal.search.engine.adapter.SearchRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(immediate = true, service = SearchEngine.class)
public class ElasticsearchSearchEngineImpl implements SearchEngine {

	@Override
	public void execute(ClusterRequest clusterRequest) {
	}

	@Override
	public DocumentResponse execute(DocumentRequest documentRequest) {
		return documentRequest.accept(documentRequestExecutor);
	}

	@Override
	public IndexResponse execute(IndexRequest indexRequest) {
		return indexRequest.accept(indexRequestExecutor);
	}

	@Override
	public void execute(SearchRequest searchRequest) {
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected DocumentRequestExecutor<DocumentResponse> documentRequestExecutor;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected IndexRequestExecutor<IndexResponse> indexRequestExecutor;

}