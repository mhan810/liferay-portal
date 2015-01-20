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
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.search.elasticsearch.ElasticsearchIndexSearcher;
import com.liferay.portal.search.elasticsearch.ElasticsearchUpdateDocumentCommandImpl;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.connection.EmbeddedElasticsearchConnection;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;

import org.elasticsearch.client.Client;

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

	public Hits search(String field, String value) throws SearchException {
		TermQuery termQuery = new TermQueryImpl(field, value);

		return _elasticsearchIndexSearcher.search(
			getSearchContext(), termQuery);
	}

	@Before()
	public final void setUp() throws Exception {
		_documentFactory = new DefaultElasticsearchDocumentFactory();

		setUpPropsUtil();
		setUpLocalizationUtil();
		setUpElasticsearch();

		doBeforeRunTest();
	}

	@After
	public final void tearDown() {
		doAfterRunTest();
		_embeddedConnection.close();
		_deleteDataDirectory();
	}

	public String updateDocument(Document document) throws SearchException {
		return _updateDocumentCommand.updateDocument(
			getDocumentType(), getSearchContext(), document, false);
	}

	protected DocumentImpl createDocumentWithRequiredData() {
		DocumentImpl document = new DocumentImpl();

		document.addUID(Field.PORTLET_ID, RandomUtils.nextLong());

		document.addKeyword(Field.COMPANY_ID, getCompanyId());
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
		return _elasticsearchConnectionManager.getClient();
	}

	protected String getCompanyId() {
		return "1";
	}

	protected String getDocumentType() {
		return "LiferayDocumentType";
	}

	protected SearchContext getSearchContext() {
		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(Long.valueOf(getCompanyId()));
		searchContext.setSearchEngineId(getSystemEngine());

		searchContext.setCommitImmediately(true);

		searchContext.setStart(QueryUtil.ALL_POS);
		searchContext.setEnd(QueryUtil.ALL_POS);

		QueryConfig queryConfig = new QueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		searchContext.setQueryConfig(queryConfig);

		return searchContext;
	}

	protected String getSystemEngine() {
		return "SYSTEM_ENGINE";
	}

	protected void setUpElasticsearch() throws Exception {
		setUpElasticsearchConnectionManager();
		setUpElasticsearchUpdateDocumentCommandImpl();
		setUpElasticsearchIndexSearcher();
	}

	protected void setUpElasticsearchConnectionManager() {
		_elasticsearchConnectionManager = new ElasticsearchConnectionManager();
		_embeddedConnection = new EmbeddedElasticsearchConnection();
		_embeddedConnection.setConfigFileName(
			"META-INF/elasticsearch-embedded-test.yml");
		_embeddedConnection.setTestConfigFileName(
			"META-INF/elasticsearch-embedded-test.yml");
		_embeddedConnection.initialize();
		_elasticsearchConnectionManager.setElasticsearchConnection(
			_embeddedConnection);
	}

	protected void setUpElasticsearchIndexSearcher() {
		_elasticsearchIndexSearcher = new ElasticsearchIndexSearcher();

		_elasticsearchIndexSearcher.setElasticsearchConnectionManager(
			_elasticsearchConnectionManager);
	}

	protected void setUpElasticsearchUpdateDocumentCommandImpl()
		throws Exception {

		_updateDocumentCommand = spy(
			new ElasticsearchUpdateDocumentCommandImpl());

		_updateDocumentCommand.setElasticsearchDocumentFactory(
			new DefaultElasticsearchDocumentFactory());

		_updateDocumentCommand.setElasticsearchConnectionManager(
			_elasticsearchConnectionManager);
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

	protected DefaultElasticsearchDocumentFactory _documentFactory;

	private void _deleteDataDirectory() {
		try {
			FileUtils.deleteDirectory(new File("elasticsearch-test"));
		}
		catch (IOException e) {
			throw new RuntimeException(
				"Could not delete data directory of embedded elasticsearch " +
					"server", e);
		}
	}

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchIndexSearcher _elasticsearchIndexSearcher;
	private EmbeddedElasticsearchConnection _embeddedConnection;
	private ElasticsearchUpdateDocumentCommandImpl _updateDocumentCommand;

}