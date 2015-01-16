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

package com.liferay.portal.search.elasticsearch.document.nested;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.search.elasticsearch.document.BaseElasticsearchTest;

import java.io.IOException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
public class GenerateJsonDocumentTest
extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testGetDocument() throws Exception {

		DocumentImpl document = createDocumentWithRequiredData();

		document.addText("liferay.blogs.posts.tags.name", "liferay");

		_assertAllConditions(document);
	}

	@Test
	public void testGetDocumentWithLocalizable() throws Exception {

		DocumentImpl document = createDocumentWithRequiredData();

		Map<Locale, String> localizedFieldValues =
			new HashMap<Locale, String>();
		localizedFieldValues.clear();
		localizedFieldValues.put(Locale.ENGLISH, "contract");
		localizedFieldValues.put(new Locale("pt", "BR"), "contrato");

		document.addLocalizedText(
			"liferay.webcontent.documents.name", localizedFieldValues);

		_assertAllConditions(document);
	}

	private void _assertAllConditions(DocumentImpl document) throws IOException
	{

		String elasticSearchDocument = generateElasticsearchJson(document);
		assertNotNull(elasticSearchDocument);
		assertNotEquals("", elasticSearchDocument);

//		String id = indexJsonDocument(elasticSearchDocument);
//		String documentFromElasticSearch = getIndexedJsonDocument(id);
//		assertEquals(elasticSearchDocument, documentFromElasticSearch);
	}

}