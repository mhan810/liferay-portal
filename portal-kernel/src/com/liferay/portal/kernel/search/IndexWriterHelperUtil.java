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

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.Serializable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Michael C. Han
 */
@ProviderType
public class IndexWriterHelperUtil {

	public static void addDocument(
			String searchEngineId, long companyId, Document document,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.addDocument(
			searchEngineId, companyId, document, commitImmediately);
	}

	public static void addDocuments(
			String searchEngineId, long companyId,
			Collection<Document> documents, boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.addDocuments(
			searchEngineId, companyId, documents, commitImmediately);
	}

	public static void commit(String searchEngineId) throws SearchException {
		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.commit(searchEngineId);
	}

	public static void commit(String searchEngineId, long companyId)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.commit(searchEngineId, companyId);
	}

	public static void deleteDocument(
			String searchEngineId, long companyId, String uid,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.deleteDocument(
			searchEngineId, companyId, uid, commitImmediately);
	}

	public static void deleteDocuments(
			String searchEngineId, long companyId, Collection<String> uids,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.deleteDocuments(
			searchEngineId, companyId, uids, commitImmediately);
	}

	public static void deleteEntityDocuments(
			String searchEngineId, long companyId, String className,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.deleteEntityDocuments(
			searchEngineId, companyId, className, commitImmediately);
	}

	public static IndexWriterHelper getIndexWriterHelper() {
		if (_indexWriterHelperServiceTrackerCustomizer.getService() == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"IndexWriterHelper service is not active to be invoked. " +
						"A dummy stand-in proxy will be used instead.",
					new Throwable());
			}
		}

		return _indexWriterHelper;
	}

	public static IndexWriterHelper getIndexWriterHelperProxy() {
		return _indexWriterHelper;
	}

	public static IndexWriterHelper getIndexWriterHelperService() {
		return _indexWriterHelperServiceTrackerCustomizer.waitForService();
	}

	public static int getReindexTaskCount(long groupId, boolean completed)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		return indexWriterHelper.getReindexTaskCount(groupId, completed);
	}

	public static void indexKeyword(
			long companyId, String querySuggestion, float weight,
			String keywordType, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexKeyword(
			companyId, querySuggestion, weight, keywordType, locale);
	}

	public static void indexKeyword(
			String searchEngineId, long companyId, String querySuggestion,
			float weight, String keywordType, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexKeyword(
			searchEngineId, companyId, querySuggestion, weight, keywordType,
			locale);
	}

	public static void indexQuerySuggestionDictionaries(long companyId)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexQuerySuggestionDictionaries(companyId);
	}

	public static void indexQuerySuggestionDictionaries(
			String searchEngineId, long companyId)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexQuerySuggestionDictionaries(
			searchEngineId, companyId);
	}

	public static void indexQuerySuggestionDictionary(
			long companyId, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexQuerySuggestionDictionary(companyId, locale);
	}

	public static void indexQuerySuggestionDictionary(
			String searchEngineId, long companyId, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexQuerySuggestionDictionary(
			searchEngineId, companyId, locale);
	}

	public static void indexSpellCheckerDictionaries(long companyId)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexSpellCheckerDictionaries(companyId);
	}

	public static void indexSpellCheckerDictionaries(
			String searchEngineId, long companyId)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexSpellCheckerDictionaries(
			searchEngineId, companyId);
	}

	public static void indexSpellCheckerDictionary(
			long companyId, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexSpellCheckerDictionary(companyId, locale);
	}

	public static void indexSpellCheckerDictionary(
			String searchEngineId, long companyId, Locale locale)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.indexSpellCheckerDictionary(
			searchEngineId, companyId, locale);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.portal.search.index.IndexStatusManager#
	 *             isIndexReadOnly}
	 */
	@Deprecated
	public static boolean isIndexReadOnly() {
		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		return indexWriterHelper.isIndexReadOnly();
	}

	public static void partiallyUpdateDocument(
			String searchEngineId, long companyId, Document document,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.partiallyUpdateDocument(
			searchEngineId, companyId, document, commitImmediately);
	}

	public static void partiallyUpdateDocuments(
			String searchEngineId, long companyId,
			Collection<Document> documents, boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.partiallyUpdateDocuments(
			searchEngineId, companyId, documents, commitImmediately);
	}

	public static BackgroundTask reindex(
			long userId, String jobName, long[] companyIds,
			Map<String, Serializable> taskContextMap)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		return indexWriterHelper.reindex(
			userId, jobName, companyIds, taskContextMap);
	}

	public static BackgroundTask reindex(
			long userId, String jobName, long[] companyIds, String className,
			Map<String, Serializable> taskContextMap)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		return indexWriterHelper.reindex(
			userId, jobName, companyIds, className, taskContextMap);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.portal.search.index.IndexStatusManager#
	 *             setIndexReadOnly(boolean)}
	 */
	@Deprecated
	public static void setIndexReadOnly(boolean indexReadOnly) {
		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.setIndexReadOnly(indexReadOnly);
	}

	public static void updateDocument(
			String searchEngineId, long companyId, Document document,
			boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.updateDocument(
			searchEngineId, companyId, document, commitImmediately);
	}

	public static void updateDocuments(
			String searchEngineId, long companyId,
			Collection<Document> documents, boolean commitImmediately)
		throws SearchException {

		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.updateDocuments(
			searchEngineId, companyId, documents, commitImmediately);
	}

	public static void updatePermissionFields(String name, String primKey) {
		IndexWriterHelper indexWriterHelper = getIndexWriterHelper();

		indexWriterHelper.updatePermissionFields(name, primKey);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexWriterHelperUtil.class);

	private static volatile IndexWriterHelper _indexWriterHelper =
		ProxyFactory.newServiceTrackedInstance(
			IndexWriterHelper.class, IndexWriterHelperUtil.class,
			"_indexWriterHelper");
	private static final IndexWriterHelperServiceTrackerCustomizer
		_indexWriterHelperServiceTrackerCustomizer =
			new IndexWriterHelperServiceTrackerCustomizer();

	private static class IndexWriterHelperServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<IndexWriterHelper, IndexWriterHelper> {

		@Override
		public IndexWriterHelper addingService(
			ServiceReference<IndexWriterHelper> serviceReference) {

			_countDownLatch.countDown();

			Registry registry = RegistryUtil.getRegistry();

			return registry.getService(serviceReference);
		}

		@Override
		public void modifiedService(
			ServiceReference<IndexWriterHelper> serviceReference,
			IndexWriterHelper messageBus) {
		}

		@Override
		public void removedService(
			ServiceReference<IndexWriterHelper> serviceReference,
			IndexWriterHelper service) {
		}

		protected IndexWriterHelper getService() {
			return _serviceTracker.getService();
		}

		protected IndexWriterHelper waitForService() {
			try {
				_countDownLatch.await();

				while (true) {
					IndexWriterHelper indexWriterHelper =
						_serviceTracker.getService();

					if (indexWriterHelper != null) {
						return indexWriterHelper;
					}

					if (_log.isDebugEnabled()) {
						_log.debug("Waiting for an IndexWriterHelper");
					}

					Thread.sleep(500);
				}
			}
			catch (InterruptedException ie) {
				throw new IllegalStateException(
					"Unable to initialize IndexWriterHelper", ie);
			}
		}

		private IndexWriterHelperServiceTrackerCustomizer() {
			Registry registry = RegistryUtil.getRegistry();

			_serviceTracker = registry.trackServices(
				IndexWriterHelper.class, this);

			_serviceTracker.open();

			if (_serviceTracker.getService() != null) {
				_countDownLatch.countDown();
			}
		}

		private final CountDownLatch _countDownLatch = new CountDownLatch(1);
		private final ServiceTracker<IndexWriterHelper, IndexWriterHelper>
			_serviceTracker;

	}

}