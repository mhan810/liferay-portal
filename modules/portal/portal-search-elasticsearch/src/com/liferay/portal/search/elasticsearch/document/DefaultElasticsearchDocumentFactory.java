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

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * @author Michael C. Han
 * @author Milen Dyankov
 */
public class DefaultElasticsearchDocumentFactory
	implements ElasticsearchDocumentFactory {

	@Override
	public String getElasticsearchDocument(Document document)
		throws IOException {

		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();

		Map<String, Field> fields = document.getFields();
		xContentBuilder.startObject();
		_addFields(fields.values(), xContentBuilder);
		xContentBuilder.endObject();
		return xContentBuilder.string();
	}

	private void _addField(XContentBuilder xContentBuilder, Field field)
		throws IOException {

		String name = field.getName();

		if (!field.isLocalized()) {

			xContentBuilder.field(name, field.getValues());
		}
		else {
			Map<Locale, String> localizedValues = field.getLocalizedValues();

			for (Map.Entry<Locale, String> entry : localizedValues.entrySet()) {
				String value = entry.getValue();

				if (Validator.isNull(value)) {
					continue;
				}

				Locale locale = entry.getKey();

				String languageId = LocaleUtil.toLanguageId(locale);

				String defaultLanguageId = LocaleUtil.toLanguageId(
					LocaleUtil.getDefault());

				if (languageId.equals(defaultLanguageId)) {
					xContentBuilder.field(name, value.trim());
				}

				String localizedName = DocumentImpl.getLocalizedName(
					languageId, name);

				xContentBuilder.field(localizedName, value.trim());
			}
		}
	}

	private void _addFields(Collection<Field> fields,
	XContentBuilder xContentBuilder) throws IOException {
		//xContentBuilder.startObject();

		for (Field field : fields) {
			if (field.hasChildren()) {
				_addNestedField(xContentBuilder, field);
			}
			else {
				_addField(xContentBuilder, field);
			}
		}

		//xContentBuilder.endObject();
	}

	private void _addNestedField(XContentBuilder xContentBuilder, Field field)
		throws IOException {

		if (field.isArray()) {
			xContentBuilder.startArray(field.getName());
			_addFields(field.getFields(), xContentBuilder);
			xContentBuilder.endArray();
		}
		else {

			if (field.getName() == null) {
				xContentBuilder.startObject();
			}
			else {
				xContentBuilder.startObject(field.getName());
			}

			_addFields(field.getFields(), xContentBuilder);
			xContentBuilder.endObject();
		}

	}

}