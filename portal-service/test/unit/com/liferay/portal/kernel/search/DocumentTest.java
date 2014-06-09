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

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;

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
		setUpOrderedDependencies();
	}

	@Test
	public void testGetFields() {
		Map<String, Field> fields = _document.getFields();

		Assert.assertEquals(1, fields.size());
	}

	@Test
	public void testGetNestedField() {
		Field field = _document.getField(_fieldName);

		Assert.assertNotNull(field);

		Assert.assertEquals("name", field.getName());
		Assert.assertEquals(_fieldValue, field.getValue());

		Field localizedField = _document.getField(_localizedFieldName);

		Assert.assertNotNull(localizedField);

		Assert.assertEquals("name", localizedField.getName());
		Assert.assertEquals(
			_localizedFieldValues.get(Locale.ENGLISH),
			getLocalizedFieldValue(localizedField, Locale.ENGLISH));
	}

	@Test
	public void testGetNestedFieldValue() {
		String value = getNestedFieldValue(_fieldName, null);

		Assert.assertEquals(_fieldValue, value);

		String localizedValue = getNestedFieldValue(
			_localizedFieldName, Locale.ENGLISH);

		Assert.assertEquals(
			_localizedFieldValues.get(Locale.ENGLISH), localizedValue);
	}

	@Test
	public void testHasNestedFields() {
		boolean hasNestedFields = hasNestedFields(_document, split(_fieldName));

		Assert.assertTrue(hasNestedFields);

		hasNestedFields = hasNestedFields(
			_document, split(_localizedFieldName));

		Assert.assertTrue(hasNestedFields);
	}

	protected String getFieldValue(Field field, Locale locale) {
		if (locale == null) {
			return field.getValue();
		}

		Map<Locale, String> localizedValues = field.getLocalizedValues();

		if (localizedValues != null) {
			return localizedValues.get(locale);
		}

		return null;
	}

	protected String getLocalizedFieldValue(Field field, Locale locale) {
		Map<Locale, String> localizedValues = field.getLocalizedValues();

		return localizedValues.get(locale);
	}

	protected String getNestedFieldValue(String name, Locale locale) {
		Map<String, Field> fields = _document.getFields();

		String[] nestedFieldNames = split(name);

		Field field = fields.get(nestedFieldNames[0]);

		if (field == null) {
			return null;
		}

		for (int i = 1; i < nestedFieldNames.length; i++) {
			String fieldName = nestedFieldNames[i];

			Map<String, Field> nestedFields = field.getNestedFields();

			if (!nestedFields.containsKey(fieldName)) {
				return null;
			}

			field = nestedFields.get(fieldName);
		}

		return getFieldValue(field, locale);
	}

	protected boolean hasNestedFields(Document document, String... fieldNames) {
		Map<String, Field> fields = document.getFields();

		if (!fields.containsKey(fieldNames[0])) {
			return false;
		}

		Field field = fields.get(fieldNames[0]);

		for (int i = 1; i < fieldNames.length; i++) {
			Map<String, Field> nestedFields = field.getNestedFields();

			if (!nestedFields.containsKey(fieldNames[i])) {
				return false;
			}

			field = nestedFields.get(fieldNames[i]);
		}

		return true;
	}

	protected void setUpDocument() {
		_document = new DocumentImpl();

		_document.addText(_fieldName, _fieldValue);

		_localizedFieldValues.put(Locale.ENGLISH, "contract");

		_document.addLocalizedText(_localizedFieldName, _localizedFieldValues);
	}

	protected void setUpOrderedDependencies() {
		setUpPropsUtil();
		setUpDocument();
	}

	protected void setUpPropsUtil() {
		mockStatic(PropsUtil.class);

		when(
			PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN)
		).thenReturn(
			"yyyyMMddHHmmss"
		);

		when(
			PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS)
		).thenReturn(
			"firstName,jobTitle,lastName,name,screenName,title"
		);

		when(
			PropsUtil.get(PropsKeys.INDEX_SORTABLE_TEXT_FIELDS_TRUNCATED_LENGTH)
		).thenReturn(
			"255"
		);
	}

	protected String[] split(String fieldName) {
		return StringUtil.split(fieldName, CharPool.PERIOD);
	}

	private DocumentImpl _document;
	private String _fieldName = "liferay.blogs.posts.tags.name";
	private String _fieldValue = "liferay";
	private String _localizedFieldName = "liferay.webcontent.documents.name";
	private Map<Locale, String> _localizedFieldValues =
		new HashMap<Locale, String>();

}