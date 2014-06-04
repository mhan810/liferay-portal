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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@PrepareForTest(PropsUtil.class)
@RunWith(PowerMockRunner.class)
public class DocumentTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {

		mockStatic(PropsUtil.class);

		when(PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN)).thenReturn(
			"yyyyMMddHHmmss");

		when(PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS)).thenReturn(
			"firstName,jobTitle,lastName,name,screenName,title");

		when(
			PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH)
			).thenReturn("255");

		_localizedFieldValues.put(Locale.ENGLISH, "contract");
		_document = new DocumentImpl();
		_document.addText(_fieldName, _fieldValue);
		_document.addLocalizedText(_localizedFieldName, _localizedFieldValues);
	}

	@Test
	public final void testAddNestedField() {

		Map<String, Field> fields = _document.getFields();

		Assert.assertEquals(1, fields.size());
		Assert.assertTrue(containsFields(_document, _fieldName.split("\\.")));
		Assert.assertTrue(containsFields(_document,
		_localizedFieldName.split("\\.")));

		Assert.assertEquals(_fieldValue, getNestedValue(_fieldName));
		Assert.assertEquals(_localizedFieldValues.get(Locale.ENGLISH),
		getNestedValue(_localizedFieldName, Locale.ENGLISH));
	}

	@Test
	public final void testGetNestedField() {

		Field field = _document.getField(_fieldName);
		Field localizedField = _document.getField(_localizedFieldName);
		Map<String, Field> fields = _document.getFields();

		Assert.assertEquals(1, fields.size());
		Assert.assertNotNull(field);
		Assert.assertNotNull(localizedField);
		Assert.assertEquals("name", field.getName());
		Assert.assertEquals(_fieldValue, field.getValue());
		Assert.assertEquals("name", localizedField.getName());
		Assert.assertEquals(_localizedFieldValues.get(Locale.ENGLISH),
		localizedField.getLocalizedValues().get(Locale.ENGLISH));
	}

	private boolean containsFields(Document document,
	String... hierarchicalFieldNames) {

		boolean contain = true;

		Field field = document.getFields().get(hierarchicalFieldNames[0]);

		if (field == null) {
			contain = false;
		}
		else {
			Field currentField = field;

			for (int i = 1; i < hierarchicalFieldNames.length; i++) {
				String fieldName = hierarchicalFieldNames[i];

				if (currentField.getNestedFields().containsKey(fieldName)) {
					currentField =
						currentField.getNestedFields().get(fieldName);
				}
				else {
					contain = false;
					break;
				}
			}
		}

		return contain;
	}

	private String getFieldValue(Field field, Locale locale) {

		String value = null;

		if (locale == null) {
			value = field.getValue();
		}
		else {
			if (field.getLocalizedValues() != null) {
				value = field.getLocalizedValues().get(locale);
			}
		}

		return value;
	}

	private String getNestedValue(String name) {

		return getNestedValue(name, null);
	}

	private String getNestedValue(String name, Locale locale) {

		String value = null;

		String[] hierarchicalFieldNames = name.split("\\.");

		Field field = _document.getFields().get(hierarchicalFieldNames[0]);

		if (field != null) {
			Field currentField = field;

			for (int i = 1; i < hierarchicalFieldNames.length; i++) {
				String fieldName = hierarchicalFieldNames[i];

				if (currentField.getNestedFields().containsKey(fieldName)) {
					currentField =
						currentField.getNestedFields().get(fieldName);

					value = getFieldValue(currentField, locale);
				}
				else {
					break;
				}
			}
		}

		return value;
	}

	private DocumentImpl _document;
	private String _fieldName = "liferay.blogs.posts.tags.name";
	private String _fieldValue = "liferay";
	private String _localizedFieldName = "liferay.webcontent.documents.name";
	private Map<Locale, String> _localizedFieldValues =
		new HashMap<Locale, String>();

}