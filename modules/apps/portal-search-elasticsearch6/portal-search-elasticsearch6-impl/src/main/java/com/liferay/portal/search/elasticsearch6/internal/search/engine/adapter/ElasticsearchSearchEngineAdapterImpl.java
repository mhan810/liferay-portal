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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.query.QueryTranslator;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.cluster.ClusterRequest;
import com.liferay.portal.search.engine.adapter.cluster.ClusterRequestExecutor;
import com.liferay.portal.search.engine.adapter.cluster.ClusterResponse;
import com.liferay.portal.search.engine.adapter.document.DocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DocumentRequestExecutor;
import com.liferay.portal.search.engine.adapter.document.DocumentResponse;
import com.liferay.portal.search.engine.adapter.index.IndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndexRequestExecutor;
import com.liferay.portal.search.engine.adapter.index.IndexResponse;
import com.liferay.portal.search.engine.adapter.search.SearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchRequestExecutor;
import com.liferay.portal.search.engine.adapter.search.SearchResponse;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotRequest;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotRequestExecutor;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotResponse;

import org.elasticsearch.index.query.QueryBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
@Component(
	immediate = true, property = "search.engine.impl=Elasticsearch",
	service = SearchEngineAdapter.class
)
public class ElasticsearchSearchEngineAdapterImpl
	implements SearchEngineAdapter {

	@Override
	public <T extends ClusterResponse> T execute(
		ClusterRequest<T> clusterRequest) {

		return clusterRequestExecutor.execute(clusterRequest);
	}

	@Override
	public <S extends DocumentResponse> S execute(
		DocumentRequest<S> documentRequest) {

		return documentRequest.accept(documentRequestExecutor);
	}

	@Override
	public <U extends IndexResponse> U execute(IndexRequest<U> indexRequest) {
		return indexRequest.accept(indexRequestExecutor);
	}

	@Override
	public <V extends SearchResponse> V execute(
		SearchRequest<V> searchRequest) {

		return searchRequest.accept(searchRequestExecutor);
	}

	@Override
	public <W extends SnapshotResponse> W execute(
		SnapshotRequest<W> snapshotRequest) {

		return snapshotRequest.accept(snapshotRequestExecutor);
	}

	@Override
	public String getQueryString(Query query) {
		QueryBuilder queryBuilder = queryTranslator.translate(query, null);

		return queryBuilder.toString();
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected ClusterRequestExecutor clusterRequestExecutor;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected DocumentRequestExecutor documentRequestExecutor;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected IndexRequestExecutor indexRequestExecutor;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected QueryTranslator<QueryBuilder> queryTranslator;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected SearchRequestExecutor searchRequestExecutor;

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected SnapshotRequestExecutor snapshotRequestExecutor;

}