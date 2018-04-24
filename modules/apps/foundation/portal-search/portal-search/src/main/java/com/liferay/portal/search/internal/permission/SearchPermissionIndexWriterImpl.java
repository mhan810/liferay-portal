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

package com.liferay.portal.search.internal.permission;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.index.UpdateDocumentIndexWriter;
import com.liferay.portal.search.indexer.BaseModelDocumentFactory;
import com.liferay.portal.search.permission.SearchPermissionDocumentContributor;
import com.liferay.portal.search.permission.SearchPermissionIndexWriter;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.portal.search.spi.model.index.contributor.SearchPermissionModelDocumentContributor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = SearchPermissionIndexWriter.class)
public class SearchPermissionIndexWriterImpl
	implements SearchPermissionIndexWriter {

	@Override
	public void updatePermissionFields(
		BaseModel<?> baseModel, long companyId, String searchEngineId,
		boolean commitImmediately) {

		Document document = baseModelDocumentFactory.createDocument(baseModel);

		_defaultSearchPermissionModelDocumentContributors.forEach(
			(SearchPermissionModelDocumentContributor
				searchPermissionModelDocumentContributor) ->
				searchPermissionModelDocumentContributor.contribute(
					document, baseModel));

		if (_searchPermissionModelDocumentContributorMap.containsKey(
				baseModel.getModelClassName())) {

			List<SearchPermissionModelDocumentContributor>
				searchPermissionModelDocumentContributors =
					_searchPermissionModelDocumentContributorMap.get(
						baseModel.getModelClassName());

			searchPermissionModelDocumentContributors.forEach(
				(SearchPermissionModelDocumentContributor
					searchPermissionModelDocumentContributor) ->
					searchPermissionModelDocumentContributor.contribute(
						document, baseModel));
		}

		searchPermissionDocumentContributor.addPermissionFields(
			companyId, document);

		updateDocumentIndexWriter.updateDocumentPartially(
			searchEngineId, companyId, document, commitImmediately);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, SearchPermissionModelDocumentContributor.class,
			new SearchPermissionModelDocumentContributorTrackerCustomizer());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	@Reference
	protected BaseModelDocumentFactory baseModelDocumentFactory;

	@Reference
	protected SearchPermissionDocumentContributor
		searchPermissionDocumentContributor;

	@Reference
	protected UpdateDocumentIndexWriter updateDocumentIndexWriter;

	private BundleContext _bundleContext;
	private final CopyOnWriteArrayList<SearchPermissionModelDocumentContributor>
		_defaultSearchPermissionModelDocumentContributors =
			new CopyOnWriteArrayList<>();
	private final Map
		<String, CopyOnWriteArrayList<SearchPermissionModelDocumentContributor>>
			_searchPermissionModelDocumentContributorMap =
				new ConcurrentHashMap<>();
	private ServiceTracker
		<SearchPermissionModelDocumentContributor,
			SearchPermissionModelDocumentContributor> _serviceTracker;

	private class
		SearchPermissionModelDocumentContributorTrackerCustomizer
			implements ServiceTrackerCustomizer
				<SearchPermissionModelDocumentContributor,
					SearchPermissionModelDocumentContributor> {

		@Override
		public SearchPermissionModelDocumentContributor addingService(
			ServiceReference<SearchPermissionModelDocumentContributor>
				serviceReference) {

			SearchPermissionModelDocumentContributor
				searchPermissionModelDocumentContributor =
					_bundleContext.getService(serviceReference);

			if (searchPermissionModelDocumentContributor instanceof
					ModelDocumentContributor) {

				String indexerClassName = (String)serviceReference.getProperty(
					"indexer.class.name");

				if (indexerClassName != null) {
					if (!_searchPermissionModelDocumentContributorMap.
							containsKey(indexerClassName)) {

						_searchPermissionModelDocumentContributorMap.put(
							indexerClassName, new CopyOnWriteArrayList<>());
					}

					List<SearchPermissionModelDocumentContributor>
						searchPermissionModelDocumentContributors =
							_searchPermissionModelDocumentContributorMap.get(
								indexerClassName);

					searchPermissionModelDocumentContributors.add(
						searchPermissionModelDocumentContributor);
				}
			}
			else {
				_defaultSearchPermissionModelDocumentContributors.add(
					searchPermissionModelDocumentContributor);
			}

			return searchPermissionModelDocumentContributor;
		}

		@Override
		public void modifiedService(
			ServiceReference<SearchPermissionModelDocumentContributor>
				serviceReference,
			SearchPermissionModelDocumentContributor
				searchPermissionModelDocumentContributor) {
		}

		@Override
		public void removedService(
			ServiceReference<SearchPermissionModelDocumentContributor>
				serviceReference,
			SearchPermissionModelDocumentContributor
				searchPermissionModelDocumentContributor) {

			if (searchPermissionModelDocumentContributor instanceof
					ModelDocumentContributor) {

				String indexerClassName = (String)serviceReference.getProperty(
					"indexer.class.name");

				if (indexerClassName != null) {
					List<SearchPermissionModelDocumentContributor>
						searchPermissionModelDocumentContributors =
							_searchPermissionModelDocumentContributorMap.get(
								indexerClassName);

					if (searchPermissionModelDocumentContributors != null) {
						searchPermissionModelDocumentContributors.remove(
							searchPermissionModelDocumentContributor);
					}
				}
			}
			else {
				_defaultSearchPermissionModelDocumentContributors.remove(
					searchPermissionModelDocumentContributor);
			}
		}

	}

}