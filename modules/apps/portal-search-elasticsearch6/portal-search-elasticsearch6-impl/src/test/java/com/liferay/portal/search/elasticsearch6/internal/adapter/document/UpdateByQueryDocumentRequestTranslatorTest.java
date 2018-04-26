package com.liferay.portal.search.elasticsearch6.internal.adapter.document;

import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.document.UpdateByQueryDocumentRequest;

import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dylan Rebelak
 */
public class UpdateByQueryDocumentRequestTranslatorTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			DeleteByQueryDocumentRequestTranslatorTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testDocumentRequestTranslation() {
		BooleanQuery query = new BooleanQueryImpl();

		query.addExactTerm(_FIELD_NAME, true);

		//TODO Create script to test with
		JSONObject script = new JSONObjectImpl();

		UpdateByQueryDocumentRequest updateByQueryDocumentRequest =
			new UpdateByQueryDocumentRequest(_INDEX_NAME, query, script);

		UpdateByQueryDocumentRequestTranslatorImpl
			updateByQueryDocumentRequestTranslator =
				new UpdateByQueryDocumentRequestTranslatorImpl();

		UpdateByQueryRequestBuilder updateByQueryRequestBuilder =
			updateByQueryDocumentRequestTranslator.createBuilder(
				updateByQueryDocumentRequest, _elasticsearchConnectionManager);

		UpdateByQueryRequest request = updateByQueryRequestBuilder.request();

		String[] indices = request.indices();
		String queryString = request.getSearchRequest().toString();
		String scriptSource = request.getScript().toString();

		Assert.assertArrayEquals(new String[] {_INDEX_NAME}, indices);
		Assert.assertTrue(
			queryString.contains(
				"queryTerm={field=" + _FIELD_NAME + ", value=true}"));
		Assert.assertTrue(
			queryString.contains(
				"className=" + BooleanQueryImpl.class.getSimpleName()));
	}

	private static final String _FIELD_NAME = "testField";

	private static final String _INDEX_NAME = "test_request_index";

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;

}