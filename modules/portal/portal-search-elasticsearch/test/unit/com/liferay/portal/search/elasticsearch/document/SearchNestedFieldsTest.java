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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;

public class SearchNestedFieldsTest extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testSearchDocumentWithNestedFields() throws Exception {

		DocumentImpl document = createDocumentWithNestedFields();

		String generatedJsonDocument = generateElasticsearchJson(document);

		assertNotNull(generatedJsonDocument);
		assertNotEquals("", generatedJsonDocument);

		String id = indexJsonDocument(generatedJsonDocument);
		String documentFromElasticSearch = getIndexedJsonDocument(id);

		//Single field queries
		verifySearch(documentFromElasticSearch, 1, "fields.movie.title.value",
			"matrix");
		verifySearch(documentFromElasticSearch, 1,
			"fields.movie.details.language.value", "english");
		verifySearch(documentFromElasticSearch, 1,
			"fields.movie.cast.actors.name.value", "keanu");
		verifySearch(documentFromElasticSearch, 1,
			"fields.movie.cast.actors.character.value", "Trinity");
		verifySearch(documentFromElasticSearch, 1,
			"fields.movie.directors.value", "Lana");

		//Two related fields queries


	}

	private void verifySearch(
		String documentFromElasticSearch, int expectedTotalHits, String field,
		String value) {

		SearchHits hits = searchDocument(field, value);

		assertEquals(
			"Wrong total of hits!", expectedTotalHits, hits.getTotalHits());

		String indexedJsonDocument = hits.getAt(0).getSourceAsString();

		assertEquals(documentFromElasticSearch, indexedJsonDocument);
	}

	protected DocumentImpl createDocumentWithNestedFields() {

		DocumentImpl document = createDocumentWithRequiredData();

		Field.NestedFieldBuilder nestedFieldBuilder =
			new Field.NestedFieldBuilder();

		Field formFields = nestedFieldBuilder
			.startField("fields")
				.startField("movie")
					.addSimpleField("title", "The Matrix")
					.addSimpleField("year", "1999")
					.addSimpleField("time", "136")
					.addSimpleField("ratings", "8.7/10")
					.addSimpleField("genre", "Action", "Sci-Fi")
					.addSimpleField(
						"directors", "Andy Wachowski", "Lana Wachowski")
					.addSimpleField(
						"writers", "Andy Wachowski", "Lana Wachowski")
					.startField("details")
						.addSimpleField(
							"officialSite",
							"https://www.facebook.com/TheMatrixMovie")
						.addSimpleField("contry","USA")
						.addSimpleField("language","english")
						.startField("releaseDates")
							.startField("releaseDate")
								.addSimpleField("date","31 March 1999")
								.addSimpleField("country","USA")
							.endField()
						.endField()
					.endField()
					.startField("cast")
						.startArray("actors")
							.startField()
								.addSimpleField("name", "Keanu Reeves")
								.addSimpleField("character", "Neo")
							.endField()
							.startField()
								.addSimpleField("name", "Laurence Fishburne")
								.addSimpleField("character", "Morpheus")
							.endField()
							.startField()
								.addSimpleField("name", "Carrie-Anne Moss")
								.addSimpleField("character", "Trinity")
							.endField()
							.startField()
								.addSimpleField("name", "Gloria Foster")
								.addSimpleField("character", "Oracle")
							.endField()
							.startField()
								.addSimpleField("name", "Hugo Weaving")
								.addSimpleField("character", "Agent Smith")
							.endField()
						.endArray()
					.endField()
				.endField()
			.endField()
			.build();

		document.add(formFields);

		return document;
	}

	protected DocumentImpl createDocumentWithNestedFieldsIndex() {

		DocumentImpl document = createDocumentWithRequiredData();

		return document;
	}
}
