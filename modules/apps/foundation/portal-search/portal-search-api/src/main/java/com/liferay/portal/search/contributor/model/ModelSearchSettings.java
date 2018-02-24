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

package com.liferay.portal.search.contributor.model;

import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.SearchEngineHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class ModelSearchSettings {

	public ModelSearchSettings(String className) {
		this(SearchEngineHelper.SYSTEM_ENGINE_ID, className);
	}

	public ModelSearchSettings(String searchEngineId, String className) {
		this(searchEngineId, className, new String[] {className});
	}

	public ModelSearchSettings(String className, String... searchClassNames) {
		this(SearchEngineHelper.SYSTEM_ENGINE_ID, className, searchClassNames);
	}

	public ModelSearchSettings(
		String searchEngineId, String className, String... searchClassNames) {

		_searchEngineId = searchEngineId;
		_className = className;
		_searchClassNames = searchClassNames;
	}

	public String getClassName() {
		return _className;
	}

	public String[] getDefaultSelectedFieldNames() {
		return _defaultSelectedFieldNames;
	}

	public String[] getDefaultSelectedLocalizedFieldNames() {
		return _defaultSelectedLocalizedFieldNames;
	}

	public Iterable<IndexerPostProcessor> getIndexerPostProcessors() {
		return Collections.unmodifiableCollection(_indexerPostProcessors);
	}

	public IndexerPostProcessor[] getIndexerPostProcessorsArray() {
		return _indexerPostProcessors.toArray(
			new IndexerPostProcessor[_indexerPostProcessors.size()]);
	}

	public String[] getSearchClassNames() {
		return _searchClassNames;
	}

	public String getSearchEngineId() {
		return _searchEngineId;
	}

	public boolean isCommitImmediately() {
		return _commitImmediately;
	}

	public boolean isSelectAllLocales() {
		return _selectAllLocales;
	}

	public boolean isStagingAware() {
		return _stagingAware;
	}

	public void registerIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		_indexerPostProcessors.add(indexerPostProcessor);
	}

	public void setCommitImmediately(boolean commitImmediately) {
		_commitImmediately = commitImmediately;
	}

	public void setDefaultSelectedFieldNames(
		String... defaultLocalizedFieldNames) {

		_defaultSelectedFieldNames = defaultLocalizedFieldNames;
	}

	public void setDefaultSelectedLocalizedFieldNames(
		String... defaultLocalizedFieldNames) {

		_defaultSelectedLocalizedFieldNames = defaultLocalizedFieldNames;
	}

	public void setSelectAllLocales(boolean selectAllLocales) {
		_selectAllLocales = selectAllLocales;
	}

	public void setStagingAware(boolean stagingAware) {
		_stagingAware = stagingAware;
	}

	public void unregisterIndexerPostProcessor(
		IndexerPostProcessor indexerPostProcessor) {

		_indexerPostProcessors.remove(indexerPostProcessor);
	}

	private final String _className;
	private boolean _commitImmediately;
	private String[] _defaultSelectedFieldNames;
	private String[] _defaultSelectedLocalizedFieldNames;
	private List<IndexerPostProcessor> _indexerPostProcessors =
		new ArrayList<>();
	private final String[] _searchClassNames;
	private final String _searchEngineId;
	private boolean _selectAllLocales;
	private boolean _stagingAware = true;

}