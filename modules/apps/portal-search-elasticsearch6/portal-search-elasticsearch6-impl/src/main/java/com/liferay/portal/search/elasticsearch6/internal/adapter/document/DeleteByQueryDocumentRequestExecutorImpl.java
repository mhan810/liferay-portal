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

package com.liferay.portal.search.elasticsearch6.internal.adapter.document;

import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.DeleteByQueryDocumentResponse;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Dylan Rebelak
 */
public class DeleteByQueryDocumentRequestExecutorImpl
	implements DeleteByQueryDocumentRequestExecutor {

	@Override
	public DeleteByQueryDocumentResponse execute(
		DeleteByQueryDocumentRequest deleteByQueryDocumentRequest) {

		DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = createBuilder(
			deleteByQueryDocumentRequest, elasticsearchConnectionManager);

		BulkByScrollResponse bulkByScrollResponse =
			deleteByQueryRequestBuilder.get();

		TimeValue timeValue = bulkByScrollResponse.getTook();

		return new DeleteByQueryDocumentResponse(
			bulkByScrollResponse.getDeleted(), timeValue.getMillis());
	}

	protected DeleteByQueryRequestBuilder createBuilder(
		DeleteByQueryDocumentRequest deleteByQueryDocumentRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		Client client = elasticsearchConnectionManager.getClient();

		/* DeleteByQueryRequestBuilder deleteByQueryRequestBuilder =
			new DeleteByQueryRequestBuilder(
				client, DeleteByQueryAction.INSTANCE);*/

		DeleteByQueryRequestBuilder deleteByQueryRequestBuilder =
			DeleteByQueryAction.INSTANCE.newRequestBuilder(client);

		Query query = deleteByQueryDocumentRequest.getQuery();

		QueryBuilder queryBuilder = new QueryStringQueryBuilder(
			query.toString());

		deleteByQueryRequestBuilder.filter(queryBuilder);

		deleteByQueryRequestBuilder.refresh(
			deleteByQueryDocumentRequest.isRefresh());

		deleteByQueryRequestBuilder.source(
			deleteByQueryDocumentRequest.getIndexName());

		return deleteByQueryRequestBuilder;
	}

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

}