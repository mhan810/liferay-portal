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

package com.liferay.portal.search.elasticsearch.document.flat;

import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.search.elasticsearch.document.BaseElasticsearchTest;

import java.io.IOException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class GenerateJsonDocumentTest extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testGetDocumentWithLocalizableSimpleField() throws Exception {
		DocumentImpl document = createDocumentWithRequiredData();

		Map<Locale, String> localizedFieldValues = new HashMap<>();
		localizedFieldValues.clear();
		localizedFieldValues.put(Locale.ENGLISH, "developer");
		localizedFieldValues.put(new Locale("pt", "BR"), "desenvolvedor");

		document.addLocalizedText("tags", localizedFieldValues);

		_assertAllConditions(document);
	}

	@Test
	public void testGetDocumentWithSimpleField() throws Exception {
		DocumentImpl document = createDocumentWithRequiredData();

		document.addText("tags", "liferay");

		_assertAllConditions(document);
	}

	private void _assertAllConditions(DocumentImpl document)
		throws IOException {

		String elasticSearchDocument = generateElasticsearchJson(document);
		Assert.assertNotNull(elasticSearchDocument);
		Assert.assertNotEquals("", elasticSearchDocument);
//
//		String id = indexJsonDocument(elasticSearchDocument);
//		String documentFromElasticSearch = getIndexedJsonDocument(id);
//		assertEquals(elasticSearchDocument, documentFromElasticSearch);
	}

}