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

package com.liferay.portal.search.elasticsearch.document;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.StringQueryImpl;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.elasticsearch.ElasticsearchIndexSearcher;
import com.liferay.portal.search.elasticsearch.ElasticsearchUpdateDocumentCommandImpl;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.util.EmbeddedElasticsearchServer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@PrepareForTest( {
	ElasticsearchUpdateDocumentCommandImpl.class, LocalizationUtil.class,
	PropsUtil.class
})
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public abstract class BaseElasticsearchTest extends PowerMockito {

	public EmbeddedElasticsearchServer createSearchServer() throws Exception {

		EmbeddedElasticsearchServer.Builder searchServerBuilder =
			EmbeddedElasticsearchServer.serverBuilder(ELASTICSEARCH_CONFIG);

		return searchServerBuilder.build();
	}

	public String getIndexedJsonDocument(String id) {

		return _embeddedElasticsearchServer.getIndexedJsonDocument(id);
	}

	public String getIndexedJsonDocument(String indexName, String id) {

		return _embeddedElasticsearchServer.getIndexedJsonDocument(
			indexName, id);
	}

	public String indexJsonDocument(String jsonDocument) {

		return _embeddedElasticsearchServer.indexJsonDocument(jsonDocument);
	}

	public String indexJsonDocument(String indexName, String jsonDocument) {

		return _embeddedElasticsearchServer.indexJsonDocument(
			indexName, jsonDocument);
	}

	public Hits search() {

		StringQueryImpl query = new StringQueryImpl("");
		return _elasticsearchIndexSearcher.search(getSearchContext(), query);
	}

	public SearchHits searchDocument(String field, String value) {

		return _embeddedElasticsearchServer.searchDocument(field, value);
	}

	public SearchHits searchDocument(Map<String, String> fieldsValues) {

		return _embeddedElasticsearchServer.searchDocument(fieldsValues);
	}

	public SearchHits searchDocument(String index, String type,
		String field, String value) {

		return _embeddedElasticsearchServer.searchDocument(
			index, type, field, value);
	}

	@Before()
	public void setUp() throws Exception {

		startEmbeddedElasticsearchServer();
		_documentFactory = new DefaultElasticsearchDocumentFactory();
		setUpPropsUtil();
		setUpLocalizationUtil();
		setUpElasticsearch();

		doBeforeRunTest();
	}

	@After
	public void tearDown() {

		doAfterRunTest();
		shutdownEmbeddedElasticsearchServer();
	}

	public String updateDocument(Document document) throws SearchException {

		return _updateDocumentCommand.updateDocument(
			_embeddedElasticsearchServer.getDocumentType(), getSearchContext(),
			document);
	}

	protected DocumentImpl createDocumentWithRequiredData() {

		DocumentImpl document = new DocumentImpl();

		document.addUID(Field.PORTLET_ID, RandomUtils.nextLong());

		document.addKeyword(
			Field.COMPANY_ID, _embeddedElasticsearchServer.getCompanyId());
		document.addKeyword(Field.ENTRY_CLASS_NAME, Document.class.getName());
		document.addKeyword(Field.ENTRY_CLASS_PK, RandomUtils.nextLong());
		document.addKeyword(Field.GROUP_ID, RandomUtils.nextLong());

		return document;
	}

	protected abstract void doAfterRunTest();

	protected abstract void doBeforeRunTest();

	protected String generateElasticsearchJson(DocumentImpl document)
		throws IOException {

		return _documentFactory.getElasticsearchDocument(document);
	}

	protected Client getClient() {

		return _embeddedElasticsearchServer.getClient();
	}

	protected SearchContext getSearchContext() {

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(
			Long.valueOf(_embeddedElasticsearchServer.getCompanyId()));
		searchContext.setSearchEngineId(
			_embeddedElasticsearchServer.getSystemEngine());

		searchContext.setStart(QueryUtil.ALL_POS);
		searchContext.setEnd(QueryUtil.ALL_POS);

		QueryConfig queryConfig = new QueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		searchContext.setQueryConfig(queryConfig);

		return searchContext;
	}

	protected void setUpElasticsearch() {

		setUpElasticsearchConnectionManager();
		setUpElasticsearchUpdateDocumentCommandImpl();
		setUpElasticsearchIndexSearcher();
	}

	protected void setUpElasticsearchConnectionManager() {

		_elasticsearchConnectionManager = spy(
			new ElasticsearchConnectionManager());

		try {
			doReturn(getClient()).when(
				_elasticsearchConnectionManager, "getClient");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void setUpElasticsearchIndexSearcher() {

		_elasticsearchIndexSearcher = new ElasticsearchIndexSearcher();

		_elasticsearchIndexSearcher.setElasticsearchConnectionManager(
			_elasticsearchConnectionManager);
	}

	protected void setUpElasticsearchUpdateDocumentCommandImpl() {

		_updateDocumentCommand = spy(
			new ElasticsearchUpdateDocumentCommandImpl());

		try {
			_updateDocumentCommand.setElasticsearchDocumentFactory(
				new DefaultElasticsearchDocumentFactory());

			_updateDocumentCommand.setElasticsearchConnectionManager(
				_elasticsearchConnectionManager);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void setUpLocalizationUtil() {

		Localization localizationMock = mock(Localization.class);

		when(
			localizationMock.getLocalizedName(Matchers.anyString(), Matchers.
				anyString())).then(new Answer<String>() {
					@Override
					public String answer(InvocationOnMock invocation)
						throws Throwable {

						Object[] args = invocation.getArguments();

						String name = (String)args[0];
						String languageId = (String)args[1];

						return name.concat(StringPool.UNDERLINE).concat(
							languageId);
					}
		});

		spy(LocalizationUtil.class);

		when(LocalizationUtil.getLocalization()).thenReturn(localizationMock);
	}

	protected void setUpPropsUtil() {

		mockStatic(PropsUtil.class);

		when(PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN)).thenReturn(
			"yyyyMMddHHmmss");

		when(PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS)).thenReturn(
			"firstName,jobTitle,lastName,name,screenName,title");

		when(
			PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH)
			).thenReturn("255");
	}

	protected void shutdownEmbeddedElasticsearchServer() {

		if (_embeddedElasticsearchServer != null) {
			_embeddedElasticsearchServer.shutdown();
			_embeddedElasticsearchServer.deleteDataDirectory();
		}
	}

	protected void startEmbeddedElasticsearchServer() throws Exception {

		if (_embeddedElasticsearchServer == null) {
			_embeddedElasticsearchServer = createSearchServer();
		}
		else {
			_embeddedElasticsearchServer.start();
		}
	}

	protected DefaultElasticsearchDocumentFactory _documentFactory;

	private static final String ELASTICSEARCH_CONFIG =
		"elasticsearch-embedded-test.yml";

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchIndexSearcher _elasticsearchIndexSearcher;
	private EmbeddedElasticsearchServer _embeddedElasticsearchServer;
	private ElasticsearchUpdateDocumentCommandImpl _updateDocumentCommand;

}