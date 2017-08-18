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
import com.liferay.portal.kernel.model.BaseModel;

import org.osgi.framework.BundleContext;

/**
 * @author Michael C. Han
 */
public class ModelSearchConfigurator<T extends BaseModel> {

	public ModelSearchConfigurator(
		BundleContext bundleContext,
		ModelIndexerWriterContributor<T> modelIndexerWriterContributor,
		ModelPermissionPostFilter modelPermissionPostFilter,
		ModelSearchSettings modelSearchSettings) {

		this (
			bundleContext, modelIndexerWriterContributor,
			modelPermissionPostFilter, modelSearchSettings, null);
	}

	public ModelSearchConfigurator(
		BundleContext bundleContext,
		ModelIndexerWriterContributor<T> modelIndexerWriterContributor,
		ModelPermissionPostFilter modelPermissionPostFilter,
		ModelSearchSettings modelSearchSettings,
		ModelSummaryContributor modelSummaryContributor) {

		_modelIndexerWriterContributor = modelIndexerWriterContributor;
		_modelPermissionPostFilter = modelPermissionPostFilter;
		_modelSearchSettings = modelSearchSettings;
		_modelSummaryContributor = modelSummaryContributor;

		_modelIndexerSearcherContributorsHolder =
			new ModelIndexerSearcherContributorsHolder(
				bundleContext, _modelSearchSettings.getClassName());

		_modelDocumentContributors = ServiceTrackerListFactory.create(
			bundleContext, ModelDocumentContributor.class,
			"(indexer.class.name=" + modelSearchSettings.getClassName() + ")");
	}

	public ModelSearchConfigurator(
		BundleContext bundleContext,
		ModelIndexerWriterContributor<T> modelIndexerWriterContributor,
		ModelSearchSettings modelSearchSettings) {

		this (
			bundleContext, modelIndexerWriterContributor, null,
			modelSearchSettings, null);
	}

	public ModelSearchConfigurator(
		BundleContext bundleContext,
		ModelIndexerWriterContributor<T> modelIndexerWriterContributor,
		ModelSearchSettings modelSearchSettings,
		ModelSummaryContributor modelSummaryContributor) {

		this (
			bundleContext, modelIndexerWriterContributor, null,
			modelSearchSettings, modelSummaryContributor);
	}

	public void close() {
		_modelDocumentContributors.close();
		_modelIndexerSearcherContributorsHolder.close();
	}

	public Iterable<ModelDocumentContributor> getModelDocumentContributors() {
		return _modelDocumentContributors;
	}

	public ModelIndexerWriterContributor<T> getModelIndexerWriterContributor() {
		return _modelIndexerWriterContributor;
	}

	public ModelIndexerSearcherContributorsHolder
		getModelIndexSearcherContributorsHolder() {

		return _modelIndexerSearcherContributorsHolder;
	}

	public ModelPermissionPostFilter getModelPermissionPostFilter() {
		return _modelPermissionPostFilter;
	}

	public ModelSearchSettings getModelSearchSettings() {
		return _modelSearchSettings;
	}

	public ModelSummaryContributor getModelSummaryBuilder() {
		return _modelSummaryContributor;
	}

	private final ServiceTrackerList
		<ModelDocumentContributor, ModelDocumentContributor>
			_modelDocumentContributors;
	private final ModelIndexerSearcherContributorsHolder
		_modelIndexerSearcherContributorsHolder;
	private final ModelIndexerWriterContributor<T>
		_modelIndexerWriterContributor;
	private ModelPermissionPostFilter _modelPermissionPostFilter;
	private final ModelSearchSettings _modelSearchSettings;
	private final ModelSummaryContributor _modelSummaryContributor;

}