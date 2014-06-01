/*
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.search.elasticsearch;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.search.AbstractSearchEngineConfigurator;
import com.liferay.portal.kernel.search.IndexSearcher;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnection;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.io.StringOutputStream;
import com.liferay.portal.service.CompanyLocalService;

import java.util.List;
import java.util.concurrent.Future;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.io.stream.OutputStreamStreamOutput;

/**
 * @author Michael C. Han
 */
public class ElasticsearchEngineConfigurator
	extends AbstractSearchEngineConfigurator {

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		ElasticsearchConnectionManager elasticsearchConnectionManager =
			ElasticsearchConnectionManager.getInstance();

		ElasticsearchConnection elasticsearchConnection =
			elasticsearchConnectionManager.getElasticsearchConnection();

		elasticsearchConnection.initialize();

		AdminClient adminClient = elasticsearchConnection.getClient().admin();

		IndicesAdminClient indicesAdminClient = adminClient.indices();

		try {
			List<Company> companies = _companyLocalService.getCompanies();

			for (Company company : companies) {
				IndicesExistsRequestBuilder indicesExistsRequestBuilder =
					indicesAdminClient.prepareExists(
						String.valueOf(company.getCompanyId()));

				Future<IndicesExistsResponse> indicesExistsFuture =
					indicesExistsRequestBuilder.execute();

				IndicesExistsResponse indicesExistsResponse =
					indicesExistsFuture.get();

				if (indicesExistsResponse.isExists()) {
					continue;
				}

				CreateIndexRequestBuilder createIndexRequestBuilder =
					indicesAdminClient.prepareCreate(
						String.valueOf(company.getCompanyId()));

				Future<CreateIndexResponse> createIndexFuture =
					createIndexRequestBuilder.execute();

				CreateIndexResponse createIndexResponse = createIndexFuture.get();

				if (_log.isInfoEnabled()) {
					StringOutputStream stringOutputStream =
						new StringOutputStream();

					createIndexResponse.writeTo(
						new OutputStreamStreamOutput(stringOutputStream));

					_log.info(stringOutputStream);
				}
			}
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		ElasticsearchConnectionManager elasticsearchConnectionManager =
			ElasticsearchConnectionManager.getInstance();

		ElasticsearchConnection elasticsearchConnection =
			elasticsearchConnectionManager.getElasticsearchConnection();

		elasticsearchConnection.close();

		Client client = elasticsearchConnectionManager.getClient();

		client.close();
	}

	@Override
	protected String getDefaultSearchEngineId() {
		return SearchEngineUtil.SYSTEM_ENGINE_ID;
	}

	@Override
	protected IndexSearcher getIndexSearcher() {
		return _indexSearcher;
	}

	@Override
	protected IndexWriter getIndexWriter() {
		return _indexWriter;
	}

	@Override
	protected MessageBus getMessageBus() {
		return _messageBus;
	}

	@Override
	protected ClassLoader getOperatingClassloader() {
		Class<?> clazz = getClass();

		return clazz.getClassLoader();
	}

	public void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	public void setIndexSearcher(IndexSearcher indexSearcher) {
		_indexSearcher = indexSearcher;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		_indexWriter = indexWriter;
	}

	public void setMessageBus(MessageBus messageBus) {
		_messageBus = messageBus;
	}

	private static Log _log = LogFactoryUtil.getLog(
		ElasticsearchEngineConfigurator.class);

	private CompanyLocalService _companyLocalService;
	private IndexSearcher _indexSearcher;
	private IndexWriter _indexWriter;
	private MessageBus _messageBus;

}