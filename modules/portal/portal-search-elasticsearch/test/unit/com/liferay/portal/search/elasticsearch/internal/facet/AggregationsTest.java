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

package com.liferay.portal.search.elasticsearch.internal.facet;

import static com.liferay.portal.kernel.util.PropsKeys.INDEX_DATE_FORMAT_PATTERN;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_ENABLED;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORES_THRESHOLD;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_HIGHLIGHT_ENABLED;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_HIGHLIGHT_FRAGMENT_SIZE;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_HIGHLIGHT_REQUIRE_FIELD_MATCH;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_HIGHLIGHT_SNIPPET_SIZE;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_QUERY_INDEXING_ENABLED;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_QUERY_INDEXING_THRESHOLD;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_QUERY_SUGGESTION_ENABLED;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_QUERY_SUGGESTION_MAX;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_QUERY_SUGGESTION_SCORES_THRESHOLD;
import static com.liferay.portal.kernel.util.PropsKeys.INDEX_SEARCH_SCORING_ENABLED;

import aQute.bnd.annotation.metatype.Configurable;

import com.ibm.icu.text.SimpleDateFormat;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.search.elasticsearch.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.document.ElasticsearchDocumentFactory;
import com.liferay.portal.search.elasticsearch.document.ElasticsearchUpdateDocumentCommand;
import com.liferay.portal.search.elasticsearch.internal.ElasticsearchIndexSearcher;
import com.liferay.portal.search.elasticsearch.internal.ElasticsearchIndexWriter;
import com.liferay.portal.search.elasticsearch.internal.ElasticsearchUpdateDocumentCommandImpl;
import com.liferay.portal.search.elasticsearch.internal.cluster.ClusterSettingsContext;
import com.liferay.portal.search.elasticsearch.internal.connection.EmbeddedElasticsearchConnection;
import com.liferay.portal.search.elasticsearch.internal.document.DefaultElasticsearchDocumentFactory;
import com.liferay.portal.search.elasticsearch.internal.facet.daterange.DateRangeFacetProcessor;
import com.liferay.portal.search.elasticsearch.internal.index.CompanyIndexFactory;
import com.liferay.portal.search.elasticsearch.internal.query.BooleanQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.DisMaxQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.ElasticsearchQueryTranslator;
import com.liferay.portal.search.elasticsearch.internal.query.FuzzyQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.MatchAllQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.MatchQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.MoreLikeThisQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.MultiMatchQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.NestedQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.StringQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.TermQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.TermRangeQueryTranslatorImpl;
import com.liferay.portal.search.elasticsearch.internal.query.WildcardQueryTranslatorImpl;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@PrepareForTest( {
	ClusterSettingsContext.class, CompanyIndexFactory.class, Configurable.class,
	ElasticsearchConnectionManager.class, FastDateFormatFactoryUtil.class,
	PropsUtil.class
})
@PowerMockIgnore( {
	"javax.management.*", "java.lang.*"
})
@RunWith(PowerMockRunner.class)
public abstract class AggregationsTest extends PowerMockito {

	@Before
	public final void setUp() throws Exception {
		setUpFastDateFormatFactoryUtil();
		setUpPropsUtil();
		setUpOSGIConfigurable();
		setUpIndexSearchWriter();

		initEnviroment();
	}

	@After
	public final void tearDown() throws Exception {
		clearEnviroment();
	}

	protected abstract void clearEnviroment() throws Exception;

	protected SearchContext getSearchContext() {
		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(COMPANY_ID);
		searchContext.setGroupIds(new long[] {GROUP_ID});
		searchContext.setSearchEngineId("Elasticsearch");

		searchContext.setStart(QueryUtil.ALL_POS);
		searchContext.setEnd(QueryUtil.ALL_POS);

		QueryConfig queryConfig = new QueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		searchContext.setQueryConfig(queryConfig);

		return searchContext;
	}

	protected abstract void initEnviroment() throws Exception;

	protected void mockProperty(String property, String value) {
		when(PropsUtil.get(property)).thenReturn(value);
	}

	protected Document newDocument(Date createDate) {
		DocumentImpl document = new DocumentImpl();

		long entryClassPk = RandomUtils.nextLong();

		document.addUID(ENTRY_CLASS_NAME, entryClassPk);
		document.addKeyword(Field.ENTRY_CLASS_NAME, ENTRY_CLASS_NAME);
		document.addKeyword(Field.COMPANY_ID, COMPANY_ID);
		document.addKeyword(Field.ENTRY_CLASS_PK, entryClassPk);
		document.addKeyword(Field.GROUP_ID, GROUP_ID);
		document.addDate(Field.CREATE_DATE, createDate);

		return document;
	}

	protected void setUpCompanyIndexFactory() throws Exception {
		whenNew(CompanyIndexFactory.class).withNoArguments().thenReturn(
			new CompanyIndexFactory() {

				{
					this.setIndexConfigFileName(
						"/META-INF/index-settings.json");
					Map<String, String> mappings = new HashMap<>();
					mappings.put(
						"KeywordQueryDocumentType",
						"/META-INF/mappings/keyword-query-type-mappings.json");
					mappings.put(
						"LiferayDocumentType",
						"/META-INF/mappings/liferay-type-mappings.json");
					mappings.put(
						"SpellCheckDocumentType",
						"/META-INF/mappings/spellcheck-type-mappings.json");
					this.setTypeMappings(mappings);

					this.createIndices(
						connectionManager.getAdminClient(), COMPANY_ID);
				}

				@Override
				protected String loadFileContent(
					ClassLoader classLoader, String relativePath)
						throws IOException {

					String absolutePath = ClassLoader.class.getResource(
						relativePath).getPath();

					String mapping = FileUtils.readFileToString(
						new File(absolutePath), "UTF-8");

					return mapping;
				}

			});
	}

	protected void setUpConfiguration() {
		configuration = new FakeElasticsearchConfiguration();
	}

	protected void setUpConnectionManager() throws Exception {
		final Props props = Mockito.mock(Props.class);

		Mockito.when(props.get(PropsKeys.LIFERAY_HOME)).thenReturn("tmp/");

		final ClusterSettingsContext clusterSettingsContext = mock(
			ClusterSettingsContext.class);

		final EmbeddedElasticsearchConnection embeddedElasticsearchConnection =
			new EmbeddedElasticsearchConnection() {

				{
					setClusterSettingsContext(clusterSettingsContext);
					setProps(props);
					this.activate(emptyMap);
				}
			};

		connectionManager = new ElasticsearchConnectionManager() {

			{
				this.setEmbeddedElasticsearchConnection(
					embeddedElasticsearchConnection);
				this.activate(emptyMap);
			}
		};

		connectionManager.connect();
	}

	protected void setUpElasticsearchQueryTranslator() {
		queryTranslator = new ElasticsearchQueryTranslator() {
			{
				this.setBooleanQueryTranslator(
					new BooleanQueryTranslatorImpl());
				this.setDisMaxQueryTranslator(new DisMaxQueryTranslatorImpl());
				this.setFuzzyQueryTranslator(new FuzzyQueryTranslatorImpl());
				this.setMatchAllQueryTranslator(
					new MatchAllQueryTranslatorImpl());
				this.setMatchQueryTranslator(new MatchQueryTranslatorImpl());
				this.setMoreLikeThisQueryTranslator(
					new MoreLikeThisQueryTranslatorImpl());
				this.setMultiMatchQueryTranslator(
					new MultiMatchQueryTranslatorImpl());
				this.setNestedQueryTranslator(new NestedQueryTranslatorImpl());
				this.setStringQueryTranslator(new StringQueryTranslatorImpl());
				this.setTermQueryTranslator(new TermQueryTranslatorImpl());
				this.setTermRangeQueryTranslator(
					new TermRangeQueryTranslatorImpl());
				this.setWildcardQueryTranslator(
					new WildcardQueryTranslatorImpl());
			}
		};
	}

	protected void setUpFastDateFormatFactoryUtil() {
		mockStatic(FastDateFormatFactoryUtil.class);

		when(
			FastDateFormatFactoryUtil.getSimpleDateFormat(
				"yyyyMMddHHmmss")).thenReturn(
					new SimpleDateFormat("yyyyMMddHHmmss"));
	}

	protected void setUpIndexSearchWriter() throws Exception {
		setUpConnectionManager();

		setUpCompanyIndexFactory();

		documentFactory = new DefaultElasticsearchDocumentFactory();

		updateDocumentCommand = new ElasticsearchUpdateDocumentCommandImpl() {

			{
				this.setElasticsearchConnectionManager(connectionManager);
				this.setElasticsearchDocumentFactory(documentFactory);
				this.activate(emptyMap);
			}
		};

		indexWriter = new ElasticsearchIndexWriter() {

			{
				this.setElasticsearchUpdateDocumentCommand(
					updateDocumentCommand);
				this.setElasticsearchConnectionManager(connectionManager);
				this.activate();
			}
		};

		setUpElasticsearchQueryTranslator();

		indexSearcher = new ElasticsearchIndexSearcher() {

			{
				this.setElasticsearchConnectionManager(connectionManager);
				this.setQueryTranslator(queryTranslator);
				this.setFacetProcessor(new DateRangeFacetProcessor());

				this.activate(emptyMap);
			}
		};
	}

	protected void setUpOSGIConfigurable() {
		mockStatic(Configurable.class);

		setUpConfiguration();

		when(
			Configurable.createConfigurable(
				ElasticsearchConfiguration.class, emptyMap)).thenReturn(
					configuration);
	}

	protected void setUpPropsUtil() {
		mockStatic(PropsUtil.class);

		mockProperty(INDEX_DATE_FORMAT_PATTERN, "yyyyMMddHHmmss");
		mockProperty(INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_ENABLED, "true");
		mockProperty(
			INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORES_THRESHOLD, "50");
		mockProperty(INDEX_SEARCH_HIGHLIGHT_ENABLED, "true");
		mockProperty(INDEX_SEARCH_HIGHLIGHT_FRAGMENT_SIZE, "80");
		mockProperty(INDEX_SEARCH_HIGHLIGHT_REQUIRE_FIELD_MATCH, "true");
		mockProperty(INDEX_SEARCH_HIGHLIGHT_SNIPPET_SIZE, "3");
		mockProperty(INDEX_SEARCH_QUERY_INDEXING_ENABLED, "true");
		mockProperty(INDEX_SEARCH_QUERY_INDEXING_THRESHOLD, "50");
		mockProperty(INDEX_SEARCH_QUERY_SUGGESTION_ENABLED, "true");
		mockProperty(INDEX_SEARCH_QUERY_SUGGESTION_MAX, "yyyyMMddHHmmss");
		mockProperty(INDEX_SEARCH_QUERY_SUGGESTION_SCORES_THRESHOLD, "0");
		mockProperty(INDEX_SEARCH_SCORING_ENABLED, "true");
	}

	protected static final long COMPANY_ID = 11111L;

	protected static final String ENTRY_CLASS_NAME =
		"com.liferay.portlet.asset.model.AssetFake";

	protected static final long GROUP_ID = 33333L;

	protected static final Map<String, Object> emptyMap = new HashMap<>();

	protected ElasticsearchConfiguration configuration;
	protected ElasticsearchConnectionManager connectionManager;
	protected ElasticsearchDocumentFactory documentFactory;
	protected ElasticsearchIndexSearcher indexSearcher;
	protected ElasticsearchIndexWriter indexWriter;
	protected ElasticsearchQueryTranslator queryTranslator;
	protected ElasticsearchUpdateDocumentCommand updateDocumentCommand;

}