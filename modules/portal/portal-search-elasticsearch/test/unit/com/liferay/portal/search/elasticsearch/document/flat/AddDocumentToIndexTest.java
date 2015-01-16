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
public class AddDocumentToIndexTest
extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testAddDocumentWithLocalizableSimpleField() throws Exception {

		DocumentImpl document = createDocumentWithRequiredData();

		Map<Locale, String> localizedFieldValues =
			new HashMap<Locale, String>();
		localizedFieldValues.clear();
		localizedFieldValues.put(Locale.ENGLISH, "developer");
		localizedFieldValues.put(new Locale("pt", "BR"), "desenvolvedor");

		document.addLocalizedText("tags", localizedFieldValues);

		String id = updateDocument(document);

		_assertAllConditions(id, document);
	}

	@Test
	public void testAddDocumentWithSimpleField() throws Exception {
		String propertyName = "tags";
		String propertyValue = "liferay";

		DocumentImpl document = createDocumentWithRequiredData();
		document.addText(propertyName, propertyValue);
		String id = updateDocument(document);
		_assertAllConditions(id, document);
	}

	private void _assertAllConditions(String id, DocumentImpl document)
		throws IOException
	{

		String generatedJsonDocument = generateElasticsearchJson(document
			);
		assertNotNull(generatedJsonDocument);
		assertNotEquals("", generatedJsonDocument);

//		String indexedJsonDocument = getIndexedJsonDocument(id);
//		assertEquals(generatedJsonDocument, indexedJsonDocument);
	}

}