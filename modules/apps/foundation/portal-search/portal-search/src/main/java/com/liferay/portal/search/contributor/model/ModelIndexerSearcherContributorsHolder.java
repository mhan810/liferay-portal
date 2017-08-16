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

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.search.contributor.query.KeywordQueryContributor;
import com.liferay.portal.search.contributor.query.QueryConfigContributor;
import com.liferay.portal.search.contributor.query.QueryPreFilterContributor;
import com.liferay.portal.search.contributor.query.SearchContextContributor;

import org.osgi.framework.BundleContext;

/**
 * @author Michael C. Han
 */
public class ModelIndexerSearcherContributorsHolder {

	public ModelIndexerSearcherContributorsHolder(
		BundleContext bundleContext, String className) {

		_keywordQueryContributors = ServiceTrackerListFactory.create(
			bundleContext, KeywordQueryContributor.class,
			"(indexer.class.name=" + className + ")");

		_queryConfigContributors = ServiceTrackerListFactory.create(
			bundleContext, QueryConfigContributor.class,
			"(indexer.class.name=" + className + ")");

		_queryPreFilterContributors = ServiceTrackerListFactory.create(
			bundleContext, QueryPreFilterContributor.class,
			"(indexer.class.name=" + className + ")");

		_searchContextContributors = ServiceTrackerListFactory.create(
			bundleContext, SearchContextContributor.class,
			"(indexer.class.name=" + className + ")");
	}

	public void close() {
		_keywordQueryContributors.close();
		_queryConfigContributors.close();
		_queryPreFilterContributors.close();
		_searchContextContributors.close();
	}

	public Iterable<KeywordQueryContributor>
		getModelKeywordQueryContributors() {

		return _keywordQueryContributors;
	}

	public Iterable<QueryConfigContributor> getModelQueryConfigContributors() {
		return _queryConfigContributors;
	}

	public Iterable<QueryPreFilterContributor>
		getModelQueryPreFilterContributors() {

		return _queryPreFilterContributors;
	}

	public Iterable<SearchContextContributor>
		getModelSearchContextContributors() {

		return _searchContextContributors;
	}

	private final ServiceTrackerList
		<KeywordQueryContributor, KeywordQueryContributor>
			_keywordQueryContributors;
	private final ServiceTrackerList
		<QueryConfigContributor, QueryConfigContributor>
			_queryConfigContributors;
	private final ServiceTrackerList
		<QueryPreFilterContributor, QueryPreFilterContributor>
			_queryPreFilterContributors;
	private final ServiceTrackerList
		<SearchContextContributor, SearchContextContributor>
			_searchContextContributors;

}