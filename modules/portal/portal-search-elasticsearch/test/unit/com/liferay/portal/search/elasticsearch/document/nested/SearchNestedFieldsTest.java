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
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.search.elasticsearch.document.BaseElasticsearchTest;

import org.junit.Test;
public class SearchNestedFieldsTest extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testSearchDocuments() throws Exception {
		DocumentImpl document = createDocuments();

		String generatedJsonDocument = generateElasticsearchJson(document);

		assertNotNull(generatedJsonDocument);
		assertNotEquals("", generatedJsonDocument);

		updateDocument(document);

		verifySearch(1, "fields.movie.title.value",
			"matrix");
		verifySearch(1,
			"fields.movie.details.language.value", "english");
		verifySearch(1,
			"fields.movie.cast.actors.name.value", "keanu");
		verifySearch(1,
			"fields.movie.cast.actors.character.value", "Trinity");
		verifySearch(1,
			"fields.movie.directors.value", "Lana");
	}

	protected DocumentImpl createDocuments() {
		DocumentImpl document = createDocumentWithRequiredData();

		Field.NestedFieldBuilder builder = new Field.NestedFieldBuilder();

		Field formFields = builder
			.startField("fields")
				.startField("movie")
					.addNestedField("title", "The Matrix")
					.addNestedField("year", "1999")
					.addNestedField("time", "136")
					.addNestedField("ratings", "8.7/10")
					.addNestedField("genre", "Action", "Sci-Fi")
					.addNestedField(
						"directors", "Andy Wachowski", "Lana Wachowski")
					.addNestedField(
						"writers", "Andy Wachowski", "Lana Wachowski")
					.startField("details")
						.addNestedField(
							"officialSite",
							"https://www.facebook.com/TheMatrixMovie")
						.addNestedField("contry","USA")
						.addNestedField("language","english")
						.startField("releaseDates")
							.startField("releaseDate")
								.addNestedField("date","31 March 1999")
								.addNestedField("country","USA")
							.endField()
						.endField()
					.endField()
					.startField("cast")
						.startArray("actors")
							.startField()
								.addNestedField("name", "Keanu Reeves")
								.addNestedField("character", "Neo")
							.endField()
							.startField()
								.addNestedField("name", "Laurence Fishburne")
								.addNestedField("character", "Morpheus")
							.endField()
							.startField()
								.addNestedField("name", "Carrie-Anne Moss")
								.addNestedField("character", "Trinity")
							.endField()
							.startField()
								.addNestedField("name", "Gloria Foster")
								.addNestedField("character", "Oracle")
							.endField()
							.startField()
								.addNestedField("name", "Hugo Weaving")
								.addNestedField("character", "Agent Smith")
							.endField()
						.endArray()
					.endField()
				.endField()
			.endField()
			.getField();

		document.add(formFields);

		return document;
	}

	private void verifySearch(int expectedTotalHits, String field,
		String value) throws SearchException {

		Hits hits = search(field, value);

		assertEquals(
			"Wrong total of hits!", expectedTotalHits, hits.getLength());
	}

}