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

package com.liferay.portal.search.elasticsearch.util;

public enum ElasticSearchConfigEnum {

	DOCUMENT_COMPANY_ID("document.company.id"), DOCUMENT_TYPE("document.type"),
	INDEX_NAME("index.name"), PATH_DATA("path.data"), PATH_LOGS("path.logs"),
	PATH_TEST_DATA_DIR("path.test.data.dir"), PATH_WORK("path.work"),
	SYSTEM_ENGINE("system.engine"), JSON_TYPE_MAPPINGS("json.type.mappings");

	public String getConfigName() {
		return _configName;
	}

	private ElasticSearchConfigEnum(String configName) {
		_configName = configName;
	}

	private final String _configName;

}