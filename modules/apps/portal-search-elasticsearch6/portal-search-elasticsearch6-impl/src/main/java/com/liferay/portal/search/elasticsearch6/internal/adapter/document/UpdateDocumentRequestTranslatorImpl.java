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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.document.DefaultElasticsearchDocumentFactory;
import com.liferay.portal.search.elasticsearch6.internal.document.ElasticsearchDocumentFactory;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentResponse;

import java.io.IOException;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;

/**
 * @author Dylan Rebelak
 */
public class UpdateDocumentRequestTranslatorImpl
	implements UpdateDocumentRequestTranslator {

	@Override
	public UpdateDocumentResponse execute(
		UpdateDocumentRequest updateDocumentRequest,
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		UpdateRequestBuilder updateRequestBuilder;
		try {
			updateRequestBuilder = createBuilder(
				updateDocumentRequest, elasticsearchConnectionManager);
		}
		catch (IOException ioe) {
			if (_log.isDebugEnabled()) {
				_log.debug(ioe);
			}

			return null;
		}

		UpdateResponse updateResponse = updateRequestBuilder.get();

		RestStatus restStatus = updateResponse.status();

		return new UpdateDocumentResponse(restStatus.getStatus());
	}

	protected UpdateRequestBuilder createBuilder(
			UpdateDocumentRequest updateDocumentRequest,
			ElasticsearchConnectionManager elasticsearchConnectionManager)
		throws IOException {

		Client client = elasticsearchConnectionManager.getClient();

		UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();

		Document document = updateDocumentRequest.getDocument();

		updateRequestBuilder.setIndex(updateDocumentRequest.getIndexName());
		updateRequestBuilder.setId(document.getUID());
		updateRequestBuilder.setType(document.get(Field.TYPE));

		ElasticsearchDocumentFactory elasticsearchDocumentFactory =
			new DefaultElasticsearchDocumentFactory();

		String elasticsearchDocument =
			elasticsearchDocumentFactory.getElasticsearchDocument(document);

		updateRequestBuilder.setDoc(elasticsearchDocument, XContentType.JSON);

		return updateRequestBuilder;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateDocumentRequestTranslatorImpl.class);

}