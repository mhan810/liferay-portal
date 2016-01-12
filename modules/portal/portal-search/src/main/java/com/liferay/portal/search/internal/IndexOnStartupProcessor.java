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

package com.liferay.portal.search.internal;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.UserConstants;
import com.liferay.portal.util.PortalInstances;

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
@Component(immediate = true)
public class IndexOnStartupProcessor {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = new ServiceTracker<>(
			bundleContext, Indexer.class,
			new IndexerServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext = null;

		if (_serviceTracker != null) {
			_serviceTracker.close();
		}
	}

	@Reference(unbind = "-")
	protected void setIndexWriterHelper(IndexWriterHelper indexWriterHelper) {
		_indexWriterHelper = indexWriterHelper;
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		_props = props;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexOnStartupProcessor.class);

	private BundleContext _bundleContext;
	private IndexWriterHelper _indexWriterHelper;
	private Props _props;
	private ServiceTracker<Indexer, Indexer> _serviceTracker;

	private class IndexerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Indexer, Indexer> {

		@Override
		public Indexer addingService(
			ServiceReference<Indexer> serviceReference) {

			Indexer<?> indexer = _bundleContext.getService(serviceReference);

			boolean indexerIndexOnStartup = GetterUtil.getBoolean(
				serviceReference.getProperty(PropsKeys.INDEX_ON_STARTUP), true);

			if (!GetterUtil.getBoolean(
					_props.get(PropsKeys.INDEX_ON_STARTUP)) ||
				!indexerIndexOnStartup) {

				return indexer;
			}

			String className = indexer.getClassName();

			if (Validator.isNull(className)) {
				return indexer;
			}

			try {
				_indexWriterHelper.reindex(
					UserConstants.USER_ID_DEFAULT, "reindexOnActivate",
					PortalInstances.getCompanyIds(), className, null);
			}
			catch (SearchException se) {
				if (_log.isErrorEnabled()) {
					_log.error("Unable to reindex on activation", se);
				}
			}

			return indexer;
		}

		@Override
		public void modifiedService(
			ServiceReference<Indexer> serviceReference, Indexer indexer) {
		}

		@Override
		public void removedService(
			ServiceReference<Indexer> serviceReference, Indexer indexer) {
		}

	}

}