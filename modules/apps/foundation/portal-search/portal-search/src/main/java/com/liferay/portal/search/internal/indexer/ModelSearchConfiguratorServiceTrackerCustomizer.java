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

package com.liferay.portal.search.internal.indexer;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.search.IndexSearcherHelper;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.hits.HitsProcessorRegistry;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.contributor.model.ModelIndexerSearcherContributorsHolder;
import com.liferay.portal.search.contributor.model.ModelSearchConfigurator;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.contributor.query.KeywordQueryContributor;
import com.liferay.portal.search.contributor.query.QueryConfigContributor;
import com.liferay.portal.search.contributor.query.QueryPreFilterContributor;
import com.liferay.portal.search.contributor.query.SearchContextContributor;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;
import com.liferay.portal.search.indexer.IndexerPermissionPostFilter;
import com.liferay.portal.search.indexer.IndexerQueryBuilder;
import com.liferay.portal.search.indexer.IndexerSearcher;
import com.liferay.portal.search.indexer.IndexerSummaryBuilder;
import com.liferay.portal.search.indexer.IndexerWriter;

import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	service = ModelSearchConfiguratorServiceTrackerCustomizer.class
)
public class ModelSearchConfiguratorServiceTrackerCustomizer
	<T extends BaseModel>
		implements ServiceTrackerCustomizer
			<ModelSearchConfigurator, ModelSearchConfigurator> {

	@Override
	@SuppressWarnings(value = "unchecked")
	public ModelSearchConfigurator addingService(
		ServiceReference<ModelSearchConfigurator> serviceReference) {

		int serviceRanking = GetterUtil.getInteger(
			serviceReference.getProperty(Constants.SERVICE_RANKING));

		final ModelSearchConfigurator<T> modelSearchConfigurator =
			_bundleContext.getService(serviceReference);

		ModelSearchSettings modelSearchSettings =
			modelSearchConfigurator.getModelSearchSettings();

		ServiceRegistrationHolder serviceRegistrationHolder =
			_serviceRegistrationHolders.get(modelSearchSettings.getClassName());

		if ((serviceRegistrationHolder != null) &&
			(serviceRegistrationHolder._serviceRanking > serviceRanking)) {

			if (_log.isWarnEnabled()) {
				StringBundler sb = new StringBundler(5);

				sb.append(ClassUtil.getClassName(serviceRegistrationHolder));
				sb.append(" is already registered with a higher ranking of ");
				sb.append(serviceRegistrationHolder._serviceRanking);
				sb.append(" for: ");
				sb.append(modelSearchSettings.getClassName());

				_log.warn(sb.toString());
			}

			return modelSearchConfigurator;
		}

		Indexer defaultIndexer = buildIndexer(
			modelSearchConfigurator, modelSearchSettings);

		ServiceRegistration<Indexer> serviceRegistration =
			_bundleContext.registerService(
				Indexer.class, defaultIndexer, new HashMapDictionary<>());

		serviceRegistrationHolder = new ServiceRegistrationHolder(
			modelSearchConfigurator, serviceRanking, serviceRegistration);

		_serviceRegistrationHolders.put(
			modelSearchSettings.getClassName(), serviceRegistrationHolder);

		return modelSearchConfigurator;
	}

	@Override
	public void modifiedService(
		ServiceReference<ModelSearchConfigurator> serviceReference,
		ModelSearchConfigurator modelSearchConfigurator) {

		removedService(serviceReference, modelSearchConfigurator);

		addingService(serviceReference);
	}

	@Override
	public void removedService(
		ServiceReference<ModelSearchConfigurator> serviceReference,
		ModelSearchConfigurator modelSearchConfigurator) {

		ModelSearchSettings modelSearchSettings =
			modelSearchConfigurator.getModelSearchSettings();

		ServiceRegistrationHolder serviceRegistrationHolder =
			_serviceRegistrationHolders.remove(
				modelSearchSettings.getClassName());

		if (serviceRegistrationHolder != null) {
			serviceRegistrationHolder.close();
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, ModelSearchConfigurator.class, this);

		_documentContributors = ServiceTrackerListFactory.open(
			_bundleContext, DocumentContributor.class,
			"(!(indexer.class.name=*))");

		_keywordQueryContributors = ServiceTrackerListFactory.open(
			_bundleContext, KeywordQueryContributor.class,
			"(!(indexer.class.name=*))");

		_queryPreFilterContributors = ServiceTrackerListFactory.open(
			_bundleContext, QueryPreFilterContributor.class,
			"(!(indexer.class.name=*))");

		_searchContextContributors = ServiceTrackerListFactory.open(
			_bundleContext, SearchContextContributor.class,
			"(!(indexer.class.name=*))");

		_queryConfigContributor = ServiceTrackerListFactory.open(
			_bundleContext, QueryConfigContributor.class,
			"(!(indexer.class.name=*))");
	}

	@SuppressWarnings(value = "unchecked")
	protected Indexer buildIndexer(
		ModelSearchConfigurator<T> modelSearchConfigurator,
		ModelSearchSettings modelSearchSettings) {

		IndexerDocumentBuilder<T> indexerDocumentBuilder =
			new IndexerDocumentBuilderImpl(
				modelSearchConfigurator.getModelDocumentContributors(),
				modelSearchConfigurator.getModelIndexerWriterContributor(),
				_documentContributors,
				modelSearchSettings.getIndexerPostProcessors());

		ModelIndexerSearcherContributorsHolder
			modelIndexerSearcherContributorsHolder =
				modelSearchConfigurator.
					getModelIndexSearcherContributorsHolder();

		IndexerQueryBuilder indexerQueryBuilder = new IndexerQueryBuilderImpl<>(
			modelSearchSettings,
			modelIndexerSearcherContributorsHolder.
				getModelKeywordQueryContributors(),
			modelIndexerSearcherContributorsHolder.
				getModelQueryPreFilterContributors(),
			modelIndexerSearcherContributorsHolder.
				getModelSearchContextContributors(),
			_keywordQueryContributors, _queryPreFilterContributors,
			_searchContextContributors,
			modelSearchSettings.getIndexerPostProcessors());

		IndexerPermissionPostFilter indexerPermissionPostFilter =
			new IndexerPermissionPostFilterImpl(
				modelSearchConfigurator.getModelPermissionPostFilter());

		IndexerSearcher indexerSearcher = new IndexerSearcherImpl<>(
			modelSearchSettings,
			modelIndexerSearcherContributorsHolder.
				getModelQueryConfigContributors(),
			indexerPermissionPostFilter, indexerQueryBuilder,
			hitsProcessorRegistry, indexSearcherHelper,
			_queryConfigContributor);

		IndexerWriter<T> indexerWriter = new IndexerWriterImpl<>(
			modelSearchSettings,
			modelSearchConfigurator.getModelIndexerWriterContributor(),
			indexerDocumentBuilder, indexStatusManager, indexWriterHelper,
			props);

		IndexerSummaryBuilder indexerSummaryBuilder =
			new IndexerSummaryBuilderImpl(
				modelSearchConfigurator.getModelSummaryBuilder(),
				modelSearchSettings.getIndexerPostProcessors());

		return new DefaultIndexer<>(
			modelSearchSettings, indexerDocumentBuilder, indexerSearcher,
			indexerWriter, indexerPermissionPostFilter, indexerQueryBuilder,
			indexerSummaryBuilder);
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext = null;

		_serviceTracker.close();
		_documentContributors.close();
		_keywordQueryContributors.close();
		_queryPreFilterContributors.close();
		_queryConfigContributor.close();
		_searchContextContributors.close();

		_serviceRegistrationHolders.forEach(
			(key, serviceRegistrationHolder) ->
				serviceRegistrationHolder.close());
	}

	@Reference
	protected HitsProcessorRegistry hitsProcessorRegistry;

	@Reference
	protected IndexSearcherHelper indexSearcherHelper;

	@Reference
	protected IndexStatusManager indexStatusManager;

	@Reference
	protected IndexWriterHelper indexWriterHelper;

	@Reference
	protected Props props;

	private static final Log _log = LogFactoryUtil.getLog(
		ModelSearchConfiguratorServiceTrackerCustomizer.class);

	private BundleContext _bundleContext;
	private ServiceTrackerList<DocumentContributor, DocumentContributor>
		_documentContributors;
	private ServiceTrackerList<KeywordQueryContributor, KeywordQueryContributor>
		_keywordQueryContributors;
	private ServiceTrackerList<QueryConfigContributor, QueryConfigContributor>
		_queryConfigContributor;
	private ServiceTrackerList
		<QueryPreFilterContributor, QueryPreFilterContributor>
			_queryPreFilterContributors;
	private ServiceTrackerList
		<SearchContextContributor, SearchContextContributor>
			_searchContextContributors;
	private final Map<String, ServiceRegistrationHolder>
		_serviceRegistrationHolders = new Hashtable<>();
	private ServiceTracker
		<ModelSearchConfigurator, ModelSearchConfigurator> _serviceTracker;

	private class ServiceRegistrationHolder {

		public ServiceRegistrationHolder(
			ModelSearchConfigurator modelSearchConfigurator, int serviceRanking,
			ServiceRegistration<Indexer> indexerServiceRegistration) {

			_modelSearchConfigurator = modelSearchConfigurator;
			_serviceRanking = serviceRanking;
			_indexerServiceRegistration = indexerServiceRegistration;
		}

		public void close() {
			_modelSearchConfigurator.close();
			_indexerServiceRegistration.unregister();
		}

		private final ServiceRegistration<Indexer> _indexerServiceRegistration;
		private final ModelSearchConfigurator _modelSearchConfigurator;
		private final int _serviceRanking;

	}

}