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

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentResponse;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;

/**
 * @author Dylan Rebelak
 */
public class UpdateByQueryDocumentRequestTranslatorImpl
	implements UpdateByQueryDocumentRequestTranslator {

	@Override
	public UpdateByQueryDocumentResponse execute(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		UpdateByQueryRequestBuilder updateByQueryRequestBuilder = createBuilder(
			updateByQueryDocumentRequest, elasticsearchConnectionManager);

		BulkByScrollResponse bulkByScrollResponse =
			updateByQueryRequestBuilder.get();

		TimeValue timeValue = bulkByScrollResponse.getTook();

		return new UpdateByQueryDocumentResponse(
			bulkByScrollResponse.getUpdated(), timeValue.getMillis());
	}

	protected UpdateByQueryRequestBuilder createBuilder(
		UpdateByQueryDocumentRequest updateByQueryDocumentRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		Client client = elasticsearchConnectionManager.getClient();

		UpdateByQueryRequestBuilder updateByQueryRequestBuilder =
			new UpdateByQueryRequestBuilder(
				client, UpdateByQueryAction.INSTANCE);

		Query query = updateByQueryDocumentRequest.getQuery();

		QueryBuilder queryBuilder = new QueryStringQueryBuilder(
			query.toString());

		updateByQueryRequestBuilder.filter(queryBuilder);

		JSONObject jsonScript = updateByQueryDocumentRequest.getScriptJson();

		if (jsonScript != null) {
			Script script = new Script(jsonScript.toString());

			updateByQueryRequestBuilder.script(script);
		}

		updateByQueryRequestBuilder.source(
			updateByQueryDocumentRequest.getIndexName());

		return updateByQueryRequestBuilder;
	}

}